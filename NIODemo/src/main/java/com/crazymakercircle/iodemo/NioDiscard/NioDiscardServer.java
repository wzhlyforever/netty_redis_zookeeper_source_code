package com.crazymakercircle.iodemo.NioDiscard;

import com.crazymakercircle.NioDemoConfig;
import com.crazymakercircle.util.Logger;
import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.rmi.registry.Registry;
import java.util.Iterator;
import java.util.Set;

/**
 * @program: netty_redis_zookeeper_source_code
 * @description:
 * @author: Mr.Wang
 * @create: 2022-07-19 19:16
 **/
public class NioDiscardServer {

  public static void startServer() throws IOException {
     // 1 获取selector实例
     Selector selector = Selector.open();
     // 2 获取通道
    ServerSocketChannel  serverSocketChannel = ServerSocketChannel.open();
    // 3 设置为非阻塞
    serverSocketChannel.configureBlocking(false);
    // 4 绑定监听端口
    serverSocketChannel.bind(new InetSocketAddress(NioDemoConfig.SOCKET_SERVER_PORT));

    //5 注册通道到选择器上 返回一个selectionkey
     serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);

    // 6 轮询感兴趣的 io就绪事件
    while (selector.select() > 0) {
      // 7 获取选择键的集合
      final Iterator<SelectionKey> iterator = selector.selectedKeys().iterator();

      while (iterator.hasNext()) {
        // 8、获取单个的选择键，并处理
        final SelectionKey selectedKey = iterator.next();
        // 9、判断key是具体的什么事件
        if (selectedKey.isAcceptable()) {
          Logger.info("有新连接到来: " + selectedKey.channel());
          // 10、若选择键的IO事件是“连接就绪”事件,就获取客户端连接
          SocketChannel socketChannel = serverSocketChannel.accept();
          // 11、切换为非阻塞模式
          socketChannel.configureBlocking(false);
          // 12、将该通道注册到selector选择器上
          socketChannel.register(selector,
              SelectionKey.OP_READ | SelectionKey.OP_WRITE  | SelectionKey.OP_CONNECT);

        }

        if (selectedKey.isWritable()) {
          Logger.info("发生了 写就绪事件" + selectedKey.channel());
        }
        if (selectedKey.isConnectable()) {
          Logger.info("发生了 客户端  连接成功事件" + selectedKey.channel());

        }
        // 一个socket 通道有数据可读，就会发生 读操作
        if (selectedKey.isReadable()) {
          Logger.info("发生了 读绪事件：" + selectedKey.channel());

          // 13、若选择键的IO事件是“可读”事件,读取数据
          SocketChannel socketChannel = (SocketChannel) selectedKey.channel();

          // 14、读取数据
          ByteBuffer byteBuffer = ByteBuffer.allocate(1024);
          int length = 0;
          while ((length = socketChannel.read(byteBuffer)) > 0) {

            byteBuffer.flip();

            Logger.info(new String(byteBuffer.array(), 0, length));

            byteBuffer.clear();

          }
          socketChannel.close();
        }

        // 15、移除选择键
        iterator.remove();
      }



    }
    serverSocketChannel.close();


  }



  public static void main(String[] args) throws IOException {
    startServer();
  }

}
