package com.zhangyiwen.study.shutdownhook.demo;

import sun.misc.Signal;

/**
 * Created by zhangyiwen on 16/12/18.
 */
public class TestJavaHook {

    public static void main(String[] args) throws Exception {
        Signal signal = new Signal(getOSSignalType());
        Signal.handle(signal,new SystemShutdownHandler());
        Thread.sleep(5*1000L);
        Runtime.getRuntime().exit(0);
    }

    private static String getOSSignalType()
    {
        return System.getProperties().getProperty("os.name").toLowerCase().startsWith("win") ? "INT" : "USR2";
    }

}
