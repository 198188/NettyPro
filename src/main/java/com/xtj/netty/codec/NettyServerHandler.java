package com.xtj.netty.codec;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.*;
import io.netty.util.CharsetUtil;

/**
 * @author 涓
 * @date 2021/2/12
 */
// public class NettyServerHandler extends ChannelInboundHandlerAdapter {
public class NettyServerHandler extends SimpleChannelInboundHandler<StudentPOJO.Student> {
    /**
     * 读取客户端发送过来的消息
     * @param ctx 上下文对象，含有管道pipeline,通道channel，地址
     * @param msg 客户端发送过来的数据，默认是object
     * @throws Exception
     */
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, StudentPOJO.Student msg) throws Exception {
        System.out.println("客户端发送的数据 id = "+msg.getId() + ",名字 = "+msg.getName());
    }

//    @Override
//    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
//        // 读取客户端发送的studentPojo.Student
//        if (msg instanceof StudentPOJO.Student) {
//            StudentPOJO.Student student = (StudentPOJO.Student) msg;
//            System.out.println("客户端发送的数据 id = "+student.getId() + ",名字 = "+student.getName());
//        }
//    }

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
