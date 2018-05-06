package com.d.net_brains_test.controller;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.d.net_brains_test.R;
import com.d.net_brains_test.controller.search.CourseSaver;
import com.d.net_brains_test.model.SearchResponse;

import java.util.List;

/**
 * Created by Алексей on 06.05.2018.
 */

public class FavoritesRecycleViewAdapter extends SearchRecycleViewAdapter {
    public FavoritesRecycleViewAdapter(Context context, List<SearchResponse.Result> cources) {
        super(context, cources);
    }
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.favorite_item, parent, false);
        return new CardViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        final SearchResponse.Result result = mCources.get(position);
        CardViewHolder cardViewHolder = (CardViewHolder) holder;
        cardViewHolder.title.setText(result.course_title);
        Glide.with(mContext)
                .load(result.course_cover)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(cardViewHolder.image);
        cardViewHolder.button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CourseSaver.delete(mContext, result);
                Toast.makeText(mContext, R.string.delete_from_favorites, Toast.LENGTH_SHORT).show();
            }
        });
    }

}

