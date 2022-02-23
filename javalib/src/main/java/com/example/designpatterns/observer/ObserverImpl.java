package com.example.designpatterns.observer;

/**
 * 观察者实现类。当事件发生时，处理逻辑。
 *
 * @author VanceKing
 * @since 2022/2/23
 */
class ObserverImpl implements Observer {
    @Override
    public void observe(Message message) {
        System.out.println("观察者 " + this + " 接收到消息: " + message);
    }
}
