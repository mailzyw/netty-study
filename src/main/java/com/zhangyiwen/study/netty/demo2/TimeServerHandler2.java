package com.zhangyiwen.study.netty.demo2;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

import java.util.Date;

/**
 * 服务端Handler.用于模拟TCP粘包问题.接收客户端请求数据，返回客户端请求数据及当前时间
 * Created by zhangyiwen on 16/1/27.
 */
public class TimeServerHandler2 extends ChannelInboundHandlerAdapter{

    private int counter;

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
//        //case1:不解决粘包问题
//        ByteBuf buf = (ByteBuf)msg;
//        byte[] req = new byte[buf.readableBytes()];
//        buf.readBytes(req); //从buf中读取数据到req
//        String body = new String(req,"UTF-8").substring(0, req.length - System.getProperty("line.separator").length());

        //case2:解决粘包问题
        String body = (String)msg;

        System.out.println("The time server receive order:" + body + "; the counter is:"+ ++counter);
        String currentTime = new Date(System.currentTimeMillis()).toString();
        currentTime = currentTime + System.getProperty("line.separator");
        System.out.println("~~~"+currentTime);
        ByteBuf resp = Unpooled.copiedBuffer(currentTime.getBytes());
        ctx.writeAndFlush(resp);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        System.out.println("Unexpected exception cause.");
        ctx.close();
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        System.out.println("channelInactive");
        super.channelInactive(ctx);
    }
}
