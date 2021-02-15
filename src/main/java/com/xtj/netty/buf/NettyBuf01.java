package com.xtj.netty.buf;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

import java.nio.ByteBuffer;
import java.sql.SQLOutput;

/**
 * @author 涓
 * @date 2021/2/13
 */
public class NettyBuf01 {
    public static void main(String[] args) {
        ByteBuf buffer = Unpooled.buffer(10);
        for (int i = 0; i < 10; i++) {
            buffer.writeByte(i);
        }
        // 获取buff的大小
        int capacity = buffer.capacity();
        // 输出
        for (int i = 0; i < capacity; i++) {
            System.out.println(buffer.getByte(i));
            System.out.println(buffer.readByte());
        }

    }
}
