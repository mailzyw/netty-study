package com.zhangyiwen.study.disruptor.demo1;

import com.lmax.disruptor.ExceptionHandler;

/**
 * Created by zhangyiwen on 16/12/8.
 */
public class IntEventExceptionHandler implements ExceptionHandler<IntEvent>{
    public void handleEventException(Throwable throwable, long l, IntEvent intEvent) {

    }

    public void handleOnStartException(Throwable throwable) {

    }

    public void handleOnShutdownException(Throwable throwable) {

    }
}
