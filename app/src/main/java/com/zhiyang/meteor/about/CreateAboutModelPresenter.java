package com.zhiyang.meteor.about;

import android.content.Context;

import com.zhiyang.meteor.R;
import com.zhiyang.meteor.about.model.AboutModel;
import com.zhiyang.meteor.about.model.AppObject;
import com.zhiyang.meteor.about.model.CategoryObject;
import com.zhiyang.meteor.about.model.HeaderObject;
import com.zhiyang.meteor.about.model.LibraryObject;
import com.zhiyang.meteor.about.ui.AboutAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Create about model presenter.
 *
 * This implementor is used to build a list by {@link AboutModel}. This list will provide data for
 * {@link AboutAdapter}.
 *
 * */

public class CreateAboutModelPresenter {

    public static List<AboutModel> createModelList(Context c) {
        List<AboutModel> modelList = new ArrayList<>(29);

        // header.
        modelList.add(new HeaderObject());

        // about app.
        modelList.add(new CategoryObject(c.getString(R.string.about_app)));
        modelList.add(new AppObject(
                1,
                R.drawable.ic_book,
                c.getString(R.string.introduce)));
        modelList.add(new AppObject(
                2,
                R.drawable.ic_github,
                c.getString(R.string.gitHub)));
        modelList.add(new AppObject(
                3,
                R.drawable.ic_email,
                c.getString(R.string.email)));
        modelList.add(new AppObject(
                4,
                R.drawable.ic_android_studio,
                c.getString(R.string.source_code)));


        // library.
        modelList.add(new CategoryObject(c.getString(R.string.libraries)));
        modelList.add(new LibraryObject(
                c.getString(R.string.retrofit),
                c.getString(R.string.about_retrofit),
                "https://github.com/square/retrofit"));
        modelList.add(new LibraryObject(
                c.getString(R.string.glide),
                c.getString(R.string.about_glide),
                "https://github.com/bumptech/glide"));
        modelList.add(new LibraryObject(
                c.getString(R.string.circular_progress_view),
                c.getString(R.string.about_circular_progress_view),
                "https://github.com/rahatarmanahmed/CircularProgressView"));
        modelList.add(new LibraryObject(
                c.getString(R.string.circle_image_view),
                c.getString(R.string.about_circle_image_view),
                "https://github.com/hdodenhof/CircleImageView"));
        modelList.add(new LibraryObject(
                c.getString(R.string.photo_view),
                c.getString(R.string.about_photo_view),
                "https://github.com/bm-x/PhotoView"));
        modelList.add(new LibraryObject(
                c.getString(R.string.page_indicator),
                c.getString(R.string.about_page_indicator),
                "https://github.com/DavidPacioianu/InkPageIndicator"));
        modelList.add(new LibraryObject(
                c.getString(R.string.greendao_db),
                c.getString(R.string.about_greendao_db),
                "https://github.com/greenrobot/greenDAO"));
        modelList.add(new LibraryObject(
                c.getString(R.string.butter_knife),
                c.getString(R.string.about_butter_knife),
                "https://github.com/JakeWharton/butterknife"));
        modelList.add(new LibraryObject(
                c.getString(R.string.number_anim_text_view),
                c.getString(R.string.about_number_anim_text_view),
                "https://github.com/Bakumon/NumberAnimTextView"));
        modelList.add(new LibraryObject(
                c.getString(R.string.number_anim_text_view),
                c.getString(R.string.about_number_anim_text_view),
                "https://github.com/Bakumon/NumberAnimTextView"));
        modelList.add(new LibraryObject(
                c.getString(R.string.file_downloader),
                c.getString(R.string.about_file_downloader),
                "https://github.com/lingochamp/FileDownloader"));

        return modelList;
    }
}
