package com.zhangyiwen.study.netty.demo3;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

/**
 * Created by zhangyiwen on 16/11/17.
 */
public class EchoClientHandler extends ChannelInboundHandlerAdapter {

    private final int sendNumber;

    public EchoClientHandler(int sendNumber) {
        this.sendNumber = sendNumber;
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        UserInfo[] infos = getUserInfoList();
        for(UserInfo info:infos){
            System.out.println("send.."+info);
            ctx.writeAndFlush(info);
        }
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        System.out.println("Client receive the msgpack message:"+msg);
        ctx.write(msg);
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        ctx.flush();
    }

    private UserInfo[] getUserInfoList(){
        UserInfo[] userInfos = new UserInfo[sendNumber];
        UserInfo userInfo = null;
        for(int i=0;i<sendNumber;i++){
            userInfo = new UserInfo();
            userInfo.setAge(i);
            userInfo.setName("ABC" + i);
            userInfos[i] = userInfo;
        }
        return userInfos;
    }

}
