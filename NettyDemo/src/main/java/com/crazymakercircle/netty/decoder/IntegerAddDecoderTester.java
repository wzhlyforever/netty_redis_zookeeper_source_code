package com.crazymakercircle.netty.decoder;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.embedded.EmbeddedChannel;
import org.junit.Test;

/**
 * @program: netty_redis_zookeeper_source_code
 * @description:
 * @author: Mr.Wang
 * @create: 2022-07-27 17:05
 **/
public class IntegerAddDecoderTester {


  /**
   * 整数解码器的使用实例
   */
  @Test
  public void testByteToIntegerDecoder() {
    ChannelInitializer i = new ChannelInitializer<EmbeddedChannel>() {
      protected void initChannel(EmbeddedChannel ch) {
        ch.pipeline().addLast(new IntegerAddDecoder());
        ch.pipeline().addLast(new IntegerProcessHandler());
      }
    };
    EmbeddedChannel channel = new EmbeddedChannel(i);

    for (int j = 0; j < 10; j++) {
      ByteBuf buf = Unpooled.buffer();
      buf.writeInt(j);
      channel.writeInbound(buf);
    }
    
  }

}
