package com.zhiyang.meteor.collection.vm;

import com.zhiyang.meteor.collection.repository.CollectionActivityRepository;
import com.zhiyang.meteor.common.basic.model.Resource;
import com.zhiyang.meteor.common.network.json.Collection;
import com.zhiyang.meteor.common.basic.vm.BrowsableViewModel;
import com.zhiyang.meteor.common.utils.bus.CollectionEvent;
import com.zhiyang.meteor.common.utils.bus.MessageBus;

import javax.inject.Inject;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;

/**
 * Collection activity model.
 * */
public class CollectionActivityModel extends BrowsableViewModel<Collection>
        implements Consumer<CollectionEvent> {

    private CollectionActivityRepository repository;
    private Disposable disposable;

    private MutableLiveData<Boolean> deleted;

    private Integer collectionId;
    private Boolean curated;

    @Inject
    public CollectionActivityModel(CollectionActivityRepository repository) {
        super();
        this.repository = repository;
        this.disposable = MessageBus.getInstance()
                .toObservable(CollectionEvent.class)
                .subscribe(this);
        this.deleted = null;
        this.collectionId = null;
        this.curated = null;
    }

    public boolean init(@NonNull Resource<Collection> resource, int collectionId, boolean curated) {
        boolean init = super.init(resource);
        if (this.deleted == null) {
            this.deleted = new MutableLiveData<>();
            this.deleted.setValue(false);
        }
        if (this.collectionId == null) {
            this.collectionId = collectionId;
        }
        if (this.curated == null) {
            this.curated = curated;
        }

        if (init && resource.data == null) {
            requestACollection();
        }

        return init;
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        repository.cancel();
        disposable.dispose();
    }

    public void requestACollection() {
        if (curated) {
            repository.getACuratedCollection(getResource(), String.valueOf(collectionId));
        } else {
            repository.getACollection(getResource(), String.valueOf(collectionId));
        }
    }

    public MutableLiveData<Boolean> getDeleted() {
        return deleted;
    }

    // interface.

    @Override
    public void accept(CollectionEvent collectionEvent) {
        if (getResource().getValue() != null
                && getResource().getValue().data != null
                && getResource().getValue().data.id == collectionEvent.collection.id) {
            switch (collectionEvent.event) {
                case UPDATE:
                    getResource().setValue(Resource.success(collectionEvent.collection));
                    break;

                case DELETE:
                    deleted.setValue(true);
                    break;
            }
        }
    }
}
