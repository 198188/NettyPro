package com.xtj.netty.inboundhandlerAndOutBoundHandler;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageCodec;
import io.netty.handler.codec.ByteToMessageDecoder;

import java.util.List;

/**
 * @author 涓
 * @date 2021/2/15
 */
public class MyByteToLongDecoder extends ByteToMessageDecoder {
    /**
     * Decode 会根据接收的数据，被调用多次，直到确定没有新的元素被添加到List，或者是ByteBuf 没有更多的可读字节为止
     * 如果 list out 不为空 ，就会将list 内容传递给你下一个 channelInBoundHandler 处理，该处理器的方法也会被调用多次
     * @param ctx 上下文对象
     * @param in 入站的ByteBuf
     * @param out List集合，将解码后的数据传给下一个handler
     * @throws Exception
     */
    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
        System.out.println("MyByteToLongDecoder decode  被调用");
        // 因为 Long 8个字节 需要判断有8个字节 ，才能读取一个Long
        if (in.readableBytes() >= 8) {
            out.add(in.readLong());
        }
    }
}
