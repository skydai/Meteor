package com.zhiyang.meteor.common.ui.activity;

import android.os.Bundle;

import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;

import com.zhiyang.meteor.Mysplash;
import com.zhiyang.meteor.R;
import com.zhiyang.meteor.common.basic.activity.ReadWriteActivity;
import com.zhiyang.meteor.common.db.DatabaseHelper;
import com.zhiyang.meteor.common.download.DownloadMission;
import com.zhiyang.meteor.common.download.imp.DownloaderService;
import com.zhiyang.meteor.common.ui.dialog.PathDialog;
import com.zhiyang.meteor.common.ui.widget.SwipeBackCoordinatorLayout;
import com.zhiyang.meteor.common.utils.DisplayUtils;
import com.zhiyang.meteor.common.utils.helper.IntentHelper;
import com.zhiyang.meteor.common.download.DownloadHelper;
import com.zhiyang.meteor.common.db.DownloadMissionEntity;
import com.zhiyang.meteor.common.ui.adapter.DownloadAdapter;
import com.zhiyang.meteor.common.ui.widget.coordinatorView.StatusBarView;
import com.zhiyang.meteor.common.utils.manager.ThemeManager;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.nekocode.rxlifecycle.LifecycleEvent;
import cn.nekocode.rxlifecycle.compact.RxLifecycleCompact;
import io.reactivex.Observable;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.ObservableSource;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;

/**
 * Download manage activity.
 *
 * This activity is used to show and manage download missions.
 *
 * */

