package com.crazymakercircle.iodemo.filedemos;

import com.crazymakercircle.NioDemoConfig;
import com.crazymakercircle.util.IOUtil;
import com.crazymakercircle.util.Logger;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * @program: netty_redis_zookeeper_source_code
 * @description:  基于BIO 的输入输出流进行 文件复制
 * @author: Mr.Wang
 * @create: 2022-07-19 12:10
 **/
public class FileCopyDemo {


  public static void main(String[] args) {

    copyResourceFile();

  }

  private static void copyResourceFile() {

    final String fileResourceSrcPath = NioDemoConfig.FILE_RESOURCE_SRC_PATH;
    final String resourcePath = IOUtil.getResourcePath(fileResourceSrcPath);
    Logger.debug(resourcePath);

    final String fileResourceDestPath = NioDemoConfig.FILE_RESOURCE_DEST_PATH;
    final String destPath = IOUtil.builderResourcePath(fileResourceDestPath);
    Logger.debug(destPath);
    blockcopyfile(resourcePath, destPath);

  }

  private static void blockcopyfile(String resourcePath, String destPath) {
    InputStream in = null;
    OutputStream out = null;

    File sourceFile = new File(resourcePath);
    File destFile = new File(destPath);
    try {
      long startTime = System.currentTimeMillis();
      in =  new FileInputStream(sourceFile);
      out = new FileOutputStream(destFile);

      byte[] bytes = new byte[1024];
      int read;
      while ((read = in.read(bytes)) != -1) {

        out.write(bytes,0,read);
      }

      out.flush();
      long end = System.currentTimeMillis();

      Logger.debug("IO流复制毫秒数：" + (end - startTime));
    } catch (IOException e) {
      e.printStackTrace();
    } finally {
      IOUtil.closeQuietly(in);
      IOUtil.closeQuietly(out);
    }
  }

}
