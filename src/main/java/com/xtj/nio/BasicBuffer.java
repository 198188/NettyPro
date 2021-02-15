package com.xtj.nio;

import java.nio.IntBuffer;

/**
 * @author 涓
 * @date 2021/2/9
 */
public class BasicBuffer {
    public static void main(String[] args) {
        // 创建一个容量为5的buffer
        IntBuffer allocate = IntBuffer.allocate(5);
        for (int i = 0; i < allocate.capacity(); i++) {
            allocate.put(i*2);
        }
        // 读取数据
        // buffer反转
        allocate.flip();
        while (allocate.hasRemaining()) {
            System.out.println(allocate.get());
        }
    }
}
