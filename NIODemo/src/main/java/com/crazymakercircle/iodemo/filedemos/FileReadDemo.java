package com.crazymakercircle.iodemo.filedemos;

import com.crazymakercircle.NioDemoConfig;
import com.crazymakercircle.util.IOUtil;
import com.crazymakercircle.util.Logger;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

/**
 * @program: netty_redis_zookeeper_source_code
 * @description: 读取文件
 * @author: Mr.Wang
 * @create: 2022-07-19 10:47
 **/
public class FileReadDemo {


  public static void main(String[] args) {

    readSourceFile();
  }

  public static void readSourceFile() {
    final String path = NioDemoConfig.FILE_RESOURCE_SRC_PATH;
    final String decodePath = IOUtil.getResourcePath(path);
    Logger.debug("decodePath=" + decodePath);
    readFile(decodePath);
  }

  public static void readFile(String fileName) {

    try {
      File file = new File(fileName);
      FileReader fileReader = null;
      fileReader = new FileReader(file);
      BufferedReader bufferedReader = new BufferedReader(fileReader);
      String data;

      while ((data = bufferedReader.readLine()) != null) {   // 一行一行的读
        Logger.debug(data);

      }

    } catch (FileNotFoundException e) {
      e.printStackTrace();
    } catch (IOException e) {
      e.printStackTrace();
    }

  }

}
