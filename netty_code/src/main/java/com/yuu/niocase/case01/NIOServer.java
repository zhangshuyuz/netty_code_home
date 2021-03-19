package com.yuu.niocase.case01;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.util.Set;

/**
 * 编写一个NIO入门案例，实现客户端和服务券之间简单通信（非阻塞）
 */
public class NIOServer {
    public static void main(String[] args) throws IOException {

        // 创建ServerSocketChannel
        ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();

        // 获取Selector实例
        Selector selector = Selector.open();

        // 绑定端口
        InetSocketAddress address = new InetSocketAddress(8300);

        // 监听
        serverSocketChannel.socket().bind(address);
        // 设置为非阻塞
        serverSocketChannel.configureBlocking(false);

        // 将ServerSocketChannel注册到Selector
        serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);

        // 循环等待客户端的连接
        while (true) {
            // 通过Selector来监听Channel中是否有事件发生
            if (selector.select(1000) == 0) {
                // 没有事件发生，打印一句话，结束本次循环，开启下一次监听Channel
                System.out.println("服务器等待1秒，无连接");
                continue;
            }

            // 有事件发生

            // 获取到有事件发生的SelectionKey集合
            Set<SelectionKey> selectionKeys = selector.selectedKeys();

            // 分别处理不同Channel的不同事件
            for (SelectionKey selection :
                    selectionKeys) {

                if (selection.isAcceptable()) {
                    // 说明有客户端连接
                    // 反向获取ServerSocketChannel
                    ServerSocketChannel reServerSocketChannel = (ServerSocketChannel) selection.channel();
                    // 1. 给客户端分配一个SocketChannel
                    SocketChannel socketChannel = reServerSocketChannel.accept();
                    // 2. 设置SocketChannel为非阻塞
                    socketChannel.configureBlocking(false);
                    // 3. 将客户端分配的SocketChannel也注册到Selector。此时，关注事件为读事件，同时给该SocketChannel绑定一个Buffer
                    socketChannel.register(selector, SelectionKey.OP_READ, ByteBuffer.allocate(1024));
                    System.out.println("客户端连接成功，生成一个对应的SocketChannel" + socketChannel.hashCode());
                }

                if (selection.isReadable()) {
                    // 说明Channel需要读取客户端发送的数据
                    // 通过selection反向获取SocketChannel
                    SocketChannel socketChannel = (SocketChannel) selection.channel();
                    // 获取Channel关联的Buffer
                    ByteBuffer byteBuffer = (ByteBuffer) selection.attachment();
                    // 将客户端数据读取到Buffer
                    socketChannel.read(byteBuffer);
                    System.out.println("客户端发送了数据" + new String(byteBuffer.array()));
                }

                // 事件处理完毕，必须手动删除集合中处理完毕后的selection
                selectionKeys.remove(selection);

            }

        }

    }
}
