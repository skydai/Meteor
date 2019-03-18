package com.zhiyang.meteor.common.basic.model;

public interface PagerManageView {

    void onRefresh(int index);
    void onLoad(int index);

    boolean canLoadMore(int index);
    boolean isLoading(int index);
}
