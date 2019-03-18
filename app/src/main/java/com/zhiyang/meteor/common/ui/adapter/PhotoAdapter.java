package com.zhiyang.meteor.common.ui.adapter;

import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatImageButton;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.zhiyang.meteor.Mysplash;
import com.zhiyang.meteor.R;
import com.zhiyang.meteor.common.basic.adapter.FooterAdapter;
import com.zhiyang.meteor.common.di.annotation.GridColumnCount;
import com.zhiyang.meteor.common.di.annotation.ItemList;
import com.zhiyang.meteor.common.network.json.Photo;
import com.zhiyang.meteor.common.basic.activity.MysplashActivity;
import com.zhiyang.meteor.common.ui.dialog.DownloadRepeatDialog;
import com.zhiyang.meteor.common.ui.widget.CircleImageView;
import com.zhiyang.meteor.common.ui.widget.CircularProgressIcon;
import com.zhiyang.meteor.common.utils.DisplayUtils;
import com.zhiyang.meteor.common.utils.FileUtils;
import com.zhiyang.meteor.common.download.NotificationHelper;
import com.zhiyang.meteor.common.db.DatabaseHelper;
import com.zhiyang.meteor.common.image.ImageHelper;
import com.zhiyang.meteor.common.utils.bus.CollectionEvent;
import com.zhiyang.meteor.common.utils.bus.PhotoEvent;
import com.zhiyang.meteor.common.utils.helper.IntentHelper;
import com.zhiyang.meteor.common.utils.manager.AuthManager;
import com.zhiyang.meteor.common.ui.dialog.DeleteCollectionPhotoDialog;
import com.zhiyang.meteor.common.ui.dialog.SelectCollectionDialog;
import com.zhiyang.meteor.common.ui.widget.freedomSizeView.FreedomImageView;
import com.zhiyang.meteor.collection.ui.CollectionActivity;
import com.zhiyang.meteor.common.utils.bus.MessageBus;
import com.zhiyang.meteor.common.utils.presenter.DispatchCollectionsChangedPresenter;
import com.zhiyang.meteor.user.ui.UserActivity;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Photo adapter.
 *
 * Adapter for {@link RecyclerView} to show photos.
 *
 * */

