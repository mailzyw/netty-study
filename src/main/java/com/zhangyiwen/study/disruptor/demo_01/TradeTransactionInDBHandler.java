package com.zhangyiwen.study.disruptor.demo_01;

import com.lmax.disruptor.EventHandler;
import com.lmax.disruptor.WorkHandler;

import java.util.UUID;

/**
 * Created by zhangyiwen on 17/3/3.
 */
public class TradeTransactionInDBHandler implements EventHandler<TradeTransaction>,WorkHandler<TradeTransaction>
{
    long index = 0;

    @Override
    public void onEvent(TradeTransaction event, long sequence, boolean endOfBatch) throws Exception {
        this.onEvent(event);
    }

    @Override
    public void onEvent(TradeTransaction event) throws Exception {
        //这里做具体的消费逻辑
        event.setId(String.valueOf(index++));
        System.out.println("InDB msg:" + event);
    }
}
