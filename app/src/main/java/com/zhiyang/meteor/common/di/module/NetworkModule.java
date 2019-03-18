package com.zhiyang.meteor.common.di.module;

import com.zhiyang.meteor.Mysplash;
import com.zhiyang.meteor.common.di.annotation.ApplicationInstace;
import com.zhiyang.meteor.common.network.TLSCompactHelper;

import dagger.Module;
import dagger.Provides;
import okhttp3.OkHttpClient;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

@Module()
public class NetworkModule {

    @Provides
    public OkHttpClient getOkHttpClient() {
        return TLSCompactHelper.getOKHttpClient();
    }

    @ApplicationInstace
    @Provides
    public OkHttpClient getApplicationOkHttpClient() {
        return Mysplash.getInstance().getHttpClient();
    }

    @Provides
    public GsonConverterFactory getGsonConverterFactory() {
        return GsonConverterFactory.create();
    }

    @ApplicationInstace
    @Provides
    public GsonConverterFactory getApplicationGsonConverterFactory() {
        return Mysplash.getInstance().getGsonConverterFactory();
    }

    @Provides
    public RxJava2CallAdapterFactory getRxJava2CallAdapterFactory() {
        return RxJava2CallAdapterFactory.create();
    }

    @ApplicationInstace
    @Provides
    public RxJava2CallAdapterFactory getApplicationRxJava2CallAdapterFactory() {
        return Mysplash.getInstance().getRxJava2CallAdapterFactory();
    }
}
