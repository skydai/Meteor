package com.zhiyang.meteor.common.ui.adapter;

import androidx.recyclerview.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.zhiyang.meteor.Mysplash;
import com.zhiyang.meteor.R;
import com.zhiyang.meteor.common.basic.activity.MysplashActivity;
import com.zhiyang.meteor.common.db.WallpaperSource;
import com.zhiyang.meteor.common.image.ImageHelper;
import com.zhiyang.meteor.common.utils.helper.IntentHelper;

import org.jetbrains.annotations.NotNull;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Wallpaper source adapter.
 *
 * Adapter for {@link RecyclerView} to show {@link WallpaperSource}.
 *
 * */

public class WallpaperSourceAdapter extends RecyclerView.Adapter<WallpaperSourceAdapter.ViewHolder> {

    public List<WallpaperSource> itemList;

    public class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.item_wallpaper_source_cover) ImageView cover;
        @BindView(R.id.item_wallpaper_source_title) TextView title;

        ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        void onBindView(WallpaperSource source) {
            if (TextUtils.isEmpty(source.coverUrl)) {
                ImageHelper.loadResourceImage(cover.getContext(), cover, R.drawable.default_collection_cover);
            } else {
                ImageHelper.loadImageFromUrl(cover.getContext(), cover, source.coverUrl, false, null);
            }
            title.setText(source.title.toUpperCase());
        }

        void onRecycled() {
            ImageHelper.releaseImageView(cover);
        }

        @OnClick(R.id.item_wallpaper_source) void clickItem() {
            MysplashActivity activity = Mysplash.getInstance().getTopActivity();
            if (activity != null) {
                IntentHelper.startCollectionActivity(
                        activity,
                        String.valueOf(itemList.get(getAdapterPosition()).collectionId));
            }
        }

        @OnClick(R.id.item_wallpaper_source_deleteBtn) void deleteItem() {
            itemList.remove(getAdapterPosition());
            notifyItemRemoved(getAdapterPosition());

            if (itemList.size() == 0) {
                itemList.add(WallpaperSource.mysplashSource());
                notifyItemInserted(0);
            }
        }
    }

    public WallpaperSourceAdapter(List<WallpaperSource> list) {
        this.itemList = list;
    }

    @NotNull
    @Override
    public ViewHolder onCreateViewHolder(@NotNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_wallpaper_source, parent, false);
        return new WallpaperSourceAdapter.ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NotNull ViewHolder holder, int position) {
        holder.onBindView(itemList.get(position));
    }

    @Override
    public void onViewRecycled(@NotNull ViewHolder holder) {
        super.onViewRecycled(holder);
        holder.onRecycled();
    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }
}
