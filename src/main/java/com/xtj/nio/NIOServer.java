package com.xtj.nio;

import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;

/**
 * @author 涓
 * @date 2021/2/10
 */
public class NIOServer {
    public static void main(String[] args) throws Exception{

        ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();

        Selector selector = Selector.open();

        serverSocketChannel.socket().bind(new InetSocketAddress(6666));
        // 设置非阻塞
        serverSocketChannel.configureBlocking(false);
        // 把serverSocketChannel 注册到 select , 关心事件为： OP_ACCEPT 有新的客户端连接
        serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);

        int count = 0 ;
        while (true) {
            // Select 进行监听 select 方法，返回有事件发生的通道数
            if (selector.select(1000) == 0) {
                System.out.println("服务器等待了"+ ++count +",无连接");
                continue;
            }

            count = 0;
            // 获取selectionKey集合，反向获取通道
            Set<SelectionKey> selectionKeys = selector.selectedKeys();

            // //遍历 Set<SelectionKey>，使用迭代器遍历
            Iterator<SelectionKey> keyIterator = selectionKeys.iterator();
            while (keyIterator.hasNext()) {
                // //获取到SelectionKey
                SelectionKey key = keyIterator.next();
                // 根据 key 对应的通道发生的事件，做相应的处理
                // 如果是 OP_ACCEPT，有新的客户端连接
                if (key.isAcceptable()) {
                    // 该客户端生成一个 SocketChannel
                    SocketChannel socketChannel = serverSocketChannel.accept();

                    System.out.println("客户端连接成功，生成了一个SocketChannel：" + socketChannel.hashCode());

                    socketChannel.configureBlocking(false);
                    //将socketChannel注册到selector，关注事件为 OP_READ，同时给SocketChannel关联一个Buffer
                    socketChannel.register(selector,SelectionKey.OP_READ, ByteBuffer.allocate(1024));
                }

                if (key.isReadable()) {
                    // 通过key，反向获取到对应的Channel
                    SocketChannel channel = (SocketChannel) key.channel();

                    ByteBuffer buffer = (ByteBuffer) key.attachment();

                    channel.read(buffer);

                    System.out.println("from 客户端：" + new String(buffer.array()));
                }
                // 手动从集合中移除当前的 selectionKey，防止重复操作
                keyIterator.remove();
            }
        }
    }
}
