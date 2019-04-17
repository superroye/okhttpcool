package com.supylc.mobilearch.rxjava2;

import io.reactivex.Observable;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

/**
 * @author Roye
 * @date 2018/9/6
 */
public class RxSimple {

    public static <T> Observable<T> createSubscriber(ObservableOnSubscribe<T> observableOnSubscribe) {
        Observable<T> observable = Observable.create(observableOnSubscribe)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
        return observable;
    }

    public static <T, R> Observable<R> createMap(Function<T, R> function, T input) {
        Observable<R> observable = Observable.just(input).map(function).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
        return observable;
    }

    public static <R> Observable<R> createMap(Function<String, R> function) {
        Observable<R> observable = Observable.just("").map(function).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
        return observable;
    }

    public static void runOnUIThread(final Runnable runnable) {
        Observable.just("").map(new Function<String, String>() {
            @Override
            public String apply(String s) throws Exception {
                runnable.run();
                return "";
            }
        }).subscribeOn(AndroidSchedulers.mainThread()).subscribe();
    }

}
