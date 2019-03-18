package com.zhiyang.meteor.common.di.module.android;

import com.zhiyang.meteor.about.ui.AboutActivity;
import com.zhiyang.meteor.collection.ui.CollectionActivity;
import com.zhiyang.meteor.common.di.module.NetworkModule;
import com.zhiyang.meteor.common.ui.activity.CustomApiActivity;
import com.zhiyang.meteor.common.ui.activity.DownloadManageActivity;
import com.zhiyang.meteor.common.ui.activity.IntroduceActivity;
import com.zhiyang.meteor.common.ui.activity.LoginActivity;
import com.zhiyang.meteor.common.ui.activity.PreviewActivity;
import com.zhiyang.meteor.common.ui.activity.SetWallpaperActivity;
import com.zhiyang.meteor.common.ui.activity.SettingsActivity;
import com.zhiyang.meteor.common.ui.activity.UpdateMeActivity;
import com.zhiyang.meteor.common.ui.activity.muzei.MuzeiCollectionSourceConfigActivity;
import com.zhiyang.meteor.common.ui.activity.muzei.MuzeiSettingsActivity;
import com.zhiyang.meteor.main.MainActivity;
import com.zhiyang.meteor.me.ui.MeActivity;
import com.zhiyang.meteor.me.ui.MyFollowActivity;
import com.zhiyang.meteor.photo3.ui.PhotoActivity3;
import com.zhiyang.meteor.search.ui.SearchActivity;
import com.zhiyang.meteor.user.ui.UserActivity;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;

@Module
public abstract class ActivityModule {

    @ContributesAndroidInjector(modules = NetworkModule.class)
    abstract MainActivity contributeMainActivityInjector();

    @ContributesAndroidInjector(modules = NetworkModule.class)
    abstract CollectionActivity contributeCollectionActivityInjector();

    @ContributesAndroidInjector(modules = NetworkModule.class)
    abstract MeActivity contributeMeActivityInjector();

    @ContributesAndroidInjector(modules = NetworkModule.class)
    abstract MyFollowActivity contributeMyFollowActivityInjector();

    @ContributesAndroidInjector(modules = NetworkModule.class)
    abstract PhotoActivity3 contributePhotoActivity3Injector();

    @ContributesAndroidInjector(modules = NetworkModule.class)
    abstract SearchActivity contributeSearchActivityInjector();

    @ContributesAndroidInjector(modules = NetworkModule.class)
    abstract UserActivity contributeUserActivityInjector();

    @ContributesAndroidInjector(modules = NetworkModule.class)
    abstract PreviewActivity contributePreviewActivityInjector();

    @ContributesAndroidInjector(modules = NetworkModule.class)
    abstract LoginActivity contributeLoginActivityInjector();

    @ContributesAndroidInjector(modules = NetworkModule.class)
    abstract UpdateMeActivity contributeUpdateMeActivityInjector();

    @ContributesAndroidInjector(modules = NetworkModule.class)
    abstract DownloadManageActivity contributeDownloadManageActivityInjector();

    @ContributesAndroidInjector(modules = NetworkModule.class)
    abstract SettingsActivity contributeSettingsActivityInjector();

    @ContributesAndroidInjector(modules = NetworkModule.class)
    abstract AboutActivity contributeAboutActivityInjector();

    @ContributesAndroidInjector(modules = NetworkModule.class)
    abstract IntroduceActivity contributeIntroduceActivityInjector();

    @ContributesAndroidInjector(modules = NetworkModule.class)
    abstract CustomApiActivity contributeCustomApiActivityInjector();

    @ContributesAndroidInjector(modules = NetworkModule.class)
    abstract SetWallpaperActivity contributeSetWallpaperActivityInjector();

    @ContributesAndroidInjector(modules = NetworkModule.class)
    abstract MuzeiSettingsActivity contributeMuzeiSettingsActivityInjector();

    @ContributesAndroidInjector(modules = NetworkModule.class)
    abstract MuzeiCollectionSourceConfigActivity contributeMuzeiCollectionSourceConfigActivityInjector();
}
