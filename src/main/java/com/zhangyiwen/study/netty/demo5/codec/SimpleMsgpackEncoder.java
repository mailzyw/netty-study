package com.zhangyiwen.study.netty.demo5.codec;

import io.netty.buffer.ByteBuf;
import org.msgpack.MessagePack;

/**
 * Created by zhangyiwen on 16/11/17.
 */
public class SimpleMsgpackEncoder {

//    public void encode(Object msg, ByteBuf out) throws Exception {
//        MessagePack msgpack = new MessagePack();
//        byte[] raw = msgpack.write(msg);    //编码
//        out.writeBytes(raw);
//    }

    public byte[] encode(Object msg) throws Exception {
        MessagePack msgpack = new MessagePack();
        byte[] raw = msgpack.write(msg);    //编码
        return raw;
    }
}
