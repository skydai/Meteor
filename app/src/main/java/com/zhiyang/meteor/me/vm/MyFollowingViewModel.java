package com.zhiyang.meteor.me.vm;

import com.zhiyang.meteor.common.utils.presenter.event.UserEventResponsePresenter;
import com.zhiyang.meteor.me.repository.MyFollowUserViewRepository;

import javax.inject.Inject;

public class MyFollowingViewModel extends MyFollowerViewModel {

    @Inject
    public MyFollowingViewModel(MyFollowUserViewRepository repository,
                                UserEventResponsePresenter presenter) {
        super(repository, presenter);
    }

    @Override
    public void refresh() {
        getRepository().getFollowing(getListResource(), true);
    }

    @Override
    public void load() {
        getRepository().getFollowing(getListResource(), false);
    }
}
