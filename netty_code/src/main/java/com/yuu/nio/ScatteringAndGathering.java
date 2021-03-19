package com.yuu.nio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.Buffer;
import java.nio.ByteBuffer;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Arrays;

/**
 * Scattering分散和Gathering聚合演示
 */
public class ScatteringAndGathering {
    public static void main(String[] args) throws IOException {
        // 这里使用ServerSocketChannel和SocketChannel来做通道，这就涉及到网络

        // 创建ServerSocketChannel进行监听
        ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
        // 创建监听网络端口地址
        InetSocketAddress inetSocketAddress = new InetSocketAddress(7000);
        // 绑定监听端口到Socket，并启动监听
        serverSocketChannel.socket().bind(inetSocketAddress);

        // 创建Buffer数组，并分配空间
        ByteBuffer[] byteBuffers = new ByteBuffer[2];
        byteBuffers[0] = ByteBuffer.allocate(5);
        byteBuffers[1] = ByteBuffer.allocate(3);

        // ServerSocketChannel等待客户端连接
        SocketChannel socketChannel = serverSocketChannel.accept();

        // 循环读取数据
        int message = 8; // 假定从客户端接收8个字节
        while (true) {
            int byteRead = 0;
            while (byteRead < message) {
                // 分散写入
                long read = socketChannel.read(byteBuffers);
                byteRead += read; //累积读取的字节数
                System.out.println("byteRead=" + byteRead);
                // 使用Stream流的map()方法
                // 将Buffer数组中的两个Buffer中的position和limit通过标准打印流打印出来
                Arrays.asList(byteBuffers).stream()
                        .map(byteBuffer -> "position=" + byteBuffer.position() + "；limit=" + byteBuffer.limit())
                        .forEach(System.out::println);
            }

            // 一次读取完毕，反转Buffer
            Arrays.asList(byteBuffers).forEach(Buffer::flip);

            // 重新将数据显示到客户端
            long byteWrite = 0;
            while (byteWrite < message) {
                // 聚合导出
                socketChannel.write(byteBuffers);
                byteWrite += 1;
            }

            // 将所有Buffer进行复位
            Arrays.asList(byteBuffers).forEach(Buffer::clear);

            System.out.println("byteRead=" + byteRead + ";byteWrite=" + byteWrite + ";message=" + message);
        }

    }

}