public class PhotoAdapter extends FooterAdapter<RecyclerView.ViewHolder>
        implements PhotoHolder.ParentAdapter {

    private List<Photo> itemList;

    private int columnCount;
    private boolean showDeleteButton;

    @Nullable private ItemEventCallback callback;

    @Inject
    public PhotoAdapter(Context context, @ItemList List<Photo> list, @GridColumnCount int columnCount) {
        super(context);
        this.itemList = list;
        this.columnCount = columnCount;
        this.showDeleteButton = false;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int position) {
        if (isFooter(position)) {
            // footer.
            return FooterHolder.buildInstance(parent);
        } else {
            View v = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_photo, parent, false);
            return new PhotoHolder(v);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof PhotoHolder && position < getRealItemCount()) {
            ((PhotoHolder) holder).onBindView(
                    itemList.get(position), showDeleteButton, columnCount, false,
                    callback, this);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position,
                                 @NonNull List<Object> payloads) {
        if (payloads.isEmpty()) {
            onBindViewHolder(holder, position);
        } else {
            ((PhotoHolder) holder).onBindView(
                    itemList.get(position), showDeleteButton, columnCount, true,
                    callback, this);
        }
    }

    @Override
    public void onViewRecycled(@NonNull RecyclerView.ViewHolder holder) {
        super.onViewRecycled(holder);
        if (holder instanceof PhotoHolder) {
            ((PhotoHolder) holder).onRecycled();
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

    public void setShowDeleteButton(boolean showDeleteButton) {
        this.showDeleteButton = showDeleteButton;
        notifyDataSetChanged();
    }

    // interface.

    public interface ItemEventCallback {
        void onLikeOrDislikePhoto(Photo photo, int adapterPosition, boolean setToLike);
        void onDownload(Photo photo);
    }

    public void setItemEventCallback(@Nullable ItemEventCallback c) {
        this.callback = c;
    }

    // parent adapter.

    @Override
    public void startPhotoActivity(View image, View background, int adapterPosition) {
        MysplashActivity activity = Mysplash.getInstance().getTopActivity();
        if (activity != null) {
            ArrayList<Photo> list = new ArrayList<>();
            int headIndex = adapterPosition - 2;
            int size = 5;
            if (headIndex < 0) {
                headIndex = 0;
            }
            if (headIndex + size - 1 > itemList.size() - 1) {
                size = itemList.size() - headIndex;
            }
            for (int i = headIndex; i < headIndex + size; i ++) {
                list.add(itemList.get(i));
            }

            IntentHelper.startPhotoActivity(
                    activity, image, background, list, adapterPosition, headIndex);
        }
    }
}

class PhotoHolder extends RecyclerView.ViewHolder {

    @BindView(R.id.item_photo) CardView card;
    @BindView(R.id.item_photo_image) FreedomImageView image;

    @BindView(R.id.item_photo_avatar) CircleImageView avatar;
    @BindView(R.id.item_photo_title) TextView title;

    @BindView(R.id.item_photo_deleteButton) AppCompatImageButton deleteButton;

    @BindView(R.id.item_photo_collectionButton) AppCompatImageButton collectionButton;
    @BindView(R.id.item_photo_likeButton) CircularProgressIcon likeButton;

    private Photo photo;
    @Nullable private PhotoAdapter.ItemEventCallback callback;
    @Nullable private ParentAdapter parentAdapter;

    PhotoHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
    }

    void onBindView(Photo photo, boolean showDeleteButton, int columnCount, boolean update,
                    @Nullable PhotoAdapter.ItemEventCallback callback, @NonNull ParentAdapter adapter) {
        Context context = itemView.getContext();

        this.photo = photo;
        this.callback = callback;
        this.parentAdapter = adapter;

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

        image.setSize(photo.width, photo.height);

        if (!update) {
            ImageHelper.loadAvatar(avatar.getContext(), avatar, photo.user, null);

            title.setText("");
            image.setShowShadow(false);

            ImageHelper.loadRegularPhoto(image.getContext(), image, photo, () -> {
                photo.loadPhotoSuccess = true;
                if (!photo.hasFadedIn) {
                    photo.hasFadedIn = true;
                    ImageHelper.startSaturationAnimation(image.getContext(), image);
                }
                title.setText(photo.user.name);
                image.setShowShadow(true);
            });
        }

        if (showDeleteButton) {
            deleteButton.setVisibility(View.VISIBLE);
        } else {
            deleteButton.setVisibility(View.GONE);
        }

        if (photo.current_user_collections != null && photo.current_user_collections.size() != 0) {
            collectionButton.setImageResource(R.drawable.ic_item_collected);
        } else {
            collectionButton.setImageResource(R.drawable.ic_item_collect);
        }

        likeButton.setProgressColor(Color.WHITE);
        if (photo.settingLike) {
            likeButton.setProgressState();
        } else {
            likeButton.setResultState(
                    photo.liked_by_user
                            ? R.drawable.ic_item_heart_red : R.drawable.ic_item_heart_outline);
        }

        card.setCardBackgroundColor(
                ImageHelper.computeCardBackgroundColor(card.getContext(), photo.color));

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            image.setTransitionName(photo.id + "-cover");
            card.setTransitionName(photo.id + "-background");
        }
    }

    void onRecycled() {
        ImageHelper.releaseImageView(image);
        likeButton.recycleImageView();
    }

    // interface.

    public interface ParentAdapter {
        void startPhotoActivity(View image, View background, int adapterPosition);
    }

    @OnClick(R.id.item_photo) void clickItem() {
        if (parentAdapter != null) {
            parentAdapter.startPhotoActivity(image, card, getAdapterPosition());
        }
    }

    @OnClick(R.id.item_photo_deleteButton) void deletePhoto() {
        MysplashActivity activity = Mysplash.getInstance().getTopActivity();
        if (activity instanceof CollectionActivity) {
            DeleteCollectionPhotoDialog dialog = new DeleteCollectionPhotoDialog();
            dialog.setDeleteInfo(((CollectionActivity) activity).getCollection(), photo);
            dialog.setOnDeleteCollectionListener(result -> {
                MessageBus.getInstance().post(new PhotoEvent(
                        result.photo, result.collection, PhotoEvent.Event.REMOVE_FROM_COLLECTION));

                MessageBus.getInstance().post(new CollectionEvent(
                        result.collection, CollectionEvent.Event.UPDATE));

                MessageBus.getInstance().post(result.user);
            });
            dialog.show(activity.getSupportFragmentManager(), null);
        }
    }

    @OnClick(R.id.item_photo_avatar) void checkAuthor() {
        MysplashActivity activity = Mysplash.getInstance().getTopActivity();
        if (activity != null) {
            IntentHelper.startUserActivity(
                    activity, avatar, card, photo.user, UserActivity.PAGE_PHOTO);
        }
    }

    @OnClick(R.id.item_photo_likeButton) void likePhoto() {
        MysplashActivity activity = Mysplash.getInstance().getTopActivity();
        if (activity == null) {
            return;
        }
        if (AuthManager.getInstance().isAuthorized()) {
            if (likeButton.isUsable()) {
                photo.settingLike = true;
                MessageBus.getInstance().post(new PhotoEvent(photo));
                if (callback != null) {
                    callback.onLikeOrDislikePhoto(photo, getAdapterPosition(), !photo.liked_by_user);
                }
            }
        } else {
            IntentHelper.startLoginActivity(activity);
        }
    }

    @OnClick(R.id.item_photo_collectionButton) void collectPhoto() {
        MysplashActivity activity = Mysplash.getInstance().getTopActivity();
        if (activity != null) {
            if (!AuthManager.getInstance().isAuthorized()) {
                IntentHelper.startLoginActivity(activity);
            } else {
                SelectCollectionDialog dialog = new SelectCollectionDialog();
                dialog.setPhotoAndListener(photo, new DispatchCollectionsChangedPresenter());
                dialog.show(activity.getSupportFragmentManager(), null);
            }
        }
    }

    @OnClick(R.id.item_photo_downloadButton) void downloadPhoto() {
        MysplashActivity activity = Mysplash.getInstance().getTopActivity();
        if (activity != null) {
            if (DatabaseHelper.getInstance(activity).readDownloadingEntityCount(photo.id) > 0) {
                NotificationHelper.showSnackbar(activity.getString(R.string.feedback_download_repeat));
            } else if (FileUtils.isPhotoExists(activity, photo.id)) {
                DownloadRepeatDialog dialog = new DownloadRepeatDialog();
                dialog.setDownloadKey(photo);
                dialog.setOnCheckOrDownloadListener(new DownloadRepeatDialog.OnCheckOrDownloadListener() {

                    private final Photo p = photo;
                    private final PhotoAdapter.ItemEventCallback c = callback;

                    @Override
                    public void onCheck(Object obj) {
                        MysplashActivity activity = Mysplash.getInstance().getTopActivity();
                        if (activity != null) {
                            IntentHelper.startCheckPhotoActivity(activity, p.id);
                        }
                    }

                    @Override
                    public void onDownload(Object obj) {
                        if (c != null) {
                            c.onDownload(p);
                        }
                    }
                });
                dialog.show(activity.getSupportFragmentManager(), null);
            } else if (callback != null) {
                callback.onDownload(photo);
            }
        }
    }
}