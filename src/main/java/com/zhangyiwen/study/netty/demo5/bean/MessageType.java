package com.zhangyiwen.study.netty.demo5.bean;

/**
 * Created by zhangyiwen on 16/11/22.
 */
public enum MessageType {

    MSG_REQ,    //业务请求消息
    MSG_RESP,   //业务响应消息
    MSG_ONEWAY, //业务ONE WAY消息
    LOGIN_REQ,   //握手请求消息
    LOGIN_RESP,  //握手应答消息
    HEART_REQ,  //心跳请求消息
    HEART_RESP; //心跳应答消息


}
