package com.xtj.netty.groupchat;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.util.concurrent.GlobalEventExecutor;
import sun.plugin.net.protocol.jar.CachedJarURLConnection;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author 涓
 * @date 2021/2/13
 */
public class GroupChatServerHandler extends SimpleChannelInboundHandler<String> {
    // 定义一个channel组，管理所有的Channel
    // globalEventExecutor.INSTANCE 是全局事件执行器，是一个单例
    private static ChannelGroup channelGroup = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    /**
     *  此方法表示连接建立，一旦建立连接，就第一个被执行
     * @param ctx
     * @throws Exception
     */
    @Override
    public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
        Channel channel = ctx.channel();
        // 该方法会将 ChannelGroup 中的所有Channel 遍历，并发送消息，并不需要我们自己去遍历
        channelGroup.writeAndFlush("[客户端] "+channel.remoteAddress() +"  "+ sdf.format(new Date())+ " 加入聊天 \n");
        // 将当前的channel加入到ChannelGroup中
        channelGroup.add(channel);
    }

    /**
     * 表示channe处于活动状态 ，提示XXX上线
     * @param ctx
     * @throws Exception
     */
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        System.out.println(ctx.channel().remoteAddress() + " " + sdf.format(new Date()) + "上线了~");
    }

    /**
     * 表示Channel断开连接，将XX客户离开信息推送给当前在线客户
     * @param ctx
     * @throws Exception
     */
    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        Channel channel = ctx.channel();
        // channelGroup.writeAndFlush("[客户端] "+ channel.remoteAddress() + " " + sdf.format(new Date())+ "下线了\n ");
        System.out.println("[客户端] "+ channel.remoteAddress() + " " + sdf.format(new Date())+ "下线了\n ");

    }

    /**
     * 断开连接。将xx客户离开信息推送给当前在线的客户
     * @param ctx
     * @throws Exception
     */
    @Override
    public void handlerRemoved(ChannelHandlerContext ctx) throws Exception {
        Channel channel = ctx.channel();
        channelGroup.writeAndFlush("[客户端] "+ channel.remoteAddress() + " " + sdf.format(new Date())+ "离开了\n ");
        System.out.println("当前channelGroup大小 ：" + channelGroup.size());

    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, String msg) throws Exception {
        // 获取当前Channel
        Channel channel = ctx.channel();
        // 这时遍历ChannelGroup，根据不同的情况，回显不同的消息
        channelGroup.forEach(item->{
            if (item != channel) {
                item.writeAndFlush("[客户] "+channel.remoteAddress() + " 发送了消息: "+ msg + "\n");
            } else {
                // 把自己发送的消息发送给自己
                item.writeAndFlush("[自己] 发送了消息: "+msg+" \n");
            }
        });
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        ctx.close();
    }
}
