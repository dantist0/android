package com.d.net_brains_test.controller;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.d.net_brains_test.R;
import com.d.net_brains_test.controller.search.CourseSaver;
import com.d.net_brains_test.controller.search.FSearch;
import com.d.net_brains_test.model.SearchResponse;

import java.util.List;

/**
 * Created by Алексей on 06.05.2018.
 */

public class SearchRecycleViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{
    protected Context mContext;
    protected List<SearchResponse.Result> mCources;
    public SearchRecycleViewAdapter(Context context, List<SearchResponse.Result> cources){
        mContext = context;
        mCources = cources;
    }
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.lesson_item, parent, false);
        return new CardViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        final SearchResponse.Result result = mCources.get(position);
        CardViewHolder cardViewHolder = (CardViewHolder)holder;
        cardViewHolder.title.setText(result.course_title);
        Glide.with(mContext)
                .load(result.course_cover)
                .into(cardViewHolder.image);
        cardViewHolder.button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CourseSaver.save(mContext, result);
                Toast.makeText(mContext, R.string.add_to_favorites, Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return mCources.size();
    }
    protected class CardViewHolder extends RecyclerView.ViewHolder{
        ImageView image;
        TextView title;
        Button button;
        public CardViewHolder(View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.image);
            title = itemView.findViewById(R.id.title);
            button = itemView.findViewById(R.id.button_to_favorite);
        }
    }
}

