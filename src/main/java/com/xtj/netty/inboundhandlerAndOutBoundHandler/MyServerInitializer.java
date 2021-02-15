package com.xtj.netty.inboundhandlerAndOutBoundHandler;

import com.sun.scenario.effect.impl.prism.ps.PPSBlend_ADDPeer;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;

/**
 * @author 涓
 * @date 2021/2/14
 */
public class MyServerInitializer extends ChannelInitializer<SocketChannel> {
    @Override
    protected void initChannel(SocketChannel ch) throws Exception {
        ChannelPipeline pipeline = ch.pipeline();

        // 入站的handler进行解码 MyByteToLongDecoder
        pipeline.addLast(new MyByteToLongDecoder());

        // 对出站的handle进行编码
        pipeline.addLast(new MyLongToByteEncoder());

        // 自定义的handler 处理业务逻辑
        pipeline.addLast(new MyServerHandler());
    }
}
