package com.zhangyiwen.study.disruptor.demo_04;

/**
 * 事件:就是通过Disruptor进行交换到数据类型
 * Created by zhangyiwen on 17/3/3.
 */
public class LongEvent {

    private long value;

    public LongEvent() {
    }

    public LongEvent(long value) {
        this.value = value;
    }

    public long getValue() {
        return value;
    }

    public void setValue(long value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return "LongEvent{" +
                "value=" + value +
                '}';
    }
}
