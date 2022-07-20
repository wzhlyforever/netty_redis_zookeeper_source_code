package com.crazymakercircle.iodemo.NioDiscard;

import com.crazymakercircle.NioDemoConfig;
import com.crazymakercircle.util.Logger;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

/**
 * @program: netty_redis_zookeeper_source_code
 * @description:
 * @author: Mr.Wang
 * @create: 2022-07-19 20:13
 **/
public class NioDiscardClient {



  public static void main(String[] args) throws IOException {
    startClient();
  }

  /**
   * 模拟客户端
   * @throws IOException
   */
  private static void startClient() throws IOException {
   // 绑定ip 和 port  服务器端
    InetSocketAddress address =
        new InetSocketAddress(NioDemoConfig.SOCKET_SERVER_IP,
            NioDemoConfig.SOCKET_SERVER_PORT);

    // 1、获取通道（channel）
    SocketChannel socketChannel = SocketChannel.open(address);
    // 2、切换成非阻塞模式
    socketChannel.configureBlocking(false);
    //不断的自旋、等待连接完成，或者做一些其他的事情
    while (!socketChannel.finishConnect()) {

    }

    Logger.info("客户端连接成功");
    // 3、分配指定大小的缓冲区
    ByteBuffer byteBuffer = ByteBuffer.allocate(1024);
    byteBuffer.put("hello world".getBytes());
    byteBuffer.flip();
    socketChannel.write(byteBuffer);   // 从缓冲区中读取数据，并写入socketChannel中
    Logger.info("客户端写入成功");

    socketChannel.shutdownOutput();
    socketChannel.close();
  }
}
