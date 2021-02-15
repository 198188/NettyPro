package com.xtj.nio.GroupChat;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.util.GregorianCalendar;
import java.util.Iterator;

/**
 * @author 涓
 * @date 2021/2/10
 */
public class Server {

    private Selector selector;

    private ServerSocketChannel listenChannel;

    private static final int PORT = 6666;

    public Server() {
        try {
            // 得到选择器
            selector = Selector.open();
            listenChannel = ServerSocketChannel.open();
            // 绑定端口
            listenChannel.socket().bind(new InetSocketAddress(PORT));
            // 设置非阻塞方式
            listenChannel.configureBlocking(false);
            // 将listenChannel注册到selector，绑定监听事件
            listenChannel.register(selector, SelectionKey.OP_ACCEPT);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // 监听
    public void listen() {
        try {
            while (true) {
                int count = selector.select();
                if (count > 0) {
                    // 有事件
                    Iterator<SelectionKey> iterator = selector.selectedKeys().iterator();
                    while (iterator.hasNext()) {
                        // 取出SelectionKey
                        SelectionKey key = iterator.next();

                        // 监听到accept，连接事件
                        if (key.isAcceptable()) {
                            SocketChannel socketChannel = listenChannel.accept();
                            // 将该channel设置非阻塞并注册到selector
                            socketChannel.configureBlocking(false);
                            socketChannel.register(selector,SelectionKey.OP_READ);
                            //提示
                            System.out.println(socketChannel.getRemoteAddress() + " 上线...");
                        }
                        if (key.isReadable()) {
                            //通道可以读取数据，即server端收到客户端的消息，
                            //处理读（专门写方法）
                            readData(key);
                        }
                        iterator.remove();
                    }
                } else {
                    System.out.println("等待。。。");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // 读取客户端消息
    public void readData(SelectionKey key) {
        // 定义一个SocketChannel
        SocketChannel channel = null;
        try{
            channel = (SocketChannel) key.channel();
            ByteBuffer buffer = ByteBuffer.allocate(1024);
            int count = channel.read(buffer);
            // 根据count值判断是否读取到数据
            if (count > 0) {
                // 把缓冲区的数据转成字符串
                String msg = new String(buffer.array());
                System.out.println("from 客户端: "+msg);

                //向其他的客户端转发消息（去掉自己），专门写一个方法处理
                sendInfoToOtherClients(msg, channel);

            }
        }catch (Exception e) {
            try{
                System.out.println(channel.getRemoteAddress() + "离线了。。。");
                // 取消注册
                key.cancel();
                // 关闭通道
                channel.close();
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
            e.printStackTrace();
        }
    }

    //转发消息给其他客户端（通道）
    private void sendInfoToOtherClients(String msg, SocketChannel self) throws IOException {
        System.out.println("服务器转发消息中。。。");
        // 遍历所有注册到selector上的SocketChannel,并排除self
        for (SelectionKey key : selector.keys()) {
            // 通过key取出对应的SocketChannel
            Channel targetChannel = key.channel();
            // 排除自己
            if (targetChannel instanceof SocketChannel && targetChannel != self) {
                // 转型
                SocketChannel dest = (SocketChannel) targetChannel;
                ByteBuffer buffer = ByteBuffer.wrap(msg.getBytes());
                // 将buffer的数据写入通道
                dest.write(buffer);
            }
        }
    }

    public static void main(String[] args) {
        // 创建服务器对象
        Server server = new Server();
        server.listen();
    }
}
