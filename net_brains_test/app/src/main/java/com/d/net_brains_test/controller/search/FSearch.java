package com.d.net_brains_test.controller.search;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.support.v7.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.d.net_brains_test.R;
import com.d.net_brains_test.controller.Api;
import com.d.net_brains_test.controller.SearchRecycleViewAdapter;
import com.d.net_brains_test.model.Config;
import com.d.net_brains_test.model.SearchResponse;
import com.d.net_brains_test.model.SearchState;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;
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

public class FSearch extends Fragment {
    private View mRootView;
    private RecyclerView mRecycleView;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private View mLayoutError;
    private SearchView mSerachView;
    private SearchState mState = new SearchState();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mRootView = inflater.inflate(R.layout.list, null);
        mRecycleView = mRootView.findViewById(R.id.recycle_view);
        mSwipeRefreshLayout = mRootView.findViewById(R.id.swipe_refresh);
        mLayoutError = mRootView.findViewById(R.id.layout_error);
        mSerachView = mRootView.findViewById(R.id.search_view);

        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mSwipeRefreshLayout.setRefreshing(true);
                loadList(mSerachView.getQuery().toString(), 1);
            }
        });
        mRecycleView.setLayoutManager(new LinearLayoutManager(getContext()));
        if (savedInstanceState != null)
            mState = (SearchState) savedInstanceState.getSerializable("state");
        mRecycleView.setAdapter(new SearchRecycleViewAdapter(getContext(), mState.search_results));


        if (mState.error)
            mLayoutError.setVisibility(View.VISIBLE);
        else {
            mLayoutError.setVisibility(View.GONE);
        }
        if (savedInstanceState == null) {
            loadList("", 1);
        }
        if (mState.page <= 1)
            mSwipeRefreshLayout.setRefreshing(isLoading);
        mRecycleView.setOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                LinearLayoutManager linearLayoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
                int totalItemCount = linearLayoutManager.getItemCount();
                int lastVisibleItem = linearLayoutManager.findLastVisibleItemPosition();
                if (!isLoading && totalItemCount <= (lastVisibleItem + 4)) {
                    loadList("", mState.page + 1);
                }
            }
        });

        Caller.setListener(new Callback<SearchResponse>() {
            @Override
            public void onResponse(Call<SearchResponse> call, Response<SearchResponse> response) {
                isLoading = false;
                if(response.body() == null)
                    return;
                if (response.body().meta.page == 1)
                    mState.search_results.clear();

                List<SearchResponse.Result> results = response.body().search_results;

                mState.search_results.addAll(results);
                mState.page = response.body().meta.page;
                mRecycleView.getAdapter().notifyDataSetChanged();
                mSwipeRefreshLayout.setRefreshing(false);
                mLayoutError.setVisibility(View.GONE);
                mState.error = false;
            }

            @Override
            public void onFailure(Call<SearchResponse> call, Throwable t) {
                isLoading = false;
                mSwipeRefreshLayout.setRefreshing(false);
                if (mState.search_results.size() == 0) {
                    mLayoutError.setVisibility(View.VISIBLE);
                    mState.error = true;
                    ((TextView) mLayoutError.findViewById(R.id.text_error)).setText(t.getMessage());
                }
                if (getContext() != null)
                    Toast.makeText(getContext(), t.getMessage(), Toast.LENGTH_LONG).show();
                t.printStackTrace();

            }
        });

        mSerachView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                mSwipeRefreshLayout.setRefreshing(true);
                Caller.cancel();
                loadList(query, 1);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });

        return mRootView;
    }

    private static boolean isLoading;

    private void loadList(String text, int page) {
        isLoading = true;

        Caller.call(text, page);

    }

    @Override
    public void onDetach() {
        super.onDetach();
        Caller.setListener(null);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putSerializable("state", mState);
    }
}


