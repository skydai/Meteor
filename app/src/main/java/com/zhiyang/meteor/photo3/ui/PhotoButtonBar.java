package com.zhiyang.meteor.photo3.ui;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Color;
import androidx.appcompat.widget.AppCompatImageButton;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.zhiyang.meteor.R;
import com.zhiyang.meteor.common.network.json.Photo;
import com.zhiyang.meteor.common.ui.widget.CircularProgressIcon;
import com.zhiyang.meteor.common.utils.DisplayUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnLongClick;

/**
 * Photo button bar.
 * */

public class PhotoButtonBar extends RelativeLayout {

    @BindView(R.id.container_photo_2_button_bar) LinearLayout container;

    @BindView(R.id.container_photo_2_button_bar_likeButton) CircularProgressIcon likeButton;
    @OnClick(R.id.container_photo_2_button_bar_likeButton)
    void likePhoto() {
        if (listener != null && likeButton.getState() == CircularProgressIcon.STATE_RESULT) {
            listener.onLikeButtonClicked();
        }
    }

    @BindView(R.id.container_photo_2_button_bar_collectButton) AppCompatImageButton collectButton;
    @OnClick(R.id.container_photo_2_button_bar_collectButton)
    void collectPhoto() {
        if (listener != null) {
            listener.onCollectButtonClicked();
        }
    }

    @BindView(R.id.container_photo_2_button_bar_downloadButton) CircularProgressIcon downloadButton;
    @OnClick(R.id.container_photo_2_button_bar_downloadButton)
    void downloadPhoto() {
        if (listener != null && downloadButton.getState() == CircularProgressIcon.STATE_RESULT) {
            listener.onDownloadButtonClicked();
        }
    }
    @OnLongClick(R.id.container_photo_2_button_bar_downloadButton)
    boolean downloadPhotoDirectly() {
        if (listener != null && downloadButton.getState() == CircularProgressIcon.STATE_RESULT) {
            listener.onDownloadButtonLongClicked();
        }
        return true;
    }

    private int likeIconId;
    private int collectIconId;
    private int progress;

    private OnClickButtonListener listener;

    public PhotoButtonBar(Context context) {
        super(context);
        this.initialize();
    }

    public PhotoButtonBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.initialize();
    }

    public PhotoButtonBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.initialize();
    }

    @SuppressLint("InflateParams")
    private void initialize() {
        addView(
                LayoutInflater.from(getContext())
                        .inflate(R.layout.container_photo_2_button_bar, null));

        ButterKnife.bind(this, this);
        initData();
        initWidget();
    }

    private void initData() {
        likeIconId = getLikeIcon(false);
        collectIconId = getCollectIcon(false);
        progress = -1;
    }

    private void initWidget() {
        likeButton.setResultState(getLikeIcon(false));
        likeButton.setProgressColor(Color.WHITE);

        collectButton.setImageResource(getCollectIcon(false));

        downloadButton.setResultState(getDownloadIcon());
        downloadButton.setProgressColor(Color.WHITE);
    }

    // draw.

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        LayoutParams params = (LayoutParams) container.getLayoutParams();
        if (DisplayUtils.isTabletDevice(getContext())
                || getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            params.width = getResources()
                    .getDimensionPixelSize(R.dimen.tablet_download_button_bar_width);
        } else {
            params.width = getMeasuredWidth();
        }
        params.addRule(CENTER_IN_PARENT);
        container.setLayoutParams(params);
    }

    // control.

    public void setState(Photo photo) {
        setLikeState(photo);
        setCollectState(photo);
    }

    public void setLikeState(Photo photo) {
        if (photo != null) {
            if (photo.settingLike) {
                likeButton.setProgressState();
            } else {
                int newIconId = getLikeIcon(photo.liked_by_user);
                if (likeButton.getState() == CircularProgressIcon.STATE_PROGRESS) {
                    likeIconId = newIconId;
                    likeButton.setResultState(newIconId);
                } else if (likeIconId != newIconId) {
                    likeIconId = newIconId;
                    likeButton.setResultState(newIconId);
                }
            }
        }
    }

    public void setCollectState(Photo photo) {
        if (photo != null) {
            boolean collected = photo.current_user_collections != null
                    && photo.current_user_collections.size() != 0;
            int newIconId = getCollectIcon(collected);
            if (newIconId != collectIconId) {
                collectIconId = newIconId;
                collectButton.setImageResource(
                        getCollectIcon(
                                photo.current_user_collections != null
                                        && photo.current_user_collections.size() != 0));
            }
        }
    }

    @SuppressLint("SetTextI18n")
    public void setDownloadState(boolean downloading, int progress) {
        if (downloading && downloadButton.getState() == CircularProgressIcon.STATE_RESULT) {
            this.progress = -1;
            downloadButton.setProgressState();
        } else if (!downloading && downloadButton.getState() == CircularProgressIcon.STATE_PROGRESS) {
            this.progress = -1;
            downloadButton.setResultState(getDownloadIcon());
        }

        if (downloadButton.getState() == CircularProgressIcon.STATE_PROGRESS
                && progress != this.progress
                && progress >= 0) {
            this.progress = progress;
        }
    }

    private int getLikeIcon(boolean liked) {
        if (liked) {
            return R.drawable.ic_item_heart_red;
        } else {
            return R.drawable.ic_item_heart_outline;
        }
    }

    private int getCollectIcon(boolean collected) {
        if (collected) {
            return R.drawable.ic_item_collected;
        } else {
            return R.drawable.ic_item_collect;
        }
    }

    private int getDownloadIcon() {
        return R.drawable.ic_download_png_dark;
    }

    // interface.

    public interface OnClickButtonListener {
        void onLikeButtonClicked();
        void onCollectButtonClicked();
        void onDownloadButtonClicked();
        void onDownloadButtonLongClicked();
    }

    public void setOnClickButtonListener(OnClickButtonListener l) {
        listener = l;
    }
}
