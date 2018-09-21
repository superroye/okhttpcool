package com.wolf.lib.okhttpcool.demo;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import com.wolf.lib.okhttpcool.demo.bean.TaobaoTest;
import com.wolf.lib.okhttpcool.response.PriorityCacheResponseCallback;
import com.wolf.lib.okhttpcool.response.ResponseObserver;

public class NetActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_net_test);
    }

    //只读缓存（无视过期时间）
    public void cacheOnly(View view) {
        CoolService.api().testSearchOnlyCache("零食").subscribe(new ResponseObserver<TaobaoTest>() {

            @Override
            public void onResponse(TaobaoTest result) {
                Log.d("okhttp", "cacheOnly ====== " + result.toString());
            }
        });
    }

    //自定义缓存时间
    public void cacheAge(View view) {
        CoolService.api().testSearchCacheAge("零食").subscribe(new ResponseObserver<TaobaoTest>() {
            @Override
            public void onResponse(TaobaoTest result) {
                Log.d("okhttp", "cacheAge ====== " + result.toString());
            }
        });
    }

    //不缓存，读网络
    public void noCache(View view) {
        CoolService.api().testSearchNetwork("零食").subscribe(new ResponseObserver<TaobaoTest>() {
            @Override
            public void onResponse(TaobaoTest result) {
                Log.d("okhttp", "noCache ====== " + result.toString());
            }
        });
    }

    //固定缓存1天
    public void cacheLong(View view) {
        CoolService.api().testSearchSceneCache("零食").subscribe(new ResponseObserver<TaobaoTest>() {
            @Override
            public void onResponse(TaobaoTest result) {
                Log.d("okhttp", "cacheLong ====== " + result.toString());
            }
        });
    }

    //读网络，并存缓存
    public void refresh(View view) {
        CoolService.api().testSearchSceneRefresh("零食").subscribe(new ResponseObserver<TaobaoTest>() {
            @Override
            public void onResponse(TaobaoTest result) {
                Log.d("okhttp", "refresh ====== " + result.toString());
            }
        });
    }

    //读缓存，同时请求网络，并写缓存
    public void cacheAndRefresh(View view) {
        PriorityCacheResponseCallback cb = new PriorityCacheResponseCallback<TaobaoTest>() {
            @Override
            public void onResponse(TaobaoTest result) {
                Log.d("okhttp", "cacheAndRefresh ====== " + result.toString());
            }
        };
        CoolService.api()
                .testSearchSceneCacheCall("零食")
                .enqueue(cb);
    }
}
