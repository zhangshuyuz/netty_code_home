package com.yuu.nio;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

/**
 * 读取文件数据，控制台展示
 */
public class NIOFileChannel02 {
    public static void main(String[] args) throws IOException {
        File file = new File("d:\\file01.txt");
        // 获取文件输入流
        FileInputStream fs = new FileInputStream(file);
        // 获取FileChannel
        FileChannel fileChannel = fs.getChannel();

        // 创建ByteBuffer
        ByteBuffer byteBuffer = ByteBuffer.allocate(1024);

        // 往Buffer中读数据
        fileChannel.read(byteBuffer);

        // 从Buffer中取出数据
        System.out.println(new String(byteBuffer.array()));

        // 关闭流
        fs.close();
    }

}
