package com.yuu.nettysimplecase;


import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

public class NettyTcpClient {
    public static void main(String[] args) throws InterruptedException {

        // 客户端需要一个事件循环组
        NioEventLoopGroup eventLoopGroup = new NioEventLoopGroup();

        try {
            // 创建客户端启动配置对象BootStrap
            Bootstrap bootstrap = new Bootstrap();
            // 设置相关参数
            bootstrap.group(eventLoopGroup)
                    .channel(NioSocketChannel.class) // 设置客户端Channel的实现类
                    .handler(new ChannelInitializer<SocketChannel>() {

                        // 向客户端的Pipeline添加Handler
                        @Override
                        protected void initChannel(SocketChannel socketChannel) throws Exception {
                            ChannelPipeline pipeline = socketChannel.pipeline();// 加入自己的处理器
                            pipeline.addLast(new NettyTcpClientHandler());
                        }

                    });

            System.out.println("客户端准备成功");

            // 启动客户端去连接服务器端
            ChannelFuture sync = bootstrap.connect("127.0.0.1", 8900).sync();

            // 监听关闭通道
            sync.channel().closeFuture().sync();
        } finally {
            eventLoopGroup.shutdownGracefully();
        }

    }
}
