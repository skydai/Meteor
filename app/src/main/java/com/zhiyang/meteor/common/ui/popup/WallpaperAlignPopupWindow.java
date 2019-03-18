package com.zhiyang.meteor.common.ui.popup;

import android.annotation.SuppressLint;
import android.content.Context;
import androidx.core.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.zhiyang.meteor.R;
import com.zhiyang.meteor.common.basic.MysplashPopupWindow;
import com.zhiyang.meteor.common.ui.activity.SetWallpaperActivity;

/**
 * Wallpaper align popup window.
 *
 * This popup window is used to select where to wallpaper align.
 *
 * */

public class WallpaperAlignPopupWindow extends MysplashPopupWindow
        implements View.OnClickListener {

    private OnAlignTypeChangedListener listener;
    private int valueNow;

    public WallpaperAlignPopupWindow(Context c, View anchor, int valueNow) {
        super(c);
        this.initialize(c, anchor, valueNow);
    }

    @SuppressLint("InflateParams")
    private void initialize(Context c, View anchor, int valueNow) {
        View v = LayoutInflater.from(c).inflate(R.layout.popup_wallpaper_align, null);
        setContentView(v);

        initData(valueNow);
        initWidget();
        show(anchor, 0, 0);
    }

    private void initData(int valueNow) {
        this.valueNow = valueNow;
    }

    private void initWidget() {
        View v = getContentView();

        v.findViewById(R.id.popup_wallpaper_align_left).setOnClickListener(this);
        v.findViewById(R.id.popup_wallpaper_align_center).setOnClickListener(this);
        v.findViewById(R.id.popup_wallpaper_align_right).setOnClickListener(this);

        TextView leftTxt = v.findViewById(R.id.popup_wallpaper_align_leftTxt);
        if (valueNow == SetWallpaperActivity.ALIGN_TYPE_LEFT) {
            leftTxt.setTextColor(ContextCompat.getColor(v.getContext(), R.color.colorTextSubtitle));
        }

        TextView centerTxt = v.findViewById(R.id.popup_wallpaper_align_centerTxt);
        if (valueNow == SetWallpaperActivity.ALIGN_TYPE_CENTER) {
            centerTxt.setTextColor(ContextCompat.getColor(v.getContext(), R.color.colorTextSubtitle));
        }

        TextView rightTxt = v.findViewById(R.id.popup_wallpaper_align_rightTxt);
        if (valueNow == SetWallpaperActivity.ALIGN_TYPE_RIGHT) {
            rightTxt.setTextColor(ContextCompat.getColor(v.getContext(), R.color.colorTextSubtitle));
        }
    }

    // interface.

    // on align type changed listener.

    public interface OnAlignTypeChangedListener {
        void onAlignTypeChanged(int type);
    }

    public void setAlignTypeChangedListener(OnAlignTypeChangedListener l) {
        listener = l;
    }

    // on click listener.

    @Override
    public void onClick(View view) {
        int newValue = valueNow;
        switch (view.getId()) {
            case R.id.popup_wallpaper_align_left:
                newValue = SetWallpaperActivity.ALIGN_TYPE_LEFT;
                break;

            case R.id.popup_wallpaper_align_center:
                newValue = SetWallpaperActivity.ALIGN_TYPE_CENTER;
                break;

            case R.id.popup_wallpaper_align_right:
                newValue = SetWallpaperActivity.ALIGN_TYPE_RIGHT;
                break;
        }

        if (newValue != valueNow && listener != null) {
            listener.onAlignTypeChanged(newValue);
            dismiss();
        }
    }
}