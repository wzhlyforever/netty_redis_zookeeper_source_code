package com.crazymakercircle.iodemo.filedemos;

import com.crazymakercircle.NioDemoConfig;
import com.crazymakercircle.util.IOUtil;
import com.crazymakercircle.util.Logger;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

/**
 * @program: netty_redis_zookeeper_source_code
 * @description:
 * @author: Mr.Wang
 * @create: 2022-07-19 13:06
 **/
public class FileNioCopyDemo {

  public static void main(String[] args) {
    nioCopyResourceFile();
  }

  private static void nioCopyResourceFile() {

    String resourceSrcPath = NioDemoConfig.FILE_RESOURCE_SRC_PATH;
    final String resourcePath = IOUtil.getResourcePath(resourceSrcPath);
    Logger.debug(resourcePath);

    final String resourceDestPath = NioDemoConfig.FILE_RESOURCE_DEST_PATH;
    final String destpath = IOUtil.builderResourcePath(resourceDestPath);

    Logger.debug(destpath);

    niocopyFile(resourcePath, destpath);
  }

  private static void niocopyFile(String resourcePath, String destpath) {

    final File resFile = new File(resourcePath);
    final File desFile = new File(destpath);

    FileInputStream in = null;
    FileOutputStream out = null;
    FileChannel inChannel = null;  // 输入通道
    FileChannel outChannel = null;  // 输出通道

    try {
      in = new FileInputStream(resFile);
      inChannel = in.getChannel();

      out = new FileOutputStream(desFile);
      outChannel = out.getChannel();

      ByteBuffer buffer = ByteBuffer.allocateDirect(1024);
      int len = -1;
      // 从通道中读取 并写入到缓冲区
      while ((len = inChannel.read(buffer)) != -1) {
        buffer.flip();

        int outLenth = 0;
        // 从缓冲区读取，并写入到 通道中
        while ((outLenth = outChannel.write(buffer)) != 0) {
          System.out.println("写入的字节数：" + outLenth);
        }

        buffer.clear();

      }

    } catch (IOException e) {
      e.printStackTrace();
    } finally {
      IOUtil.closeQuietly(outChannel);
      IOUtil.closeQuietly(out);
      IOUtil.closeQuietly(inChannel);
      IOUtil.closeQuietly(in);

    }


  }

}
