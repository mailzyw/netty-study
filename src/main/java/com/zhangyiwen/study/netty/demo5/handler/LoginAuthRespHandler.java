package com.zhangyiwen.study.netty.demo5.handler;

import com.zhangyiwen.study.netty.demo5.bean.Header;
import com.zhangyiwen.study.netty.demo5.bean.MessageType;
import com.zhangyiwen.study.netty.demo5.bean.NettyMessage;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

import java.net.InetSocketAddress;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by zhangyiwen on 16/11/22.
 * 握手认证的服务端ChannelHandler,用于响应客户端发来的握手请求
 */
public class LoginAuthRespHandler extends ChannelInboundHandlerAdapter{

    private Map<String,Boolean> nodeCheck = new ConcurrentHashMap<String, Boolean>();
    private String[] whiteList = {"127.0.0.1"};

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        NettyMessage message = (NettyMessage)msg;
        //  如果是握手请求消息,处理;其他消息透传
        if(message.getHeader() != null && message.getHeader().getType() == (byte)MessageType.LOGIN_REQ.ordinal()){
            String nodeIndex = ctx.channel().remoteAddress().toString();
            System.out.println("==="+nodeIndex);
            NettyMessage loginResp = null;
            if(nodeCheck.containsKey(nodeIndex)){// 重复登录,拒绝.防止由于客户端重复登录导致的句柄泄露
                loginResp = buildResponse(-1);
                System.out.println(Thread.currentThread().getId()+"[get login request] "+message);
                System.out.println(Thread.currentThread().getId()+"[send login response] "+loginResp);
                ctx.writeAndFlush(loginResp);
                ctx.close();
            }else {
                InetSocketAddress address = (InetSocketAddress)ctx.channel().remoteAddress();
                String ip = address.getAddress().getHostAddress();
                boolean isOK = false;
                for(String WIP:whiteList){
                    if(WIP.equals(ip)){
                        isOK = true;
                        break;
                    }
                }
                loginResp = isOK?buildResponse(0):buildResponse(-1);
                if(isOK){
//                    nodeCheck.put(nodeIndex,true);
                    System.out.println(Thread.currentThread().getId()+"[get login request] "+message);
                    System.out.println(Thread.currentThread().getId()+"[send login response] "+loginResp);
                    ctx.writeAndFlush(loginResp);
                }else{
                    System.out.println(Thread.currentThread().getId()+"[get login request] "+message);
                    System.out.println(Thread.currentThread().getId()+"[send login response] "+loginResp);
                    ctx.writeAndFlush(loginResp);
                    ctx.close();
                }
            }
        }else {
            ctx.fireChannelRead(msg);
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        System.out.println(Thread.currentThread().getId()+"==auth resp exceptionCaught==");
        nodeCheck.remove(ctx.channel().remoteAddress().toString());
        ctx.close();
        ctx.fireExceptionCaught(cause);
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        System.out.println(Thread.currentThread().getId()+"==auth resp channelInactive==");
        super.channelInactive(ctx);
    }



    private NettyMessage buildResponse(int result){
        NettyMessage message = new NettyMessage();
        Header header = new Header();
        header.setType((byte)MessageType.LOGIN_RESP.ordinal());
        message.setHeader(header);
        message.setBody(result);
        return message;
    }
}
