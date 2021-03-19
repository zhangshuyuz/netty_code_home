package com.yuu.nettyhandlercase;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
import sun.rmi.runtime.Log;

public class MyLongByteEncoder extends MessageToByteEncoder<Long> {
    @Override
    protected void encode(ChannelHandlerContext channelHandlerContext, Long aLong, ByteBuf byteBuf) throws Exception {
        // 将long类型数据，写入byteBuf中
        byteBuf.writeLong(aLong);
    }
}
