package com.zhiyang.meteor.common.di.module.android;

import com.zhiyang.meteor.common.di.module.NetworkModule;
import com.zhiyang.meteor.common.ui.dialog.ConfirmExitWithoutSaveDialog;
import com.zhiyang.meteor.common.ui.dialog.DeleteCollectionPhotoDialog;
import com.zhiyang.meteor.common.ui.dialog.DownloadRepeatDialog;
import com.zhiyang.meteor.common.ui.dialog.DownloadTypeDialog;
import com.zhiyang.meteor.common.ui.dialog.PathDialog;
import com.zhiyang.meteor.common.ui.dialog.ProfileDialog;
import com.zhiyang.meteor.common.ui.dialog.RequestBrowsableDataDialog;
import com.zhiyang.meteor.common.ui.dialog.RetryDialog;
import com.zhiyang.meteor.common.ui.dialog.SelectCollectionDialog;
import com.zhiyang.meteor.common.ui.dialog.TimePickerDialog;
import com.zhiyang.meteor.common.ui.dialog.TotalDialog;
import com.zhiyang.meteor.common.ui.dialog.UpdateCollectionDialog;
import com.zhiyang.meteor.common.ui.dialog.WallpaperWhereDialog;
import com.zhiyang.meteor.main.collection.ui.CollectionFragment;
import com.zhiyang.meteor.main.following.ui.FollowingFeedFragment;
import com.zhiyang.meteor.main.home.ui.HomeFragment;
import com.zhiyang.meteor.main.multiFilter.ui.MultiFilterFragment;
import com.zhiyang.meteor.main.selected.ui.SelectedFragment;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;

@Module
public abstract class FragmentModule {

    @ContributesAndroidInjector(modules = NetworkModule.class)
    abstract HomeFragment contributeHomeFragmentInjector();

    @ContributesAndroidInjector(modules = NetworkModule.class)
    abstract CollectionFragment contributeCollectionFragmentInjector();

    @ContributesAndroidInjector(modules = NetworkModule.class)
    abstract FollowingFeedFragment contributeFollowingFeedFragmentInjector();

    @ContributesAndroidInjector(modules = NetworkModule.class)
    abstract MultiFilterFragment contributeMultiFilterFragmentInjector();

    @ContributesAndroidInjector(modules = NetworkModule.class)
    abstract SelectedFragment contributeSelectedFragmentInjector();

    @ContributesAndroidInjector(modules = NetworkModule.class)
    abstract ConfirmExitWithoutSaveDialog contributeConfirmExitWithoutSaveDialogInjector();

    @ContributesAndroidInjector(modules = NetworkModule.class)
    abstract DeleteCollectionPhotoDialog contributeDeleteCollectionPhotoDialogInjector();

    @ContributesAndroidInjector(modules = NetworkModule.class)
    abstract DownloadRepeatDialog contributeDownloadRepeatDialogInjector();

    @ContributesAndroidInjector(modules = NetworkModule.class)
    abstract DownloadTypeDialog contributeDownloadTypeDialogInjector();

    @ContributesAndroidInjector(modules = NetworkModule.class)
    abstract PathDialog contributePathDialogInjector();

    @ContributesAndroidInjector(modules = NetworkModule.class)
    abstract ProfileDialog contributeProfileDialogInjector();

    @ContributesAndroidInjector(modules = NetworkModule.class)
    abstract RequestBrowsableDataDialog contributeRequestBrowsableDataDialogInjector();

    @ContributesAndroidInjector(modules = NetworkModule.class)
    abstract SelectCollectionDialog contributeSelectCollectionDialogInjector();

    @ContributesAndroidInjector(modules = NetworkModule.class)
    abstract TimePickerDialog contributeTimePickerDialogInjector();

    @ContributesAndroidInjector(modules = NetworkModule.class)
    abstract TotalDialog contributeTotalDialogInjector();

    @ContributesAndroidInjector(modules = NetworkModule.class)
    abstract UpdateCollectionDialog contributeUpdateCollectionDialogInjector();

    @ContributesAndroidInjector(modules = NetworkModule.class)
    abstract WallpaperWhereDialog contributeWallpaperWhereDialogInjector();

    @ContributesAndroidInjector(modules = NetworkModule.class)
    abstract RetryDialog contributeRetryDialogInjector();
}
