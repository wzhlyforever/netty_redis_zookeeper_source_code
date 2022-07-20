package com.crazymakercircle;

import com.crazymakercircle.util.ConfigProperties;

/**
 * @program: netty_redis_zookeeper_source_code
 * @description: 读取配置文件信息  单例
 * @author: Mr.Wang
 * @create: 2022-07-18 15:25
 **/
public class NioDemoConfig extends ConfigProperties {


  static ConfigProperties singleton = new NioDemoConfig("system.properties");


  public NioDemoConfig(String fileName) {
    super(fileName);
    super.loadFromFile();
  }

  public static final String SOCKET_SERVER_IP = singleton.getValue("socket.server.ip");

  public static final int SOCKET_SERVER_PORT = singleton.getIntValue("socket.server.port");

  public static final int SERVER_BUFFER_SIZE
      = singleton.getIntValue("server.buffer.size");

  //file.resource.src.path
  public static final String FILE_RESOURCE_SRC_PATH = singleton.getValue("file.resource.src.path");

  public static final String SOCKET_SEND_FILE = singleton.getValue("socket.send.file");

  // file.resource.dest.path=/system.copy.properties
  public static final String FILE_RESOURCE_DEST_PATH = singleton
      .getValue("file.resource.dest.path");

  public static final int SEND_BUFFER_SIZE
      = singleton.getIntValue("send.buffer.size");

  public static final String SOCKET_RECEIVE_PATH
      = singleton.getValue("socket.receive.path");


  public static void main(String[] args) {
    System.out.println(NioDemoConfig.SOCKET_SERVER_PORT);
  }


}
