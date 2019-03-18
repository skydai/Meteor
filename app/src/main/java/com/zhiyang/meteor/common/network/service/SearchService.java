package com.zhiyang.meteor.common.network.service;

import android.text.TextUtils;

import com.zhiyang.meteor.Mysplash;
import com.zhiyang.meteor.common.di.annotation.ApplicationInstace;
import com.zhiyang.meteor.common.network.SchedulerTransformer;
import com.zhiyang.meteor.common.network.api.SearchNodeApi;
import com.zhiyang.meteor.common.network.api.SearchApi;
import com.zhiyang.meteor.common.network.interceptor.NapiInterceptor;
import com.zhiyang.meteor.common.network.json.SearchCollectionsResult;
import com.zhiyang.meteor.common.network.json.SearchPhotosResult;
import com.zhiyang.meteor.common.network.json.SearchUsersResult;
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
 * Search service.
 * */

public class SearchService {

    private SearchApi api;
    private SearchNodeApi nodeApi;
    private CompositeDisposable compositeDisposable;

    @Inject
    public SearchService(@ApplicationInstace OkHttpClient client,
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
                .create((SearchApi.class));
        nodeApi = TextUtils.isEmpty(Mysplash.UNSPLASH_NODE_API_URL)
                ? null
                : new Retrofit.Builder()
                .baseUrl(Mysplash.UNSPLASH_URL)
                .client(client.newBuilder()
                        .addInterceptor(new AuthInterceptor())
                        .addInterceptor(new NapiInterceptor())
                        .build())
                .addConverterFactory(gsonConverterFactory)
                .addCallAdapterFactory(rxJava2CallAdapterFactory)
                .build()
                .create((SearchNodeApi.class));
        compositeDisposable = disposable;
    }

    public void searchPhotos(String query, int page, BaseObserver<SearchPhotosResult> observer) {
        api.searchPhotos(query, page, Mysplash.DEFAULT_PER_PAGE)
                .compose(SchedulerTransformer.create())
                .subscribe(new ObserverContainer<>(compositeDisposable, observer));
    }

    public void searchUsers(String query, int page, BaseObserver<SearchUsersResult> observer) {
        if (nodeApi == null) {
            api.searchUsers(query, page, Mysplash.DEFAULT_PER_PAGE)
                    .compose(SchedulerTransformer.create())
                    .subscribe(new ObserverContainer<>(compositeDisposable, observer));
        } else {
            nodeApi.searchUsers(query, page, Mysplash.DEFAULT_PER_PAGE)
                    .compose(SchedulerTransformer.create())
                    .subscribe(new ObserverContainer<>(compositeDisposable, observer));
        }
    }

    public void searchCollections(String query, int page,
                                  BaseObserver<SearchCollectionsResult> observer) {
        if (nodeApi == null) {
            api.searchCollections(query, page, Mysplash.DEFAULT_PER_PAGE)
                    .compose(SchedulerTransformer.create())
                    .subscribe(new ObserverContainer<>(compositeDisposable, observer));
        } else {
            nodeApi.searchCollections(query, page, Mysplash.DEFAULT_PER_PAGE)
                    .compose(SchedulerTransformer.create())
                    .subscribe(new ObserverContainer<>(compositeDisposable, observer));
        }
    }

    public void cancel() {
        compositeDisposable.clear();
    }
}
