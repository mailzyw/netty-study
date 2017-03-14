package com.zhangyiwen.study.disruptor.demo1;

import com.lmax.disruptor.EventFactory;

/**
 * Created by zhangyiwen on 16/12/8.
 * RingBuffer中存储的单元
 */
public class IntEvent {

    private int value = -1;

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public String toString() {
        return String.valueOf(value);
    }

    public static EventFactory<IntEvent> INT_ENEVT_FACTORY = new EventFactory<IntEvent>() {
        public IntEvent newInstance() {
            return new IntEvent();
        }
    };

}
