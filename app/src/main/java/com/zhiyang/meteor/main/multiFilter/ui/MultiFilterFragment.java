package com.zhiyang.meteor.main.multiFilter.ui;

import android.content.Context;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.widget.AppCompatImageButton;
import androidx.appcompat.widget.Toolbar;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;

import com.zhiyang.meteor.Mysplash;
import com.zhiyang.meteor.R;
import com.zhiyang.meteor.common.basic.model.ListResource;
import com.zhiyang.meteor.common.basic.DaggerViewModelFactory;
import com.zhiyang.meteor.common.basic.fragment.LoadableFragment;
import com.zhiyang.meteor.common.basic.model.PagerView;
import com.zhiyang.meteor.common.ui.adapter.PhotoAdapter;
import com.zhiyang.meteor.common.utils.presenter.pager.PagerLoadablePresenter;
import com.zhiyang.meteor.common.basic.model.PagerManageView;
import com.zhiyang.meteor.common.network.json.Photo;
import com.zhiyang.meteor.common.ui.widget.coordinatorView.StatusBarView;
import com.zhiyang.meteor.common.ui.widget.nestedScrollView.NestedScrollAppBarLayout;
import com.zhiyang.meteor.common.utils.BackToTopUtils;
import com.zhiyang.meteor.common.utils.DisplayUtils;
import com.zhiyang.meteor.common.utils.ValueUtils;
import com.zhiyang.meteor.common.utils.manager.ThemeManager;
import com.zhiyang.meteor.common.utils.presenter.pager.PagerViewManagePresenter;
import com.zhiyang.meteor.main.MainActivity;
import com.zhiyang.meteor.main.multiFilter.vm.MultiFilterFragmentModel;
import com.zhiyang.meteor.main.multiFilter.vm.MultiFilterPhotoViewModel;

import java.util.List;
import java.util.Objects;

import javax.inject.Inject;

import androidx.lifecycle.ViewModelProviders;
import butterknife.BindView;
import butterknife.BindViews;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Multi filter fragment.
 *
 * This fragment is used to search photos by multiple parameters.
 *
 * */

