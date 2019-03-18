package com.zhiyang.meteor.about.ui.holder;

import androidx.appcompat.widget.AppCompatImageView;
import android.view.View;
import android.widget.TextView;

import com.zhiyang.meteor.Mysplash;
import com.zhiyang.meteor.R;
import com.zhiyang.meteor.about.model.AboutModel;
import com.zhiyang.meteor.common.basic.activity.MysplashActivity;
import com.zhiyang.meteor.about.ui.AboutAdapter;
import com.zhiyang.meteor.common.ui.dialog.TotalDialog;
import com.zhiyang.meteor.common.image.ImageHelper;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Header holder.
 *
 * This ViewHolder class is used to show header for {@link AboutAdapter}.
 *
 * */

public class HeaderHolder extends AboutAdapter.ViewHolder {

    @BindView(R.id.item_about_header_appIcon) AppCompatImageView appIcon;

    @OnClick(R.id.item_about_header_backButton) void close() {
        MysplashActivity activity = Mysplash.getInstance()
                .getTopActivity();
        if (activity != null) {
            activity.finishSelf(true);
        }
    }

    @OnClick(R.id.item_about_header_unsplashContainer) void checkTotal() {
        MysplashActivity activity = Mysplash.getInstance()
                .getTopActivity();
        if (activity != null) {
            TotalDialog dialog = new TotalDialog();
            dialog.show(activity.getSupportFragmentManager(), null);
        }
    }

    public HeaderHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);

        TextView unsplashTitle = itemView.findViewById(R.id.item_about_header_unsplashTitle);
        unsplashTitle.setText(itemView.getContext().getString(R.string.unsplash));

        TextView unsplashContent = itemView.findViewById(R.id.item_about_header_unsplashContent);
        unsplashContent.setText(itemView.getContext().getString(R.string.about_unsplash));
    }

    @Override
    protected void onBindView(MysplashActivity a, AboutModel model) {
        ImageHelper.loadResourceImage(a, appIcon, R.drawable.ic_launcher);
    }

    @Override
    protected void onRecycled() {
        ImageHelper.releaseImageView(appIcon);
    }
}
