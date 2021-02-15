package com.xtj.http;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.HttpServerCodec;

/**
 * @author 涓
 * @date 2021/2/12
 */
public class TestServerInitializer extends ChannelInitializer<SocketChannel> {

    @Override
    protected void initChannel(SocketChannel ch) throws Exception {
        // 向管道加入处理器

        // 得到管道
        ChannelPipeline pipeline = ch.pipeline();
        // 加入一个netty提供的httpServerCoder: code => [coder - decoder]
        // 1、HttpServerCodec 是netty 提供的处理http的编解码器
        pipeline.addLast("MyHttpServerCodec",new HttpServerCodec());
        //2、增加自定义的Handler
        pipeline.addLast("MyTestHttpServerHandler",new TestHttpServerHandler());
    }
}
