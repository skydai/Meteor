package com.zhiyang.meteor.collection.repository;

import com.zhiyang.meteor.common.basic.model.Resource;
import com.zhiyang.meteor.common.network.json.Collection;
import com.zhiyang.meteor.common.network.observer.ResourceObserver;
import com.zhiyang.meteor.common.network.service.CollectionService;

import javax.inject.Inject;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

public class CollectionActivityRepository {

    private CollectionService service;

    @Inject
    public CollectionActivityRepository(CollectionService service) {
        this.service = service;
    }

    public void getACollection(@NonNull MutableLiveData<Resource<Collection>> current, String id) {
        assert current.getValue() != null;
        current.setValue(Resource.loading(current.getValue().data));

        service.cancel();
        service.requestACollections(id, new ResourceObserver<>(current));
    }

    public void getACuratedCollection(@NonNull MutableLiveData<Resource<Collection>> current, String id) {
        assert current.getValue() != null;
        current.setValue(Resource.loading(current.getValue().data));

        service.cancel();
        service.requestACuratedCollections(id, new ResourceObserver<>(current));
    }

    public void cancel() {
        service.cancel();
    }
}
