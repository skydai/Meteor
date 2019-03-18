package com.zhiyang.meteor.about.ui.holder;

import android.view.View;
import android.widget.TextView;

import com.zhiyang.meteor.R;
import com.zhiyang.meteor.about.model.AboutModel;
import com.zhiyang.meteor.common.basic.activity.MysplashActivity;
import com.zhiyang.meteor.about.ui.AboutAdapter;
import com.zhiyang.meteor.about.model.CategoryObject;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Category holder.
 *
 * This ViewHolder is used to show category for {@link AboutAdapter}.
 *
 * */

public class CategoryHolder extends AboutAdapter.ViewHolder {

    @BindView(R.id.item_about_category_title) TextView text;

    public CategoryHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
    }

    @Override
    protected void onBindView(MysplashActivity a, AboutModel model) {
        text.setText(((CategoryObject) model).category);
    }

    @Override
    protected void onRecycled() {
        // do nothing.
    }
}
