package com.zhangyiwen.study.netty.demo;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

import java.io.BufferedReader;
import java.io.InputStreamReader;

/**
 * 客户端Handler.读取用户命令发送给服务器，接收服务器响应
 * Created by zhangyiwen on 16/1/27.
 */
public class TimeClientHandler extends ChannelInboundHandlerAdapter{

    private final ByteBuf firstMessage; //客户端与服务器连接时，主动发送的第一条问候消息
    private BufferedReader reader;  //获取用户输入的Reader

    public TimeClientHandler(){
        byte[] req = "First Hello".getBytes();
        firstMessage = Unpooled.buffer(req.length);
        firstMessage.writeBytes(req);
        reader = new BufferedReader(new InputStreamReader(System.in));
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        ctx.writeAndFlush(firstMessage);
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {

        //获取服务器响应
        ByteBuf buf = (ByteBuf)msg;
        byte[] resp = new byte[buf.readableBytes()];
        buf.readBytes(resp);    //从buf中读取数据到resp
        String body = new String(resp,"UTF-8");
        System.out.println("Server response is: " + body);

        //读取用户命令并发送给服务器
        System.out.println("\r\nPlease input your command line...");
        String command = reader.readLine();
        if(!"QUIT".equalsIgnoreCase(command)){
            ByteBuf timeMessage = Unpooled.buffer(command.getBytes().length);
            timeMessage.writeBytes(command.getBytes());
            ctx.writeAndFlush(timeMessage);
        }else {
            ByteBuf timeMessage = Unpooled.buffer("QUIT".getBytes().length);
            timeMessage.writeBytes("QUIT".getBytes());
            ctx.writeAndFlush(timeMessage);
            ctx.close();
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        System.out.println("Unexpected exception cause.");
        ctx.close();
    }
}
