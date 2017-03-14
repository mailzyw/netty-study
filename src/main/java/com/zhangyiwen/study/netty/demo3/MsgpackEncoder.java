package com.zhangyiwen.study.netty.demo3;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
import org.msgpack.MessagePack;

/**
 * Created by zhangyiwen on 16/11/17.
 */
public class MsgpackEncoder extends MessageToByteEncoder<Object>{

    @Override
    protected void encode(ChannelHandlerContext ctx, Object msg, ByteBuf out) throws Exception {
        MessagePack msgpack = new MessagePack();
        byte[] raw = msgpack.write(msg);    //编码
        out.writeBytes(raw);
    }
}
