package com.zhangyiwen.study.disruptor.demo1;

import com.lmax.disruptor.*;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * 一个经典的Disruptor例子.
 * IntEvent -> RingBuffer -> crawler处理,设置IntEvent.value的值 -> applier处理,显示IntEvent.value的值
 * Created by zhangyiwen on 16/12/8.
 */
public class DisruptorTest {

    public static void main(String[] args) throws Exception {
        //执行线程池
        ThreadPoolExecutor executor = new ThreadPoolExecutor(7,7,10, TimeUnit.MINUTES,new LinkedBlockingQueue<Runnable>(5));
        //生产者集合
        IntEventProducer[] producers = new IntEventProducer[1];
        for (int i = 0; i < producers.length; i++) {
            producers[i] = new IntEventProducer();
        }
        //消费者集合
        IntEventProcessor[] processors = new IntEventProcessor[1];
        for (int i = 0; i < processors.length; i++) {
            processors[i] = new IntEventProcessor();
        }

        //创建一个RingBuffer对象
        RingBuffer<IntEvent> ringBuffer = RingBuffer.createSingleProducer(IntEvent.INT_ENEVT_FACTORY,1024, new SleepingWaitStrategy());

        //使用WorkerPool创建生产者
        SequenceBarrier sequenceBarrier = ringBuffer.newBarrier();
        WorkerPool<IntEvent> crawler = new WorkerPool<IntEvent>(ringBuffer,sequenceBarrier,new IntEventExceptionHandler(),producers);

        //使用WorkerPool创建消费者
        SequenceBarrier sb = ringBuffer.newBarrier(crawler.getWorkerSequences());
        WorkerPool<IntEvent> applier = new WorkerPool<IntEvent>(ringBuffer,sb, new IntEventExceptionHandler(), processors);

        //Add the specified gating sequences to this instance of the Disruptor.
        List<Sequence> gatingSequences = new ArrayList<Sequence>();
        for(Sequence s : crawler.getWorkerSequences()) {
            gatingSequences.add(s);
        }
        for(Sequence s : applier.getWorkerSequences()) {
            gatingSequences.add(s);
        }
        ringBuffer.addGatingSequences(gatingSequences.toArray(new Sequence[gatingSequences.size()]));

        //启动消费者和生产者
        crawler.start(executor);
        applier.start(executor);

        //不断生产事件
        while (true){
            Thread.sleep(1000);
            long lastSeq = ringBuffer.next();
            ringBuffer.publish(lastSeq);
        }
    }

}
