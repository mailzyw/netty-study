package com.zhangyiwen.study.netty.demo5.test;

import com.zhangyiwen.study.netty.demo5.bean.Header;
import com.zhangyiwen.study.netty.demo5.bean.NettyMessage;
import com.zhangyiwen.study.netty.demo5.codec.SimpleMsgpackDecoder;
import com.zhangyiwen.study.netty.demo5.codec.SimpleMsgpackEncoder;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

/**
 * Created by zhangyiwen on 16/11/22.
 */
public class MsgPackTest {

    static SimpleMsgpackEncoder msgpackEncoder = new SimpleMsgpackEncoder();
    static SimpleMsgpackDecoder msgpackDecoder = new SimpleMsgpackDecoder();

    public static void main(String[] args) throws Exception {
        //初始化数据
        NettyMessage msg = new NettyMessage();
        Header header = new Header();
        header.setCrcCode(0xabef0101);
        header.setLength(0);
        header.setSessionID(123);
        header.setType("a".getBytes()[0]);
        header.setPriority("a".getBytes()[0]);
        msg.setHeader(header);
        msg.setBody("abcdefgaaaa");

        //编码
        ByteBuf sendBuf = Unpooled.buffer();
        sendBuf.writeInt(msg.getHeader().getCrcCode());
        sendBuf.writeInt(msg.getHeader().getLength());
        sendBuf.writeLong(msg.getHeader().getSessionID());
        sendBuf.writeByte(msg.getHeader().getType());
        sendBuf.writeByte(msg.getHeader().getPriority());
        Object body = msg.getBody();
        if(body == null){
            sendBuf.writeInt(0);
        }else {
            byte[] dst = msgpackEncoder.encode(body);
            sendBuf.writeInt(dst.length);
            sendBuf.writeBytes(dst);
        }
        sendBuf.setInt(4, sendBuf.readableBytes());  //最后设置消息长度


        //解码
        ByteBuf receiveBuf = Unpooled.copiedBuffer(sendBuf);
        NettyMessage message = new NettyMessage();
        Header header2 = new Header();
        header2.setCrcCode(receiveBuf.readInt());
        header2.setLength(receiveBuf.readInt());
        header2.setSessionID(receiveBuf.readLong());
        header2.setType(receiveBuf.readByte());
        header2.setPriority(receiveBuf.readByte());
        message.setHeader(header2);

        int msgBodySize = receiveBuf.readInt();
        if(msgBodySize>0){
            byte[]dst = new byte[msgBodySize];
            receiveBuf.readBytes(dst);
            Object body2 = msgpackDecoder.decode(dst);
            message.setBody(body2);
        }

        System.out.println(message);

    }


}
