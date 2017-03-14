package com.zhangyiwen.study.disruptor.demo_04;

import com.lmax.disruptor.EventHandler;

/**
 * 事件处理的具体实现
 * Created by zhangyiwen on 17/3/4.
 */
public class LongEventHandler implements EventHandler<LongEvent> {
    @Override
    public void onEvent(LongEvent event, long sequence, boolean endOfBatch) throws Exception {
        System.out.println(Thread.currentThread().getName()+" Event: " + event);
    }
}
