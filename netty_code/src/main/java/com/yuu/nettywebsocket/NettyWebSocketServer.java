package com.yuu.nettywebsocket;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.handler.stream.ChunkedWriteHandler;

public class NettyWebSocketServer {
    public static void main(String[] args) throws Exception{

        NioEventLoopGroup bossGroup = new NioEventLoopGroup();
        NioEventLoopGroup workerGroup = new NioEventLoopGroup();

        ServerBootstrap serverBootstrap = new ServerBootstrap();
        serverBootstrap.group(bossGroup, workerGroup)
                .channel(NioServerSocketChannel.class)
                .handler(new LoggingHandler(LogLevel.INFO))
                .childHandler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel socketChannel) throws Exception {

                        ChannelPipeline pipeline = socketChannel.pipeline();

                        // 因为基于Http协议，因此使用Http的编解码器
                        pipeline.addLast(new HttpServerCodec());

                        // 因为基于Http协议的数据传输，数据是以块来传输，因此还需要引入ChunkedWriteHandler
                        pipeline.addLast(new ChunkedWriteHandler());

                        // 因为基于Http协议的数据传输，数据传输时是分段的，即将大数据分段然后通过多次http请求发送分段数据
                        // 因此需要将分段的数据聚合，HttpObjectAggregator就是聚合分段数据的处理器，参数为最大正文长度
                        // 这也就是为什么，当浏览器发送大量数据时，就会发出多次http请求
                        pipeline.addLast(new HttpObjectAggregator(8192));

                        // 对于WebSocket，它的数据是通过帧(frame)形式来传递
                        // WebSocketServerProtocolHandler参数为浏览器请求的应用程序上下文，如ws://localhost:8600/hello中的/hello。
                        // WebSocketServerProtocolHandler，是用来将Http协议升级为WebSocket协议，保持长连接
                        pipeline.addLast(new WebSocketServerProtocolHandler("/hello"));

                        // 自定义Handler，用来处理业务
                        pipeline.addLast(new NettyWebSocketHandler());

                    }
                });

        ChannelFuture sync = serverBootstrap.bind(8600).sync();
        sync.channel().closeFuture().sync();

    }

}
