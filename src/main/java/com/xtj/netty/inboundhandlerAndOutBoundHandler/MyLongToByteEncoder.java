package com.xtj.netty.inboundhandlerAndOutBoundHandler;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

/**
 * @author 涓
 * @date 2021/2/15
 */
public class MyLongToByteEncoder extends MessageToByteEncoder<Long> {
    /**
     * 编码方法
      * @param ctx
     * @param msg
     * @param out
     * @throws Exception
     */
    @Override
    protected void encode(ChannelHandlerContext ctx, Long msg, ByteBuf out) throws Exception {
        System.out.println("MyLongToByteEncoder encoder 被调用");
        System.out.println("msg = "+msg);
        out.writeLong(msg);
    }
}
