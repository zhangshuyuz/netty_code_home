package com.yuu.nettygroupchat;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;

public class NettyGroupChatServer {

    private final int PORT;

    public NettyGroupChatServer(int port) {
        this.PORT = port;
    }

    // 编写run方法，用来处理客户端请求
    public void run() throws InterruptedException {

        NioEventLoopGroup bossGroup = new NioEventLoopGroup();
        NioEventLoopGroup workerGroup = new NioEventLoopGroup();

        try {
            ServerBootstrap serverBootstrap = new ServerBootstrap();
            serverBootstrap.group(bossGroup, workerGroup)
                    .channel(NioServerSocketChannel.class)
                    .option(ChannelOption.SO_BACKLOG, 128)
                    .childOption(ChannelOption.SO_KEEPALIVE, true)
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel socketChannel) throws Exception {
                            // 获取Pipeline
                            ChannelPipeline pipeline = socketChannel.pipeline();
                            // 向Pipeline中添加解码器，用来解码客户端传来的字节流
                            pipeline.addLast("decoder", new StringDecoder());
                            // 向Pipeline中添加编码器，用来编码返还数据为字节流，然后发给客户端
                            pipeline.addLast("encoder", new StringEncoder());
                            // 向Pipeline中添加自己的业务处理Handler
                            pipeline.addLast(new NettyGroupChatServerHandler());
                        }
                    });

            System.out.println("Netty服务器已经启动，启动端口为" + this.PORT);

            ChannelFuture sync = serverBootstrap.bind(this.PORT).sync();
            sync.channel().closeFuture().sync();
        } finally {
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }

    }

    public static void main(String[] args) throws InterruptedException {
        NettyGroupChatServer nettyGroupChatServer = new NettyGroupChatServer(8900);
        nettyGroupChatServer.run();
    }
}
