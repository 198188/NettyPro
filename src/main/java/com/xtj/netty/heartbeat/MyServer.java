package com.xtj.netty.heartbeat;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.handler.timeout.IdleStateHandler;

import java.util.concurrent.TimeUnit;

/**
 * @author 涓
 * @date 2021/2/13
 */
public class MyServer {
    public static void main(String[] args) {
        EventLoopGroup bossGroup = new NioEventLoopGroup(1);
        EventLoopGroup workerGroup = new NioEventLoopGroup();

        try{
            ServerBootstrap bootstrap = new ServerBootstrap();
            bootstrap.group(bossGroup,workerGroup)
                    .channel(NioServerSocketChannel.class)
                    .handler(new LoggingHandler(LogLevel.INFO))
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {
                            ChannelPipeline pipeline = ch.pipeline();
                            //加入一个 netty 提供的 IdleStateHandler
                            /**
                             * 1、IdleStateHandler 是 netty 提供的检测空闲状态的处理器
                             * 2、long readerIdleTime：表示多长时间没有读，就会发送一个心跳检测包检测是否还是连接的状态
                             * 3、long writerIdleTime：表示多长时间没有写，就会发送一个心跳检测包检测是否还是连接的状态
                             * 4、long allIdleTime：表示多长时间没有读写，就会发送一个心跳检测包检测是否还是连接的状态
                             * 5、当 IdleStateEvent 触发后，就会传递给管道的下一个 Handler，通过调用（触发）下一个Handler的 userEventTriggered，在该方法区处理这个事件。
                             */
                            pipeline.addLast(new IdleStateHandler(3,5,7, TimeUnit.SECONDS));

                            // 加入一个对空闲进一步检测处理的handler(自定义)
                            pipeline.addLast(new MyServerHandler());



                        }
                    });
            ChannelFuture channelFuture = bootstrap.bind(7000).sync();
            channelFuture.channel().closeFuture().sync();


        }catch (Exception e) {
            e.printStackTrace();
        } finally {
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
    }
}
