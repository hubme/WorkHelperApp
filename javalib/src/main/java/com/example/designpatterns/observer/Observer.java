package com.example.designpatterns.observer;

/**
 * 定义观察者接口。观察者是接收到事件时做出响应。
 *
 * @author VanceKing
 * @since 2022/2/23
 */
interface Observer {
    void observe(Message message);
}
