package com.zhiyang.meteor.main.collection.vm;

import com.zhiyang.meteor.common.basic.model.ListResource;
import com.zhiyang.meteor.common.basic.vm.PagerViewModel;
import com.zhiyang.meteor.common.network.json.Collection;
import com.zhiyang.meteor.common.utils.bus.CollectionEvent;
import com.zhiyang.meteor.common.utils.bus.MessageBus;
import com.zhiyang.meteor.common.utils.presenter.event.CollectionEventResponsePresenter;
import com.zhiyang.meteor.main.collection.CollectionsViewRepository;

import androidx.annotation.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;

/**
 * Abstract collections view model.
 * */
public abstract class AbstractCollectionsViewModel extends PagerViewModel<Collection>
        implements Consumer<CollectionEvent> {

    private CollectionsViewRepository repository;
    private CollectionEventResponsePresenter presenter;
    private Disposable disposable;

    public AbstractCollectionsViewModel(CollectionsViewRepository repository,
                                        CollectionEventResponsePresenter presenter) {
        super();
        this.repository = repository;
        this.presenter = presenter;
        this.disposable = MessageBus.getInstance()
                .toObservable(CollectionEvent.class)
                .subscribe(this);
    }

    @Override
    public boolean init(@NonNull ListResource<Collection> resource) {
        if (super.init(resource)) {
            refresh();
            return true;
        }
        return false;
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        getRepository().cancel();
        presenter.clearResponse();
        disposable.dispose();
    }

    CollectionsViewRepository getRepository() {
        return repository;
    }

    // interface.

    @Override
    public void accept(CollectionEvent collectionEvent) {
        switch (collectionEvent.event) {
            case UPDATE:
                presenter.updateCollection(getListResource(), collectionEvent.collection);
                break;

            case DELETE:
                presenter.deleteCollection(getListResource(), collectionEvent.collection);
                break;
        }
    }
}
