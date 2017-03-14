package com.zhangyiwen.study.disruptor.demo_03;

import com.lmax.disruptor.EventTranslator;
import com.zhangyiwen.study.disruptor.demo_01.TradeTransaction;

import java.util.Random;

/**
 * Created by zhangyiwen on 17/3/3.
 */
public class TradeTransactionEventTranslator implements EventTranslator<TradeTransaction>{
    private Random random = new Random();

    @Override
    public void translateTo(TradeTransaction event, long sequence) {
        this.generateTradeTransaction(event);
    }

    private TradeTransaction generateTradeTransaction(TradeTransaction trade){
        trade.setPrice(random.nextDouble()*9999);
        return trade;
    }
}
