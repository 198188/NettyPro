package com.xtj.netty.protocoltcp;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import io.netty.handler.codec.ReplayingDecoder;

import java.util.List;

/**
 *
 * ReplayingDecoder 另一种解码器
 * @author 涓
 * @date 2021/2/15
 */
//public class MyMessageDecoder extends ReplayingDecoder<Void> {
public class MyMessageDecoder extends ByteToMessageDecoder {

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
        System.out.println("MyMessageDecoder decode 被调用");

        // 方式1
        int length;
        if (in.readableBytes() >= (length =  in.readInt())) {
            byte[] content = new byte[length];
            in.readBytes(content);

            // 封装成MessageProtocol 对象，放入 Out .传递下一个handler 业务处理
            MessageProtocol messageProtocol = new MessageProtocol();
            messageProtocol.setLen(length);
            messageProtocol.setContent(content);

            out.add(messageProtocol);
        }
        // 方式2
//        int length = in.readInt();
//        byte[] content = new byte[length];
//        in.readBytes(content);
//
//        // 封装成MessageProtocol 对象，放入 Out .传递下一个handler 业务处理
//        MessageProtocol messageProtocol = new MessageProtocol();
//        messageProtocol.setLen(length);
//        messageProtocol.setContent(content);
//
//        out.add(messageProtocol);
    }
}
