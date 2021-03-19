package com.yuu.niocase.case01;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

public class NIOClient {
    public static void main(String[] args) throws IOException {
        // 得到一个网络通道
        SocketChannel socketChannel = SocketChannel.open();

        // 设置非阻塞模式
        socketChannel.configureBlocking(false);

        // 提供服务器端IP和端口
        InetSocketAddress address = new InetSocketAddress("127.0.0.1", 8300);

        // 连接服务器
        if (!socketChannel.connect(address)) {
            // 连接不成功

            while (!socketChannel.finishConnect()) {
                System.out.println("因为连接需要事件，客户端不会阻塞，可以进行其他工作");
            }

        }

        // 连接成功，发送数据
        String s = "hello, Server!";

        // 创建一个Buffer
        // 使用wrap()方法，可以创建一个底层数组和参数数组大小相同的Buffer，并将参数中数组数据写入Buffer底层数组
        ByteBuffer byteBuffer = ByteBuffer.wrap(s.getBytes());

        // 发送数据。将Buffer中的数据，写入到Channel
        socketChannel.write(byteBuffer);
        System.in.read();

        socketChannel.close();
    }
}
