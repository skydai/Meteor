package com.zhiyang.meteor.common.network.api;

import com.zhiyang.meteor.Mysplash;

import io.reactivex.Observable;
import okhttp3.ResponseBody;
import retrofit2.http.DELETE;
import retrofit2.http.POST;
import retrofit2.http.Path;

/**
 * Follow api.
 * */
public interface FollowApi {

    @POST(Mysplash.UNSPLASH_NODE_API_URL + "users/{username}/follow")
    Observable<ResponseBody> follow(@Path("username") String username);

    @DELETE(Mysplash.UNSPLASH_NODE_API_URL + "users/{username}/follow")
    Observable<ResponseBody> cancelFollow(@Path("username") String username);
}
