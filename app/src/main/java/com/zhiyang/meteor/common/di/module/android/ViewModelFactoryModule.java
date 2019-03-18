package com.zhiyang.meteor.common.di.module.android;

import com.zhiyang.meteor.collection.vm.CollectionActivityModel;
import com.zhiyang.meteor.collection.vm.CollectionPhotosViewModel;
import com.zhiyang.meteor.common.basic.vm.PagerManageViewModel;
import com.zhiyang.meteor.common.basic.DaggerViewModelFactory;
import com.zhiyang.meteor.common.di.annotation.ViewModelKey;
import com.zhiyang.meteor.main.MainActivityModel;
import com.zhiyang.meteor.main.collection.vm.AllCollectionsViewModel;
import com.zhiyang.meteor.main.collection.vm.CuratedCollectionsViewModel;
import com.zhiyang.meteor.main.collection.vm.FeaturedCollectionsViewModel;
import com.zhiyang.meteor.main.following.FollowingFeedViewModel;
import com.zhiyang.meteor.main.home.vm.FeaturedHomePhotosViewModel;
import com.zhiyang.meteor.main.home.vm.NewHomePhotosViewModel;
import com.zhiyang.meteor.main.multiFilter.vm.MultiFilterFragmentModel;
import com.zhiyang.meteor.main.multiFilter.vm.MultiFilterPhotoViewModel;
import com.zhiyang.meteor.main.selected.SelectedViewModel;
import com.zhiyang.meteor.me.vm.MeActivityModel;
import com.zhiyang.meteor.me.vm.MeCollectionsViewModel;
import com.zhiyang.meteor.me.vm.MeLikesViewModel;
import com.zhiyang.meteor.me.vm.MePhotosViewModel;
import com.zhiyang.meteor.me.vm.MyFollowerViewModel;
import com.zhiyang.meteor.me.vm.MyFollowingViewModel;
import com.zhiyang.meteor.photo3.PhotoActivityModel;
import com.zhiyang.meteor.search.vm.CollectionSearchPageViewModel;
import com.zhiyang.meteor.search.vm.PhotoSearchPageViewModel;
import com.zhiyang.meteor.search.vm.SearchActivityModel;
import com.zhiyang.meteor.search.vm.UserSearchPageViewModel;
import com.zhiyang.meteor.user.vm.UserActivityModel;
import com.zhiyang.meteor.user.vm.UserCollectionsViewModel;
import com.zhiyang.meteor.user.vm.UserLikesViewModel;
import com.zhiyang.meteor.user.vm.UserPhotosViewModel;

import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import dagger.Binds;
import dagger.Module;
import dagger.multibindings.IntoMap;

@Module
public abstract class ViewModelFactoryModule {

    @Binds
    public abstract ViewModelProvider.Factory bindViewModelFactory(DaggerViewModelFactory factory);

    @Binds
    @IntoMap
    @ViewModelKey(CollectionActivityModel.class)
    public abstract ViewModel getCollectionActivityModel(CollectionActivityModel model);

    @Binds
    @IntoMap
    @ViewModelKey(CollectionPhotosViewModel.class)
    public abstract ViewModel getCollectionPhotosViewModel(CollectionPhotosViewModel model);

    @Binds
    @IntoMap
    @ViewModelKey(AllCollectionsViewModel.class)
    public abstract ViewModel getAllCollectionsViewModel(AllCollectionsViewModel model);

    @Binds
    @IntoMap
    @ViewModelKey(CuratedCollectionsViewModel.class)
    public abstract ViewModel getCuratedCollectionsViewModel(CuratedCollectionsViewModel model);

    @Binds
    @IntoMap
    @ViewModelKey(FeaturedCollectionsViewModel.class)
    public abstract ViewModel getFeaturedCollectionsViewModel(FeaturedCollectionsViewModel model);

    @Binds
    @IntoMap
    @ViewModelKey(FollowingFeedViewModel.class)
    public abstract ViewModel getFollowingFeedViewModel(FollowingFeedViewModel model);

