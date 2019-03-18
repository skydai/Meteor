package com.zhiyang.meteor.common.di.component;

import com.zhiyang.meteor.Mysplash;
import com.zhiyang.meteor.common.di.module.RxJavaModule;
import com.zhiyang.meteor.common.di.module.android.ViewModelFactoryModule;
import com.zhiyang.meteor.common.di.module.android.ActivityModule;
import com.zhiyang.meteor.common.di.module.android.FragmentModule;
import com.zhiyang.meteor.common.di.module.NetworkModule;
import com.zhiyang.meteor.common.download.DownloadHelper;
import com.zhiyang.meteor.common.muzei.MysplashMuzeiWorker;
import com.zhiyang.meteor.common.utils.manager.AuthManager;
import com.zhiyang.meteor.common.utils.manager.UserNotificationManager;

import dagger.Component;
import dagger.android.AndroidInjectionModule;
import dagger.android.support.AndroidSupportInjectionModule;

@Component(modules = {
        AndroidInjectionModule.class, AndroidSupportInjectionModule.class,
        ActivityModule.class, FragmentModule.class, ViewModelFactoryModule.class,
        RxJavaModule.class, NetworkModule.class})
public interface ApplicationComponent {

    void inject(Mysplash application);

    void inject(UserNotificationManager manager);

    void inject(DownloadHelper helper);

    void inject(AuthManager manager);

    void inject(MysplashMuzeiWorker worker);
}
