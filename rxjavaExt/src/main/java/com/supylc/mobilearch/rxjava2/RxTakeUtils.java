package com.supylc.mobilearch.rxjava2;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.ObservableTransformer;
import io.reactivex.functions.Action;
import io.reactivex.functions.Predicate;

/**
 * @author Roye
 * @date 2019/3/5
 */
public class RxTakeUtils {

    //只通知最新的订阅
    public static ObservableTransformer keepLast() {
        return new ObservableTransformer() {

            AtomicLong counter = new AtomicLong();

            @Override
            public ObservableSource apply(Observable upstream) {
                final long subscribeOnTime = counter.getAndSet(System.currentTimeMillis());
                return upstream.skipWhile(new Predicate() {
                    @Override
                    public boolean test(Object o) {
                        return subscribeOnTime != counter.get();
                    }
                });
            }
        };
    }

    //忽略其他并发事件，只保留一个
    public static ObservableTransformer keepFirst() {
        return new ObservableTransformer() {

            AtomicInteger counter = new AtomicInteger();

            @Override
            public ObservableSource apply(Observable upstream) {

                return upstream.takeWhile(new Predicate() {
                    @Override
                    public boolean test(Object o) {
                        return counter.getAndSet(1) == 0;
                    }
                }).doOnComplete(new Action() {
                    @Override
                    public void run() {
                        counter.getAndSet(0);
                    }
                });
            }
        };
    }
}
