package com.zhiyang.meteor.common.utils.helper;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.app.ActivityOptionsCompat;
import androidx.core.content.FileProvider;
import androidx.core.util.Pair;
import android.text.TextUtils;
import android.view.View;

import com.zhiyang.meteor.BuildConfig;
import com.zhiyang.meteor.Mysplash;
import com.zhiyang.meteor.R;
import com.zhiyang.meteor.common.network.json.Collection;
import com.zhiyang.meteor.common.network.json.Photo;
import com.zhiyang.meteor.common.network.json.User;
import com.zhiyang.meteor.common.basic.activity.MysplashActivity;
import com.zhiyang.meteor.common.ui.activity.CustomApiActivity;
import com.zhiyang.meteor.common.ui.activity.muzei.MuzeiCollectionSourceConfigActivity;
import com.zhiyang.meteor.common.ui.activity.muzei.MuzeiSettingsActivity;
import com.zhiyang.meteor.common.utils.FileUtils;
import com.zhiyang.meteor.photo3.ui.PhotoActivity3;
import com.zhiyang.meteor.search.ui.SearchActivity;
import com.zhiyang.meteor.common.ui.activity.DownloadManageActivity;
import com.zhiyang.meteor.common.ui.activity.IntroduceActivity;
import com.zhiyang.meteor.common.ui.activity.LoginActivity;
import com.zhiyang.meteor.common.ui.activity.PreviewActivity;
import com.zhiyang.meteor.common.ui.activity.SettingsActivity;
import com.zhiyang.meteor.common.ui.activity.UpdateMeActivity;
import com.zhiyang.meteor.common.utils.manager.AuthManager;
import com.zhiyang.meteor.about.ui.AboutActivity;
import com.zhiyang.meteor.collection.ui.CollectionActivity;
import com.zhiyang.meteor.main.MainActivity;
import com.zhiyang.meteor.me.ui.MeActivity;
import com.zhiyang.meteor.me.ui.MyFollowActivity;
import com.zhiyang.meteor.user.ui.UserActivity;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Intent helper.
 *
 * This helper that can build {@link Intent} and make start {@link MysplashActivity} easier.
 *
 * */

public class IntentHelper {

    public static void startMainActivity(Activity a) {
        Intent intent = new Intent(a, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        a.startActivity(intent);
    }

    public static void startSearchActivity(MysplashActivity a, @Nullable String query) {
        Intent intent = new Intent(a, SearchActivity.class);
        if (!TextUtils.isEmpty(query)) {
            intent.putExtra(SearchActivity.KEY_SEARCH_ACTIVITY_QUERY, query);
        }

        a.startActivity(intent);
        a.overridePendingTransition(R.anim.activity_slide_in, R.anim.none);
    }

    public static void startPhotoActivity(MysplashActivity a, View image, View background,
                                          ArrayList<Photo> photoList, int currentIndex, int headIndex) {
        Intent intent = new Intent(a, PhotoActivity3.class);
        intent.putParcelableArrayListExtra(PhotoActivity3.KEY_PHOTO_ACTIVITY_2_PHOTO_LIST, photoList);
        intent.putExtra(PhotoActivity3.KEY_PHOTO_ACTIVITY_2_PHOTO_CURRENT_INDEX, currentIndex);
        intent.putExtra(PhotoActivity3.KEY_PHOTO_ACTIVITY_2_PHOTO_HEAD_INDEX, headIndex);
        intent.putExtra(PhotoActivity3.KEY_PHOTO_ACTIVITY_2_ID, photoList.get(currentIndex - headIndex).id);

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            ActivityOptionsCompat options = ActivityOptionsCompat
                    .makeScaleUpAnimation(
                            background,
                            (int) background.getX(), (int) background.getY(),
                            background.getMeasuredWidth(), background.getMeasuredHeight());
            ActivityCompat.startActivity(a, intent, options.toBundle());
        } else {
            ActivityOptionsCompat options = ActivityOptionsCompat
                    .makeSceneTransitionAnimation(
                            a,
                            Pair.create(image, a.getString(R.string.transition_photo_image)),
                            Pair.create(background, a.getString(R.string.transition_photo_background)));
            ActivityCompat.startActivity(a, intent, options.toBundle());
        }
    }

