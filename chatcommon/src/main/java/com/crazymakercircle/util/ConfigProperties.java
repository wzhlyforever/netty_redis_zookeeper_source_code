package com.crazymakercircle.util;

import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;

import java.util.Properties;

/**
 * @program: netty_redis_zookeeper_source_code
 * @description:
 * @author: Mr.Wang
 * @create: 2022-07-18 15:17
 **/
public class ConfigProperties {

  private Properties properties = new Properties();

  private String propertyFileName = "";


  public ConfigProperties() {

  }

  public ConfigProperties(String propertyFileName) {
    this.propertyFileName = propertyFileName;
  }

  protected void loadFromFile() {

    InputStream in = null;
    InputStreamReader inReader = null;

    try {
      in = ConfigProperties.class.getClassLoader()
          .getResourceAsStream(propertyFileName);

      if (in != null) {
        inReader = new InputStreamReader(in, "utf-8");
      } else {

        String filePath = IOUtil.getResourcePath(propertyFileName);
        in = new FileInputStream(filePath);
        inReader = new InputStreamReader(in, "utf-8");

      }
      properties.load(inReader);
    } catch (Exception e) {
      e.printStackTrace();
    } finally {

      IOUtil.closeQuietly(inReader);
    }


  }

  public String readProperty(String key) {
    String value = "";
    value = properties.getProperty(key);

    return value;
  }

  public String getValue(String key) {

    return readProperty(key);

  }

  public int getIntValue(String key) {
    return Integer.parseInt(readProperty(key));
  }


}
