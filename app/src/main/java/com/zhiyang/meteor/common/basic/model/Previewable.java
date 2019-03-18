package com.zhiyang.meteor.common.basic.model;

/**
 * Previewable.
 *
 * If an Object needs to be sent to {@link com.zhiyang.meteor.common.ui.activity.PreviewActivity},
 * it should implement this interface.
 *
 * */

public interface Previewable {

    String getRegularUrl();
    String getFullUrl();
    String getDownloadUrl();

    int getWidth();
    int getHeight();
}
