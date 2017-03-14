package com.zhangyiwen.study.shutdownhook.demo;

import java.util.concurrent.TimeUnit;

/**
 * Created by zhangyiwen on 16/12/18.
 */
public class ShutdownHook implements Runnable{
    @Override
    public void run() {
        System.out.println("ShutdownHook execute start...");
        System.out.print("Netty NioEventLoopGroup shutdownGracefully...");
        try {
            TimeUnit.SECONDS.sleep(10);//模拟应用进程退出前的处理操作
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("ShutdownHook execute end...");
        System.out.println("Sytem shutdown over, the cost time is 10000MS");
    }
}
