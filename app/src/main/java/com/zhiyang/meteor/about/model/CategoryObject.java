package com.zhiyang.meteor.about.model;

import com.zhiyang.meteor.about.ui.AboutAdapter;

/**
 * Category object.
 *
 * category in {@link AboutAdapter}.
 *
 * */

public class CategoryObject
        implements AboutModel {

    public int type = AboutModel.TYPE_CATEGORY;
    public String category;

    public CategoryObject(String category) {
        this.category = category;
    }

    @Override
    public int getType() {
        return type;
    }
}
