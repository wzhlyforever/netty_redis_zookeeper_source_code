package com.crazymakercircle.reactorModel;

import com.crazymakercircle.NioDemoConfig;
import com.crazymakercircle.util.Logger;
import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;

/**
 * @program: netty_redis_zookeeper_source_code
 * @description:   一个反应器类
 * @author: Mr.Wang
 * @create: 2022-07-20 15:51
 **/
public class EchoServerReactor implements Runnable {


  Selector selector;
  ServerSocketChannel server;

  public EchoServerReactor() throws IOException {
    // 获取选择器实例
    selector = Selector.open();
    // 获取通道 用于监听 新连接事件
    server = ServerSocketChannel.open();
    server.configureBlocking(false);
     // 绑定地址 和监听端口
    InetSocketAddress address = new InetSocketAddress(NioDemoConfig.SOCKET_SERVER_IP,
        NioDemoConfig.SOCKET_SERVER_PORT);

    server.socket().bind(address);

    SelectionKey sk = server.register(selector, SelectionKey.OP_ACCEPT);

    sk.attach(new AcceptHandler());

  }

  @Override
  public void run() {
    try {
      while (!Thread.interrupted()) {
        selector.select(1000 );

        Set<SelectionKey> selectionKeys = selector.selectedKeys();
        if (selectionKeys.size() == 0 || selectionKeys == null) {
          continue;
        }

        Iterator<SelectionKey> it = selectionKeys.iterator();

        while (it.hasNext()) {
          SelectionKey selectionKey = it.next();
          dispatch(selectionKey);
        }


      }
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  private void dispatch(SelectionKey selectionKey) {
    AcceptHandler attachment = (AcceptHandler) selectionKey.attachment();
    attachment.run();

  }

  /**
   * 1 完成新连接的接收工作
   * 2 为新连接创建一个负责数据传输的handler处理器
   */

  class AcceptHandler implements Runnable {


    @Override
    public void run() {
      try {
        SocketChannel socketChannel = server.accept();
        Logger.info("接收到一个连接");
        // EchoHandler就是负责socket连接的数据输入、业务处理、结果输出
        new EchoHandler(selector, socketChannel);
      } catch (IOException e) {
        e.printStackTrace();
      }
    }
  }


  public static void main(String[] args) throws IOException{
    new Thread(new EchoServerReactor()).start();
  }

}
