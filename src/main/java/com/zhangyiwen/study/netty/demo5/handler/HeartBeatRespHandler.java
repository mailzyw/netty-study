package com.zhangyiwen.study.netty.demo5.handler;

import com.zhangyiwen.study.netty.demo5.bean.Header;
import com.zhangyiwen.study.netty.demo5.bean.MessageType;
import com.zhangyiwen.study.netty.demo5.bean.NettyMessage;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by zhangyiwen on 16/11/22.
 * 心跳的服务端ChannelHandler,用于接收心跳请求消息及发送心跳响应消息
 */
public class HeartBeatRespHandler extends ChannelInboundHandlerAdapter{

    private volatile AtomicInteger count = new AtomicInteger(0);

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        NettyMessage message = (NettyMessage)msg;
        //  处理心跳请求消息,返回心跳应答消息;透传其他消息
        if(message.getHeader() != null && message.getHeader().getType() == (byte) MessageType.HEART_REQ.ordinal()){
            System.out.println(Thread.currentThread().getId()+"[heart beat receive"+this+","+count.incrementAndGet()+"] "+message);
            NettyMessage heartBeat = buildHeartBeat();
            System.out.println(Thread.currentThread().getId()+"[heart beat send] "+heartBeat);
            ctx.writeAndFlush(heartBeat);
        }else {
            ctx.fireChannelRead(msg);
        }
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        System.out.println(Thread.currentThread().getId()+"==heart resp channelInactive==");
        super.channelInactive(ctx);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        System.out.println(Thread.currentThread().getId()+"==heart resp exceptionCaught==");
        super.exceptionCaught(ctx, cause);
    }

    private NettyMessage buildHeartBeat(){
        NettyMessage message = new NettyMessage();
        Header header = new Header();
        header.setType((byte)MessageType.HEART_RESP.ordinal());
        message.setHeader(header);
        return message;
    }
}
