package com.zhiyang.meteor.me.ui;

import android.os.Bundle;

import androidx.coordinatorlayout.widget.CoordinatorLayout;
import com.google.android.material.tabs.TabLayout;

import androidx.lifecycle.ViewModelProviders;
import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.widget.Toolbar;
import android.view.View;

import com.zhiyang.meteor.Mysplash;
import com.zhiyang.meteor.R;
import com.zhiyang.meteor.common.basic.DaggerViewModelFactory;
import com.zhiyang.meteor.common.basic.model.ListResource;
import com.zhiyang.meteor.common.basic.model.PagerView;
import com.zhiyang.meteor.common.basic.vm.PagerManageViewModel;
import com.zhiyang.meteor.common.utils.presenter.list.FollowOrCancelFollowPresenter;
import com.zhiyang.meteor.common.basic.model.PagerManageView;
import com.zhiyang.meteor.common.network.json.User;
import com.zhiyang.meteor.common.basic.activity.MysplashActivity;
import com.zhiyang.meteor.common.ui.adapter.MyPagerAdapter;
import com.zhiyang.meteor.common.ui.widget.SwipeBackCoordinatorLayout;
import com.zhiyang.meteor.common.ui.widget.coordinatorView.StatusBarView;
import com.zhiyang.meteor.common.ui.widget.nestedScrollView.NestedScrollAppBarLayout;
import com.zhiyang.meteor.common.utils.BackToTopUtils;
import com.zhiyang.meteor.common.utils.manager.AuthManager;
import com.zhiyang.meteor.common.utils.manager.ThemeManager;
import com.zhiyang.meteor.common.utils.presenter.pager.PagerViewManagePresenter;
import com.zhiyang.meteor.me.vm.MyFollowerViewModel;
import com.zhiyang.meteor.me.vm.MyFollowingViewModel;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * My follow activity.
 *
 * This activity is used to show followers for application user.
 *
 * */

