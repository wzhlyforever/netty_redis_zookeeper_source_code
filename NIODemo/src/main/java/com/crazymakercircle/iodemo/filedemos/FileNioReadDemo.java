package com.crazymakercircle.iodemo.filedemos;

import com.crazymakercircle.NioDemoConfig;
import com.crazymakercircle.util.IOUtil;
import com.crazymakercircle.util.Logger;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

/**
 * @program: netty_redis_zookeeper_source_code
 * @description: nio的方式读取文件
 * @author: Mr.Wang
 * @create: 2022-07-19 11:21
 **/
public class FileNioReadDemo {

  private static final Integer CAPACITY = 1024;

  public static void main(String[] args) {

    readSourceFile();

  }

  private static void readSourceFile() {
    final String path = NioDemoConfig.FILE_RESOURCE_SRC_PATH;
    String decodePath = IOUtil.getResourcePath(path);

    Logger.debug(decodePath);
    readfile(decodePath);

  }

  private static void readfile(String filename) {

    try {
      final RandomAccessFile accessFile = new RandomAccessFile(filename, "rw");
      final FileChannel fileChannel = accessFile.getChannel();
      final ByteBuffer buffer = ByteBuffer.allocate(CAPACITY);
      int length = -1;
      // 从通道中读取，并写入到缓冲区中
      while ((length = fileChannel.read(buffer)) != -1) {
        buffer.flip();  // 之前缓冲区为读取模式，现在修改为写入模式
        final byte[] bytes = buffer.array();
        final String data = new java.lang.String(bytes, 0, length);
        System.out.println(data);
      }

      IOUtil.closeQuietly(fileChannel);
      IOUtil.closeQuietly(accessFile);
    } catch (FileNotFoundException e) {
      e.printStackTrace();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }


}
