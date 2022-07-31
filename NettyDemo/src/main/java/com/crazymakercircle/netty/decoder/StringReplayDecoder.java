package com.crazymakercircle.netty.decoder;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ReplayingDecoder;
import java.util.List;

/**
 * @program: netty_redis_zookeeper_source_code
 * @description:
 * @author: Mr.Wang
 * @create: 2022-07-27 17:25
 **/
public class StringReplayDecoder extends ReplayingDecoder<StringReplayDecoder.STATUS> {

  int length;

  byte[] inBytes;

  enum STATUS {
    PHASE_1,
    PHASE_2;
  }

  public StringReplayDecoder() {
    super(STATUS.PHASE_1);
  }

  @Override
  protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {

    switch (state()) {
      case PHASE_1:
        length = in.readInt();
        inBytes = new byte[length];
        checkpoint(STATUS.PHASE_2);
        break;
      case PHASE_2:
        in.readBytes(inBytes, 0, length);
        String content = new String(inBytes, "utf-8");
        out.add(content);
        checkpoint(STATUS.PHASE_1);
        break;
      default:
        break;
    }
  }


}
