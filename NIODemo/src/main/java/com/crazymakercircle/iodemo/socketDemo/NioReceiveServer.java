package com.crazymakercircle.iodemo.socketDemo;

import com.crazymakercircle.NioDemoConfig;
import com.crazymakercircle.util.Logger;
import com.sun.xml.internal.bind.v2.TODO;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.StandardSocketOptions;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.SelectableChannel;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;
import java.nio.file.StandardOpenOption;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import javax.lang.model.element.VariableElement;

/**
 * @program: netty_redis_zookeeper_source_code
 * @description: 文件传输端
 * @author: Mr.Wang
 * @create: 2022-07-19 15:14
 **/
public class NioReceiveServer {

  // 接收文件路径
  private static final String RECEIVE_PATH = NioDemoConfig.SOCKET_RECEIVE_PATH;


  private Charset charset = Charset.forName("UTF-8");

  /*
    服务器端保存的客户端对象，对应一个客户端文件
   */
  static class Session {

    int step = 1;

    long fileLength;    // 文件长度
    int fileNameLength;  // 文件名称长度

    String fileName = null;    // 文件名称

    //开始传输的时间
    long startTime;

    //客户端的地址
    InetSocketAddress remoteAddress;

    //输出的文件通道
    FileChannel fileChannel;

    //接收长度
    long receiveLength;

    public boolean isFinished() {
      return receiveLength >= fileLength;
    }

  }

  private ByteBuffer buffer
      = ByteBuffer.allocate(NioDemoConfig.SERVER_BUFFER_SIZE);

  // 使用Map 保存每个客户端传输，当OP_READ通道可读时，根据channel 找到对应的对象
  Map<SelectableChannel, Session> clientMap = new HashMap<>();


  public void startServer() throws IOException {
    // 1 获取Selector选择器
    Selector selector = Selector.open();

    // 2 获取通道
    ServerSocketChannel serverChannel = ServerSocketChannel.open();
    ServerSocket serverSocket = serverChannel.socket();

    // 3 设置为非阻塞
    serverChannel.configureBlocking(false);

    // 4 绑定连接
    serverSocket.bind(new InetSocketAddress(NioDemoConfig.SOCKET_SERVER_PORT));
    // 5 注册通道到选择器上
    serverChannel.register(selector, SelectionKey.OP_ACCEPT);
    Logger.tcfo("serverChannel is linstening.....");

    // 6 轮询感兴趣的I/O就绪事件（选择键集合）
    while (selector.select() > 0) {
      if (null == selector.selectedKeys()) {
        continue;
      }
      final Iterator<SelectionKey> it = selector.selectedKeys().iterator();
      while (it.hasNext()) {
        SelectionKey key = it.next();
        if (null == key) {
          continue;
        }
        // 9 判断key 是什么具体事件
        if (key.isAcceptable()) {
          // 监听到的事件为“新连接”事件，就获取客户端新连接
          final ServerSocketChannel server = (ServerSocketChannel) key.channel();
          final SocketChannel socketChannel = server.accept();
          if (socketChannel == null) {
            continue;
          }
          socketChannel.configureBlocking(false);
          socketChannel.setOption(StandardSocketOptions.TCP_NODELAY, true);
          socketChannel.register(selector, SelectionKey.OP_READ);

          Session session = new Session();
          session.remoteAddress = ((InetSocketAddress) socketChannel.getRemoteAddress());
          clientMap.put(socketChannel, session);
          Logger.debug(socketChannel.getLocalAddress() + "连接成功");


        } else if (key.isReadable()) {
          handleData(key);
        }
        it.remove();
      }
    }

  }

  /**
   * @param key 处理客户端传输过来的数据
   */
  private void handleData(SelectionKey key) throws IOException{
    final SocketChannel socketChannel = (SocketChannel) key.channel();
    Session session = clientMap.get(key.channel());
    int num = 0;
    buffer.clear();   // 切换为写入模式
    while ((num = socketChannel.read(buffer)) > 0) {
      Logger.cfo("收到的字节数 = " + num);

      buffer.flip();
      process(session, buffer);
      buffer.clear();
    }
  }

  /*
     解决的问题 nio的半包问题 丢包  粘包
   */
  private void process(Session session, ByteBuffer buffer) {
    // TODOLIST

  }

  public static void main(String[] args) throws IOException {
    NioReceiveServer server = new NioReceiveServer();
    server.startServer();
  }
}
