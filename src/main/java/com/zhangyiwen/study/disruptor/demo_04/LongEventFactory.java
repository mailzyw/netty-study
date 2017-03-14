package com.zhangyiwen.study.disruptor.demo_04;

import com.lmax.disruptor.EventFactory;

/**
 * 事件工厂(Event Factory):定义了如何实例化事件
 * Created by zhangyiwen on 17/3/4.
 */
public class LongEventFactory implements EventFactory<LongEvent> {
    @Override
    public LongEvent newInstance() {
        return new LongEvent();
    }
}
