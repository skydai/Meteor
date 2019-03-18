package com.zhiyang.meteor.about.ui.holder;

import androidx.appcompat.widget.AppCompatImageView;
import android.view.View;
import android.widget.TextView;

import com.zhiyang.meteor.Mysplash;
import com.zhiyang.meteor.R;
import com.zhiyang.meteor.about.model.AboutModel;
import com.zhiyang.meteor.common.basic.activity.MysplashActivity;
import com.zhiyang.meteor.common.ui.activity.IntroduceActivity;
import com.zhiyang.meteor.about.ui.AboutAdapter;
import com.zhiyang.meteor.common.utils.helper.IntentHelper;
import com.zhiyang.meteor.about.model.AppObject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * App holder.
 *
 * This ViewHolder class is used to show app information for {@link AboutAdapter}.
 *
 * */

public class AppHolder extends AboutAdapter.ViewHolder {

    @BindView(R.id.item_about_app_icon) AppCompatImageView icon;
    @BindView(R.id.item_about_app_title) TextView text;

    private int id;

    public AppHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
    }

    @Override
    public void onBindView(MysplashActivity a, AboutModel model) {
        AppObject object = (AppObject) model;

        icon.setImageResource(object.iconId);
        text.setText(object.text);
        id = object.id;
    }

    @Override
    protected void onRecycled() {
        // do nothing.
    }

    @OnClick(R.id.item_about_app_container) void clickItem() {
        switch (id) {
            case 1:
                if (Mysplash.getInstance().getTopActivity() != null) {
                    IntroduceActivity.watchAllIntroduce(Mysplash.getInstance().getTopActivity());
                }
                break;

            case 2:
                IntentHelper.startWebActivity(itemView.getContext(), "https://github.com/skydai");
                break;

            case 3:
                IntentHelper.startWebActivity(itemView.getContext(), "mailto:corndai1997@gmail.com");
                break;

            case 4:
                IntentHelper.startWebActivity(itemView.getContext(), "https://github.com/skydai/Meteor");
                break;

        }
    }
}
