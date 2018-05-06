package com.d.net_brains_test.controller.search;

import com.d.net_brains_test.controller.Api;
import com.d.net_brains_test.model.Config;
import com.d.net_brains_test.model.SearchResponse;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Алексей on 06.05.2018.
 */

//класс -- костыль для продолжения запроса при повороте экрана
public class Caller{
    private static boolean isLoading;
    private static Call<SearchResponse> call;
    private static Response<SearchResponse> response;
    private static Throwable t;
    static boolean call(String search, int page){
        if(isLoading)
            return isLoading;
        isLoading = true;

        OkHttpClient client = new OkHttpClient.Builder()
                .readTimeout(30, TimeUnit.SECONDS)
                .connectTimeout(30,TimeUnit.SECONDS)
                .build();

        Retrofit retrofit= new Retrofit.Builder()
                .baseUrl(Config.API_LINK)
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build();

        Api api = retrofit.create(Api.class);
        Call<SearchResponse> call = api.getSearchResult(search, page);

        call.enqueue(new Callback<SearchResponse>() {
            @Override
            public void onResponse(Call<SearchResponse> call, Response<SearchResponse> response) {
                isLoading = false;

                Caller.call = call;
                Caller.response = response;
                responce();

            }

            @Override
            public void onFailure(Call<SearchResponse> call, Throwable t) {
                isLoading = false;

                Caller.call = call;
                Caller.t = t;
                failure();

            }
        });
        return isLoading;
    }
    private static void failure(){
        if(callback!= null){
            callback.onFailure(call,t);
            call = null;
            t = null;
        }
    }
    private static void responce(){
        if(callback != null){
            callback.onResponse(call, response);
            call = null;
            response = null;
        }
    }
    static void setListener(Callback<SearchResponse> callback){
        Caller.callback = callback;
        if(response != null)
            responce();
        else if(t != null)
            failure();
    }
    public static void  cancel(){
        if(call != null)
            call.cancel();
    }
    static Callback<SearchResponse> callback;
}

