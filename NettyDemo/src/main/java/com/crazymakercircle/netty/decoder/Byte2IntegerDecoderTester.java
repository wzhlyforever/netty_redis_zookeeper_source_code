package com.crazymakercircle.netty.decoder;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.embedded.EmbeddedChannel;
import org.junit.Test;

/**
 * @program: netty_redis_zookeeper_source_code
 * @description: 整数解码器的使用案例
 * @author: Mr.Wang
 * @create: 2022-07-27 14:40
 **/
public class Byte2IntegerDecoderTester {

  @Test
  public void testByteToIntegerDecoder() {
    ChannelInitializer i = new ChannelInitializer<EmbeddedChannel>() {
      protected void initChannel(EmbeddedChannel ch) {
        ch.pipeline().addLast(new Byte2IntegerDecoder());
        ch.pipeline().addLast(new IntegerProcessHandler());
      }
    };
    EmbeddedChannel channel = new EmbeddedChannel(i);

    for (int j = 0; j < 100; j++) {
      ByteBuf buf = Unpooled.buffer();
      buf.writeInt(j);
      channel.writeInbound(buf);
    }

    try {
      Thread.sleep(Integer.MAX_VALUE);
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
  }

}
