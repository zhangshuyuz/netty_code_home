package com.yuu.nio;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

/**
 * 拷贝刚才的文件，放入项目下
 */
public class NIOFileChannel03 {
    public static void main(String[] args) throws IOException {

        File file = new File("d:\\file01.txt");
        FileInputStream fileInputStream = new FileInputStream(file);
        FileChannel channel01 = fileInputStream.getChannel();

        FileOutputStream fileOutputStream = new FileOutputStream("file02.txt");
        FileChannel channel02 = fileOutputStream.getChannel();

        ByteBuffer byteBuffer = ByteBuffer.allocate(5);

        // 因为不知道文件有多大，因此使用循环
        while (true) {

            // 每次读取后，清除Buffer
            byteBuffer.clear();

            // 读取数据到Buffer，返回读取数据字节数
            int read = channel01.read(byteBuffer);
            if (read == -1) {
                // 读取结束
                break;
            }
            // 反转Buffer
            byteBuffer.flip();
            // Buffer数据写入到Channel
            channel02.write(byteBuffer);

        }

        fileInputStream.close();
        fileOutputStream.close();

    }
}
