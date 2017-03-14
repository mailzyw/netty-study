package com.zhangyiwen.study.shutdownhook;

import java.util.concurrent.TimeUnit;

/**
 * Created by zhangyiwen on 16/12/18.
 * 程序正常退出时调用关闭钩子
 */
public class HookTest {

    public static void main(String[] args) throws Exception {
        new HookTest().start();
        System.out.println("The Application is doing something");
        try
        {
            TimeUnit.MILLISECONDS.sleep(5000);
        }
        catch (InterruptedException e)
        {
            e.printStackTrace();
        }
    }

    public void start(){
        Runtime.getRuntime().addShutdownHook(new Thread(new Runnable() {
            @Override
            public void run() {
                System.out.println("Execute hook...");
            }
        }));
    }

}
