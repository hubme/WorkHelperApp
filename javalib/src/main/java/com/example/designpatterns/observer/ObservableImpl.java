package com.example.designpatterns.observer;

import java.util.ArrayList;
import java.util.List;

/**
 * 被观察者实现类。
 *
 * @author VanceKing
 * @since 2022/2/23
 */
class ObservableImpl implements Observable {
    //保存观察者，用户通知观察者
    private final List<Observer> mObservers = new ArrayList<>();

    @Override
    public void register(Observer observer) {
        mObservers.add(observer);
    }

    @Override
    public void unRegister(Observer observer) {
        mObservers.remove(observer);
    }

    @Override
    public void notifyObserver(Message message) {
        mObservers.forEach(observer -> observer.observe(message));
    }
}
