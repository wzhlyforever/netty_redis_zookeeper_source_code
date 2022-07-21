package com.crazymakercircle.reactorModel;

import com.crazymakercircle.NioDemoConfig;
import com.crazymakercircle.util.DateUtil;
import com.crazymakercircle.util.Logger;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.StandardSocketOptions;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Scanner;
import java.util.concurrent.atomic.AtomicBoolean;
import lombok.Data;

/**
 * @program: netty_redis_zookeeper_source_code
 * @description:
 * @author: Mr.Wang
 * @create: 2022-07-20 21:46
 **/
public class EchoClient {

  public void start() throws IOException {

    InetSocketAddress address =
        new InetSocketAddress(NioDemoConfig.SOCKET_SERVER_IP,
            NioDemoConfig.SOCKET_SERVER_PORT);

    // 1、获取通道（channel）
    SocketChannel socketChannel = SocketChannel.open(address);
    Logger.info("客户端连接成功");
    // 2、切换成非阻塞模式
    socketChannel.configureBlocking(false);
    socketChannel.setOption(StandardSocketOptions.TCP_NODELAY, true);
    //不断的自旋、等待连接完成，或者做一些其他的事情
    while (!socketChannel.finishConnect()) {

    }
    Logger.tcfo("客户端启动成功！");

    //启动接受线程
    Processor processor = new Processor(socketChannel);    // 处理任务的线程
    Commander commander = new Commander(processor);    // 命令行的线程
    new Thread(commander).start();
    new Thread(processor).start();

  }

  @Data
  static class Processor implements Runnable {

    ByteBuffer sendBuffer = ByteBuffer.allocate(NioDemoConfig.SERVER_BUFFER_SIZE);
    ByteBuffer readBuffer = ByteBuffer.allocate(NioDemoConfig.SERVER_BUFFER_SIZE);
    SocketChannel socketChannel;
    Selector selector;

    AtomicBoolean hasData = new AtomicBoolean(false);


    public Processor(SocketChannel socketChannel) throws IOException {
      this.selector = Selector.open();
      this.socketChannel = socketChannel;

      socketChannel.register(selector, SelectionKey.OP_READ | SelectionKey.OP_WRITE);

    }

    public void run() {
      try {
        while (!Thread.interrupted()) {
          selector.select();
          Iterator<SelectionKey> iterator = selector.selectedKeys().iterator();
          while (iterator.hasNext()) {
            SelectionKey sel = iterator.next();
            if (sel.isReadable()) {
              // 若选择键的IO事件是“可读”事件,读取数据
              SocketChannel socketChannel = (SocketChannel) sel.channel();

              int length = 0;
              while ((length = socketChannel.read(readBuffer)) > 0) {
                readBuffer.flip();
                Logger.info("server echo:" + new String(readBuffer.array(), 0, length));
                readBuffer.clear();
              }
            }

            if (sel.isWritable()) {
              if (hasData.get()) {
                SocketChannel socketChannel = (SocketChannel) sel.channel();
                sendBuffer.flip();
                // 操作三：发送数据
                socketChannel.write(sendBuffer);   // 写入通道中的数据
                sendBuffer.clear();
                hasData.set(false);
              }
            }

          }

        }
      } catch (IOException e) {
        e.printStackTrace();
      }
    }
  }

  static class Commander implements Runnable {
    Processor processor;

    Commander(Processor processor) throws IOException {
      //Reactor初始化
      this.processor = processor;
    }

    public void run() {
      while (!Thread.interrupted()) {

        ByteBuffer buffer = processor.getSendBuffer();

        Scanner scanner = new Scanner(System.in);
        while (processor.hasData.get()) {
          Logger.tcfo("还有消息没有发送完，请稍等");

        }
        Logger.tcfo("请输入发送内容:");
        if (scanner.hasNext()) {

          String next = scanner.next();
          buffer.put((DateUtil.getNow() + " >>" + next).getBytes());

          processor.hasData.set(true);
        }

      }
    }
  }


  public static void main(String[] args) throws IOException {
    new EchoClient().start();
  }

}
