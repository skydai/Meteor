package com.zhiyang.meteor.photo3.ui.holder;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;

import com.zhiyang.meteor.R;
import com.zhiyang.meteor.common.network.json.Collection;
import com.zhiyang.meteor.common.network.json.Photo;
import com.zhiyang.meteor.photo3.ui.MoreHorizontalAdapter3;
import com.zhiyang.meteor.photo3.ui.PhotoInfoAdapter3;
import com.zhiyang.meteor.common.ui.widget.horizontalScrollView.SwipeSwitchLayout;
import com.zhiyang.meteor.common.ui.widget.coordinatorView.NavigationBarView;
import com.zhiyang.meteor.common.utils.DisplayUtils;
import com.zhiyang.meteor.photo3.ui.PhotoActivity3;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * More landscape holder.
 * */

public class MoreLandscapeHolder extends PhotoInfoAdapter3.ViewHolder {

    @BindView(R.id.item_photo_3_more_landscape_container) LinearLayout container;
    @BindView(R.id.item_photo_3_more_landscape_recyclerView) SwipeSwitchLayout.RecyclerView recyclerView;
    @BindView(R.id.item_photo_3_more_landscape_navigationBar) NavigationBarView navigationBar;

    public static final int TYPE_MORE_LANDSCAPE = 8;

    public MoreLandscapeHolder(PhotoActivity3 a, View itemView) {
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
        List<Collection> collectionList = new ArrayList<>();
        if (photo.related_collections != null) {
            collectionList.addAll(photo.related_collections.results);
        }
        recyclerView.setAdapter(new MoreHorizontalAdapter3(collectionList));

        if (DisplayUtils.isLandscape(a)) {
            navigationBar.setVisibility(View.GONE);
        } else {
            navigationBar.setVisibility(View.VISIBLE);
        }
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
