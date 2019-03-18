package com.zhiyang.meteor.common.utils.presenter;

import com.zhiyang.meteor.common.network.json.Collection;
import com.zhiyang.meteor.common.network.json.Photo;
import com.zhiyang.meteor.common.network.json.User;
import com.zhiyang.meteor.common.ui.dialog.SelectCollectionDialog;
import com.zhiyang.meteor.common.utils.bus.CollectionEvent;
import com.zhiyang.meteor.common.utils.bus.PhotoEvent;
import com.zhiyang.meteor.common.utils.manager.AuthManager;
import com.zhiyang.meteor.common.utils.bus.MessageBus;

public class DispatchCollectionsChangedPresenter
        implements SelectCollectionDialog.OnCollectionsChangedListener {

    @Override
    public void onAddCollection(Collection c) {
        MessageBus.getInstance().post(new CollectionEvent(c, CollectionEvent.Event.CREATE));

        User user = AuthManager.getInstance().getUser();
        if (user != null) {
            user.total_collections ++;
            MessageBus.getInstance().post(user);
        }
    }

    @Override
    public void onUpdateCollection(Collection c, User u, Photo p) {
        boolean addPhotoToCollection = false;
        for (int i = 0;
             p.current_user_collections != null && i < p.current_user_collections.size();
             i ++) {
            if (c.id == p.current_user_collections.get(i).id) {
                addPhotoToCollection = true;
                MessageBus.getInstance().post(new PhotoEvent(p, c, PhotoEvent.Event.ADD_TO_COLLECTION));
                break;
            }
        }
        if (!addPhotoToCollection) {
            MessageBus.getInstance().post(new PhotoEvent(p, c, PhotoEvent.Event.REMOVE_FROM_COLLECTION));
        }

        MessageBus.getInstance().post(new CollectionEvent(c, CollectionEvent.Event.UPDATE));

        MessageBus.getInstance().post(u);
    }
}
