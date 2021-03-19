package com.yuu.nettyheartbeatsimplecase;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.timeout.IdleStateEvent;

public class NettyUselessConnectionSolveHandler extends ChannelInboundHandlerAdapter {


    /**
     * 当心跳检测的Handler被触发，该方法会被自动执行
     * ctx就是上下文
     * evt就是心跳检测机制检测到的事件
     * @param ctx
     * @param evt
     * @throws Exception
     */
    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {

        if (evt instanceof IdleStateEvent) {
            // 确定传过来的事件，确实是IdleStateEvent。即心跳检测Handler传来的事件

            // 强转一下事件
            IdleStateEvent idleStateEvent = (IdleStateEvent) evt;

            String eventMsg = null;
            // 通过switch语句，对不同事件类型做处理
            switch (idleStateEvent.state()) {
                // 如果现在连接是读空闲
                case READER_IDLE:
                    eventMsg = "读空闲状态";
                    break;
                // 如果现在连接是写空闲
                case WRITER_IDLE:
                    eventMsg = "写空闲状态";
                    break;
                // 如果现在连接是读写空闲
                case ALL_IDLE:
                    eventMsg = "读写空闲状态";
                    break;
            }

            System.out.println(ctx.channel().remoteAddress() + "现在是" + eventMsg);
        }

    }

}
