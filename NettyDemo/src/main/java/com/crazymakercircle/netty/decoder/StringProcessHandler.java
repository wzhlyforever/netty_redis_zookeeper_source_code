package com.crazymakercircle.netty.decoder;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

/**
 * @program: netty_redis_zookeeper_source_code
 * @description:
 * @author: Mr.Wang
 * @create: 2022-07-27 17:54
 **/
public class StringProcessHandler extends ChannelInboundHandlerAdapter {

  @Override
  public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {

    if (msg instanceof String) {
      String s = (String) msg;
      System.out.println("¥Ú”°: " + s);
    } else {
      super.channelRead(ctx, msg);
    }


  }
}
