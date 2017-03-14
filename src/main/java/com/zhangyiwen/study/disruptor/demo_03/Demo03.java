package com.zhangyiwen.study.disruptor.demo_03;

import com.lmax.disruptor.BusySpinWaitStrategy;
import com.lmax.disruptor.EventFactory;
import com.lmax.disruptor.dsl.Disruptor;
import com.lmax.disruptor.dsl.EventHandlerGroup;
import com.lmax.disruptor.dsl.ProducerType;
import com.zhangyiwen.study.disruptor.demo_01.TradeTransaction;
import com.zhangyiwen.study.disruptor.demo_01.TradeTransactionInDBHandler;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by zhangyiwen on 17/3/3.
 */
public class Demo03 {

    public static void main(String[] args) throws Exception {
        long beginTime = System.currentTimeMillis();
        int bufferSize = 1024;
        ExecutorService executor = Executors.newFixedThreadPool(4);

        //创建Disruptor
        Disruptor<TradeTransaction> disruptor = new Disruptor<TradeTransaction>(new EventFactory<TradeTransaction>() {
            @Override
            public TradeTransaction newInstance() {
                return new TradeTransaction();
            }
        },bufferSize,executor, ProducerType.SINGLE,new BusySpinWaitStrategy());

        //使用disruptor创建消费者组C1,C2
        EventHandlerGroup<TradeTransaction> handlerGroup = disruptor.handleEventsWith(new TradeTransactionVasHandler(),new TradeTransactionInDBHandler());
        //创建消费者C3
        TradeTransactionJMSNotifyHandler jmsHandler = new TradeTransactionJMSNotifyHandler();

        //声明在C1,C2完事之后执行JMS消息发送操作,也就是流程走到C3
        handlerGroup.then(jmsHandler);

        disruptor.start();//启动
        CountDownLatch latch=new CountDownLatch(1);
        executor.submit(new TradeTransactionPublisher(latch, disruptor));//生产者准备
        latch.await();//等待生产者完事.
        disruptor.shutdown();
        executor.shutdown();
        System.out.println("总耗时:" + (System.currentTimeMillis()-beginTime));
    }


}
