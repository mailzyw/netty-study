package com.zhangyiwen.study.disruptor.demo_03;

import com.lmax.disruptor.EventHandler;
import com.zhangyiwen.study.disruptor.demo_01.TradeTransaction;

/**
 * 消费者C3的EventHandler
 * Created by zhangyiwen on 17/3/3.
 */
public class TradeTransactionVasHandler implements EventHandler<TradeTransaction>{
    @Override
    public void onEvent(TradeTransaction event, long sequence, boolean endOfBatch) throws Exception {
        System.out.println("vas message:"+event);
    }
}
