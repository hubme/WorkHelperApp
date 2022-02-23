package com.example.designpatterns.observer;

class Main {
    public static void main(String[] args) {
        ObserverImpl observer1 = new ObserverImpl();
        ObserverImpl observer2 = new ObserverImpl();

        ObservableImpl observable = new ObservableImpl();
        observable.register(observer1);
        observable.register(observer2);

        observable.notifyObserver(new Message());
    }
}
