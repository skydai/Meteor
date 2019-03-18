package com.zhiyang.meteor.common.ui.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Build;
import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.zhiyang.meteor.Mysplash;
import com.zhiyang.meteor.R;
import com.zhiyang.meteor.common.basic.activity.MysplashActivity;
import com.zhiyang.meteor.common.basic.adapter.FooterAdapter;
import com.zhiyang.meteor.common.ui.widget.CircleImageView;
import com.zhiyang.meteor.common.utils.DisplayUtils;
import com.zhiyang.meteor.common.image.ImageHelper;
import com.zhiyang.meteor.common.network.json.Collection;
import com.zhiyang.meteor.common.ui.widget.freedomSizeView.FreedomImageView;
import com.zhiyang.meteor.common.utils.helper.IntentHelper;
import com.zhiyang.meteor.user.ui.UserActivity;

import org.jetbrains.annotations.NotNull;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Collection adapter.
 *
 * Adapter for {@link RecyclerView} to show {@link Collection}.
 *
 * */

public class CollectionAdapter extends FooterAdapter<RecyclerView.ViewHolder> {

    private List<Collection> itemList;
    private int columnCount;

    public CollectionAdapter(Context context, List<Collection> list, int columnCount) {
        super(context);
        this.itemList = list;
        this.columnCount = columnCount;
    }

    @NotNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NotNull ViewGroup parent, int position) {
        if (isFooter(position)) {
            // footer.
            return FooterHolder.buildInstance(parent);
        } else {
            View v = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_collection, parent, false);
            return new CollectionHolder(v);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof CollectionHolder && position < itemList.size()) {
            ((CollectionHolder) holder).onBindView(itemList.get(position), columnCount);
        }
    }

    @Override
    public void onViewRecycled(@NonNull RecyclerView.ViewHolder holder) {
        super.onViewRecycled(holder);
        if (holder instanceof CollectionHolder) {
            ((CollectionHolder) holder).onRecycled();
        }
    }

    @Override
    public int getRealItemCount() {
        return itemList.size();
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    protected boolean hasFooter(Context context) {
        return !DisplayUtils.isLandscape(context)
                && DisplayUtils.getNavigationBarHeight(context.getResources()) != 0;
    }

    public List<Collection> getItemList() {
        return itemList;
    }
}

class CollectionHolder extends RecyclerView.ViewHolder {

    @BindView(R.id.item_collection) CardView card;
    @BindView(R.id.item_collection_cover) FreedomImageView image;

    @BindView(R.id.item_collection_title) TextView title;
    @BindView(R.id.item_collection_subtitle) TextView subtitle;
    @BindView(R.id.item_collection_avatar) CircleImageView avatar;
    @BindView(R.id.item_collection_name) TextView name;

    CollectionHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
    }

    void onBindView(Collection collection, int columnCount) {
        Context context = itemView.getContext();

        ViewGroup.MarginLayoutParams params = (ViewGroup.MarginLayoutParams) card.getLayoutParams();
        if (columnCount > 1) {
            int margin = context.getResources().getDimensionPixelSize(R.dimen.normal_margin);
            params.setMargins(0, 0, margin, margin);
            card.setLayoutParams(params);
            card.setRadius(context.getResources().getDimensionPixelSize(R.dimen.material_card_radius));
        } else {
            params.setMargins(0, 0, 0, 0);
            card.setLayoutParams(params);
            card.setRadius(0);
        }
        card.setOnClickListener(v -> {
            MysplashActivity activity = Mysplash.getInstance().getTopActivity();
            if (activity != null) {
                IntentHelper.startCollectionActivity(activity, avatar, card, collection);
            }
        });

        if (collection.cover_photo != null
                && collection.cover_photo.width != 0
                && collection.cover_photo.height != 0) {
            image.setSize(
                    collection.cover_photo.width,
                    collection.cover_photo.height);
        }

        if (collection.cover_photo != null) {
            setCardText(context, collection, true);
            ImageHelper.loadCollectionCover(image.getContext(), image, collection, () -> {
                collection.cover_photo.loadPhotoSuccess = true;
                if (!collection.cover_photo.hasFadedIn) {
                    collection.cover_photo.hasFadedIn = true;
                    ImageHelper.startSaturationAnimation(context, image);
                }
                setCardText(context, collection, false);
            });
            card.setCardBackgroundColor(
                    ImageHelper.computeCardBackgroundColor(
                            image.getContext(),
                            collection.cover_photo.color));
        } else {
            setCardText(context, collection, false);
            ImageHelper.loadResourceImage(image.getContext(), image, R.drawable.default_collection_cover);
        }

        ImageHelper.loadAvatar(avatar.getContext(), avatar, collection.user, null);
        avatar.setOnClickListener(v -> {
            MysplashActivity activity = Mysplash.getInstance().getTopActivity();
            if (activity != null) {
                IntentHelper.startUserActivity(
                        activity, avatar, card, collection.user, UserActivity.PAGE_PHOTO);
            }
        });

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            card.setTransitionName(collection.id + "-background");
            avatar.setTransitionName(collection.user.username + "-avatar");
        }
    }

    @SuppressLint("SetTextI18n")
    private void setCardText(Context context, Collection collection, boolean setNull) {
        if (setNull) {
            title.setText("");
            subtitle.setText("");
            name.setText("");
            image.setShowShadow(false);
        } else {
            title.setText(collection.title.toUpperCase());
            subtitle.setText(collection.total_photos
                    + " " + context.getResources().getStringArray(R.array.user_tabs)[0]);
            name.setText(collection.user.name);
            if (collection.cover_photo == null) {
                image.setShowShadow(false);
            } else {
                image.setShowShadow(true);
            }
        }
    }

    void onRecycled() {
        ImageHelper.releaseImageView(image);
        ImageHelper.releaseImageView(avatar);
    }
}