package com.zhangyiwen.study.netty.demo5.codec;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageDecoder;
import org.msgpack.MessagePack;

import java.util.List;

/**
 * Created by zhangyiwen on 16/11/17.
 */
public class SimpleMsgpackDecoder {

//    public void decode(ByteBuf msg, List<Object> out) throws Exception {
//        final byte[] array;
//        final int length = msg.readableBytes();
//        array = new byte[length];
//        msg.getBytes(msg.readerIndex(),array,0,length);//将msg中的字节流放入byte[] array中
//        MessagePack messagePack = new MessagePack();
//        out.add(messagePack.read(array));   //解码
//    }
//
//    public Object decode(ByteBuf msg) throws Exception {
//        final byte[] array;
//        final int length = msg.readableBytes();
//        array = new byte[length];
//        msg.getBytes(msg.readerIndex(),array,0,length);//将msg中的字节流放入byte[] array中
//        MessagePack messagePack = new MessagePack();
//        Object object = messagePack.read(array);   //解码
//        return object;
//    }

    public Object decode(byte[] bytes) throws Exception {
        MessagePack messagePack = new MessagePack();
        Object object = messagePack.read(bytes);   //解码
        return object;
    }
}
