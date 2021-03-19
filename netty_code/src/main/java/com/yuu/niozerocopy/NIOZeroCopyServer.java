package com.yuu.niozerocopy;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Set;

public class NIOZeroCopyServer {
    public static void main(String[] args) throws IOException {

        InetSocketAddress address = new InetSocketAddress(8600);

        ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
        serverSocketChannel.socket().bind(address);

        ByteBuffer byteBuffer = ByteBuffer.allocate(1024);

        while (true) {
            SocketChannel accept = serverSocketChannel.accept();
            int readcount = 0;
            while (readcount != -1) {
                readcount = accept.read(byteBuffer);
                byteBuffer.rewind(); // 倒带，让position变为0，mark作废
            }
        }
    }
}
