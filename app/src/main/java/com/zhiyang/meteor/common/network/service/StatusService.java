package com.zhiyang.meteor.common.network.service;

import com.zhiyang.meteor.Mysplash;
import com.zhiyang.meteor.common.di.annotation.ApplicationInstace;
import com.zhiyang.meteor.common.network.SchedulerTransformer;
import com.zhiyang.meteor.common.network.api.StatusApi;
import com.zhiyang.meteor.common.network.json.Total;
import com.zhiyang.meteor.common.network.interceptor.AuthInterceptor;
import com.zhiyang.meteor.common.network.observer.BaseObserver;
import com.zhiyang.meteor.common.network.observer.ObserverContainer;

import javax.inject.Inject;

import io.reactivex.disposables.CompositeDisposable;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Status service.
 * */

public class StatusService {

    private StatusApi api;
    private CompositeDisposable compositeDisposable;

    @Inject
    public StatusService(@ApplicationInstace OkHttpClient client,
                         @ApplicationInstace GsonConverterFactory gsonConverterFactory,
                         @ApplicationInstace RxJava2CallAdapterFactory rxJava2CallAdapterFactory,
                         CompositeDisposable disposable) {
        api = new Retrofit.Builder()
                .baseUrl(Mysplash.UNSPLASH_API_BASE_URL)
                .client(client.newBuilder()
                        .addInterceptor(new AuthInterceptor())
                        .build())
                .addConverterFactory(gsonConverterFactory)
                .addCallAdapterFactory(rxJava2CallAdapterFactory)
                .build()
                .create((StatusApi.class));
        compositeDisposable = disposable;
    }

    public void requestTotal(BaseObserver<Total> observer) {
        api.getTotal()
                .compose(SchedulerTransformer.create())
                .subscribe(new ObserverContainer<>(compositeDisposable, observer));
    }

    public void cancel() {
        compositeDisposable.clear();
    }
}
