package com.yuu.nettyhttpcase;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.HttpServerCodec;

public class NettyHttpServerInitHandler extends ChannelInitializer<SocketChannel> {

    @Override
    protected void initChannel(SocketChannel socketChannel) throws Exception {
        // 向Pipeline加入Handler
        // 1. 得到Pipeline
        ChannelPipeline pipeline = socketChannel.pipeline();
        // 2. 加入Netty提供的编解码处理器HttpServerCodec，codec = coder + decoder
        // HttpServerCodec，是Netty提供的处理Http请求的编解码器。
        pipeline.addLast("myHttpServerCodec", new HttpServerCodec());
        // 3. 添加自定义的Handler，NettyHttpServerHandler
        pipeline.addLast("myHandler", new NettyHttpServerHandler());
    }
}
