package com.zhangyiwen.study.netty.demo5.handler;

import com.zhangyiwen.study.netty.demo5.bean.NettyMessage;
import com.zhangyiwen.study.netty.demo5.codec.SimpleMsgpackEncoder;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

/**
 * Created by zhangyiwen on 16/11/22.
 */
public class NettyMessageEncoder extends MessageToByteEncoder<NettyMessage> {

    private SimpleMsgpackEncoder msgpackEncoder;

    public NettyMessageEncoder() {
        this.msgpackEncoder = new SimpleMsgpackEncoder();
    }

    @Override
    protected void encode(ChannelHandlerContext ctx, NettyMessage msg, ByteBuf sendBuf) throws Exception {
        System.out.println(Thread.currentThread().getId()+"encode");
        if(msg == null || msg.getHeader() == null){
            throw new Exception("The encode message is null.");
        }
        //header
        sendBuf.writeInt(msg.getHeader().getCrcCode());
        sendBuf.writeInt(msg.getHeader().getLength());
        sendBuf.writeLong(msg.getHeader().getSessionID());
        sendBuf.writeByte(msg.getHeader().getType());
        sendBuf.writeByte(msg.getHeader().getPriority());
        //body
        Object body = msg.getBody();
        if(body == null){
            sendBuf.writeInt(0);
        }else {
            byte[] dst = msgpackEncoder.encode(body);
            sendBuf.writeInt(dst.length);
            sendBuf.writeBytes(dst);
        }
        sendBuf.setInt(4, sendBuf.readableBytes() - 8);  //最后设置消息长度
    }

}
