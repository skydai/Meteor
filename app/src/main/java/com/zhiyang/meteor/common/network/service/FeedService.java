package com.zhiyang.meteor.common.network.service;

import com.zhiyang.meteor.Mysplash;
import com.zhiyang.meteor.common.di.annotation.ApplicationInstace;
import com.zhiyang.meteor.common.network.SchedulerTransformer;
import com.zhiyang.meteor.common.network.api.FeedApi;
import com.zhiyang.meteor.common.network.json.Photo;
import com.zhiyang.meteor.common.network.interceptor.FeedInterceptor;
import com.zhiyang.meteor.common.network.interceptor.NapiInterceptor;
import com.zhiyang.meteor.common.network.observer.BaseObserver;
import com.zhiyang.meteor.common.network.observer.ObserverContainer;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.disposables.CompositeDisposable;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Feed service.
 * */

public class FeedService {

    private FeedApi api;
    private CompositeDisposable compositeDisposable;

    @Inject
    public FeedService(@ApplicationInstace OkHttpClient client,
                       @ApplicationInstace GsonConverterFactory gsonConverterFactory,
                       @ApplicationInstace RxJava2CallAdapterFactory rxJava2CallAdapterFactory,
                       CompositeDisposable disposable) {
        api = new Retrofit.Builder()
                .baseUrl(Mysplash.UNSPLASH_URL)
                .client(client.newBuilder()
                        .addInterceptor(new FeedInterceptor())
                        .addInterceptor(new NapiInterceptor())
                        .build())
                .addConverterFactory(gsonConverterFactory)
                .addCallAdapterFactory(rxJava2CallAdapterFactory)
                .build()
                .create((FeedApi.class));
        compositeDisposable = disposable;
    }

    public void requestFollowingFeed(@Mysplash.PageRule int page, @Mysplash.PerPageRule int per_page,
                                     BaseObserver<List<Photo>> observer) {
        api.getFollowingFeed(page, per_page)
                .compose(SchedulerTransformer.create())
                .subscribe(new ObserverContainer<>(compositeDisposable, observer));
    }

    public void cancel() {
        compositeDisposable.clear();
    }
}
