package com.zhiyang.meteor.common.utils.presenter.list;

import com.zhiyang.meteor.common.network.json.LikePhotoResult;
import com.zhiyang.meteor.common.network.json.Photo;
import com.zhiyang.meteor.common.network.json.User;
import com.zhiyang.meteor.common.network.observer.BaseObserver;
import com.zhiyang.meteor.common.network.service.PhotoService;
import com.zhiyang.meteor.common.utils.bus.PhotoEvent;
import com.zhiyang.meteor.common.utils.manager.AuthManager;
import com.zhiyang.meteor.common.utils.bus.MessageBus;

import javax.inject.Inject;

public class LikeOrDislikePhotoPresenter {

    private PhotoService service;

    @Inject
    public LikeOrDislikePhotoPresenter(PhotoService service) {
        this.service = service;
    }

    public void likeOrDislikePhoto(Photo photo, boolean setToLike) {
        BaseObserver<LikePhotoResult> observer = new BaseObserver<LikePhotoResult>() {
            @Override
            public void onSucceed(LikePhotoResult likePhotoResult) {
                photo.settingLike = false;
                photo.liked_by_user = likePhotoResult.photo.liked_by_user;
                photo.likes = likePhotoResult.photo.likes;
                MessageBus.getInstance().post(new PhotoEvent(photo));

                User user = AuthManager.getInstance().getUser();
                if (user != null) {
                    user.total_likes += setToLike ? 1 : -1;
                    MessageBus.getInstance().post(user);
                }
            }

            @Override
            public void onFailed() {
                photo.settingLike = false;
                MessageBus.getInstance().post(new PhotoEvent(photo));
            }
        };

        if (setToLike) {
            service.likePhoto(photo.id, observer);
        } else {
            service.cancelLikePhoto(photo.id, observer);
        }
    }
}
