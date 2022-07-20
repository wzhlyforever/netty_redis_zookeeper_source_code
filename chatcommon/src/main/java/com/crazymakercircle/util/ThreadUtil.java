package com.crazymakercircle.util;

import java.util.concurrent.locks.LockSupport;

/**
 * @program: netty_redis_zookeeper_source_code
 * @description:
 * @author: Mr.Wang
 * @create: 2022-07-19 21:57
 **/
public class ThreadUtil {



  /**
   * 线程睡眠
   *
   * @param second 秒
   */
  public static void sleepSeconds(int second) {
    LockSupport.parkNanos(second * 1000L * 1000L * 1000L);
  }

}
