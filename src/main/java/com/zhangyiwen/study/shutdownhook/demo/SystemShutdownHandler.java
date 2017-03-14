package com.zhangyiwen.study.shutdownhook.demo;

import sun.misc.Signal;
import sun.misc.SignalHandler;

/**
 * Created by zhangyiwen on 16/12/18.
 */
public class SystemShutdownHandler implements SignalHandler{

    public SystemShutdownHandler() {
        invokeShutdownHook();
    }

    @Override
    public void handle(Signal signal) {
        System.out.println("handler handle start.");
        Runtime.getRuntime().exit(0);
        System.out.println("handler handle end.");
    }

    private void invokeShutdownHook()
    {
        System.out.println("invokeShutdownHook start.");
        Thread t = new Thread(new ShutdownHook(), "ShutdownHook-Thread");
        Runtime.getRuntime().addShutdownHook(t);
        System.out.println("invokeShutdownHook end.");
    }
}
