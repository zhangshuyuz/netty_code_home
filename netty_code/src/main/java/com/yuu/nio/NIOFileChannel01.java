package com.yuu.nio;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

/**
 * 使用 FileChannel和ByteBuffer，将一段话写入文件。文件不存在则创建文件
 */
public class NIOFileChannel01 {
    public static void main(String[] args) throws IOException {
        String s = "hello, world!";

        // 创建文件输出流
        FileOutputStream fileOutputStream = new FileOutputStream("d:\\file01.txt");
        // 通过输出流，获取对应的FileChannel
        FileChannel fileChannel = fileOutputStream.getChannel();
        // 创建一个缓冲区
        ByteBuffer byteBuffer = ByteBuffer.allocate(1024);

        // 将String放入Buffer
        byteBuffer.put(s.getBytes());
        // 反转Buffer
        byteBuffer.flip();
        // 将Buffer数据写入Channel
        fileChannel.write(byteBuffer);

        // 最后关闭FileOutputStream流
        fileOutputStream.close();
    }
}
