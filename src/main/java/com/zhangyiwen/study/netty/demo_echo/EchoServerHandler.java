package com.zhangyiwen.study.netty.demo_echo;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by zhangyiwen on 17/3/4.
 */
public class EchoServerHandler extends ChannelInboundHandlerAdapter {

    private static final Logger logger = Logger.getLogger(
            EchoServerHandler.class.getName());

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
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        // Close the connection when an exception is raised.
        logger.log(Level.WARNING, "Unexpected exception from downstream.", cause);
        ctx.close();
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        logger.log(Level.INFO, "socket closed.");
    }
}
