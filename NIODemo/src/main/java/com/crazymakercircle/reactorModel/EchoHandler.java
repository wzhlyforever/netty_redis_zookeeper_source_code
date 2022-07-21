package com.crazymakercircle.reactorModel;

import com.crazymakercircle.util.Logger;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;

/**
 * @program: netty_redis_zookeeper_source_code
 * @description: EchoHandler回显处理器，也是一个传输处理器，主要是完成客户端的内容读取和回显
 * @author: Mr.Wang
 * @create: 2022-07-20 16:49
 **/
public class EchoHandler implements Runnable{

  SocketChannel socketChannel;
  SelectionKey  selectionKey;

  ByteBuffer buffer = ByteBuffer.allocate(1024);
  final int RECEIVE = 0, SEND = 1;
  int state  = RECEIVE;

  public EchoHandler(Selector selector, SocketChannel sk) throws IOException {

    socketChannel = sk;
    socketChannel.configureBlocking(false);
    selectionKey = socketChannel.register(selector, 0);
    selectionKey.attach(this);
    selectionKey.interestOps(SelectionKey.OP_READ);

  }

  @Override
  public void run() {
    try {
      if (state == SEND) {
        //写入通道
        socketChannel.write(buffer);
        //写完后,准备开始从通道读,byteBuffer切换成写模式
        buffer.clear();
        //写完后,注册read就绪事件
        selectionKey.interestOps(SelectionKey.OP_READ);
        //写完后,进入接收的状态
        state = RECEIVE;
      } else if (state == RECEIVE) {   // 通道中有数据 ，写入缓冲区
        int len = 0;
        while ((len = socketChannel.read(buffer)) > 0) {
          Logger.info(new String(buffer.array(), 0, len));
        }
        buffer.flip();   // 切换为读取模式
        selectionKey.interestOps(SelectionKey.OP_WRITE);
        state = SEND;
      }
    } catch (IOException e) {
      e.printStackTrace();
      selectionKey.cancel();

      try {
        socketChannel.finishConnect();
      } catch (IOException ioException) {
        ioException.printStackTrace();
      }
    }
  }
}

