package com.zhiyang.meteor.main.collection.vm;

import com.zhiyang.meteor.common.utils.presenter.event.CollectionEventResponsePresenter;
import com.zhiyang.meteor.main.collection.CollectionsViewRepository;

import javax.inject.Inject;

public class CuratedCollectionsViewModel extends AbstractCollectionsViewModel {

    @Inject
    public CuratedCollectionsViewModel(CollectionsViewRepository repository,
                                       CollectionEventResponsePresenter presenter) {
        super(repository, presenter);
    }

    @Override
    public void refresh() {
        getRepository().getCuratedCollections(getListResource(), true);
    }

    @Override
    public void load() {
        getRepository().getCuratedCollections(getListResource(), false);
    }
}
