package com.crazymakercircle.util;

/**
 * @program: netty_redis_zookeeper_source_code
 * @description:
 * @author: Mr.Wang
 * @create: 2022-07-18 17:25
 **/
public class Logger {









  /**
   * 带着线程名+类名+方法名称输出
   *
   * @param args 待输出的字符串形参
   */
  synchronized public static void info(Object... args) {
    StringBuilder content = new StringBuilder();
    for (int i = 0; i < args.length; i++) {
      content.append(args[i] != null ? args[i].toString() : "null");
      content.append(" ");
    }

    String cft = "[" + Thread.currentThread().getName() + "|" + ReflectionUtil.getNakeCallClassMethod() + "]";

    String out = String.format("%20s |>  %s ", cft, content.toString());
    System.out.println(out);

  }

  /**
   * 带着方法名输出，方法名称放在前面
   *
   * @param s 待输出的字符串形参
   */
  public static void debug(Object s) {
    String content = null;
    if (null != s) {
      content = s.toString().trim();
    } else {
      content = "";
    }

    String out = String.format("%20s |>  %s ", ReflectionUtil.getCallMethod(), content);
    System.out.println(out);
  }

  /**
   * 带着线程名+类名+方法名称输出
   *
   * @param s 待输出的字符串形参
   */
  synchronized public static void tcfo(Object s) {
    String cft = "[" + Thread.currentThread().getName() + "|" + ReflectionUtil.getNakeCallClassMethod() + "]";
    String content = null;
    if (null != s) {
      content = s.toString().trim();
    } else {
      content = "";
    }

    String out = String.format("%20s |>  %s ", cft, content);
    System.out.println(out);
  }

  /**
   * 带着类名+方法名输出
   *
   * @param s 待输出的字符串形参
   */
  synchronized public static void cfo(Object s) {

    System.out.println(ReflectionUtil.getCallClassMethod() + ":: " + s);
  }
}
