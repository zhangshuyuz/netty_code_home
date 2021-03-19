package com.yuu.nettysimplecase;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.CharsetUtil;

public class NettyTcpClientHandler extends ChannelInboundHandlerAdapter {

    /**
     * SocketChannel就绪，触发该方法
     * @param ctx
     * @throws Exception
     */
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        System.out.println("client:" + ctx);
        ByteBuf byteBuf = Unpooled.copiedBuffer("hello, Server, 这里是客户端", CharsetUtil.UTF_8);
        ctx.writeAndFlush(byteBuf);
    }

    /**
     * 当通道有数据可读时，执行该方法
     * @param ctx
     * @param msg
     * @throws Exception
     */
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        System.out.println("从服务端来接收消息");
        ByteBuf byteBuf = (ByteBuf)msg;
        System.out.println("服务器[" + ctx.channel().remoteAddress() + "]回复消息为：" + byteBuf.toString(CharsetUtil.UTF_8));
    }

    /**
     * 异常发生，触发该方法
     * @param ctx
     * @param cause
     * @throws Exception
     */
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        // 关闭Channel
        ctx.close();
    }

}
