package com.crazymakercircle.iodemo.OIO;

import com.crazymakercircle.NioDemoConfig;
import com.crazymakercircle.util.Logger;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * @program: netty_redis_zookeeper_source_code
 * @description: java BIO 模型
 * @author: Mr.Wang
 * @create: 2022-07-11 17:38
 **/
public class ConnectionPerThreadWithPool implements Runnable {


  public void run() {

    try {
      final ServerSocket serverSocket = new ServerSocket(NioDemoConfig.SOCKET_SERVER_PORT);
      System.out.println("服务器启动........");


      while (!Thread.interrupted()) {

        System.out.println("等待连接");
        // 监听客户端的连接
        final Socket socket = serverSocket.accept();

        System.out.println("连接到一个客户端");
        //每接收一个客户端的socket连接， 创建一个线程， 进行阻塞式的读写
        Handler handler = new Handler(socket);
        //创建新线程来handle
        //或者，使用线程池来处理
        new Thread(handler).start();
      }


    } catch (IOException e) {
      e.printStackTrace();
    }


  }

  static class Handler implements Runnable {

    final Socket socket;

    Handler(Socket s) {
      socket = s;

      Logger.info("连接的两个端口:", socket.getPort(), socket.getLocalPort());

    }

    public void run() {
      while (true) {
        try {
          byte[] input = new byte[NioDemoConfig.SERVER_BUFFER_SIZE];

          System.out.println("read..........");
          /* 读取数据 */
          final int read = socket.getInputStream().read(input);
          if (read != 1) {

            Logger.info("收到：" + new String(input));

            /* 处理业务逻辑，获取处理结果 */
            byte[] output = input;
            /* 写入结果 */
            socket.getOutputStream().write(output);
          } else {
            break;
          }


        } catch (IOException ex) { /*处理异常*/ }
      }
    }
  }


  public static void main(String[] args) {
    new ConnectionPerThreadWithPool().run();
  }
}
