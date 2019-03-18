package com.zhiyang.meteor.common.utils.bus;

import com.zhiyang.meteor.common.network.json.Collection;

public class CollectionEvent {

    public Collection collection;

    public Event event;

    public enum Event {
        UPDATE, CREATE, DELETE
    }

    public CollectionEvent(Collection collection, Event event) {
        this.collection = collection;
        this.event = event;
    }
}
