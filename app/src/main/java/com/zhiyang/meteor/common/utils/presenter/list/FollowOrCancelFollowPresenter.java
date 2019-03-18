package com.zhiyang.meteor.common.utils.presenter.list;

import com.zhiyang.meteor.common.network.json.User;
import com.zhiyang.meteor.common.network.observer.NoBodyObserver;
import com.zhiyang.meteor.common.network.service.FollowService;
import com.zhiyang.meteor.common.utils.bus.MessageBus;

import javax.inject.Inject;

import okhttp3.ResponseBody;

public class FollowOrCancelFollowPresenter {

    private FollowService service;

    @Inject
    public FollowOrCancelFollowPresenter(FollowService service) {
        this.service = service;
    }

    public void followOrCancelFollowUser(User user, boolean setToFollow) {
        NoBodyObserver<ResponseBody> callback = new NoBodyObserver<ResponseBody>() {
            @Override
            public void onSucceed(ResponseBody responseBody) {
                user.settingFollow = false;
                user.followed_by_user = setToFollow;
                user.followers_count += setToFollow ? 1 : -1;
                MessageBus.getInstance().post(user);
            }

            @Override
            public void onFailed() {
                user.settingFollow = false;
                MessageBus.getInstance().post(user);
            }
        };

        if (setToFollow) {
            service.followUser(user.username, callback);
        } else {
            service.cancelFollowUser(user.username, callback);
        }
    }
}
