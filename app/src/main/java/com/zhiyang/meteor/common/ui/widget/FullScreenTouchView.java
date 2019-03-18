package com.zhiyang.meteor.common.ui.widget;

import android.content.Context;
import androidx.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

import com.zhiyang.meteor.common.utils.DisplayUtils;

/**
 * Full screen touch view.
 * */

public class FullScreenTouchView extends View {

    public FullScreenTouchView(Context context) {
        super(context);
    }

    public FullScreenTouchView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public FullScreenTouchView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int[] size = DisplayUtils.getScreenSize(getContext());
        setMeasuredDimension(size[0], size[1]);
    }
}
