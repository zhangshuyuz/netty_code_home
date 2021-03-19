package com.yuu.nettygroupchat;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;

import java.util.Scanner;

public class NettyGroupChatClient {

    private final String HOST;
    private final int PORT;

    public NettyGroupChatClient(String host, int port) {
        this.HOST = host;
        this.PORT = port;
    }

    public void run() throws InterruptedException {

        NioEventLoopGroup workerGroup = new NioEventLoopGroup();

        try {
            Bootstrap bootstrap = new Bootstrap();
            bootstrap.group(workerGroup)
                    .channel(NioSocketChannel.class)
                    .handler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel socketChannel) throws Exception {

                            ChannelPipeline pipeline = socketChannel.pipeline();
                            pipeline.addLast("decoder", new StringDecoder());
                            pipeline.addLast("encoder", new StringEncoder());
                            pipeline.addLast(new NettyGroupChatClientHandler());

                        }
                    });

            ChannelFuture sync = bootstrap.connect(this.HOST, this.PORT).sync();

            System.out.println("客户端准备完毕");

            Channel channel = sync.channel();
            Scanner scanner = new Scanner(System.in);
            while (scanner.hasNextLine()) {
                String msg = scanner.nextLine();
                channel.writeAndFlush(msg);
            }

        } finally {
            workerGroup.shutdownGracefully();
        }

    }

    public static void main(String[] args) throws InterruptedException {
        NettyGroupChatClient nettyGroupChatClient = new NettyGroupChatClient("127.0.0.1", 8900);
        nettyGroupChatClient.run();
    }
}
