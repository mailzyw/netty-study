package com.zhangyiwen.study.event;

import java.util.Observable;

/**
 * 可以看做业务po，业务逻辑的处理应该在此类中完成
 * 需要继承Observable是因为我们用到了java提供的功能
 * Created by zhangyiwen on 17/2/23.
 */
public class EventObserveable extends Observable{

    public void action(Object arg){
        super.setChanged();
        super.notifyObservers(arg);
    }

    public void logicHandler(String name){
        //logic handle here,lime:
        System.out.println("Object arg is:" + name);
    }

}
