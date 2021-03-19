package com.yuu.nettysimplecase;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.CharsetUtil;


/**
 * Pipeline中提供了许多可以使用的Handler，也可以使用我们自定义的Handler
 * 自定义的Handler必须继承Netty规定好的某个Handler，才能被Pipeline使用
 */
public class NettyTcpServerHandler extends ChannelInboundHandlerAdapter {

    /**
     * 用来读取客户端发送的消息，然后处理消息
     * ChannelHandlerContext是一个上下文对象，含有Pipeline、SocketChannel、客户端连接地址等信息
     * Object 是客户端发送的数据，默认是Object，根据实际需要做类型转换
     * @param ctx
     * @param msg
     * @throws Exception
     */
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {

        System.out.println("server ctx=" + ctx);
        // 将msg转为一个ByteBuf
        // ByteBuf 是Netty提供的字节缓冲，是对NIO的ByteBuffer的包装升级
        ByteBuf byteBuffer = (ByteBuf)msg;
        System.out.println("客户端[" + ctx.channel().remoteAddress() + "]发送消息为：" + byteBuffer.toString(CharsetUtil.UTF_8));

    }

    /**
     * 数据读取完毕后，给客户端回送消息
     * @param ctx
     * @throws Exception
     */
    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {

        System.out.println("发送消息给客户端");

        // 将要发送的字符串经过编码，放入一个ByteBuf
        ByteBuf msg = Unpooled.copiedBuffer("消息已经接收到", CharsetUtil.UTF_8);

        // 将数据写入缓冲，然后刷入SocketChannel
        ctx.writeAndFlush(msg);

    }

    /**
     * 处理异常的方法。一般出现异常，必须关闭通道
     * @param ctx
     * @param cause
     * @throws Exception
     */
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        ctx.close();
    }
}