public class MultiFilterFragment extends LoadableFragment<Photo>
        implements PagerManageView, EditText.OnEditorActionListener,
        NestedScrollAppBarLayout.OnNestedScrollingListener {

    @BindView(R.id.fragment_multi_filter_statusBar) StatusBarView statusBar;
    @BindView(R.id.fragment_multi_filter_container) CoordinatorLayout container;

    @BindView(R.id.fragment_multi_filter_appBar) NestedScrollAppBarLayout appBar;
    @BindViews({
            R.id.fragment_multi_filter_photos_editText,
            R.id.fragment_multi_filter_users_editText}) EditText[] editTexts;
    @OnClick(R.id.fragment_multi_filter_searchBtn) void clickSearchButton() {
        injectSearchParameters();
        PagerViewManagePresenter.initRefresh(photoViewModel, photoAdapter);
    }

    @BindViews({
            R.id.fragment_multi_filter_orientationTxt,
            R.id.fragment_multi_filter_featuredTxt}) TextView[] menuTexts;
    @BindViews({
            R.id.fragment_multi_filter_orientationBtn,
            R.id.fragment_multi_filter_featuredBtn}) AppCompatImageButton[] menuIcons;

    @OnClick({
            R.id.fragment_multi_filter_orientationBtn,
            R.id.fragment_multi_filter_orientationContainer}) void showOrientationList() {
        SearchOrientationPopupWindow orientation = new SearchOrientationPopupWindow(
                getActivity(),
                menuIcons[0],
                multiFilterFragmentModel.getSearchOrientation().getValue());
        orientation.setOnSearchOrientationChangedListener(
                orientationValue -> multiFilterFragmentModel.setSearchOrientation(orientationValue));
    }

    @OnClick({
            R.id.fragment_multi_filter_featuredBtn,
            R.id.fragment_multi_filter_featuredContainer}) void showFeaturedList() {
        SearchFeaturedPopupWindow featured = new SearchFeaturedPopupWindow(
                getActivity(),
                menuIcons[1],
                String.valueOf(multiFilterFragmentModel.getSearchFeatured().getValue()));
        featured.setOnSearchFeaturedChangedListener(
                newValue -> multiFilterFragmentModel.setSearchFeatured(newValue));
    }

    @BindView(R.id.fragment_multi_filter_photosView) MultiFilterPhotosView photosView;
    private PhotoAdapter photoAdapter;

    private PagerLoadablePresenter loadablePresenter;

    private MultiFilterFragmentModel multiFilterFragmentModel;
    private MultiFilterPhotoViewModel photoViewModel;
    @Inject DaggerViewModelFactory viewModelFactory;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_multi_filter, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initModel();
        initView(Objects.requireNonNull(getView()));
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        hideKeyboard();
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        if (hidden) {
            hideKeyboard();
        } else {
            showKeyboard();
        }
    }

    @Override
    public void initStatusBarStyle() {
        if (getActivity() != null) {
            DisplayUtils.setStatusBarStyle(getActivity(), needSetDarkStatusBar());
        }
    }

    @Override
    public void initNavigationBarStyle() {
        if (getActivity() != null) {
            DisplayUtils.setNavigationBarStyle(
                    getActivity(), 
                    photosView.getState() == PagerView.State.NORMAL,
                    true);
        }
    }

    @Override
    public boolean needSetDarkStatusBar() {
        return appBar.getY() <= -appBar.getMeasuredHeight();
    }

    @Override
    public boolean needBackToTop() {
        return photosView.checkNeedBackToTop();
    }

    @Override
    public void backToTop() {
        statusBar.animToInitAlpha();
        if (getActivity() != null) {
            DisplayUtils.setStatusBarStyle(getActivity(), false);
        }
        BackToTopUtils.showTopBar(appBar, photosView);
        photosView.scrollToPageTop();
    }

    @Override
    public CoordinatorLayout getSnackbarContainer() {
        return container;
    }

    @Override
    public List<Photo> loadMoreData(List<Photo> list, int headIndex, boolean headDirection) {
        return loadablePresenter.loadMore(
                list, headIndex, headDirection,
                photosView, photosView.getRecyclerView(), photoAdapter,
                this, 0);
    }

    // init.

    private void initModel() {
        multiFilterFragmentModel = ViewModelProviders.of(this, viewModelFactory)
                .get(MultiFilterFragmentModel.class);
        multiFilterFragmentModel.init(
                "", "", "", false);
        
        photoViewModel = ViewModelProviders.of(this, viewModelFactory)
                .get(MultiFilterPhotoViewModel.class);
        photoViewModel.init(ListResource.error(0, Mysplash.DEFAULT_PER_PAGE));
    }

    private void initView(View v) {
        appBar.setOnNestedScrollingListener(this);

        Toolbar toolbar = v.findViewById(R.id.fragment_multi_filter_toolbar);
        toolbar.setTitle(getString(R.string.action_multi_filter));
        ThemeManager.setNavigationIcon(
                toolbar, R.drawable.ic_toolbar_menu_light, R.drawable.ic_toolbar_menu_dark);
        toolbar.setOnClickListener(v12 -> backToTop());
        toolbar.setNavigationOnClickListener(v1 -> {
            if (getActivity() != null) {
                DrawerLayout drawer = getActivity().findViewById(R.id.activity_main_drawerLayout);
                drawer.openDrawer(GravityCompat.START);
            }
        });

        editTexts[0].setOnEditorActionListener(this);
        editTexts[1].setOnEditorActionListener(this);

        photoAdapter = new PhotoAdapter(
                getActivity(),
                Objects.requireNonNull(photoViewModel.getListResource().getValue()).dataList,
                DisplayUtils.getGirdColumnCount(getActivity()));
        photoAdapter.setItemEventCallback((MainActivity) getActivity());
        photosView.setAdapter(photoAdapter);
        photosView.setPagerManageView(this);
        photosView.setClickListenerForFeedbackView(v13 -> hideKeyboard());

        loadablePresenter = new PagerLoadablePresenter() {
            @Override
            public List<Photo> subList(int fromIndex, int toIndex) {
                return photoViewModel.getListResource().getValue().dataList.subList(fromIndex, toIndex);
            }
        };

        multiFilterFragmentModel.getSearchQuery().observe(this, query -> editTexts[0].setText(query));
        multiFilterFragmentModel.getSearchUser().observe(this, user -> editTexts[1].setText(user));

        multiFilterFragmentModel.getSearchOrientation().observe(this, orientation -> {
            if (TextUtils.isEmpty(orientation)) {
                menuTexts[0].setText(R.string.all);
            } else {
                menuTexts[0].setText(ValueUtils.getOrientationName(getActivity(), orientation));
            }
        });
        multiFilterFragmentModel.getSearchFeatured().observe(this, featured -> {
            if (featured) {
                menuTexts[1].setText(getResources().getStringArray(R.array.home_tabs)[1]);
            } else {
                menuTexts[1].setText(R.string.all);
            }
        });

        photoViewModel.getListResource().observe(this, resource ->
                PagerViewManagePresenter.responsePagerListResourceChanged(resource, photosView, photoAdapter));

        v.post(this::showKeyboard);
    }

    // control.

    private void injectSearchParameters() {
        photoViewModel.setQuery(multiFilterFragmentModel.getSearchQuery().getValue());
        photoViewModel.setUsername(multiFilterFragmentModel.getSearchUser().getValue());
        photoViewModel.setOrientation(multiFilterFragmentModel.getSearchOrientation().getValue());
        photoViewModel.setFeatured(
                Objects.requireNonNull(multiFilterFragmentModel.getSearchFeatured().getValue()));
    }

    private void showKeyboard() {
        if (getActivity() != null) {
            InputMethodManager manager
                    = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
            if (manager != null) {
                editTexts[0].setFocusable(true);
                editTexts[0].requestFocus();
                manager.showSoftInput(editTexts[0], 0);
            }
        }
    }

    private void hideKeyboard() {
        if (getActivity() != null) {
            InputMethodManager manager = (InputMethodManager) getActivity().getSystemService(
                    Context.INPUT_METHOD_SERVICE);
            if (manager != null) {
                manager.hideSoftInputFromWindow(editTexts[0].getWindowToken(), 0);
                manager.hideSoftInputFromWindow(editTexts[1].getWindowToken(), 0);
            }
        }
    }

    // interface.

    // pager manage view.

    @Override
    public void onRefresh(int index) {
        photoViewModel.refresh();
    }

    @Override
    public void onLoad(int index) {
        photoViewModel.load();
    }

    @Override
    public boolean canLoadMore(int index) {
        return photoViewModel.getListResource().getValue() != null
                && photoViewModel.getListResource().getValue().state != ListResource.State.REFRESHING
                && photoViewModel.getListResource().getValue().state != ListResource.State.LOADING
                && photoViewModel.getListResource().getValue().state != ListResource.State.ALL_LOADED;
    }

    @Override
    public boolean isLoading(int index) {
        return Objects.requireNonNull(
                photoViewModel.getListResource().getValue()).state == ListResource.State.LOADING;
    }

    // on editor action listener.

    @Override
    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
        injectSearchParameters();
        PagerViewManagePresenter.initRefresh(photoViewModel, photoAdapter);
        hideKeyboard();
        return true;
    }

    // on nested scrolling listener.

    @Override
    public void onStartNestedScroll() {
        // do nothing.
    }

    @Override
    public void onNestedScrolling() {
        if (getActivity() == null) {
            return;
        }
        InputMethodManager manager = (InputMethodManager) getActivity()
                .getSystemService(Context.INPUT_METHOD_SERVICE);
        if (manager != null && (manager.isActive(editTexts[0]) || manager.isActive(editTexts[1]))) {
            hideKeyboard();
        }
        if (needSetDarkStatusBar()) {
            if (statusBar.isInitState()) {
                statusBar.animToDarkerAlpha();
                DisplayUtils.setStatusBarStyle(getActivity(), true);
            }
        } else {
            if (!statusBar.isInitState()) {
                statusBar.animToInitAlpha();
                DisplayUtils.setStatusBarStyle(getActivity(), false);
            }
        }
    }

    @Override
    public void onStopNestedScroll() {
        // do nothing.
    }
}
