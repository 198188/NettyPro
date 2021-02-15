package com.xtj.nio;

import java.io.FileOutputStream;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

/**
 * @author 涓
 * @date 2021/2/9
 */
public class NIOFileChannel01 {
    public static void main(String[] args) throws Exception{
        String str = "Hello Word!";


        // 创建buffer流
        ByteBuffer byteBuffer = ByteBuffer.allocate(1024);

        // 将字符串放入buffer
        byteBuffer.put(str.getBytes());

        byteBuffer.flip();

        // 放入channel
        FileOutputStream fileOutputStream = new FileOutputStream("d:\\file01.txt");
        FileChannel channel = fileOutputStream.getChannel();
        channel.write(byteBuffer);

        // 关闭输出流
        fileOutputStream.close();

    }
}
