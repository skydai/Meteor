package com.zhiyang.meteor.common.network.service;

import android.content.Context;

import com.zhiyang.meteor.Mysplash;
import com.zhiyang.meteor.common.di.annotation.ApplicationInstace;
import com.zhiyang.meteor.common.network.SchedulerTransformer;
import com.zhiyang.meteor.common.network.api.AuthorizeApi;
import com.zhiyang.meteor.common.network.json.AccessToken;
import com.zhiyang.meteor.common.network.observer.BaseObserver;
import com.zhiyang.meteor.common.network.observer.ObserverContainer;

import javax.inject.Inject;

import io.reactivex.disposables.CompositeDisposable;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Authorize service.
 * */

public class AuthorizeService {

    private AuthorizeApi api;
    private CompositeDisposable compositeDisposable;

    @Inject
    public AuthorizeService(@ApplicationInstace OkHttpClient client,
                            @ApplicationInstace GsonConverterFactory gsonConverterFactory,
                            @ApplicationInstace RxJava2CallAdapterFactory rxJava2CallAdapterFactory,
                            CompositeDisposable disposable) {
        api = new Retrofit.Builder()
                .baseUrl(Mysplash.UNSPLASH_URL)
                .client(client)
                .addConverterFactory(gsonConverterFactory)
                .addCallAdapterFactory(rxJava2CallAdapterFactory)
                .build()
                .create(AuthorizeApi.class);
        compositeDisposable = disposable;
    }

    public void requestAccessToken(Context c, String code, BaseObserver<AccessToken> observer) {
        api.getAccessToken(
                Mysplash.getAppId(c, true),
                Mysplash.getSecret(c),
                "mysplash://" + Mysplash.UNSPLASH_LOGIN_CALLBACK,
                code,
                "authorization_code")
                .compose(SchedulerTransformer.create())
                .subscribe(new ObserverContainer<>(compositeDisposable, observer));
    }

    public void cancel() {
        compositeDisposable.clear();
    }
}
