package com.yuu.nio;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;

public class NIOMappedByteBuffer {
    public static void main(String[] args) throws IOException {
        // 使用RandomAccessFile对文件进行rw操作
        RandomAccessFile randomAccessFile = new RandomAccessFile("d:\\file01.txt", "rw");

        // 获取对应通道
        FileChannel channel = randomAccessFile.getChannel();
        // 通过Channel获取MappedByteBuffer
        /*
         * map()方法的各个参数说明
         * 第一个参数，代表使用的模式。FileChannel.MapMode.READ_WRITE说明是读写模式
         * 第二个参数，代表直接修改的起始位置，从0开始计算。0代表起始位置
         * 第三个参数，代表从起始位置开始算，允许映射到内存的大小，单位是字节。5代表5个字节
         */
        MappedByteBuffer mappedBuffer = channel.map(FileChannel.MapMode.READ_WRITE, 0, 5);

        // 修改
        mappedBuffer.put(0, (byte)'3');
        mappedBuffer.put(4, (byte)'9');

        // 修改成功，关闭流
        randomAccessFile.close();

    }
}
