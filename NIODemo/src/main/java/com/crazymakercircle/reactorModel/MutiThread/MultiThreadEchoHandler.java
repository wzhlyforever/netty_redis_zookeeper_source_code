package com.crazymakercircle.reactorModel.MutiThread;

import com.crazymakercircle.util.Logger;
import java.io.IOException;
import java.net.StandardSocketOptions;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * @program: netty_redis_zookeeper_source_code
 * @description:
 * @author: Mr.Wang
 * @create: 2022-07-21 12:53
 **/
public class MultiThreadEchoHandler implements Runnable{

  SocketChannel channel;
  SelectionKey selectionKey;

  final int RECEVING = 0, SENDING = 1;
  int state = RECEVING;

  ByteBuffer buffer = ByteBuffer.allocate(1024);
  ExecutorService pool = Executors.newFixedThreadPool(4);

  public MultiThreadEchoHandler(SocketChannel socketChannel, Selector selector) throws IOException {

    channel = socketChannel;
    channel.setOption(StandardSocketOptions.TCP_NODELAY, true);
    channel.configureBlocking(false);
    selectionKey = channel.register(selector, 0 );
    selectionKey.attach(this);
    selectionKey.interestOps(SelectionKey.OP_READ);

  }


  public void run() {
     //  异步任务，在独立的线程池中执行
    //提交数据传输任务到线程池
    //使得IO处理不在IO事件轮询线程中执行，在独立的线程池中执行
    pool.execute(new AsynicTask());
  }
  //异步任务，不在Reactor线程中执行
  //数据传输与业务处理任务，不在IO事件轮询线程中执行，在独立的线程池中执行
  public synchronized void asnyc() {
    try {
      if (state == SENDING) {
        //写入通道
        channel.write(buffer);

        //写完后,准备开始从通道读,byteBuffer切换成写模式
        buffer.clear();
        //写完后,注册read就绪事件
        selectionKey.interestOps(SelectionKey.OP_READ);
        //写完后,进入接收的状态
        state = RECEVING;
      } else if (state == RECEVING) {
        //从通道读
        int length = 0;
        while ((length = channel.read(buffer)) > 0) {
          Logger.info(new String(buffer.array(), 0, length));
        }
        //读完后，准备开始写入通道,byteBuffer切换成读模式
        buffer.flip();
        //读完后，注册write就绪事件
        selectionKey.interestOps(SelectionKey.OP_WRITE);
        //读完后,进入发送的状态
        state = SENDING;
      }
      //处理结束了, 这里不能关闭select key，需要重复使用
      //sk.cancel();
    } catch (IOException ex) {
      ex.printStackTrace();
    }

  }


  class AsynicTask implements Runnable {

    @Override
    public void run() {
         MultiThreadEchoHandler.this.asnyc();
    }
  }

}
