package com.zhangyiwen.study.netty.demo3;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageDecoder;
import org.msgpack.MessagePack;

import java.util.List;

/**
 * Created by zhangyiwen on 16/11/17.
 */
public class MsgpackDecoder extends MessageToMessageDecoder<ByteBuf>{

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf msg, List<Object> out) throws Exception {
        final byte[] array;
        final int length = msg.readableBytes();
        array = new byte[length];
        msg.getBytes(msg.readerIndex(),array,0,length);//将msg中的字节流放入byte[] array中
        MessagePack messagePack = new MessagePack();
        out.add(messagePack.read(array));   //解码
    }
}
