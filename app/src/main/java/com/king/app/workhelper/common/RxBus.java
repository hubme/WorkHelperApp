package com.king.app.workhelper.common;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import io.reactivex.Flowable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Function;
import io.reactivex.functions.Predicate;
import io.reactivex.processors.FlowableProcessor;
import io.reactivex.processors.PublishProcessor;

/**
 * Created by ari on 17-7-7.
 * https://github.com/18598925736/RxBusStudy2
 */
public class RxBus {

    private final FlowableProcessor<Object> mBus;//核心类 FlowableProcessor - 可流动的处理器

    private final Map<String, Object> mStickyEventMap;//粘性事件，无视订阅顺序，只要订阅了就能收到

    private RxBus() {
        mBus = PublishProcessor.create().toSerialized();
        mStickyEventMap = new ConcurrentHashMap<>();
    }

    private static class InstanceHolder {
        private static RxBus sInstance = new RxBus();
    }

    public static RxBus getInstance() {
        return InstanceHolder.sInstance;
    }

    /**
     * 事件封装
     * 为了区分事件类别，加入了code属性
     * 另一个属性Object，则是消息的本体
     */
    public static class RxBusBaseMessage {

        private String code;
        private Object object;

        private RxBusBaseMessage() {
        }

        public RxBusBaseMessage(String code, Object object) {
            this.code = code;
            this.object = object;
        }

        public String getCode() {
            return code;
        }

        public Object getObject() {
            return object;
        }
    }

    public void post(String code, Object o) {
        mBus.onNext(new RxBusBaseMessage(code, o));
    }

    /**
     * 订阅事件
     *
     * @param code      事件区分标识
     * @param eventType 该事件标识对应的 数据类型JavaBean
     * @param <T>       eventType的class
     * @return
     */
    public <T> Flowable<T> register(final String code, final Class<T> eventType) {
        return mBus//订阅的过程
                .ofType(RxBusBaseMessage.class)//1-过滤发布者发出的条目，只发出指定类型的条目,
                //用RxBusBaseMessage.java来过滤,表示，只接收类型为RxBusBaseMessage的消息
                .filter(new Predicate<RxBusBaseMessage>() {//2-通过只发出满足指定谓词的内容来过滤发布者发出的条目
                    //用指定的事件标识码 和 数据JavaBean来过滤
                    @Override
                    public boolean test(@io.reactivex.annotations.NonNull RxBusBaseMessage rxBusBaseMessage) throws Exception {
                        return rxBusBaseMessage.getCode().equals(code) && eventType.isInstance(rxBusBaseMessage.getObject());//再用code来过滤，表示只接收code值为我指定参数的消息
                    }
                })
                .map(new Function<RxBusBaseMessage, Object>() {//3-返回一个流，它将指定的功能应用于源发布程序发出的每一项，并释放这些函数应用程序的结果。
                    // 这个确实没看懂在说啥
                    @Override
                    public Object apply(RxBusBaseMessage o) {//
                        return o.getObject();
                    }
                })
                .cast(eventType)//4-返回一个流，它释放源发布者发出的条目，转换为指定的类型,强转为参数中的javaBean，然后还要强转成我指定的类别
                .onBackpressureLatest()//5-指示一个发布程序的发布者比其订阅者更快地释放条目，以保存最新的值并根据请求发出。这个的意思是，如果有信息积压，永远只保留最后一个未被处理的?
                .observeOn(AndroidSchedulers.mainThread());//6-修改一个发布者，在指定的调度器上执行它的排放和通知，（切换消费者的线程到 主线程 ）
        //异步地使用缓冲大小槽的有界缓冲区。

    }

    /**
     * Stciky 相关
     */

    /**
     * 发送一个新Sticky事件
     */
    public void postSticky(String code, Object event) {
        synchronized (mStickyEventMap) {
            mStickyEventMap.put(code, event);//这样的话，每一个业务好像只能保存一个消息,也就是最近的消息，补给它
        }

        post(code, event);
    }

    /**
     * 根据传递的 eventType 类型返回特定类型(eventType)的 被观察者
     */
    public <T> Flowable<T> registertoObservableSticky(String code, final Class<T> eventType) {
        synchronized (mStickyEventMap) {//也就是说，当 观察者订阅消息的时候，把前面的消息补给他?
            Flowable<T> observable = register(code, eventType);//OK,注册完了，
            final Object event = mStickyEventMap.get(code);//取出最近消息

            if (event != null) {//如果有最近消息
                return observable.mergeWith(Flowable.just(eventType.cast(event)));//就把最近的消息强转成指定类型，然后合并给这个观察者
            } else {
                return observable;
            }
        }
    }

    /**
     * 根据eventType获取Sticky事件,也就是最近一次收到的消息
     */
    public <T> T getStickyEvent(Class<T> eventType) {
        synchronized (mStickyEventMap) {
            return eventType.cast(mStickyEventMap.get(eventType));
        }
    }

    /**
     * 清除订阅,
     * 清除所有的最近消息
     */
    public void clear() {
        mStickyEventMap.clear();
        mBus.onComplete();//停止发送所有消息，即使调用了onNext，也不再执行onNext
    }

    /**
     * 移除指定eventType的Sticky事件
     */
    public <T> T removeStickyEvent(Class<T> eventType) {
        synchronized (mStickyEventMap) {
            return eventType.cast(mStickyEventMap.remove(eventType));
        }
    }

    /**
     * 移除所有的Sticky事件
     */
    public void removeAllStickyEvents() {
        synchronized (mStickyEventMap) {
            mStickyEventMap.clear();
        }
    }
}
