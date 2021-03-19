package com.yuu.nettyhttpcase;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.*;
import io.netty.util.CharsetUtil;

import java.net.URI;

/**
 * SimpleChannelInboundHandler是ChannelInboundHandler的子类
 * SimpleChannelInboundHandler的泛型，要写客户端和服务端交换数据的数据类型。
 * HttpObject说明客户端和服务器端，数据被封装成HttpObject类型然后交换
 */
public class NettyHttpServerHandler extends SimpleChannelInboundHandler<HttpObject> {

    /**
     * 当有读取事件，就触发该方法
     * @param channelHandlerContext
     * @param httpObject
     * @throws Exception
     */
    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, HttpObject httpObject) throws Exception {
        // 1. 判断httpObject是否为HttpRequest请求
        if (httpObject instanceof HttpRequest) {
            System.out.println("httpObject的类型为：" + httpObject.getClass());
            System.out.println("浏览器地址为：" + channelHandlerContext.channel().remoteAddress());

            // 过滤请求页面title的请求
            HttpRequest httpRequest = (HttpRequest)httpObject;
            URI uri = new URI(httpRequest.uri());
            if ("/favicon.ico".equals(uri.getPath())) {
                System.out.println("请求了 /favicon.ico 资源，不做响应");
                return;
            }

            // 回复信息给浏览器。因为浏览器和服务器之间使用Http协议通信，因此我们需要将请求信息封装为HttpResponse
            ByteBuf byteBuf = Unpooled.copiedBuffer("hello，这里是服务器", CharsetUtil.UTF_8);
            DefaultFullHttpResponse response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.OK, byteBuf);
            response.headers().set(HttpHeaderNames.CONTENT_TYPE, "text/html");
            response.headers().set(HttpHeaderNames.CONTENT_LENGTH, byteBuf.readableBytes());

            // 将构建好的HttpResponse返回
            channelHandlerContext.writeAndFlush(response);
        }
    }

}
