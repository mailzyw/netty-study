package com.zhangyiwen.study.netty.demo4;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.FullHttpRequest;

/**
 * Created by zhangyiwen on 16/11/18.
 */
public class HttpFileServerHandler extends SimpleChannelInboundHandler<FullHttpRequest>{
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, FullHttpRequest msg) throws Exception {

    }
}
