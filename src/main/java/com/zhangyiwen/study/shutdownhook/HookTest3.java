package com.zhangyiwen.study.shutdownhook;

import java.util.concurrent.TimeUnit;

/**
 * Created by zhangyiwen on 16/12/18.
 * 进程被中断时调用关闭钩子
 */
public class HookTest3 {
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

    public static void main(String[] args)
    {
        new HookTest3().start();
        Thread thread = new Thread(new Runnable(){

            @Override
            public void run()
            {
                while(true)
                {
                    System.out.println("thread is running....");
                    try
                    {
                        TimeUnit.MILLISECONDS.sleep(100);
                    }
                    catch (InterruptedException e)
                    {
                        e.printStackTrace();
                    }
                }
            }

        });
        thread.start();
    }
}
