package com.zhiyang.meteor.search.ui;

import android.annotation.SuppressLint;

import com.zhiyang.meteor.R;
import com.zhiyang.meteor.common.basic.model.PagerManageView;
import com.zhiyang.meteor.common.basic.adapter.FooterAdapter;
import com.zhiyang.meteor.common.utils.DisplayUtils;

import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

@SuppressLint("ViewConstructor")
public class PhotoSearchPageView extends AbstractSearchPageView {

    public PhotoSearchPageView(SearchActivity a, int id, FooterAdapter adapter,
                               boolean selected, int index, PagerManageView v) {
        super(a, id, adapter, selected, index, v);

        if (DisplayUtils.getGirdColumnCount(getContext()) > 1) {
            int margin = getResources().getDimensionPixelSize(R.dimen.normal_margin);
            recyclerView.setPadding(margin, margin, 0, 0);
        } else {
            recyclerView.setPadding(0, 0, 0, 0);
        }
    }

    @Override
    protected RecyclerView.LayoutManager getLayoutManager() {
        return new StaggeredGridLayoutManager(
                DisplayUtils.getGirdColumnCount(getContext()), StaggeredGridLayoutManager.VERTICAL);
    }

    @Override
    protected String getInitFeedbackText() {
        return getContext().getString(R.string.feedback_search_photos_tv);
    }
}
