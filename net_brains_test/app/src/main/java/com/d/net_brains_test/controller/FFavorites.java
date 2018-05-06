package com.d.net_brains_test.controller;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.d.net_brains_test.R;
import com.d.net_brains_test.controller.search.CourseSaver;
import com.d.net_brains_test.model.SearchResponse;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Алексей on 06.05.2018.
 */

public class FFavorites extends Fragment {
    private View mRootView;
    private RecyclerView mRecycleView;
    private TextView mNoCourseText;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mRootView = inflater.inflate(R.layout.favorites, null);
        mRecycleView = mRootView.findViewById(R.id.recycle_view);
        mNoCourseText = mRootView.findViewById(R.id.no_course_text);

        final List<SearchResponse.Result> courses  = new ArrayList<>();

        mRecycleView.setLayoutManager(new LinearLayoutManager(getContext()));
        CourseSaver.setChangeListener(new Runnable() {
            @Override
            public void run() {
                courses.clear();
                courses.addAll(CourseSaver.load(getContext()));
                if(courses.size() >0)
                    mNoCourseText.setVisibility(View.INVISIBLE);
                else
                    mNoCourseText.setVisibility(View.VISIBLE);
                mRecycleView.setAdapter(new FavoritesRecycleViewAdapter(getContext(), courses));
            }
        });

        return mRootView;
    }
}
