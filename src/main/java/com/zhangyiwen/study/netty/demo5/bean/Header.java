package com.zhangyiwen.study.netty.demo5.bean;

/**
 * Created by zhangyiwen on 16/11/22.
 * 通信消息头
 */
public final class Header {
    private int crcCode = 0xabef0101;
    private int length;         //消息长度
    private long sessionID;     //会话ID
    private byte type;          //消息类型
    private byte priority;      //消息优先级

    @Override
    public String toString() {
        return "Header{" +
                "crcCode=" + crcCode +
                ", length=" + length +
                ", sessionID=" + sessionID +
                ", type=" + type +
                ", priority=" + priority +
                '}';
    }

    public int getCrcCode() {
        return crcCode;
    }

    public void setCrcCode(int crcCode) {
        this.crcCode = crcCode;
    }

    public int getLength() {
        return length;
    }

    public void setLength(int length) {
        this.length = length;
    }

    public long getSessionID() {
        return sessionID;
    }

    public void setSessionID(long sessionID) {
        this.sessionID = sessionID;
    }

    public byte getType() {
        return type;
    }

    public void setType(byte type) {
        this.type = type;
    }

    public byte getPriority() {
        return priority;
    }

    public void setPriority(byte priority) {
        this.priority = priority;
    }

}
