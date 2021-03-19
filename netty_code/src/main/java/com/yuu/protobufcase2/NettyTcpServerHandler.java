package com.yuu.protobufcase2;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.util.CharsetUtil;


/**
 * Pipeline中提供了许多可以使用的Handler，也可以使用我们自定义的Handler
 * 自定义的Handler必须继承Netty规定好的某个Handler，才能被Pipeline使用
 */
public class NettyTcpServerHandler extends SimpleChannelInboundHandler<StudentPOJO.MyMsg> {


    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, StudentPOJO.MyMsg myMsg) throws Exception {
        // 根据dataType显示不同的信息
        StudentPOJO.MyMsg.DataType dataType = myMsg.getDataType();
        if (dataType == StudentPOJO.MyMsg.DataType.StudentType) {
            System.out.println("学生，客户端发送的数据为：id=" + myMsg.getStudent().getId() + ";name=" + myMsg.getStudent().getName());
        } else if (dataType == StudentPOJO.MyMsg.DataType.WorkerType) {
            System.out.println("成员，客户端发送数据为：age=" + myMsg.getWorker().getAge() + ";name=" + myMsg.getWorker().getName());
        } else {
            System.out.println("传输数据类型不正确");
        }

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
