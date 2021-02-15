package com.xtj.netty;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.ChannelPipeline;
import io.netty.util.CharsetUtil;

/**
 * @author 涓
 * @date 2021/2/12
 */
public class NettyServerHandler extends ChannelInboundHandlerAdapter {
    /**
     * 读取客户端发送过来的消息
     * @param ctx 上下文对象，含有管道pipeline,通道channel，地址
     * @param msg 客户端发送过来的数据，默认是object
     * @throws Exception
     */
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        System.out.println("服务器读取线程: "+ Thread.currentThread().getName());
        System.out.println("server ctx = " + ctx);

        //看看Channel和Pipeline的关系
        Channel channel = ctx.channel();
        ChannelPipeline pipeline = ctx.pipeline(); //本质是个双向链表，出栈入栈

        // 将msg转成一个ByteBuf,比NIO的ByteBuffer性能更高
        ByteBuf buf = (ByteBuf) msg;
        System.out.println("客户端发送过来的消息是： "+ buf.toString(CharsetUtil.UTF_8));
        System.out.println("客户端的地址："+ctx.channel().remoteAddress());
    }

    /**
     * 数据读取完毕
     * @param ctx
     * @throws Exception
     */
    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        // 它是write + flush ,将数据写入到缓存buffer，并将buffer中的数据flush进通道
        // 一般讲，我们对这个发送的数据进行编码
        ctx.writeAndFlush(Unpooled.copiedBuffer("hello 客户端！",CharsetUtil.UTF_8));
    }

    /**
     * 处理异常，一般是关闭通道
      * @param ctx
     * @param cause
     * @throws Exception
     */
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        ctx.close();
    }
}
