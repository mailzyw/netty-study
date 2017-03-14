package com.zhangyiwen.study.netty.demo5.handler;

import com.zhangyiwen.study.netty.demo5.bean.Header;
import com.zhangyiwen.study.netty.demo5.bean.NettyMessage;
import com.zhangyiwen.study.netty.demo5.codec.SimpleMsgpackDecoder;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;

/**
 * Created by zhangyiwen on 16/11/22.
 */
public class NettyMessageDecoder extends LengthFieldBasedFrameDecoder{

    private SimpleMsgpackDecoder msgpackDecoder;

    public NettyMessageDecoder(int maxFrameLength, int lengthFieldOffset, int lengthFieldLength) {
        super(maxFrameLength, lengthFieldOffset, lengthFieldLength);
        this.msgpackDecoder = new SimpleMsgpackDecoder();
    }


    @Override
    protected Object decode(ChannelHandlerContext ctx, ByteBuf in) throws Exception {
        System.out.println(Thread.currentThread().getId()+"decode");
        /*
         Netty的LengthFieldBasedFrameDecoder解码器,支持自动的TCP粘包和半包处理,
         只需要给出标识消息长度的字段偏移量和消息长度自身所占字节数,Netty就能自动实现对半包的处理
         对于业务解码器来说,调用父类LengthFieldBasedFrameDecoder的方法解码后,返回的就是整包消息或者为空
         */
        ByteBuf frame = (ByteBuf) super.decode(ctx,in);
        if(frame == null){  //如果为空则说明是个半包消息,直接返回继续由IO线程读取后续的码流
            return null;
        }

        NettyMessage message = new NettyMessage();
        //header
        Header header = new Header();
        header.setCrcCode(frame.readInt());
        header.setLength(frame.readInt());
        header.setSessionID(frame.readLong());
        header.setType(frame.readByte());
        header.setPriority(frame.readByte());
        message.setHeader(header);
        //body
        int msgBodySize = frame.readInt();
        if(msgBodySize>0){
            byte[]dst = new byte[msgBodySize];
            frame.readBytes(dst);
            Object body = msgpackDecoder.decode(dst);
            message.setBody(body);
        }
        frame.release();
        return message;
    }
}
