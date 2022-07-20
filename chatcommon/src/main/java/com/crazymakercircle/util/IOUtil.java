package com.crazymakercircle.util;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLDecoder;

/**
 * @program: netty_redis_zookeeper_source_code
 * @description:
 * @author: Mr.Wang
 * @create: 2022-07-18 15:59
 **/
public class IOUtil {

  public static String getUserHomeResourcePath(String resName) {
    return System.getProperty("user.dir") + File.separator + resName;
  }


  public static String getResourcePath(String resName) {
    final URL url = IOUtil.class.getResource(resName);
    String path = null;
    if (url == null) {
      path = IOUtil.class.getResource("/").getPath() + resName;
    } else {
      path = url.getPath();
    }
    String decodePath = null;
    try {
      decodePath = URLDecoder.decode(path, "UTF-8");
    } catch (UnsupportedEncodingException e) {
      e.printStackTrace();
    }

    if (JvmUtil.isWin()) {
      return decodePath.substring(1);
    }
    return decodePath;


  }

  public static void closeQuietly(java.io.Closeable o) {
    if (null == o) return;
    try {
      o.close();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  public static String builderResourcePath(String resName) {

    URL url = IOUtil.class.getResource("/");
    String path = url.getPath();
    String decodepath = null;
    try {
      decodepath = URLDecoder.decode(path, "UTF-8");
    } catch (UnsupportedEncodingException e) {
      e.printStackTrace();
    }
    return decodepath + resName;
  }

  public static void main(String[] args) {
    final String word = IOUtil.getResourcePath("word");
    System.out.println(word);

  }



}
