package com.zhangyiwen.study.disruptor.demo_04;

import com.lmax.disruptor.*;
import com.lmax.disruptor.dsl.Disruptor;
import com.lmax.disruptor.dsl.ProducerType;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 简单的disruptor使用例子
 * Created by zhangyiwen on 17/3/4.
 */
public class Demo04 {

    public static void main(String[] args) throws Exception {

        //定义用于事件处理的线程池
        ExecutorService executor = Executors.newFixedThreadPool(4);
        //指定等待策略
        WaitStrategy BLOCKING_WAIT = new BlockingWaitStrategy();
        WaitStrategy SLEEPING_WAIT = new SleepingWaitStrategy();
        WaitStrategy YIELDING_WAIT = new YieldingWaitStrategy();
        //RingBuffer 大小，必须是 2 的 N 次方；
        int ringBufferSize = 1024 * 1024;
        //事件工厂
        EventFactory<LongEvent> eventFactory = new LongEventFactory();
        //事件处理的具体实现
        EventHandler<LongEvent> eventHandler = new LongEventHandler();

        //启动 Disruptor
        Disruptor<LongEvent> disruptor = new Disruptor<LongEvent>(
                eventFactory,
                ringBufferSize,
                executor,
                ProducerType.SINGLE,
                new YieldingWaitStrategy()
        );
        disruptor.handleEventsWith(eventHandler);
        disruptor.start();

        //发布事件
        RingBuffer<LongEvent> ringBuffer = disruptor.getRingBuffer();
        for(int i=0;i<100;i++){
            long sequence = ringBuffer.next();//请求下一个事件序号；
            try {
                LongEvent event = ringBuffer.get(sequence);//获取该序号对应的事件对象；
                long data = i;//获取要通过事件传递的业务数据；
                event.setValue(data);
            } finally{
                ringBuffer.publish(sequence);//发布事件；
            }
        }

        //关闭Disruptor
        disruptor.shutdown();//关闭 disruptor，方法会堵塞，直至所有的事件都得到处理；
        executor.shutdown();//关闭 disruptor 使用的线程池；如果需要的话，必须手动关闭， disruptor 在 shutdown 时不会自动关闭；
    }


}
