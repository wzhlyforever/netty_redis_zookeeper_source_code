package com.crazymakercircle.reactorModel.MutiThread;

import com.crazymakercircle.NioDemoConfig;
import com.crazymakercircle.util.Logger;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;
import javax.lang.model.element.VariableElement;

/**
 * @program: netty_redis_zookeeper_source_code
 * @description:
 * @author: Mr.Wang
 * @create: 2022-07-21 11:09
 **/
public class MultiThreadEchoServerReactor {

  ServerSocketChannel serverSocketChannel;

  Selector bossSelector;

  Selector[] workSelectors;

  Reactor bossReactor;

  Reactor[] workReactors;

  AtomicInteger next = new AtomicInteger(0);


  public MultiThreadEchoServerReactor() throws IOException {

    serverSocketChannel = ServerSocketChannel.open();
    InetSocketAddress address = new InetSocketAddress(NioDemoConfig.SOCKET_SERVER_IP,
        NioDemoConfig.SOCKET_SERVER_PORT);
    serverSocketChannel.socket().bind(address);
    serverSocketChannel.configureBlocking(false);
    bossSelector = Selector.open();
    SelectionKey selectionKey = serverSocketChannel
        .register(bossSelector, SelectionKey.OP_ACCEPT);

    selectionKey.attach(new AcceptorHandler());

    workSelectors[0] = Selector.open();
    workSelectors[1] = Selector.open();
    // 一个反应器 负责一个 选择器
    bossReactor = new Reactor(bossSelector);

    Reactor workReactor1 = new Reactor(workSelectors[0]);
    Reactor workReactor2 = new Reactor(workSelectors[1]);
    workReactors = new Reactor[]{workReactor1, workReactor2};

  }

  class Reactor implements Runnable {

    Selector selector;

    public Reactor(Selector selector) {
      selector = selector;
    }

    public void run() {

      try {
        while (!Thread.interrupted()) {
          selector.select(1000);
          final Set<SelectionKey> selectionKeys = selector.selectedKeys();
          if (selectionKeys == null || selectionKeys.size() == 0) {
            continue;
          }

          Iterator<SelectionKey> iterator = selectionKeys.iterator();

          while (iterator.hasNext()) {

            SelectionKey selectionKey = iterator.next();
            dispatch(selectionKey);  // 下发分配到 对应的 handler 处理
          }
          selectionKeys.clear();
        }
      } catch (IOException e) {
        e.printStackTrace();
      }


    }

    public void dispatch(SelectionKey selectionKey) {
      final Runnable attachment = (Runnable) selectionKey.attachment();
      attachment.run();
    }
  }

  class AcceptorHandler implements Runnable {


    public void run() {
      try {
        SocketChannel socketChannel = serverSocketChannel.accept();
        Logger.info("有新连接到来.......");
        if (socketChannel != null) {
          int index = next.get();
          Selector workSelector = workSelectors[index];
          new MultiThreadEchoHandler(socketChannel, workSelector);  //也需要把this 注册到selector中
        }
      } catch (IOException e) {
        e.printStackTrace();

      }

      if (next.incrementAndGet() == workSelectors.length) {
        next.set(0);
      }
    }

  }


  public void startServer() {
    new Thread(bossReactor).start();
    new Thread(workReactors[0]).start();
    new Thread(workReactors[1]).start();
  }


  public static void main(String[] args) throws IOException {
    // 服务器开启
    MultiThreadEchoServerReactor server = new MultiThreadEchoServerReactor();
    server.startServer();
  }

}
