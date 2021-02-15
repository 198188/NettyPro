package com.xtj.nio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.Buffer;
import java.nio.ByteBuffer;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Arrays;

/**
 * @author 涓
 * @date 2021/2/10
 */
public class ScatteringAndGathering {
    /**
     * Scattering: 将数据写入到buffer时，可以采用Buffer数组，依次写
     * Gathering: 从buffer读取数据时，也可以采用数组形式，依次读
     * @param args
     */
    public static void main(String[] args) throws IOException {

        ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
        InetSocketAddress inetSocketAddress = new InetSocketAddress(7000);
        // 绑定端口到socket,并启动
        serverSocketChannel.socket().bind(inetSocketAddress);

        // 创建一个Buffer数组
        ByteBuffer[] byteBuffers = new ByteBuffer[2];
        byteBuffers[0] = ByteBuffer.allocate(5);
        byteBuffers[1] = ByteBuffer.allocate(3);

        // 等待客户端的连接
        SocketChannel socketChannel = serverSocketChannel.accept();

        // 假设从客户端接收8个字符
        int msgLength = 8;

        while (true) {
            int byteRead = 0;
            while (byteRead < msgLength) {
                System.out.println("读取数据到buffer中");
                // 读取数据到buffer中
                long l = socketChannel.read(byteBuffers);
                // /累计读取的字节数
                byteRead+=l;
                System.out.println("byteRead: "+byteRead);
                // 使用流打印，看看当前这个buffer的position和limit
                Arrays.stream(byteBuffers)
                        .map(buffer -> "position=" + buffer.position() + ",limit="+buffer.limit())
                        .forEach(System.out::println);
            }

            Arrays.asList(byteBuffers).forEach(buffer -> buffer.flip());

            int byteWrite = 0 ;
            while (byteWrite < msgLength) {
                // 写入数据到buffer
                System.out.println("写入数据到buffer");
                long l = socketChannel.write(byteBuffers);
                byteWrite += l;
                System.out.println("byteWrite: "+byteWrite);

            }

            Arrays.asList(byteBuffers).forEach(buffer->buffer.clear());
            System.out.println("byteRead= "+byteRead+",byteWrite= "+byteWrite+",msgLength= "+msgLength);

        }
    }
}
