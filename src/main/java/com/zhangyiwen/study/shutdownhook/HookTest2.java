package com.zhangyiwen.study.shutdownhook;

import java.util.concurrent.TimeUnit;

/**
 * Created by zhangyiwen on 16/12/18.
 * 程序遇到内存溢出错误后调用关闭钩子
 */
public class HookTest2 {

    public static void main(String[] args)
    {
        new HookTest2().start();
        System.out.println("The Application is doing something");
        byte[] b = new byte[500*1024*1024];
        try
        {
            TimeUnit.MILLISECONDS.sleep(5000);
        }
        catch (InterruptedException e)
        {
            e.printStackTrace();
        }
    }

    public void start()
    {
        Runtime.getRuntime().addShutdownHook(new Thread(new Runnable() {
            @Override
            public void run()
            {
                System.out.println("Execute Hook.....");
            }
        }));
    }


}
