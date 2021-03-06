package com.zhangyiwen.study.netty.demo5.bean;

/**
 * Created by zhangyiwen on 16/11/22.
 * 通信消息体
 */
public final class NettyMessage {
    private Header header;  //消息头
    private Object body;    //消息体

    @Override
    public String toString() {
        return "NettyMessage{" +
                "header=" + header +
                ", body=" + body +
                '}';
    }

    public Header getHeader() {
        return header;
    }

    public void setHeader(Header header) {
        this.header = header;
    }

    public Object getBody() {
        return body;
    }

    public void setBody(Object body) {
        this.body = body;
    }
}
