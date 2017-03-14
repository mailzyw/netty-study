package com.zhangyiwen.study.disruptor.demo1;

import com.lmax.disruptor.WorkHandler;

/**
 * Created by zhangyiwen on 16/12/8.
 */
public class IntEventProcessor implements WorkHandler<IntEvent>{

    public void onEvent(IntEvent intEvent) throws Exception {
        System.out.println("deal event. event.value="+intEvent.getValue());
    }
}
