package com.zhiyang.meteor.common.network.api;

import com.zhiyang.meteor.Mysplash;
import com.zhiyang.meteor.common.network.json.Photo;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Path;

/**
 * Photo node api.
 * */

public interface PhotoNodeApi {

    @GET(Mysplash.UNSPLASH_NODE_API_URL + "photos/{id}/info")
    Observable<Photo> getAPhoto(@Path("id") String id);
}
