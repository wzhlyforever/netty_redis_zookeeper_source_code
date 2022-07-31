package com.crazymakercircle.netty.codec;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageCodec;
import java.util.List;

/**
 * @program: netty_redis_zookeeper_source_code
 * @description: 整数到字节  字节到整数
 * @author: Mr.Wang
 * @create: 2022-07-31 12:53
 **/
public class Byte2IntegerCodec extends ByteToMessageCodec<Integer> {

  @Override
  protected void encode(ChannelHandlerContext ctx, Integer msg, ByteBuf out) throws Exception {
    out.writeInt(msg);

    System.out.println("write Integer = " + msg);
  }

  @Override
  protected void decode(ChannelHandlerContext ctx, ByteBuf in, List out) throws Exception {
    final int i = in.readInt();
    System.out.println("Decoder i= " + i);
    out.add(i);
  }
}
