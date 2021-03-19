package com.yuu.nettyhandlercase;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

public class NettyClient {
    public static void main(String[] args) throws InterruptedException {

        NioEventLoopGroup eventLoopGroup = new NioEventLoopGroup();

        try {
            Bootstrap bootstrap = new Bootstrap();
            bootstrap.group(eventLoopGroup)
                    .channel(NioSocketChannel.class)
                    .handler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel socketChannel) throws Exception {

                            ChannelPipeline pipeline = socketChannel.pipeline();
                            // 加入出站的编码器 MyClient
                            pipeline.addLast(new MyLongByteEncoder());
                            // 加入自定义Handler
                            pipeline.addLast(new MyClientHandler());
                        }
                    });

            ChannelFuture future = bootstrap.connect("127.0.0.1", 8900).sync();

        } finally {
            eventLoopGroup.shutdownGracefully();
        }

    }
}
