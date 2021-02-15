package com.xtj.netty.inboundhandlerAndOutBoundHandler;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;

/**
 * @author 涓
 * @date 2021/2/15
 */
public class MyClientInitializer extends ChannelInitializer<SocketChannel> {
    @Override
    protected void initChannel(SocketChannel ch) throws Exception {
        ChannelPipeline pipeline = ch.pipeline();

        // 加入一个出站的handler 对数据进行一个编码
        pipeline.addLast(new MyLongToByteEncoder());

        // 这是一个入站的handler解码器
        pipeline.addLast(new MyByteToLongDecoder());

        // 加入一个入站的handler 处理业务
        pipeline.addLast(new MyClientHandler());
    }
}
