package com.yuu.protobufcase2;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.CharsetUtil;

import java.util.Random;

public class NettyTcpClientHandler extends ChannelInboundHandlerAdapter {

    /**
     * SocketChannel就绪，触发该方法
     * @param ctx
     * @throws Exception
     */
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {

        // 1. 创建对象，并给属性赋值
        // 因为使用了一个Message管理其他Message，因此创建的是管理对象
        // 根据管理对象的枚举类型，来创建并赋值对应的数据对象。例如这里发送一个Student的信息
        StudentPOJO.MyMsg myMsg = null;
        int i = new Random().nextInt(3);
        if (i == 0) {
            myMsg = StudentPOJO.MyMsg.newBuilder()
                    .setDataType(StudentPOJO.MyMsg.DataType.StudentType)
                    .setStudent(StudentPOJO.Student.newBuilder().setId(2).setName("李四").build()).build();
        } else {
            myMsg = StudentPOJO.MyMsg.newBuilder()
                    .setDataType(StudentPOJO.MyMsg.DataType.WorkerType)
                    .setWorker(StudentPOJO.Worker.newBuilder().setAge(20).setName("李四的爹").build()).build();
        }

        // 2. 发送对象
        ctx.writeAndFlush(myMsg);

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
