package com.wolf.lib.okhttpcool.demo;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import com.supylc.mobilearch.rxjava2.IRxObserveDisposer;
import com.supylc.network.ApiManager;
import com.supylc.network.support.request.RxRequestUtils;

import java.util.List;

import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;

public class NetActivity extends AppCompatActivity implements IRxObserveDisposer {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_net_test);
    }

    //只读缓存（无视过期时间）
    public void cacheOnly(View view) {
        ApiManager.api(CoolAPi.class).testSearchOnlyCache("零食").compose(RxRequestUtils.keepFirst()).subscribe(new ZResponseDataObserver<List<List<String>>>(this) {

            @Override
            public void onResponse(List<List<String>> result) {

            }
        });
    }

    //自定义缓存时间
    public void cacheAge(View view) {
        ApiManager.api(CoolAPi.class).testSearchCacheAge("零食").subscribe(new ZResponseDataObserver<List<List<String>>>(this) {
            @Override
            public void onResponse(List<List<String>> result) {
                Log.d("okhttp", "cacheAge ====== " + result.toString());
            }
        });
    }

    //不缓存，读网络
    public void noCache(View view) {
        ApiManager.api(CoolAPi.class).testSearchNetwork("零食").subscribe(new ZResponseDataObserver<List<List<String>>>(this) {
            @Override
            public void onResponse(List<List<String>> result) {
                Log.d("okhttp", "noCache ====== " + result.toString());
            }
        });
    }

    //固定缓存
    public void cacheLong(View view) {
        ApiManager.api(CoolAPi.class).testSearchSceneCache("零食").subscribe(new ZResponseDataObserver<List<List<String>>>(this) {
            @Override
            public void onResponse(List<List<String>> result) {
                Log.d("okhttp", "cacheLong ====== " + result.toString());
            }
        });
    }

    //读缓存，同时请求网络，并写缓存
    public void cacheAndRefresh(View view) {
        ApiManager.api(CoolAPi.class).testSearchSceneCacheCall("零食").subscribe(new ZResponseDataObserver<List<List<String>>>(this) {
            @Override
            public void onResponse(List<List<String>> result) {
                Log.d("okhttp", "refresh ====== " + result.toString());
            }
        });
    }

    CompositeDisposable compositeDisposable = new CompositeDisposable();

    @Override
    public void addDisposable(Disposable disposable) {
        compositeDisposable.add(disposable);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        compositeDisposable.dispose();
    }
}
