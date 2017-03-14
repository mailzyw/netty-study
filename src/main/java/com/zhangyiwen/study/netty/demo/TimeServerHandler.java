package com.zhangyiwen.study.netty.demo;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

import java.util.Date;

/**
 * 服务端Handler,接收客户端请求数据，返回客户端请求数据及当前时间
 * Created by zhangyiwen on 16/1/27.
 */
public class TimeServerHandler extends ChannelInboundHandlerAdapter{

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        ByteBuf buf = (ByteBuf)msg;
        byte[] req = new byte[buf.readableBytes()];
        buf.readBytes(req); //从buf中读取数据到req
        String body = new String(req,"UTF-8");
        System.out.println("The time server receive order:" + body);
        if("QUIT".equalsIgnoreCase(body)){
            ctx.close();
        }else {
            String currentTime = new Date(System.currentTimeMillis()).toString();
            String command = body + ", and now time is " + currentTime; //响应文本
            ByteBuf resp = Unpooled.copiedBuffer(command.getBytes());
            ctx.write(resp);
        }

    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        ctx.flush();
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        System.out.println("Unexpected exception cause.");
        ctx.close();
    }
}
