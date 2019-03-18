package com.zhiyang.meteor.common.basic.model;

/**
 * Tag.
 *
 * If an Object need to be displayed in a RecyclerView with
 * {@link com.zhiyang.meteor.common.ui.adapter.TagAdapter}, it should implement this interface.
 *
 * */

public interface Tag {

    String getTitle();
    String getRegularUrl();
    String getThumbnailUrl();
}
