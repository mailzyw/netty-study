package com.zhangyiwen.study.netty.demo_echo;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by zhangyiwen on 17/3/4.
 */
public class EchoClientHandler extends ChannelInboundHandlerAdapter {

    private static final Logger logger = Logger.getLogger(EchoClientHandler.class.getName());

    private final ByteBuf firstMessage; //客户端与服务器连接时，主动发送的第一条问候消息
    private BufferedReader reader;  //获取用户输入的Reader

    /**
     * Creates a client-side handler.
     */
    public EchoClientHandler() {
        byte[] req = "First Hello".getBytes();
        firstMessage = Unpooled.buffer(req.length);
        firstMessage.writeBytes(req);
        reader = new BufferedReader(new InputStreamReader(System.in));
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) {
        System.out.println("[channelActive]" + firstMessage);
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
