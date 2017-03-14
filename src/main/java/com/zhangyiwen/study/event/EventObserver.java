package com.zhangyiwen.study.event;

import java.util.Observable;
import java.util.Observer;

/**
 * 观察者,此update方法自动由Observer类调用
 * 而 update方法主要是调用 业务方法， 当然，我们也可以在这个方法中直接的业务逻辑处理， 而不用调来调去
 * Created by zhangyiwen on 17/2/23.
 */
public class EventObserver implements Observer{

    @Override
    public void update(Observable o, Object arg) {
        ((EventObserveable)o).logicHandler(arg.toString());
    }
}
