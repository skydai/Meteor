package com.zhiyang.meteor.search.vm;

import com.zhiyang.meteor.common.network.json.User;
import com.zhiyang.meteor.common.utils.bus.MessageBus;
import com.zhiyang.meteor.common.utils.presenter.event.UserEventResponsePresenter;
import com.zhiyang.meteor.search.repository.UserSearchPageViewRepository;

import javax.inject.Inject;

import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;

public class UserSearchPageViewModel extends AbstractSearchPageViewModel<User>
        implements Consumer<User> {

    private UserSearchPageViewRepository repository;
    private UserEventResponsePresenter presenter;
    private Disposable disposable;

    @Inject
    public UserSearchPageViewModel(UserSearchPageViewRepository repository,
                                   UserEventResponsePresenter presenter) {
        super();
        this.repository = repository;
        this.presenter = presenter;
        this.disposable = MessageBus.getInstance()
                .toObservable(User.class)
                .subscribe(this);
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        repository.cancel();
        presenter.clearResponse();
        disposable.dispose();
    }

    @Override
    public void refresh() {
        repository.getUsers(getListResource(), getQuery(), true);
    }

    @Override
    public void load() {
        repository.getUsers(getListResource(), getQuery(), false);
    }

    // interface.

    @Override
    public void accept(User user) {
        presenter.updateUser(getListResource(), user, false);
    }
}
