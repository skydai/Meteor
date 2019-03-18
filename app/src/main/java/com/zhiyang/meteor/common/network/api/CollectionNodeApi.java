package com.zhiyang.meteor.common.network.api;

import com.zhiyang.meteor.Mysplash;
import com.zhiyang.meteor.common.network.json.Collection;

import java.util.List;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Collection node api.
 */

public interface CollectionNodeApi {

    @GET(Mysplash.UNSPLASH_NODE_API_URL + "collections")
    Observable<List<Collection>> getAllCollections(@Query("page") int page,
                                                   @Query("per_page") int per_page);

    @GET(Mysplash.UNSPLASH_NODE_API_URL + "collections/curated")
    Observable<List<Collection>> getCuratedCollections(@Query("page") int page,
                                                       @Query("per_page") int per_page);

    @GET(Mysplash.UNSPLASH_NODE_API_URL + "collections/featured")
    Observable<List<Collection>> getFeaturedCollections(@Query("page") int page,
                                                        @Query("per_page") int per_page);

    @GET(Mysplash.UNSPLASH_NODE_API_URL + "collections/{id}")
    Observable<Collection> getACollection(@Path("id") String id);

    @GET(Mysplash.UNSPLASH_NODE_API_URL + "collections/curated/{id}")
    Observable<Collection> getACuratedCollection(@Path("id") String id);

    @GET(Mysplash.UNSPLASH_NODE_API_URL + "users/{username}/collections")
    Observable<List<Collection>> getUserCollections(@Path("username") String username,
                                                    @Query("page") int page,
                                                    @Query("per_page") int per_page);
}
