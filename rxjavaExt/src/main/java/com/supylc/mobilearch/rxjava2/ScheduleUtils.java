package com.supylc.mobilearch.rxjava2;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.ObservableTransformer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * @author Roye
 * @date 2019/3/5
 */
public class ScheduleUtils {

    public static ObservableTransformer longForBgTransformer() {
        return new ObservableTransformer() {
            @Override
            public ObservableSource apply(Observable upstream) {
                return upstream.subscribeOn(Schedulers.io()).observeOn(Schedulers.computation());
            }
        };
    }

    public static ObservableTransformer longForUITransformer() {
        return new ObservableTransformer() {
            @Override
            public ObservableSource apply(Observable upstream) {
                return upstream.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
            }
        };
    }

    public static ObservableTransformer compForBgTransformer() {
        return new ObservableTransformer() {
            @Override
            public ObservableSource apply(Observable upstream) {
                return upstream.subscribeOn(Schedulers.computation()).observeOn(Schedulers.computation());
            }
        };
    }

    public static ObservableTransformer compForUITransformer() {
        return new ObservableTransformer() {
            @Override
            public ObservableSource apply(Observable upstream) {
                return upstream.subscribeOn(Schedulers.computation()).observeOn(AndroidSchedulers.mainThread());
            }
        };
    }
}
