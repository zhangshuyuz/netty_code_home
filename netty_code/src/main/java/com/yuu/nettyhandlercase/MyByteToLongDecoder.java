package com.yuu.nettyhandlercase;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

import java.util.List;

public class MyByteToLongDecoder extends ByteToMessageDecoder {

    /**
     * ctx为上下文，byteBuf为入站字节码放入的缓冲区，list为解码后的数据放入的集合
     * @param channelHandlerContext
     * @param byteBuf
     * @param list
     * @throws Exception
     */
    @Override
    protected void decode(ChannelHandlerContext channelHandlerContext, ByteBuf byteBuf, List<Object> list) throws Exception {

        // 客户端传入的long类型数据，因此先保证byteBuf中至少有8个字节
        if (byteBuf.readableBytes() >= 8) {
            // 将byteBuf中所有字节，读取为long类型数据，放入list
            list.add(byteBuf.readLong());
        }

    }
}
