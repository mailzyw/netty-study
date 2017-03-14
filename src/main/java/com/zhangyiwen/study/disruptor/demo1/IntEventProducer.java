package com.zhangyiwen.study.disruptor.demo1;

import com.lmax.disruptor.WorkHandler;

/**
 * Created by zhangyiwen on 16/12/8.
 * 生产者
 */
public class IntEventProducer implements WorkHandler<IntEvent>{
    private int seq = 0;
    public void onEvent(IntEvent intEvent) throws Exception {
        System.out.println("produced event. event.value="+seq);
        intEvent.setValue(++seq);
    }
}
