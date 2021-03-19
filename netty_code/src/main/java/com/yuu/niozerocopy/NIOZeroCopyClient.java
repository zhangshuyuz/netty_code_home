package com.yuu.niozerocopy;

import java.io.FileInputStream;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.FileChannel;
import java.nio.channels.SocketChannel;

public class NIOZeroCopyClient {
    public static void main(String[] args) throws IOException {
        InetSocketAddress serverAddress = new InetSocketAddress("127.0.0.1", 8600);
        SocketChannel socketChannel = SocketChannel.open(serverAddress);

        String filename = "test.html";
        // 获取文件Channel
        FileChannel fileChannel = new FileInputStream(filename).getChannel();
        // 准备发送
        long start = System.currentTimeMillis();
        long transfercount = fileChannel.transferTo(0, fileChannel.size(), socketChannel);
        System.out.println("发送总的字节数为：" + transfercount);
        System.out.println("耗时：" + (System.currentTimeMillis() - start));

        // 关闭FileChannel
        fileChannel.close();

    }
}
