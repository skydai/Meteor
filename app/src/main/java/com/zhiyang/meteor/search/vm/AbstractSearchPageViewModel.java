package com.zhiyang.meteor.search.vm;

import com.zhiyang.meteor.common.basic.model.ListResource;
import com.zhiyang.meteor.common.basic.vm.PagerViewModel;

import androidx.annotation.NonNull;

public abstract class AbstractSearchPageViewModel<T> extends PagerViewModel<T> {

    private String query;

    public void init(@NonNull ListResource<T> resource, String defaultQuery) {
        super.init(resource);
        if (query == null) {
            query = defaultQuery;
        }
    }

    public String getQuery() {
        return query;
    }

    public void setQuery(String query) {
        this.query = query;
    }
}
