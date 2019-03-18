package com.zhiyang.meteor.me.repository;

import android.text.TextUtils;

import com.zhiyang.meteor.common.basic.model.ListResource;
import com.zhiyang.meteor.common.network.json.Photo;
import com.zhiyang.meteor.common.network.observer.ListResourceObserver;
import com.zhiyang.meteor.common.network.service.PhotoService;
import com.zhiyang.meteor.common.utils.manager.AuthManager;

import javax.inject.Inject;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

public class MePhotosViewRepository {

    private PhotoService service;

    @Inject
    public MePhotosViewRepository(PhotoService service) {
        this.service = service;
    }

    public void getUserPhotos(@NonNull MutableLiveData<ListResource<Photo>> current,
                              String order, boolean refresh) {
        if (!TextUtils.isEmpty(AuthManager.getInstance().getUsername())) {
            assert current.getValue() != null;
            if (refresh) {
                current.setValue(ListResource.refreshing(current.getValue()));
            } else {
                current.setValue(ListResource.loading(current.getValue()));
            }

            service.cancel();
            service.requestUserPhotos(
                    AuthManager.getInstance().getUsername(),
                    current.getValue().getRequestPage(),
                    current.getValue().perPage,
                    order,
                    new ListResourceObserver<>(current, refresh));
        }
    }

    public void getUserLikes(@NonNull MutableLiveData<ListResource<Photo>> current,
                             String order, boolean refresh) {
        if (!TextUtils.isEmpty(AuthManager.getInstance().getUsername())) {
            assert current.getValue() != null;
            if (refresh) {
                current.setValue(ListResource.refreshing(current.getValue()));
            } else {
                current.setValue(ListResource.loading(current.getValue()));
            }

            service.cancel();
            service.requestUserLikes(
                    AuthManager.getInstance().getUsername(),
                    current.getValue().getRequestPage(),
                    current.getValue().perPage,
                    order,
                    new ListResourceObserver<>(current, refresh));
        }
    }

    public void cancel() {
        service.cancel();
    }
}
