package com.yuu.nio;

import java.nio.IntBuffer;

public class BasicBuffer {
    // 举例说明Buffer使用
    public static void main(String[] args) {
        // 创建Buffer
        IntBuffer allocate = IntBuffer.allocate(5);// 创建一个IntBuffer，大小可以存放5个int

        // 向Buffer存放数据
        for (int i = 0; i < allocate.capacity(); i++) {
            allocate.put(i + 3);
        }

        // 从Buffer读取数据
        // 将Buffer进行读写切换
        allocate.flip();
        // 切换完后读取数据
        while (allocate.hasRemaining()) {
            // Buffer中的get()方法中本身维护了个索引，每次使用get()方法索引向后移动
            System.out.println(allocate.get());
        }
    }

}
