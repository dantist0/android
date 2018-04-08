package com.d.test_for_job.controller;


import com.d.test_for_job.model.DescPost;
import com.d.test_for_job.model.Posts;
import com.d.test_for_job.model.Topics;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;


public interface Api {

//    @GET("GET /v1/oauth/authorize?client_id="+ C.TOKEN+"&redirect_uri=NONE&response_type=code&scope=public")
//    Call<Response> auth();


//    @GET(" /v1/categories/topic-4/posts")
//    Call<Post> wallGet(
//            @Query("search[category]") String category,
//            @Query("offset") int offset,
//            @Query("count") int count,
//            @Query("filter") String filter,
//            @Query("access_token") String TOKEN,
//            @Query("v") double version
//    );

    @GET("/v1/topics")
    Call<Topics> getTopics(
            @Query("search[trending]") Boolean trending,
            @Query("access_token") String token
    );
    @GET("/v1/categories/{slug}/posts")
    Call<Posts> getThrendingTopics(
            @Path(value = "slug") String slug,
            @Query("access_token") String token);
    @GET("/v1/posts/{slug}/")
    Call<DescPost> getPost(
            @Path(value = "slug") String slug,
            @Query("access_token") String token);

}
