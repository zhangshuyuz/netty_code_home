package com.yuu.nio;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;

/**
 * 使用快速拷贝，拷贝文件到项目下
 */
public class NIOFileChannel04 {
    public static void main(String[] args) throws IOException {
        FileInputStream fileInputStream = new FileInputStream("d:\\AnimeCharacter\\girl.jpg");
        FileChannel channel01 = fileInputStream.getChannel();

        FileOutputStream fileOutputStream = new FileOutputStream("girl.jpg");
        FileChannel channel02 = fileOutputStream.getChannel();

        channel02.transferFrom(channel01, 0, channel01.size());

        // 关闭通道和流
        channel01.close();
        channel02.close();
        fileInputStream.close();
        fileOutputStream.close();
    }

}