    @Binds
    @IntoMap
    @ViewModelKey(FeaturedHomePhotosViewModel.class)
    public abstract ViewModel getFeaturedHomePhotosViewModel(FeaturedHomePhotosViewModel model);

    @Binds
    @IntoMap
    @ViewModelKey(NewHomePhotosViewModel.class)
    public abstract ViewModel getNewHomePhotosViewModel(NewHomePhotosViewModel model);

    @Binds
    @IntoMap
    @ViewModelKey(MultiFilterFragmentModel.class)
    public abstract ViewModel getMultiFilterFragmentModel(MultiFilterFragmentModel model);

    @Binds
    @IntoMap
    @ViewModelKey(MultiFilterPhotoViewModel.class)
    public abstract ViewModel getMultiFilterPhotoViewModel(MultiFilterPhotoViewModel model);

    @Binds
    @IntoMap
    @ViewModelKey(SelectedViewModel.class)
    public abstract ViewModel getSelectedViewModel(SelectedViewModel model);

    @Binds
    @IntoMap
    @ViewModelKey(MainActivityModel.class)
    public abstract ViewModel getMainActivityModel(MainActivityModel model);

    @Binds
    @IntoMap
    @ViewModelKey(MeActivityModel.class)
    public abstract ViewModel getMeActivityModel(MeActivityModel model);

    @Binds
    @IntoMap
    @ViewModelKey(MeCollectionsViewModel.class)
    public abstract ViewModel getMeCollectionsViewModel(MeCollectionsViewModel model);

    @Binds
    @IntoMap
    @ViewModelKey(MeLikesViewModel.class)
    public abstract ViewModel getMeLikesViewModel(MeLikesViewModel model);

    @Binds
    @IntoMap
    @ViewModelKey(MePhotosViewModel.class)
    public abstract ViewModel getMePhotosViewModel(MePhotosViewModel model);

    @Binds
    @IntoMap
    @ViewModelKey(MyFollowerViewModel.class)
    public abstract ViewModel getMyFollowerViewModel(MyFollowerViewModel model);

    @Binds
    @IntoMap
    @ViewModelKey(MyFollowingViewModel.class)
    public abstract ViewModel getMyFollowingViewModel(MyFollowingViewModel model);

    @Binds
    @IntoMap
    @ViewModelKey(PhotoActivityModel.class)
    public abstract ViewModel getPhotoActivityModel(PhotoActivityModel model);

    @Binds
    @IntoMap
    @ViewModelKey(CollectionSearchPageViewModel.class)
    public abstract ViewModel getCollectionSearchPageViewModel(CollectionSearchPageViewModel model);

    @Binds
    @IntoMap
    @ViewModelKey(PhotoSearchPageViewModel.class)
    public abstract ViewModel getPhotoSearchPageViewModel(PhotoSearchPageViewModel model);

    @Binds
    @IntoMap
    @ViewModelKey(SearchActivityModel.class)
    public abstract ViewModel getSearchActivityModel(SearchActivityModel model);

    @Binds
    @IntoMap
    @ViewModelKey(UserSearchPageViewModel.class)
    public abstract ViewModel getUserSearchPageViewModel(UserSearchPageViewModel model);

    @Binds
    @IntoMap
    @ViewModelKey(UserActivityModel.class)
    public abstract ViewModel getUserActivityModel(UserActivityModel model);

    @Binds
    @IntoMap
    @ViewModelKey(UserCollectionsViewModel.class)
    public abstract ViewModel getUserCollectionsViewModel(UserCollectionsViewModel model);

    @Binds
    @IntoMap
    @ViewModelKey(UserLikesViewModel.class)
    public abstract ViewModel getUserLikesViewModel(UserLikesViewModel model);

    @Binds
    @IntoMap
    @ViewModelKey(UserPhotosViewModel.class)
    public abstract ViewModel getUserPhotosViewModel(UserPhotosViewModel model);

    @Binds
    @IntoMap
    @ViewModelKey(PagerManageViewModel.class)
    public abstract ViewModel getPagerManageViewModel(PagerManageViewModel model);
}
