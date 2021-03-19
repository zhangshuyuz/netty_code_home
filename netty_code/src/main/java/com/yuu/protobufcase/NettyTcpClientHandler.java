package com.yuu.protobufcase;

import io.netty.buffer.ByteBuf;
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
        // 发送StudentPOJO中内部类对象给服务器端

        // 1. 创建对象，并给属性赋值
        StudentPOJO.Student student = StudentPOJO.Student.newBuilder().setId(1).setName("张三").build();
        // 2. 发送对象
        ctx.channel().writeAndFlush(student);

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