    public static void startPhotoActivity(Activity a, String photoId) {
        Intent intent = new Intent(a, PhotoActivity3.class);
        intent.putExtra(PhotoActivity3.KEY_PHOTO_ACTIVITY_2_ID, photoId);
        a.startActivity(intent);
        a.overridePendingTransition(R.anim.activity_slide_in, R.anim.none);
    }

    public static void startPreviewActivity(MysplashActivity a, Photo photo, boolean showIcon) {
        Intent intent = new Intent(a, PreviewActivity.class);
        intent.putExtra(PreviewActivity.KEY_PREVIEW_ACTIVITY_PREVIEW, photo);
        intent.putExtra(PreviewActivity.KEY_PREVIEW_ACTIVITY_SHOW_ICON, showIcon);
        a.startActivity(intent);
        a.overridePendingTransition(R.anim.activity_slide_in, R.anim.none);
    }

    public static void startPreviewActivity(MysplashActivity a, User user, boolean showIcon) {
        Intent intent = new Intent(a, PreviewActivity.class);
        intent.putExtra(PreviewActivity.KEY_PREVIEW_ACTIVITY_PREVIEW, user);
        intent.putExtra(PreviewActivity.KEY_PREVIEW_ACTIVITY_SHOW_ICON, showIcon);
        a.startActivity(intent);
        a.overridePendingTransition(R.anim.activity_slide_in, R.anim.none);
    }

    public static void startCollectionActivity(MysplashActivity a,
                                               View avatar, View background, Collection c) {
        Intent intent = new Intent(a, CollectionActivity.class);
        intent.putExtra(CollectionActivity.KEY_COLLECTION_ACTIVITY_COLLECTION, c);

        ActivityOptionsCompat options;
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            options = ActivityOptionsCompat
                    .makeScaleUpAnimation(
                            background,
                            (int) background.getX(), (int) background.getY(),
                            background.getMeasuredWidth(), background.getMeasuredHeight());
        } else {
            options = ActivityOptionsCompat
                    .makeSceneTransitionAnimation(
                            a,
                            Pair.create(avatar, a.getString(R.string.transition_collection_avatar)),
                            Pair.create(background, a.getString(R.string.transition_collection_background)));
        }

