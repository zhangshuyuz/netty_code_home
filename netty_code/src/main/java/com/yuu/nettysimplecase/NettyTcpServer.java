package com.yuu.nettysimplecase;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;


public class NettyTcpServer {
    public static void main(String[] args) throws InterruptedException {

        // 创建BossGroup和WorkerGroup
        EventLoopGroup bossGroup = new NioEventLoopGroup();
        EventLoopGroup workerGroup = new NioEventLoopGroup();

        try {

            // 创建服务器端启动对象ServerBootstrap，去配置启动参数
            ServerBootstrap serverBootstrap = new ServerBootstrap();
            // 使用链式编程设置参数
            serverBootstrap.group(bossGroup, workerGroup)
                    .channel(NioServerSocketChannel.class) // 设置NioServerSocketChannel作为未来的服务器的ServerSocketChannel
                    .option(ChannelOption.SO_BACKLOG, 128) // 设置线程队列等待连接的个数
                    .childOption(ChannelOption.SO_KEEPALIVE, true) // 设置保持活动连接状态
                    .childHandler(new ChannelInitializer<SocketChannel>() {// ChannelInitializer是通道初始化对象

                        // 给Pipeline设置处理器
                        @Override
                        protected void initChannel(SocketChannel socketChannel) throws Exception {
                            ChannelPipeline pipeline = socketChannel.pipeline(); // 通过SocketChannel，返回与之关联的Pipeline
                            pipeline.addLast(new NettyTcpServerHandler()); // 向Pipeline的最后添加一个处理器
                        }

                    }); // 给workerGroup的某一个EventLoop对应的Pipeline设置处理器

            System.out.println("服务器准备完毕");

            // 通过ServerBootstrap绑定端口并同步，生成ChannelFuture。此时服务器启动
            ChannelFuture channelFuture = serverBootstrap.bind(8900).sync();

            // 对关闭SocketChannel的事件进行监听
            channelFuture.channel().closeFuture().sync();
        } finally {
            // 关闭两个NioEventLoopGroup
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }

    }
}
