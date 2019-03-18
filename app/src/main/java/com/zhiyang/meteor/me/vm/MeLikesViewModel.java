package com.zhiyang.meteor.me.vm;

import com.zhiyang.meteor.common.utils.manager.AuthManager;
import com.zhiyang.meteor.common.utils.presenter.event.PhotoEventResponsePresenter;
import com.zhiyang.meteor.me.repository.MePhotosViewRepository;

import javax.inject.Inject;

public class MeLikesViewModel extends MePhotosViewModel {

    @Inject
    public MeLikesViewModel(MePhotosViewRepository repository,
                            PhotoEventResponsePresenter presenter) {
        super(repository, presenter);
    }

    @Override
    public void refresh() {
        setUsername(AuthManager.getInstance().getUsername());
        getRepository().getUserLikes(getListResource(), getPhotosOrder().getValue(), true);
    }

    @Override
    public void load() {
        setUsername(AuthManager.getInstance().getUsername());
        getRepository().getUserLikes(getListResource(), getPhotosOrder().getValue(), false);
    }
}