public class DownloadManageActivity extends ReadWriteActivity
        implements Toolbar.OnMenuItemClickListener, SwipeBackCoordinatorLayout.OnSwipeListener,
        DownloadAdapter.ItemEventCallback {

    @BindView(R.id.activity_download_manage_container) CoordinatorLayout container;
    @BindView(R.id.activity_download_manage_shadow) View shadow;
    @BindView(R.id.activity_download_manage_statusBar) StatusBarView statusBar;
    @BindView(R.id.activity_download_manage_recyclerView) RecyclerView recyclerView;

    private DownloadAdapter adapter;
    private List<DownloadMission> missionList;
    private boolean readListCompleted;

    private List<OnDownloadListener> listenerList;

    public static final String ACTION_DOWNLOAD_MANAGER = "com.zhiyang.meteor.DownloadManager";

    // we can get a boolean object from intent by using this string as a key.
    // If is true, that means this activity was opened by click downloading notification.
    public static final String EXTRA_NOTIFICATION = "notification";

    private class OnDownloadListener extends DownloaderService.OnDownloadListener {

        OnDownloadListener(DownloadMission mission) {
            super(mission.entity.missionId, mission.entity.getNotificationTitle(), mission.entity.result);
        }

        @Override
        public void onProcess(float process) {
            findProgressingHolderAndUpdateIt(index -> {
                DownloadMission mission = missionList.get(index);
                float oldProcess = mission.process;
                mission.process = process;
                if (mission.entity.result != DownloaderService.RESULT_DOWNLOADING) {
                    DownloadHelper.getInstance(DownloadManageActivity.this)
                            .updateMissionResult(
                                    DownloadManageActivity.this,
                                    mission.entity,
                                    DownloaderService.RESULT_DOWNLOADING);
                    drawRecyclerItemProcess(index, mission);
                } else if (mission.process != oldProcess) {
                    drawRecyclerItemProcess(index, mission);
                }
            }, missionId);
        }

        @Override
        public void onComplete(int result) {
            listenerList.remove(this);
            findProgressingHolderAndUpdateIt(index -> {
                DownloadMission mission = missionList.get(index);
                int oldResult = mission.entity.result;
                mission.entity.result = result;
                switch (result) {
                    case DownloaderService.RESULT_SUCCEED:
                        if (oldResult != DownloaderService.RESULT_SUCCEED) {
                            DownloadHelper.getInstance(DownloadManageActivity.this)
                                    .updateMissionResult(
                                            DownloadManageActivity.this,
                                            mission.entity,
                                            DownloaderService.RESULT_SUCCEED);
                            drawRecyclerItemSucceed(index, mission);
                        }
                        break;

                    case DownloaderService.RESULT_FAILED:
                        if (oldResult != DownloaderService.RESULT_FAILED) {
                            DownloadHelper.getInstance(DownloadManageActivity.this)
                                    .updateMissionResult(
                                            DownloadManageActivity.this,
                                            mission.entity,
                                            DownloaderService.RESULT_FAILED);
                            drawRecyclerItemFailed(index, mission);
                        }
                        break;

                    case DownloaderService.RESULT_DOWNLOADING:
                        break;
                }
            }, missionId);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_download_mange);
        ButterKnife.bind(this);
        initData();
        initWidget();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (listenerList != null) {
            for (int i = listenerList.size() - 1; i >= 0; i --) {
                DownloadHelper.getInstance(this).removeOnDownloadListener(listenerList.get(i));
                listenerList.remove(i);
            }
        }
    }

    @Override
    public void handleBackPressed() {
        finishSelf(true);
    }

    @Override
    protected void backToTop() {
        // do nothing.
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

    private void initData() {
        missionList = new ArrayList<>();
        readListCompleted = false;

        List<DownloadMissionEntity> entityList;
        // read failed tasks.
        entityList = DatabaseHelper.getInstance(this)
                .readDownloadEntityList(DownloaderService.RESULT_FAILED);
        for (int i = 0; i < entityList.size(); i ++) {
            missionList.add(new DownloadMission(entityList.get(i)));
        }

        // read downloading tasks.
        entityList = DatabaseHelper.getInstance(this)
                .readDownloadEntityList(DownloaderService.RESULT_DOWNLOADING);
        for (int i = 0; i < entityList.size(); i ++) {
            missionList.add(DownloadHelper.getInstance(this).getDownloadMission(this, entityList.get(i)));
        }

        // build listener list.
        listenerList = new ArrayList<>();
        for (int i = 0; i < missionList.size(); i ++) {
            if (missionList.get(i).entity.result == DownloaderService.RESULT_DOWNLOADING) {
                OnDownloadListener listener = new OnDownloadListener(missionList.get(i));
                listenerList.add(listener);
                DownloadHelper.getInstance(DownloadManageActivity.this)
                        .addOnDownloadListener(listener);
            } else if (missionList.get(i).entity.result == DownloaderService.RESULT_SUCCEED) {
                return;
            }
        }

        adapter = new DownloadAdapter(missionList);
        adapter.setItemEventCallback(this);

        // read completed tasks.
        Observable.create((ObservableOnSubscribe<List<DownloadMission>>) emitter -> {
            List<DownloadMissionEntity> list = DatabaseHelper.getInstance(this)
                    .readDownloadEntityList(DownloaderService.RESULT_SUCCEED);
            List<DownloadMission> failedList = new ArrayList<>();
            for (int i = 0; i < list.size(); i ++) {
                failedList.add(new DownloadMission(list.get(i)));
            }

            emitter.onNext(failedList);
            emitter.onComplete();
        }).compose(RxLifecycleCompact.bind(this).disposeObservableWhen(LifecycleEvent.DESTROY))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DisposableObserver<List<DownloadMission>>() {
                    @Override
                    public void onNext(List<DownloadMission> list) {
                        int size = missionList.size();
                        missionList.addAll(list);
                        adapter.notifyItemRangeInserted(size, missionList.size() - size);
                    }

                    @Override
                    public void onError(Throwable e) {
                    }

                    @Override
                    public void onComplete() {
                        readListCompleted = true;
                    }
                });
    }

    private void initWidget() {
        SwipeBackCoordinatorLayout swipeBackView = findViewById(R.id.activity_download_manage_swipeBackView);
        swipeBackView.setOnSwipeListener(this);

        Toolbar toolbar = findViewById(R.id.activity_download_manage_toolbar);
        if (Mysplash.getInstance().getActivityCount() == 1) {
            ThemeManager.setNavigationIcon(
                    toolbar,
                    R.drawable.ic_toolbar_home_light, R.drawable.ic_toolbar_home_dark);
        } else {
            ThemeManager.setNavigationIcon(
                    toolbar,
                    R.drawable.ic_toolbar_back_light, R.drawable.ic_toolbar_back_dark);
        }
        toolbar.inflateMenu(R.menu.activity_download_manage_toolbar);
        toolbar.setNavigationOnClickListener(v -> {
            if (Mysplash.getInstance().getActivityCount() == 1) {
                IntentHelper.startMainActivity(DownloadManageActivity.this);
            }
            finishSelf(true);
        });
        toolbar.setOnMenuItemClickListener(this);

        recyclerView.setLayoutManager(
                new GridLayoutManager(this, DisplayUtils.getGirdColumnCount(this)));
        recyclerView.setAdapter(adapter);
    }

    // control.

    private void findProgressingHolderAndUpdateIt(Consumer<Integer> consumer, long missionId) {
        Observable.create((ObservableOnSubscribe<Integer>) emitter -> {
            for (int i = 0; i < missionList.size(); i ++) {
                if (missionList.get(i).entity.missionId == missionId) {
                    emitter.onNext(i);
                    return;
                } else if (missionList.get(i).entity.result == DownloaderService.RESULT_SUCCEED) {
                    return;
                }
            }
        }).compose(RxLifecycleCompact.bind(DownloadManageActivity.this)
                .disposeObservableWhen(LifecycleEvent.DESTROY))
                .subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnNext(consumer)
                .subscribe();
    }

    /**
     * Make item view show downloading progress and percent.
     *
     * @param position    Adapter position for item.
     * @param mission     A {@link DownloadMission} object which saved information of downloading task. */
    private void drawRecyclerItemProcess(int position, DownloadMission mission) {
        missionList.set(position, mission);
        adapter.notifyItemChanged(position, 1);
    }

    /**
     * Make the item view to show the information that means "Download successful".
     *
     * @param position Adapter position for item.
     * @param mission  A {@link DownloadMission} object which saved information of downloading task.
     * */
    private void drawRecyclerItemSucceed(int position, DownloadMission mission) {
        missionList.remove(position);
        adapter.notifyItemRemoved(position);

        for (int i = missionList.size() - 1; i >= 0; i --) {
            if (missionList.get(i).entity.result != DownloaderService.RESULT_SUCCEED) {
                missionList.add(i + 1, mission);
                adapter.notifyItemInserted(i + 1);
                return;
            }
        }
        missionList.add(0, mission);
        adapter.notifyItemInserted(0);
    }

    /**
     * Make the item view to show the information that means "Download failed".
     *
     * @param position Adapter position for item.
     * @param mission  A {@link DownloadMission} object which saved information of downloading task.
     * */
    private void drawRecyclerItemFailed(int position, DownloadMission mission) {
        // remove the old item and add a new item on the first position of list.

        missionList.remove(position);
        adapter.notifyItemRemoved(position);

        missionList.add(0, mission);
        adapter.notifyItemInserted(0);
    }

    private void restartMission(DownloadMissionEntity entity) {
        DownloadMission mission = DownloadHelper.getInstance(this)
                .restartMission(this, entity.missionId);
        if (mission == null) {
            return;
        }

        OnDownloadListener listener = new OnDownloadListener(mission);
        listenerList.add(listener);
        DownloadHelper.getInstance(this).addOnDownloadListener(listener);

        Observable.create((ObservableOnSubscribe<Integer>) emitter -> {
            // remove the old item.
            for (int i = 0; i < missionList.size(); i ++) {
                if (missionList.get(i).entity.missionId == entity.missionId) {
                    emitter.onNext(i);
                    return;
                }
            }
        }).compose(RxLifecycleCompact.bind(DownloadManageActivity.this)
                .disposeObservableWhen(LifecycleEvent.DESTROY))
                .subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnNext(i -> {
                    missionList.remove((int) i);
                    adapter.notifyItemRemoved(i);
                }).observeOn(Schedulers.computation())
                .flatMap((Function<Integer, ObservableSource<Integer>>) integer -> Observable.create(emitter -> {
                    // add the new item.
                    if (missionList.size() > 0) {
                        // if the list's size > 0, we need find the last failed mission item and add the new item after it.
                        for (int i = 0; i < missionList.size(); i ++) {
                            if (missionList.get(i).entity.result != DownloaderService.RESULT_FAILED) {
                                emitter.onNext(i);
                                return;
                            }
                        }
                        emitter.onNext(missionList.size());
                    } else {
                        emitter.onNext(0);
                    }
                })).observeOn(AndroidSchedulers.mainThread())
                .doOnNext(i -> {
                    missionList.add(i, mission);
                    adapter.notifyItemInserted(i);
                }).subscribe();
    }

    // interface.

    // on menu item click listener.

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_path:
                PathDialog dialog = new PathDialog();
                dialog.show(getSupportFragmentManager(), null);
                break;

            case R.id.action_cancel_all:
                if (readListCompleted) {
                    List<DownloadMissionEntity> entityList = new ArrayList<>();
                    for (int i = 0; i < missionList.size(); i ++) {
                        entityList.add(missionList.get(i).entity);
                    }
                    DownloadHelper.getInstance(this).clearMission(this, entityList);
                    missionList.clear();
                    adapter.notifyDataSetChanged();
                }
                break;
        }
        return true;
    }

    // on swipe listener.

    @Override
    public boolean canSwipeBack(int dir) {
        return SwipeBackCoordinatorLayout.canSwipeBack(recyclerView, dir);
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
    public void onDelete(DownloadMissionEntity entity, int adapterPosition) {
        if (readListCompleted) {
            DownloadHelper.getInstance(this).removeMission(this, entity);
            missionList.remove(adapterPosition);
            adapter.notifyItemRemoved(adapterPosition);
        }
    }

    @Override
    public void onRetry(DownloadMissionEntity entity, int adapterPosition) {
        requestReadWritePermission(entity, downloadable ->
                restartMission((DownloadMissionEntity) downloadable));
    }
}
