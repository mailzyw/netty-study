package com.zhangyiwen.study.event;

import java.util.Observer;

/**
 * 此类可以 看做 事件源，因为notifyEvent就是发送事件的源头 EXACTLY! 专供外部调用， 同时 notifyEvent 也是事件处理回调函数
 * 此类中，我们需要保持一个Observeable的对象，它来代替我们一部分工作
 * 当然，首先要 完成 监听器的 添加、移除 等监听器相关操作
 *      ———— 当然，我们可以有其他方式添加监听器(参：http://rokily.iteye.com/blog/775395)，但是把addEventListener放在这个累里面感觉比较直接
 * 此类只是简单实现，实际中我们需要完善其功能，可以写的很复杂 ！
 * Created by zhangyiwen on 17/2/23.
 */
public class EventSource {

    private EventObserveable ob = new EventObserveable();

    public void addEventListener(Observer listener){
        ob.addObserver(listener);
    }

    public void removeEventListener(Observer listener){
        ob.deleteObserver(listener);
    }

    public void notifyEvent(Object arg){
        ob.action(arg);
    }

}
