package com.crazymakercircle.netty.decoder;

import com.crazymakercircle.util.RandomUtil;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.embedded.EmbeddedChannel;
import java.nio.charset.Charset;
import java.util.Random;
import org.junit.Test;

/**
 * @program: netty_redis_zookeeper_source_code
 * @description:
 * @author: Mr.Wang
 * @create: 2022-07-27 18:03
 **/
public class StringReplayDecoderTester {

  static String content = "疯狂创客圈：高性能学习社群!";

  @Test
  public void testStringReplayDecoder() {

    ChannelInitializer<EmbeddedChannel> i = new ChannelInitializer<EmbeddedChannel>() {
      @Override
      protected void initChannel(EmbeddedChannel ch) throws Exception {
        ch.pipeline().addLast(new StringReplayDecoder());
        ch.pipeline().addLast(new StringProcessHandler());
      }
    };

    EmbeddedChannel channel = new EmbeddedChannel(i);

    byte[] bytes = content.getBytes(Charset.forName("utf-8"));

    for (int j = 0; j < 100; j++) {
      int inMod = RandomUtil.randInMod(3);
      ByteBuf buf = Unpooled.buffer();
      buf.writeInt(bytes.length * inMod);

      for (int k = 0; k < inMod; k++) {
        buf.writeBytes(bytes);
      }

      channel.writeInbound(buf);
    }
  }

}
