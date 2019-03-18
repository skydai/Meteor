package com.zhiyang.meteor.main.home;

import com.zhiyang.meteor.common.basic.model.ListResource;
import com.zhiyang.meteor.common.network.api.PhotoApi;
import com.zhiyang.meteor.common.network.json.Photo;
import com.zhiyang.meteor.common.network.observer.ListResourceObserver;
import com.zhiyang.meteor.common.network.observer.RandomListResourceObserver;
import com.zhiyang.meteor.common.network.service.PhotoService;

import java.util.List;

import javax.inject.Inject;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

public class HomePhotosViewRepository {

    private PhotoService service;

    @Inject
    public HomePhotosViewRepository(PhotoService service) {
        this.service = service;
    }

    public void getPhotos(@NonNull MutableLiveData<ListResource<Photo>> current,
                          List<Integer> pageList, String order,
                          boolean featured, boolean random, boolean refresh) {
        assert current.getValue() != null;
        if (refresh) {
            current.setValue(ListResource.refreshing(current.getValue()));
        } else {
            current.setValue(ListResource.loading(current.getValue()));
        }

        service.cancel();
        if (random) {
            if (featured) {
                service.requestCuratePhotos(
                        pageList.get(current.getValue().getRequestPage()),
                        current.getValue().perPage,
                        PhotoApi.ORDER_BY_LATEST,
                        new RandomListResourceObserver<>(current, pageList, refresh));
            } else {
                service.requestPhotos(
                        pageList.get(current.getValue().getRequestPage()),
                        current.getValue().perPage,
                        PhotoApi.ORDER_BY_LATEST,
                        new RandomListResourceObserver<>(current, pageList, refresh));
            }
        } else {
            if (featured) {
                service.requestCuratePhotos(
                        current.getValue().getRequestPage(),
                        current.getValue().perPage,
                        order,
                        new ListResourceObserver<>(current, refresh));
            } else {
                service.requestPhotos(
                        current.getValue().getRequestPage(),
                        current.getValue().perPage,
                        order,
                        new ListResourceObserver<>(current, refresh));
            }
        }
    }

    public void cancel() {
        service.cancel();
    }
}
