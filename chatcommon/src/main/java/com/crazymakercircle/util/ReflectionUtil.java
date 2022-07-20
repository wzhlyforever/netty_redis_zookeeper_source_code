package com.crazymakercircle.util;

import java.util.Arrays;

/**
 * @program: netty_redis_zookeeper_source_code
 * @description:
 * @author: Mr.Wang
 * @create: 2022-07-18 17:26
 **/
public class ReflectionUtil {


  /**
   * 获得调用方法的名称
   *
   * @return 方法名称
   */
  public static String getCallMethod()
  {
    StackTraceElement[] stack = Thread.currentThread().getStackTrace();
    // 获得调用方法名
    String method = stack[3].getMethodName();
    return method;
  }


  /**
   * 获得调用方法的类名+方法名
   *
   * @return 方法名称
   */
  public static String getNakeCallClassMethod()
  {
    StackTraceElement stack[] = Thread.currentThread().getStackTrace();
    // 获得调用方法名
    String[] className = stack[3].getClassName().split("\\.");
    String fullName = className[className.length - 1] + "." + stack[3].getMethodName();
    return fullName;
  }



  /**
   * 获得调用方法的类名+方法名,带上中括号
   *
   * @return 方法名称
   */
  public static String getCallClassMethod()
  {
    StackTraceElement stack[] = Thread.currentThread().getStackTrace();
    // 获得调用方法名
    String[] className = stack[3].getClassName().split("\\.");
//        String fullName = className[className.length - 1] + ":" + stack[3].getMethodName();
    String fullName = className[className.length - 1] + ":" + stack[3].getMethodName() + ":" + stack[3].getLineNumber();
    return fullName;
  }
}
