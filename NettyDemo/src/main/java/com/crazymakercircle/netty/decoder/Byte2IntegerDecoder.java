package com.crazymakercircle.netty.decoder;

import com.crazymakercircle.util.Logger;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import java.util.List;

/**
 * @program: netty_redis_zookeeper_source_code
 * @description:
 * @author: Mr.Wang
 * @create: 2022-07-27 14:31
 **/
public class Byte2IntegerDecoder extends ByteToMessageDecoder {

  @Override
  protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {

    while (in.readableBytes() >= 4) {
      final int i = in.readInt();
      Logger.info("解码出一个整数" + i);
      out.add(i);
    }

  }
}
