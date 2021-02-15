package com.xtj.http;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.http.*;
import io.netty.util.CharsetUtil;

import java.lang.management.GarbageCollectorMXBean;
import java.net.URI;
import java.net.URL;

/**
 * @author 涓
 * @date 2021/2/12
 */
public class TestHttpServerHandler  extends SimpleChannelInboundHandler<HttpObject> {

    /**
     * 读取客户端的数据
     * @param ctx
     * @param msg 客户端和服务器端互相通讯的数据被封装成 HttpObject
     * @throws Exception
     */
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, HttpObject msg) throws Exception {

        if (msg instanceof HttpRequest) {
            System.out.println("msg 类型 = " + msg.getClass());
            System.out.println("客户端地址： "+ ctx.channel().remoteAddress());
            // 获取
            HttpRequest request  = (HttpRequest) msg;

            // 获取url，进行路径过滤
            URI uri = new URI(request.uri());
            if ("/favicon.ico".equals(uri.getPath())) {
                System.out.println("请求了 favicon.ico，不做响应");
            }

            ByteBuf content = Unpooled.copiedBuffer("Hello Word", CharsetUtil.UTF_8);
            //构造一个http的响应，即HTTPResponse
            DefaultFullHttpResponse response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.OK, content);
            response.headers().set(HttpHeaderNames.CONTENT_TYPE, "text/plain");
            response.headers().set(HttpHeaderNames.CONTENT_LENGTH, content.readableBytes());

            //将构建好的 response 返回
            ctx.writeAndFlush(response);

        }
    }
}