        ActivityCompat.startActivity(a, intent, options.toBundle());
    }

    public static void startCollectionActivity(MysplashActivity a, Collection c) {
        Intent intent = new Intent(a, CollectionActivity.class);
        intent.putExtra(CollectionActivity.KEY_COLLECTION_ACTIVITY_COLLECTION, c);
        a.startActivity(intent);
        a.overridePendingTransition(R.anim.activity_slide_in, R.anim.none);
    }

    public static void startCollectionActivity(Activity a, String collectionId) {
        Intent intent = new Intent(a, CollectionActivity.class);
        intent.putExtra(CollectionActivity.KEY_COLLECTION_ACTIVITY_ID, collectionId);
        a.startActivity(intent);
        a.overridePendingTransition(R.anim.activity_slide_in, R.anim.none);
    }

    public static void startUserActivity(MysplashActivity a,
                                         View avatar, View background,
                                         User u, @UserActivity.UserPageRule int page) {
        if (AuthManager.getInstance().isAuthorized()
                && !TextUtils.isEmpty(AuthManager.getInstance().getUsername())
                && u.username.equals(AuthManager.getInstance().getUsername())) {
            startMeActivity(a, avatar, background, page);
        } else {
            Intent intent = new Intent(a, UserActivity.class);
            intent.putExtra(UserActivity.KEY_USER_ACTIVITY_USER, u);
            intent.putExtra(UserActivity.KEY_USER_ACTIVITY_PAGE_POSITION, page);

            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
                a.startActivity(intent);
                a.overridePendingTransition(R.anim.activity_slide_in, R.anim.none);
            } else {
                ActivityOptionsCompat options = ActivityOptionsCompat
                        .makeSceneTransitionAnimation(
                                a,
                                Pair.create(avatar, a.getString(R.string.transition_user_avatar)),
                                Pair.create(background, a.getString(R.string.transition_user_background)));
                ActivityCompat.startActivity(a, intent, options.toBundle());
            }
        }
    }

    public static void startUserActivity(Activity a, String username) {
        Intent intent = new Intent(a, UserActivity.class);
        intent.putExtra(UserActivity.KEY_USER_ACTIVITY_USERNAME, username);
        a.startActivity(intent);
        a.overridePendingTransition(R.anim.activity_slide_in, R.anim.none);
    }

    public static void startLoginActivity(MysplashActivity a) {
        Intent intent = new Intent(a, LoginActivity.class);
        a.startActivity(intent);
        a.overridePendingTransition(R.anim.activity_slide_in, R.anim.none);
    }

    public static void startMeActivity(MysplashActivity a,
                                       View avatar, View background,
                                       @UserActivity.UserPageRule int page) {
        if (!AuthManager.getInstance().isAuthorized()) {
            startLoginActivity(a);
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Intent intent = new Intent(a, MeActivity.class);
            intent.putExtra(MeActivity.KEY_ME_ACTIVITY_PAGE_POSITION, page);

            ActivityOptionsCompat options = ActivityOptionsCompat
                    .makeSceneTransitionAnimation(
                            a,
                            Pair.create(avatar, a.getString(R.string.transition_me_avatar)),
                            Pair.create(background, a.getString(R.string.transition_me_background)));
            ActivityCompat.startActivity(a, intent, options.toBundle());
        } else {
            Intent intent = new Intent(a, MeActivity.class);
            intent.putExtra(MeActivity.KEY_ME_ACTIVITY_PAGE_POSITION, page);

            a.startActivity(intent);
            a.overridePendingTransition(R.anim.activity_slide_in, R.anim.none);
        }
    }

    public static void startMyFollowActivity(MysplashActivity a) {
        if (!AuthManager.getInstance().isAuthorized()) {
            startLoginActivity(a);
        } else {
            Intent intent = new Intent(a, MyFollowActivity.class);
            a.startActivity(intent);
            a.overridePendingTransition(R.anim.activity_slide_in, R.anim.none);
        }
    }

    public static void startUpdateMeActivity(MysplashActivity a) {
        Intent intent = new Intent(a, UpdateMeActivity.class);
        a.startActivity(intent);
        a.overridePendingTransition(R.anim.activity_slide_in, R.anim.none);
    }

    public static void startDownloadManageActivity(MysplashActivity a) {
        Intent intent = new Intent(a, DownloadManageActivity.class);
        a.startActivity(intent);
        a.overridePendingTransition(R.anim.activity_slide_in, R.anim.none);
    }

    public static void startDownloadManageActivityFromNotification(Context context) {
        Intent intent = new Intent(context, DownloadManageActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra(DownloadManageActivity.EXTRA_NOTIFICATION, true);
        context.startActivity(intent);
    }

    public static Intent getDownloadManageActivityIntent(Context context) {
        Intent intent = new Intent(context, DownloadManageActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.putExtra(DownloadManageActivity.EXTRA_NOTIFICATION, true);
        return intent;
    }

    public static void startSettingsActivity(MysplashActivity a) {
        Intent intent = new Intent(a, SettingsActivity.class);
        a.startActivity(intent);
        a.overridePendingTransition(R.anim.activity_slide_in, R.anim.none);
    }

    public static void startAboutActivity(MysplashActivity a) {
        Intent intent = new Intent(a, AboutActivity.class);
        a.startActivity(intent);
        a.overridePendingTransition(R.anim.activity_slide_in, R.anim.none);
    }

    public static void startIntroduceActivity(MysplashActivity a) {
        Intent intent = new Intent(a, IntroduceActivity.class);
        a.startActivity(intent);
        a.overridePendingTransition(R.anim.activity_slide_in, R.anim.none);
    }

    public static void startCheckPhotoActivity(Context c, String title) {
        try {
            Intent intent = new Intent(Intent.ACTION_VIEW);
            Uri uri = FileUtils.filePathToUri(
                    c,
                    Environment.getExternalStorageDirectory()
                            + Mysplash.DOWNLOAD_PATH
                            + title + Mysplash.DOWNLOAD_PHOTO_FORMAT);
            intent.setDataAndType(uri, "image/jpg");
            intent.addCategory(Intent.CATEGORY_DEFAULT);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.addFlags(Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            intent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);

            Intent chooser = Intent.createChooser(
                    intent,
                    c.getString(R.string.check));
            chooser.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            chooser.addFlags(Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
            chooser.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            chooser.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
            c.startActivity(chooser);
        } catch (Exception e) {
            Intent intent = new Intent(Intent.ACTION_ATTACH_DATA);
            File file = new File(
                    Environment.getExternalStorageDirectory()
                            + Mysplash.DOWNLOAD_PATH
                            + title + Mysplash.DOWNLOAD_PHOTO_FORMAT);
            Uri uri = FileProvider.getUriForFile(c, BuildConfig.APPLICATION_ID, file);
            intent.setDataAndType(uri, "image/jpg");
            intent.addCategory(Intent.CATEGORY_DEFAULT);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.addFlags(Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            intent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);

            Intent chooser = Intent.createChooser(
                    intent,
                    c.getString(R.string.check));
            chooser.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            chooser.addFlags(Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
            chooser.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            chooser.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
            c.startActivity(chooser);
        }
    }

    public static void startCheckCollectionActivity(Context c, String title) {
        try {
            Intent intent = new Intent(Intent.ACTION_VIEW);
            Uri uri = Uri.parse("file://"
                    + Environment.getExternalStorageDirectory()
                    + Mysplash.DOWNLOAD_PATH
                    + title
                    + ".zip");
            intent.setDataAndType(uri, "application/x-zip-compressed");
            intent.addCategory(Intent.CATEGORY_DEFAULT);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.addFlags(Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            intent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);

            Intent chooser = Intent.createChooser(
                    intent,
                    c.getString(R.string.check));
            chooser.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            chooser.addFlags(Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
            chooser.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            chooser.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
            c.startActivity(chooser);
        } catch (Exception e) {
            Intent intent = new Intent(Intent.ACTION_VIEW);
            File file = new File(
                    Environment.getExternalStorageDirectory()
                            + Mysplash.DOWNLOAD_PATH
                            + title
                            + ".zip");
            Uri uri = FileProvider.getUriForFile(c, BuildConfig.APPLICATION_ID, file);
            intent.setDataAndType(uri, "application/x-zip-compressed");
            intent.addCategory(Intent.CATEGORY_DEFAULT);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.addFlags(Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            intent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);

            Intent chooser = Intent.createChooser(
                    intent,
                    c.getString(R.string.check));
            chooser.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            chooser.addFlags(Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
            chooser.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            chooser.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
            c.startActivity(chooser);
        }
    }

    public static void startWebActivity(Context c, String url) {
        String packageName = "com.android.chrome";
        Intent browserIntent = new Intent();
        browserIntent.setPackage(packageName);
        List<ResolveInfo> activitiesList = c.getPackageManager().queryIntentActivities(
                browserIntent, -1);
        if (activitiesList.size() > 0) {
            CustomTabHelper.startCustomTabActivity(c, url);
        } else {
            c.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(url)));
        }
    }

    public static void startCustomApiActivity(SettingsActivity a) {
        Intent intent = new Intent(a, CustomApiActivity.class);
        a.startActivityForResult(intent, Mysplash.ACTIVITY_REQUEST_CODE_CUSTOM_API);
    }

    public static void startMuzeiSettingsActivity(MysplashActivity a) {
        Intent intent = new Intent(a, MuzeiSettingsActivity.class);
        a.startActivity(intent);
    }

    public static void startMuzeiCollectionSourceConfigActivity(MysplashActivity a) {
        Intent intent = new Intent(a, MuzeiCollectionSourceConfigActivity.class);
        a.startActivity(intent);
    }

    public static void backToHome(MysplashActivity a) {
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
        a.startActivity(intent);
    }
}
