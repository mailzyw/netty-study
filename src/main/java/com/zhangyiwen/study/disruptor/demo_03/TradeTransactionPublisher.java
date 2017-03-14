package com.zhangyiwen.study.disruptor.demo_03;

import com.lmax.disruptor.dsl.Disruptor;
import com.zhangyiwen.study.disruptor.demo_01.TradeTransaction;

import java.util.concurrent.CountDownLatch;

/**
 * 交易事件生产器
 * Created by zhangyiwen on 17/3/3.
 */
public class TradeTransactionPublisher implements Runnable{

    Disruptor<TradeTransaction> disruptor;
    private CountDownLatch latch;
    private static int LOOP = 10;  //模拟一百万次交易的发生

    public TradeTransactionPublisher(CountDownLatch latch, Disruptor<TradeTransaction> disruptor){
        this.disruptor = disruptor;
        this.latch = latch;
    }

    @Override
    public void run() {
        TradeTransactionEventTranslator tradeTransactionEventTranslator = new TradeTransactionEventTranslator();
        for(int i=0;i<LOOP;i++){
            disruptor.publishEvent(tradeTransactionEventTranslator);
        }
        latch.countDown();
    }
}
