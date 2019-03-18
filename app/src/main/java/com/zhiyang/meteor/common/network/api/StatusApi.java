package com.zhiyang.meteor.common.network.api;

import com.zhiyang.meteor.common.network.json.Total;

import io.reactivex.Observable;
import retrofit2.http.GET;

/**
 * Status api.
 * */

public interface StatusApi {

    @GET("stats/total")
    Observable<Total> getTotal();
}
