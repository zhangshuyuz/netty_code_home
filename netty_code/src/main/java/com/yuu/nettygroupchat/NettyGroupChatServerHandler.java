package com.yuu.nettygroupchat;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.util.concurrent.GlobalEventExecutor;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

public class NettyGroupChatServerHandler extends SimpleChannelInboundHandler<String> {

    // 使用Netty提供的Channel组，用它来管理所有的Channel。因为该Channel组所有用户的Handler共享，因此使用static修饰
    // GlobalEventExecutor.INSTANCE 是一个全局的事件执行器，是一个单例
    private static ChannelGroup channels = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);

    private DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    /**
     * handlerAdded方法，是第一个被调用的，连接建立就立即执行该方法
     * @param ctx
     * @throws Exception
     */
    @Override
    public void handlerAdded(ChannelHandlerContext ctx) throws Exception {

        Channel channel = ctx.channel();

        // 将该Channel加入聊天的消息，发送给其他客户端
        channels.writeAndFlush("客户端[" + channel.remoteAddress() +"]加入聊天(" + dateFormat.format(new java.util.Date()) + ")\n");

        // 将当前Channel放入Channel组中
        channels.add(channel);

    }

    /**
     * 通道处于活跃状态。说明客户端上线了
     * @param ctx
     * @throws Exception
     */
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {

        System.out.println("客户端[" + ctx.channel().remoteAddress() + "]上线了");

    }

    /**
     * 通道处于不活跃状态。说明客户端离线
     * @param ctx
     * @throws Exception
     */
    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {

        System.out.println("客户端[" + ctx.channel().remoteAddress() + "]离线了");

    }

    /**
     * 客户端断开连接触发。该事件触发，会自动将Channel组中该客户端的Channel剔除，不用手动操作
     * @param ctx
     * @throws Exception
     */
    @Override
    public void handlerRemoved(ChannelHandlerContext ctx) throws Exception {

        // 给其他客户端提示离线信息
        channels.writeAndFlush("客户端[" + ctx.channel().remoteAddress() + "]已经断开(" + dateFormat.format(new java.util.Date()) + ")\n");

    }

    /**
     * 客户端Channel有数据可读。
     * @param channelHandlerContext
     * @param s
     * @throws Exception
     */
    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, String s) throws Exception {

        Channel channel = channelHandlerContext.channel();

        // 遍历Channel组，不同情况发送不同的消息
        channels.forEach(ch -> {
            if (ch != channel) {
                // 对于其他客户端Channel，转发消息
                ch.writeAndFlush("客户端[" + channel.remoteAddress() + "]发送的信息是：" + s + "\n");
            } else {
                // 对于当前客户端Channel，提示消息已经发送
                ch.writeAndFlush("当前消息[" + s + "]转发成功\n");
            }
        });
    }

    /**
     * 出现异常。
     * @param ctx
     * @param cause
     * @throws Exception
     */
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        // 出现异常，关闭
        ctx.close();
    }

}
