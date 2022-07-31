package com.crazymakercircle.netty.decoder;

import com.crazymakercircle.util.RandomUtil;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.embedded.EmbeddedChannel;
import io.netty.handler.codec.DelimiterBasedFrameDecoder;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.codec.LineBasedFrameDecoder;
import io.netty.handler.codec.string.StringDecoder;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import org.junit.Test;

/**
 * @program: netty_redis_zookeeper_source_code
 * @description: netty中内置的解码器，
 * @author: Mr.Wang
 * @create: 2022-07-27 20:00
 **/
public class NettyOpenBoxDecoder {

  static final int VERSION = 100;
  static final String SPLITER = "\r\n";
  static final String SPLITER_2 = "\t";
  static final String CONTENT = "疯狂创客圈：高性能学习社群!";

  /**
   * LineBasedFrameDecoder 使用实例
   */

  @Test
  public void testLineBasedFrameDecoder() {

    ChannelInitializer<EmbeddedChannel> i = new ChannelInitializer<EmbeddedChannel>() {
      @Override
      protected void initChannel(EmbeddedChannel ch) throws Exception {
        ch.pipeline().addLast(new LineBasedFrameDecoder(1024));
        ch.pipeline().addLast(new StringDecoder());
        ch.pipeline().addLast(new StringProcessHandler());
      }
    };

    EmbeddedChannel channel = new EmbeddedChannel(i);
    for (int j = 0; j < 10; j++) {
      int random = RandomUtil.randInMod(3);
      ByteBuf buf = Unpooled.buffer();

      for (int k = 0; k < random; k++) {
        buf.writeBytes(CONTENT.getBytes(Charset.forName("UTF-8")));
      }

      try {
        buf.writeBytes(SPLITER.getBytes("UTF-8"));
      } catch (UnsupportedEncodingException e) {
        e.printStackTrace();
      }

      channel.writeInbound(buf);


    }
  }


  @Test
  public void testDelimiterBasedFrameDecoder() {
    try {
      final ByteBuf delimiter = Unpooled.copiedBuffer(SPLITER_2.getBytes("UTF-8"));

      ChannelInitializer i = new ChannelInitializer<EmbeddedChannel>() {
        protected void initChannel(EmbeddedChannel ch) {
          ch.pipeline().addLast(
              new DelimiterBasedFrameDecoder(1024, true, delimiter));
          ch.pipeline().addLast(new StringDecoder());
          ch.pipeline().addLast(new StringProcessHandler());
        }
      };

      EmbeddedChannel channel = new EmbeddedChannel(i);

      for (int j = 0; j < 100; j++) {

        //1-3之间的随机数
        int random = RandomUtil.randInMod(3);
        ByteBuf buf = Unpooled.buffer();
        for (int k = 0; k < random; k++) {
          buf.writeBytes(CONTENT.getBytes("UTF-8"));
        }

        buf.writeBytes(SPLITER_2.getBytes("UTF-8"));

        channel.writeInbound(buf);
      }

      Thread.sleep(Integer.MAX_VALUE);
    } catch (InterruptedException e) {
      e.printStackTrace();
    } catch (UnsupportedEncodingException e) {
      e.printStackTrace();
    }
  }


  @Test
  public void testLengthFieldBasedFrameDecoder() {

    LengthFieldBasedFrameDecoder decoder = new LengthFieldBasedFrameDecoder(
        1024,
        0,
        4,
        0,
        4
    );

    ChannelInitializer i = new ChannelInitializer<EmbeddedChannel>() {
      protected void initChannel(EmbeddedChannel ch) {
        ch.pipeline().addLast(decoder);
        ch.pipeline().addLast(new StringDecoder(Charset.forName("UTF-8")));
        ch.pipeline().addLast(new StringProcessHandler());
      }
    };

    EmbeddedChannel channel = new EmbeddedChannel(i);

    for (int j = 1; j <= 100; j++) {
      //1-3之间的随机数
      int random = RandomUtil.randInMod(3);
      ByteBuf buf = Unpooled.buffer();

      String s = j + "次发送->" + CONTENT;
      byte[] bytes = new byte[0];
      try {
        bytes = s.getBytes("UTF-8");
        buf.writeInt(bytes.length * random);
        for (int k = 0; k < random; k++) {
          buf.writeBytes(bytes);

        }
        channel.writeInbound(buf);
      } catch (UnsupportedEncodingException e) {
        e.printStackTrace();
      }

    }

  }

  /**
   * LengthFieldBasedFrameDecoder 使用实例
   */
  @Test
  public void testLengthFieldBasedFrameDecoder2() {
    try {

      final LengthFieldBasedFrameDecoder spliter =
          new LengthFieldBasedFrameDecoder(1024, 0, 4,
              2, 6);
      ChannelInitializer i = new ChannelInitializer<EmbeddedChannel>() {
        protected void initChannel(EmbeddedChannel ch) {
          ch.pipeline().addLast(spliter);
          ch.pipeline().addLast(new StringDecoder(Charset.forName("UTF-8")));
          ch.pipeline().addLast(new StringProcessHandler());
        }
      };
      EmbeddedChannel channel = new EmbeddedChannel(i);

      for (int j = 1; j <= 100; j++) {

        // 分配一个bytebuf
        ByteBuf buf = Unpooled.buffer();

        String s = j + "次发送->" + CONTENT;

        byte[] bytes = s.getBytes("UTF-8");

        //首先 写入头部  head，包括 后面的数据长度

        buf.writeInt(bytes.length);

        buf.writeChar(VERSION);

        //然后 写入  content
        buf.writeBytes(bytes);

        channel.writeInbound(buf);
      }

      Thread.sleep(Integer.MAX_VALUE);
    } catch (InterruptedException e) {
      e.printStackTrace();
    } catch (UnsupportedEncodingException e) {
      e.printStackTrace();
    }
  }
}
