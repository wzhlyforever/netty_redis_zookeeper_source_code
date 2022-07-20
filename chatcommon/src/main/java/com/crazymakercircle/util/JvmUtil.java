package com.crazymakercircle.util;

/**
 * @program: netty_redis_zookeeper_source_code
 * @description:
 * @author: Mr.Wang
 * @create: 2022-07-18 16:26
 **/
public class JvmUtil {


  public static boolean isWin() {
    String os = System.getProperty("os.name");
    if(os.toLowerCase().startsWith("win")){
      return  true;
    }
    return  false;
  }

}
