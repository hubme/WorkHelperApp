package com.example.designpatterns.observer;

/**
 * 定义被观察者接口。
 *
 * @author VanceKing
 * @since 2022/2/23
 */
interface Observable {
    //注册观察者
    void register(Observer observer);

    //移除观察者
    void unRegister(Observer observer);

    void notifyObserver(Message message);
}
