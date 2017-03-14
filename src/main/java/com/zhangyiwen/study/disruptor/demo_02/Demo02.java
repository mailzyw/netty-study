package com.zhangyiwen.study.disruptor.demo_02;

import com.lmax.disruptor.*;
import com.zhangyiwen.study.disruptor.demo_01.TradeTransaction;
import com.zhangyiwen.study.disruptor.demo_01.TradeTransactionInDBHandler;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 使用WorkerPool辅助创建消费者
 * Created by zhangyiwen on 17/3/3.
 */
public class Demo02 {

    public static void main(String[] args) throws Exception {
        int BUFFER_SIZE = 1024;
        int THREAD_NUMBERS = 4;
        ExecutorService executor = Executors.newFixedThreadPool(THREAD_NUMBERS);

        EventFactory<TradeTransaction> eventFactory = new EventFactory<TradeTransaction>() {
            @Override
            public TradeTransaction newInstance() {
                return new TradeTransaction();
            }
        };

        //创建一个单生产者的RingBuffer
        RingBuffer<TradeTransaction> ringBuffer = RingBuffer.createSingleProducer(eventFactory,BUFFER_SIZE);

        //创建SequenceBarrier
        SequenceBarrier sequenceBarrier = ringBuffer.newBarrier();

        //创建EventHandler
        WorkHandler<TradeTransaction> workHandler = new TradeTransactionInDBHandler();

        //创建并启动WorkerPool
        WorkerPool<TradeTransaction> workerPool = new WorkerPool<TradeTransaction>(ringBuffer,sequenceBarrier,new IgnoreExceptionHandler(), workHandler);
        workerPool.start(executor);

        //下面这个生产8个数据，图简单就写到主线程算了
        for(int i=0;i<8;i++){
            long seq=ringBuffer.next();
            ringBuffer.get(seq).setPrice(Math.random()*9999);
            ringBuffer.publish(seq);
        }

        Thread.sleep(1000);
        workerPool.halt();
        executor.shutdown();

    }

}
