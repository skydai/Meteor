package com.zhiyang.meteor.common.network.json;

import java.util.List;

import androidx.annotation.Nullable;

/**
 * Notification result.
 * */

public class NotificationResult {

    public String id;
    public String verb;
    public String time;
    public boolean is_seen;
    public boolean is_read;

    @Nullable public List<User> actors;
    @Nullable public List<ActionObject> objects;
    @Nullable public List<Collection> targets;

    public static final String VERB_LIKED = "liked";
    public static final String VERB_COLLECTED = "collected";
    public static final String VERB_FOLLOWED = "followed";
    public static final String VERB_RELEASE = "released";
    public static final String VERB_PUBLISHED = "published";
    public static final String VERB_CURATED = "curated";
}
