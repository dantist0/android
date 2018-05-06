package com.d.net_brains_test.controller;

import com.d.net_brains_test.model.SearchResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Query;

/**
 * Created by Алексей on 06.05.2018.
 */

public interface Api {

    @GET("search-results")
    Call<SearchResponse> getSearchResult(@Query("query") String searchText, @Query("page") int page);

    @GET("search-results")
    Call<SearchResponse> getSearchResult(@Query("query") String searchText);
}
