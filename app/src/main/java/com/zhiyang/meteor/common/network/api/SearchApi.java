package com.zhiyang.meteor.common.network.api;

import com.zhiyang.meteor.common.network.json.SearchCollectionsResult;
import com.zhiyang.meteor.common.network.json.SearchPhotosResult;
import com.zhiyang.meteor.common.network.json.SearchUsersResult;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Search api.
 * */

public interface SearchApi {

    @GET("search/photos")
    Observable<SearchPhotosResult> searchPhotos(@Query("query") String query,
                                                @Query("page") int page,
                                                @Query("per_page") int per_page);

    @GET("search/users")
    Observable<SearchUsersResult> searchUsers(@Query("query") String query,
                                              @Query("page") int page,
                                              @Query("per_page") int per_page);

    @GET("search/collections")
    Observable<SearchCollectionsResult> searchCollections(@Query("query") String query,
                                                          @Query("page") int page,
                                                          @Query("per_page") int per_page);
}
