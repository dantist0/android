package com.d.test_for_job.controller;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.d.test_for_job.MainActivity;
import com.d.test_for_job.R;
import com.d.test_for_job.model.C;
import com.d.test_for_job.model.Posts;
import com.d.test_for_job.model.Topics;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static android.view.View.GONE;

/**
 * Created by Алексей on 08.04.2018.
 */

public class FProductList extends Fragment {
    private View mRootView;
    private RecyclerView mRecyclerView;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private View mLayoutError;
    private RecycleViewAdapter mRecycleViewAdapter;
    private View mNoItemsMessage;
    private Handler mHandler;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mHandler = new Handler();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mRootView = inflater.inflate(R.layout.feed, null);
        mRecyclerView = mRootView.findViewById(R.id.recycler_view);
        mSwipeRefreshLayout = mRootView.findViewById(R.id.swipe_refresh);
        mLayoutError = mRootView.findViewById(R.id.layout_error);
        mNoItemsMessage = mRootView.findViewById(R.id.no_items_message);
        mLayoutError.setVisibility(GONE);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mSwipeRefreshLayout.setRefreshing(true);
                init(mCategorySlug, true);
            }
        });
        mSwipeRefreshLayout.setRefreshing(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        ((MainActivity) getActivity()).toolbar.findViewById(R.id.title).setVisibility(View.GONE);
        ((MainActivity) getActivity()).toolbar.findViewById(R.id.spinner).setVisibility(View.VISIBLE);
        init(C.DEFOULT_CATEGORY, false);
        return mRootView;
    }
    private Api mApi;
    private Retrofit mRetrofit;
    private void init(String catehorySlug, boolean reloadForce){
        mRecyclerView.setAdapter(null);
        mRetrofit = new Retrofit.Builder()
                .baseUrl(C.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        mApi = mRetrofit.create(Api.class);
        if(mTopics == null || reloadForce)
            loadCatigories();
        else
            topicsWasLoaded(mTopics);
        if(mPosts == null || reloadForce)
            loadPost(catehorySlug);
        else
            postWasLoaded(mPosts);
    }
    private static List<Posts.Post> mPosts;
    private void postWasLoaded(List<Posts.Post> posts){
        mPosts = posts;
        mRecycleViewAdapter =  new RecycleViewAdapter((AppCompatActivity) getActivity(),posts);
        mRecyclerView.setAdapter(mRecycleViewAdapter);
        mSwipeRefreshLayout.setRefreshing(false);
        mLayoutError.setVisibility(GONE);

        if(posts.size() == 0)
            mNoItemsMessage.setVisibility(View.VISIBLE);
        else
            mNoItemsMessage.setVisibility(GONE);

    }
    private static List<Topics.Topic> mTopics;
    private void topicsWasLoaded(List<Topics.Topic> topics){
        mTopics = topics;
        String name = "";
        final List<String> topicStrings = new ArrayList<>();
        final List<String> topicSlugs = new ArrayList<>();
        for(Topics.Topic topic: topics){
            if(topic.slug.equals(mCategorySlug))
                name = topic.name;
            topicStrings.add(topic.name);
            topicSlugs.add(topic.slug);
        }
        final int pos = topicStrings.indexOf(name);
        final Spinner spinner = ((Spinner)((MainActivity) getActivity()).toolbar.findViewById(R.id.spinner));
        spinner.setAdapter(new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_dropdown_item, topicStrings));
        spinner.setSelection(pos);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(position == pos)
                    return;
                spinner.setOnItemSelectedListener(null);
                mSwipeRefreshLayout.setRefreshing(true);

                init(topicSlugs.get(position), true);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }
    static private String mCategorySlug = C.DEFOULT_CATEGORY;
    private void loadPost(String slugOfCategory){
        mCategorySlug = slugOfCategory;
        final Call<Posts> responce = mApi.getThrendingTopics(slugOfCategory, C.TOKEN);
        responce.enqueue(new Callback<Posts>() {
            @Override
            public void onResponse(Call<Posts> call, Response<Posts> response) {
                postWasLoaded(response.body().posts);
            }

            @Override
            public void onFailure(Call<Posts> call, Throwable t) {
                Log.e("MY","ERROR " +  t.getMessage());
                mLayoutError.setVisibility(View.VISIBLE);
                mHandler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        loadPost(mCategorySlug);
                    }
                }, 250);
            }
        });
    }
    private void loadCatigories(){
        Call<Topics> responceCall = mApi.getTopics(true,C.TOKEN);
        Log.e("MY", "CALL");

        responceCall.enqueue(new Callback<Topics>() {
            @Override
            public void onResponse(Call<Topics> call, Response<Topics> response) {
                Log.e("MY", "OK" + response.raw().toString());
                topicsWasLoaded(response.body().topics);
            }

            @Override
            public void onFailure(Call<Topics> call, Throwable t) {
                Log.e("MY","ERROR " +  t.getMessage());
                mHandler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        loadCatigories();
                    }
                }, 250);
            }
        });
    }
}
