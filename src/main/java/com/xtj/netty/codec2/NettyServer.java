package com.xtj.netty.codec2;


import com.xtj.netty.codec.StudentPOJO;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.protobuf.ProtobufDecoder;

/**
 * @author 涓
 * @date 2021/2/12
 */
public class NettyServer {
    public static void main(String[] args) {
        // 创建BossGroup 和 WorkerGroup
        // 1、创建两个线程组，bossGroup 和 workerGroup
        // 2、boosGroup 只是处理连接请求，真正和客户端业务处理，会交给 workerGroup 完成
        // 3、两个都是无限循环
        //4、bossGroup 和 workerGroup 含有的子线程（NioEventLoop）个数为实际 cpu 核数 * 2
        EventLoopGroup bossGroup = new NioEventLoopGroup();
        EventLoopGroup workerGroup = new NioEventLoopGroup();

        try {
            // 创建服务器端的启动对象，配置参数
            ServerBootstrap bootstrap = new ServerBootstrap();

            // 使用链式编程来进行设置、配置
            bootstrap.group(bossGroup,workerGroup)  //设置两个线程组
                    .channel(NioServerSocketChannel.class)  //使用 NioServerSocketChannel 作为服务器的通道实现
                    .option(ChannelOption.SO_BACKLOG,128)  //设置线程队列得到连接个数
                    .childOption(ChannelOption.SO_KEEPALIVE,true)  //设置保持活动连接状态
                    .childHandler(new ChannelInitializer<SocketChannel>() {//为accept channel的pipeline预添加的handler
                        //给 pipeline 添加处理器，每当有连接accept时，就会运行到此处。

                        @Override
                        protected void initChannel(SocketChannel socketChannel) throws Exception {
                            ChannelPipeline pipeline = socketChannel.pipeline();

                            pipeline.addLast("decoder",new ProtobufDecoder(MyDataInfo.MyMessage.getDefaultInstance()));
                            pipeline.addLast(new NettyServerHandler());
                        }
                    }); //给我们的 workerGroup 的 EventLoop 对应的管道设置处理器
            System.out.println("......服务器 is ready");
            // 绑定一个端口并且同步，生成一个ChannelFuture 对象
            // 启动服务器（并绑定端口）
            ChannelFuture future = bootstrap.bind(6668).sync();

            // 对关闭通道进行监听
            future.channel().closeFuture().sync();

        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }


    }
}
