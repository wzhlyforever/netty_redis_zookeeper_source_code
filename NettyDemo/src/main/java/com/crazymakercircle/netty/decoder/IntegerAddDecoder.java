package com.crazymakercircle.netty.decoder;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ReplayingDecoder;
import java.util.List;

/**
 * @program: netty_redis_zookeeper_source_code
 * @description:
 * @author: Mr.Wang
 * @create: 2022-07-27 16:26
 **/
public class IntegerAddDecoder extends ReplayingDecoder<IntegerAddDecoder.STATUS> {

  int first;
  int second;
  enum STATUS {
    PHASE_1,//第一个阶段，则仅仅提取第一个整数，完成后进入第二个阶段
    PHASE_2 //第二个整数，提取后还需要结算相加的结果，并且输出结果
  }

  public IntegerAddDecoder() {
    super(STATUS.PHASE_1);
  }

  @Override
  protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {

    switch (this.state()) {
      case PHASE_1:
        first = in.readInt();
        System.out.println(first);
        checkpoint(STATUS.PHASE_2);
        break;

      case PHASE_2:
        second = in.readInt();
        int sum = first + second;
        System.out.println(sum);
        out.add(sum);
        checkpoint(STATUS.PHASE_1);
        break;
      default:
        break;
    }

  }
}
