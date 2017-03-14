package com.zhangyiwen.study.netty.demo5.handler;

import com.zhangyiwen.study.netty.demo5.bean.Header;
import com.zhangyiwen.study.netty.demo5.bean.MessageType;
import com.zhangyiwen.study.netty.demo5.bean.NettyMessage;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

/**
 * Created by zhangyiwen on 16/11/22.
 * 握手认证的客户端ChannelHandler,用于在通道激活时发起握手请求
 */
public class LoginAuthReqHandler extends ChannelInboundHandlerAdapter {

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        NettyMessage loginMessage = buildLoginReq();
        ctx.writeAndFlush(loginMessage);
        System.out.println(Thread.currentThread().getId()+"[channel active.send login request]");
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        NettyMessage message = (NettyMessage)msg;
        //如果是握手应答消息,处理,需要判断是否认证成功;其他消息透传
        if(message.getHeader() != null && message.getHeader().getType() == (byte) MessageType.LOGIN_RESP.ordinal()){
            int loginResult = Integer.parseInt(message.getBody().toString());
            if(loginResult != 0){//握手失败,关闭连接
                System.out.println(Thread.currentThread().getId()+"[Login is failed] "+message);
                ctx.close();
            }else { //握手成功
                System.out.println(Thread.currentThread().getId()+"[Login is OK] "+message);
                ctx.fireChannelRead(msg);
            }
        }else {
            ctx.fireChannelRead(msg);
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        System.out.println(Thread.currentThread().getId()+"==auth req exceptionCaught==");
        ctx.fireExceptionCaught(cause);
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        System.out.println(Thread.currentThread().getId()+"==auth req channelInactive==");
        super.channelInactive(ctx);
    }

    private NettyMessage buildLoginReq(){
        NettyMessage message = new NettyMessage();
        Header header = new Header();
        header.setType((byte) MessageType.LOGIN_REQ.ordinal());
        message.setHeader(header);
        return message;
    }
}
