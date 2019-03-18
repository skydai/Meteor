package com.zhiyang.meteor.photo3.ui.holder;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.widget.RelativeLayout;

import com.zhiyang.meteor.R;
import com.zhiyang.meteor.common.basic.model.Tag;
import com.zhiyang.meteor.common.basic.activity.MysplashActivity;
import com.zhiyang.meteor.common.network.json.Photo;
import com.zhiyang.meteor.photo3.ui.PhotoInfoAdapter3;
import com.zhiyang.meteor.common.ui.adapter.TagAdapter;
import com.zhiyang.meteor.common.ui.widget.horizontalScrollView.SwipeSwitchLayout;
import com.zhiyang.meteor.photo3.ui.PhotoActivity3;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Tag holder.
 * */

public class TagHolder extends PhotoInfoAdapter3.ViewHolder {

    @BindView(R.id.item_photo_3_tag_container) RelativeLayout container;
    @BindView(R.id.item_photo_3_tag) SwipeSwitchLayout.RecyclerView recyclerView;

    public static final int TYPE_TAG = 6;

    public TagHolder(MysplashActivity a, View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);

        recyclerView.setLayoutManager(
                new LinearLayoutManager(
                        a,
                        LinearLayoutManager.HORIZONTAL,
                        false));
    }

    @Override
    protected void onBindView(PhotoActivity3 a, Photo photo) {
        List<Tag> tagList = new ArrayList<>();
        if (photo.tags != null) {
            tagList.addAll(photo.tags);
        }
        recyclerView.setAdapter(new TagAdapter(tagList));
    }

    @Override
    protected void onRecycled() {
        // do nothing.
    }

    public void setScrollListener(RecyclerView.OnScrollListener listener) {
        recyclerView.clearOnScrollListeners();
        recyclerView.addOnScrollListener(listener);
    }

    public void scrollTo(int x, int y) {
        recyclerView.scrollTo(x, y);
    }
}
