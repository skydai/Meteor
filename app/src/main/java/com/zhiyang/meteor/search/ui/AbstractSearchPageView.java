package com.zhiyang.meteor.search.ui;

import android.annotation.SuppressLint;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;

import com.zhiyang.meteor.R;
import com.zhiyang.meteor.common.basic.model.PagerView;
import com.zhiyang.meteor.common.ui.widget.SwipeBackCoordinatorLayout;
import com.zhiyang.meteor.common.utils.presenter.pager.PagerScrollablePresenter;
import com.zhiyang.meteor.common.basic.model.PagerManageView;
import com.zhiyang.meteor.common.basic.adapter.FooterAdapter;
import com.zhiyang.meteor.common.ui.adapter.multipleState.LargeErrorStateAdapter;
import com.zhiyang.meteor.common.ui.adapter.multipleState.LargeLoadingStateAdapter;
import com.zhiyang.meteor.common.ui.widget.MultipleStateRecyclerView;
import com.zhiyang.meteor.common.ui.widget.swipeRefreshView.BothWaySwipeRefreshLayout;
import com.zhiyang.meteor.common.utils.BackToTopUtils;
import com.zhiyang.meteor.common.utils.DisplayUtils;
import com.zhiyang.meteor.common.utils.manager.ThemeManager;
import com.zhiyang.meteor.common.utils.presenter.pager.PagerStateManagePresenter;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Search page view.
 *
 * */

@SuppressLint("ViewConstructor")
public abstract class AbstractSearchPageView extends BothWaySwipeRefreshLayout
        implements PagerView, BothWaySwipeRefreshLayout.OnRefreshAndLoadListener,
        LargeErrorStateAdapter.OnRetryListener {

    @BindView(R.id.container_photo_list_recyclerView) MultipleStateRecyclerView recyclerView;

    private OnClickListener hideKeyboardListener;

    private PagerStateManagePresenter stateManagePresenter;

    protected boolean selected;
    protected int index;
    protected PagerManageView pagerManageView;

    public AbstractSearchPageView(SearchActivity a, int id, FooterAdapter adapter,
                                  boolean selected, int index, PagerManageView v) {
        super(a);
        this.setId(id);
        this.init(adapter, selected, index, v);
    }

    // init.

    @SuppressLint("InflateParams")
    protected void init(FooterAdapter adapter, boolean selected, int index, PagerManageView v) {
        View contentView = LayoutInflater.from(getContext())
                .inflate(R.layout.container_photo_list_2, null);
        addView(contentView);
        ButterKnife.bind(this, this);
        initData(selected, index, v);
        initView(adapter);
    }

    protected void initData(boolean selected, int index, PagerManageView v) {
        this.selected = selected;
        this.index = index;
        this.pagerManageView = v;
    }

    protected void initView(FooterAdapter adapter) {
        setColorSchemeColors(ThemeManager.getContentColor(getContext()));
        setProgressBackgroundColorSchemeColor(ThemeManager.getRootColor(getContext()));
        setOnRefreshAndLoadListener(this);
        setPermitRefresh(false);
        setPermitLoad(false);

        int navigationBarHeight = DisplayUtils.getNavigationBarHeight(getResources());
        setDragTriggerDistance(
                BothWaySwipeRefreshLayout.DIRECTION_BOTTOM,
                navigationBarHeight + getResources().getDimensionPixelSize(R.dimen.normal_margin));

        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(getLayoutManager());
        recyclerView.setAdapter(
                new LargeLoadingStateAdapter(getContext(), 98,
                        v -> { if (hideKeyboardListener != null) hideKeyboardListener.onClick(v); }),
                MultipleStateRecyclerView.STATE_LOADING);
        recyclerView.setAdapter(
                new LargeErrorStateAdapter(
                        getContext(), 98,
                        R.drawable.feedback_search,
                        getInitFeedbackText(),
                        getContext().getString(R.string.search),
                        true, false,
                        v -> { if (hideKeyboardListener != null) hideKeyboardListener.onClick(v); },
                        this),
                MultipleStateRecyclerView.STATE_ERROR);
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                PagerScrollablePresenter.onScrolled(
                        AbstractSearchPageView.this, recyclerView,
                        adapter.getRealItemCount(), pagerManageView, index, dy);
            }
        });
        recyclerView.setState(MultipleStateRecyclerView.STATE_ERROR);

        stateManagePresenter = new PagerStateManagePresenter(recyclerView);
    }

    // control.

    public AbstractSearchPageView setOnClickListenerForFeedbackView(OnClickListener l) {
        hideKeyboardListener = l;
        return this;
    }

    protected abstract RecyclerView.LayoutManager getLayoutManager();

    protected abstract String getInitFeedbackText();

    // interface.

    // pager view.

    @Override
    public State getState() {
        return stateManagePresenter.getState();
    }

    @Override
    public boolean setState(State state) {
        boolean stateChanged = stateManagePresenter.setState(state, true);
        if (stateChanged && state == State.ERROR) {
            recyclerView.setAdapter(
                    new LargeErrorStateAdapter(
                            getContext(), 98,
                            R.drawable.feedback_search,
                            getContext().getString(R.string.feedback_search_failed_tv),
                            getContext().getString(R.string.feedback_click_retry),
                            true,
                            true,
                            v -> { if (hideKeyboardListener != null) hideKeyboardListener.onClick(v); },
                            this),
                    MultipleStateRecyclerView.STATE_ERROR);
            return true;
        }
        return stateChanged;
    }

    @Override
    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    @Override
    public void setSwipeRefreshing(boolean refreshing) {
        setRefreshing(refreshing);
    }

    @Override
    public void setSwipeLoading(boolean loading) {
        setLoading(loading);
    }

    @Override
    public void setPermitSwipeRefreshing(boolean permit) {
        // do nothing.
    }

    @Override
    public void setPermitSwipeLoading(boolean permit) {
        setPermitLoad(permit);
    }

    @Override
    public boolean checkNeedBackToTop() {
        return recyclerView.canScrollVertically(-1)
                && stateManagePresenter.getState() == State.NORMAL;
    }

    @Override
    public void scrollToPageTop() {
        BackToTopUtils.scrollToTop(recyclerView);
    }

    @Override
    public boolean canSwipeBack(int dir) {
        return stateManagePresenter.getState() != State.NORMAL
                || SwipeBackCoordinatorLayout.canSwipeBack(recyclerView, dir);
    }

    @Override
    public RecyclerView getRecyclerView() {
        return recyclerView;
    }

    // on refresh an load listener.

    @Override
    public void onRefresh() {
        pagerManageView.onRefresh(index);
    }

    @Override
    public void onLoad() {
        pagerManageView.onLoad(index);
    }

    // on retry listener.

    @Override
    public void onRetry() {
        pagerManageView.onRefresh(index);
    }
}
