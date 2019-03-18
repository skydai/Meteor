package com.zhiyang.meteor.about.model;

import com.zhiyang.meteor.about.ui.AboutAdapter;

import androidx.annotation.IntDef;

/**
 * About model.
 *
 * Item model for {@link AboutAdapter}.
 *
 * */

public interface AboutModel {

    @AboutTypeRule
    int getType();

    int TYPE_HEADER = 1;
    int TYPE_CATEGORY = 2;
    int TYPE_APP = 3;
    int TYPE_LIBRARY = 4;
    @IntDef({TYPE_HEADER, TYPE_CATEGORY, TYPE_APP, TYPE_LIBRARY})
    @interface AboutTypeRule {}
}
