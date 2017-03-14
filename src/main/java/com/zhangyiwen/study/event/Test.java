package com.zhangyiwen.study.event;

/**
 * Created by zhangyiwen on 17/2/23.
 */
public class Test {

    public static void main(String[] args){
        EventSource ds = new EventSource();
        ds.addEventListener(new EventObserver());
        ds.notifyEvent("Hello LuK!");
    }
}
