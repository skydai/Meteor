package com.zhiyang.meteor.search.ui;

import android.annotation.SuppressLint;

import com.zhiyang.meteor.R;
import com.zhiyang.meteor.common.basic.model.PagerManageView;
import com.zhiyang.meteor.common.basic.adapter.FooterAdapter;
import com.zhiyang.meteor.common.utils.DisplayUtils;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

@SuppressLint("ViewConstructor")
public class UserSearchPageView extends AbstractSearchPageView {

    public UserSearchPageView(SearchActivity a, int id, FooterAdapter adapter,
                              boolean selected, int index, PagerManageView v) {
        super(a, id, adapter, selected, index, v);
    }

    @Override
    protected RecyclerView.LayoutManager getLayoutManager() {
        return new GridLayoutManager(getContext(), DisplayUtils.getGirdColumnCount(getContext()));
    }

    @Override
    protected String getInitFeedbackText() {
        return getContext().getString(R.string.feedback_search_users_tv);
    }
}
