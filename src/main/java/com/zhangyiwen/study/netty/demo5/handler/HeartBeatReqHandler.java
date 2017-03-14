package com.zhangyiwen.study.netty.demo5.handler;

import com.zhangyiwen.study.netty.demo5.bean.Header;
import com.zhangyiwen.study.netty.demo5.bean.MessageType;
import com.zhangyiwen.study.netty.demo5.bean.NettyMessage;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by zhangyiwen on 16/11/22.
 * 心跳的客户端ChannelHandler,用于接收心跳响应消息及发送心跳请求消息
 */
public class HeartBeatReqHandler extends ChannelInboundHandlerAdapter{

    private volatile AtomicInteger count = new AtomicInteger(0);
    private volatile ScheduledFuture<?> heartBeat;

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        NettyMessage message = (NettyMessage)msg;
        //  握手成功,主动发送心跳消息
        if(message.getHeader() != null && message.getHeader().getType() == (byte) MessageType.LOGIN_RESP.ordinal()){
            heartBeat = ctx.executor().scheduleAtFixedRate(new HeartBeatReqHandler.HeartBeatTask(ctx),0,5000, TimeUnit.MILLISECONDS);
        }
        //  处理心跳响应
        else if(message.getHeader() != null && message.getHeader().getType() == (byte) MessageType.HEART_RESP.ordinal()){
            System.out.println(Thread.currentThread().getId()+"[heart beat receive"+count.incrementAndGet()+"] "+message);
        }
        //  其他,透传
        else{
            ctx.fireChannelRead(msg);
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        System.out.println(Thread.currentThread().getId()+"==heart req exceptionCaught==");
        if(heartBeat != null){
            heartBeat.cancel(true);
            heartBeat = null;
        }
        ctx.fireExceptionCaught(cause);
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        System.out.println(Thread.currentThread().getId()+"== heart req channelInactive==");
        if(heartBeat != null){
            heartBeat.cancel(true);
            heartBeat = null;
        }
    }

    private class HeartBeatTask implements Runnable {

        private final ChannelHandlerContext ctx;

        public HeartBeatTask(ChannelHandlerContext ctx) {
            this.ctx = ctx;
        }

        public void run() {
            NettyMessage heartBeat = buildHeartBeat();
            System.out.println(Thread.currentThread().getId()+"[heart beat send] "+heartBeat);
            ctx.writeAndFlush(heartBeat);
        }

        private NettyMessage buildHeartBeat(){
            NettyMessage message = new NettyMessage();
            Header header = new Header();
            header.setType((byte)MessageType.HEART_REQ.ordinal());
            message.setHeader(header);
            return message;
        }
    }
}