public class MyFollowActivity extends MysplashActivity
        implements PagerManageView, ViewPager.OnPageChangeListener, 
        SwipeBackCoordinatorLayout.OnSwipeListener, MyFollowAdapter.ItemEventCallback {

    @BindView(R.id.activity_my_follow_container) CoordinatorLayout container;
    @BindView(R.id.activity_my_follow_shadow) View shadow;
    @BindView(R.id.activity_my_follow_statusBar) StatusBarView statusBar;
    @BindView(R.id.activity_my_follow_appBar) NestedScrollAppBarLayout appBar;
    @BindView(R.id.activity_my_follow_viewPager) ViewPager viewPager;

    private PagerView[] pagers = new PagerView[pageCount()];
    private MyFollowAdapter[] adapters = new MyFollowAdapter[pageCount()];

    private PagerManageViewModel pagerManageModel;
    private MyFollowerViewModel[] pagerModels = new MyFollowerViewModel[pageCount()];
    @Inject DaggerViewModelFactory viewModelFactory;

    @Inject FollowOrCancelFollowPresenter followOrCancelFollowPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_follow);
        ButterKnife.bind(this);
        initModel();
        initView();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (AuthManager.getInstance().getUser() != null) {
            User user = AuthManager.getInstance().getUser();
            user.followers_count += ((MyFollowUserView) pagers[followerPage()]).getUserDeltaCount();
            user.following_count += ((MyFollowUserView) pagers[followingPage()]).getUserDeltaCount();
            AuthManager.getInstance().updateUser(user);
        }
    }

    @Override
    public void handleBackPressed() {
        if (pagers[getCurrentPagerPosition()].checkNeedBackToTop()
                && BackToTopUtils.isSetBackToTop(false)) {
            backToTop();
        } else {
            finishSelf(true);
        }
    }

    @Override
    protected void backToTop() {
        BackToTopUtils.showTopBar(appBar, viewPager);
        pagers[getCurrentPagerPosition()].scrollToPageTop();
    }

    @Override
    public void finishSelf(boolean backPressed) {
        finish();
        if (backPressed) {
            overridePendingTransition(R.anim.none, R.anim.activity_slide_out);
        } else {
            overridePendingTransition(R.anim.none, R.anim.activity_fade_out);
        }
    }

    @Override
    public CoordinatorLayout getSnackbarContainer() {
        return container;
    }

    // init.

    private void initModel() {
        pagerManageModel = ViewModelProviders.of(this, viewModelFactory).get(PagerManageViewModel.class);
        pagerManageModel.init(followerPage());

        pagerModels[followerPage()] = ViewModelProviders.of(this, viewModelFactory)
                .get(MyFollowerViewModel.class);
        pagerModels[followerPage()].init(ListResource.refreshing(0, Mysplash.DEFAULT_PER_PAGE));

        pagerModels[followingPage()] = ViewModelProviders.of(this, viewModelFactory)
                .get(MyFollowingViewModel.class);
        pagerModels[followingPage()].init(ListResource.refreshing(0, Mysplash.DEFAULT_PER_PAGE));
    }

    private void initView() {
        SwipeBackCoordinatorLayout swipeBackView = findViewById(R.id.activity_my_follow_swipeBackView);
        swipeBackView.setOnSwipeListener(this);

        Toolbar toolbar = findViewById(R.id.activity_my_follow_toolbar);
        toolbar.setTitle(getString(R.string.my_follow));
        ThemeManager.setNavigationIcon(
                toolbar, R.drawable.ic_toolbar_back_light, R.drawable.ic_toolbar_back_dark);
        toolbar.setNavigationOnClickListener(v -> finishSelf(true));

        initPages();
    }

    private void initPages() {
        adapters[followerPage()] = new MyFollowAdapter(
                this, 
                Objects.requireNonNull(pagerModels[followerPage()].getListResource().getValue()).dataList);
        adapters[followerPage()].setItemEventCallback(this);

        adapters[followingPage()] = new MyFollowAdapter(
                this,
                Objects.requireNonNull(pagerModels[followingPage()].getListResource().getValue()).dataList);
        adapters[followingPage()].setItemEventCallback(this);
        
        List<View> pageList = new ArrayList<>();
        pageList.add(
                new MyFollowUserView(
                        this, R.id.activity_my_follower,
                        adapters[followerPage()],
                        getCurrentPagerPosition() == followerPage(),
                        followerPage(), 
                        this));
        pageList.add(
                new MyFollowUserView(
                        this, R.id.activity_my_following,
                        adapters[followingPage()],
                        getCurrentPagerPosition() == followingPage(),
                        followingPage(), 
                        this));
        for (int i = 0; i < pageList.size(); i ++) {
            pagers[i] = (PagerView) pageList.get(i);
        }

        String[] myFollowTabs = getResources().getStringArray(R.array.my_follow_tabs);

        List<String> tabList = new ArrayList<>();
        Collections.addAll(tabList, myFollowTabs);
        MyPagerAdapter adapter = new MyPagerAdapter(pageList, tabList);
        if (AuthManager.getInstance().getUser() != null) {
            adapter.titleList.set(
                    followerPage(),
                    AuthManager.getInstance().getUser().followers_count + " " + myFollowTabs[followerPage()]);
            adapter.titleList.set(
                    followingPage(),
                    AuthManager.getInstance().getUser().following_count + " " + myFollowTabs[followingPage()]);
            adapter.notifyDataSetChanged();
        }

        viewPager.setAdapter(adapter);
        viewPager.addOnPageChangeListener(this);
        viewPager.setCurrentItem(getCurrentPagerPosition(), false);

        TabLayout tabLayout = findViewById(R.id.activity_my_follow_tabLayout);
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
        tabLayout.setupWithViewPager(viewPager);

        pagerManageModel.getPagerPosition().observe(this, position -> {
            for (int i = followerPage(); i < pageCount(); i ++) {
                pagers[i].setSelected(i == position);
            }
            if (Objects.requireNonNull(
                    pagerModels[position].getListResource().getValue()).dataList.size() == 0
                    && Objects.requireNonNull(
                            pagerModels[position].getListResource().getValue()).state != ListResource.State.REFRESHING
                    && Objects.requireNonNull(
                            pagerModels[position].getListResource().getValue()).state != ListResource.State.LOADING) {
                PagerViewManagePresenter.initRefresh(pagerModels[position], adapters[position]);
            }
        });

        pagerModels[followerPage()].getListResource().observe(this, resource ->
                PagerViewManagePresenter.responsePagerListResourceChanged(
                        resource, pagers[followerPage()], adapters[followerPage()]));
        pagerModels[followingPage()].getListResource().observe(this, resource ->
                PagerViewManagePresenter.responsePagerListResourceChanged(
                        resource, pagers[followingPage()], adapters[followingPage()]));
    }
    
    // control.

    private int getCurrentPagerPosition() {
        if (pagerManageModel.getPagerPosition().getValue() == null) {
            return followerPage();
        } else {
            return pagerManageModel.getPagerPosition().getValue();
        }
    }

    private static int followerPage() {
        return 0;
    }

    private static int followingPage() {
        return 1;
    }

    private static int pageCount() {
        return 2;
    }

    // interface.

    // pager manage view.

    @Override
    public void onRefresh(int index) {
        pagerModels[index].refresh();
    }

    @Override
    public void onLoad(int index) {
        pagerModels[index].load();
    }

    @Override
    public boolean canLoadMore(int index) {
        return Objects.requireNonNull(
                pagerModels[index].getListResource().getValue()).state != ListResource.State.REFRESHING
                && Objects.requireNonNull(
                        pagerModels[index].getListResource().getValue()).state != ListResource.State.LOADING
                && Objects.requireNonNull(
                        pagerModels[index].getListResource().getValue()).state != ListResource.State.ALL_LOADED;
    }

    @Override
    public boolean isLoading(int index) {
        return Objects.requireNonNull(
                pagerModels[index].getListResource().getValue()).state == ListResource.State.LOADING;
    }

    // on page change listener.

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        // do nothing.
    }

    @Override
    public void onPageSelected(int position) {
        pagerManageModel.setPagerPosition(position);
    }

    @Override
    public void onPageScrollStateChanged(int state) {
        // do nothing.
    }

    // on swipe listener.(swipe back listener)

    @Override
    public boolean canSwipeBack(int dir) {
        if (dir == SwipeBackCoordinatorLayout.UP_DIR) {
            return pagers[getCurrentPagerPosition()].canSwipeBack(dir)
                    && appBar.getY() <= -appBar.getMeasuredHeight()
                    + getResources().getDimensionPixelSize(R.dimen.tab_layout_height);
        } else {
            return pagers[getCurrentPagerPosition()].canSwipeBack(dir)
                    && appBar.getY() >= 0;
        }
    }

    @Override
    public void onSwipeProcess(float percent) {
        statusBar.setAlpha(1 - percent);
        shadow.setAlpha(SwipeBackCoordinatorLayout.getBackgroundAlpha(percent));
    }

    @Override
    public void onSwipeFinish(int dir) {
        finishSelf(false);
    }

    // item event callback.

    @Override
    public void onFollowUserOrCancel(User user, int adapterPosition, boolean follow) {
        followOrCancelFollowPresenter.followOrCancelFollowUser(user, follow);
    }
}
