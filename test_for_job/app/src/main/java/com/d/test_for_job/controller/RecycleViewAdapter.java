package com.d.test_for_job.controller;

import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.d.test_for_job.R;
import com.d.test_for_job.model.Posts;

import java.util.List;


/**
 * Created by Алексей on 08.04.2018.
 */

public class RecycleViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{
    private AppCompatActivity mActivity;
    List<Posts.Post> mPosts;
    public RecycleViewAdapter(AppCompatActivity activity, List<Posts.Post> posts){
        mActivity = activity;
        mPosts = posts;
    }
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_post, parent, false);
        return new ViewHolder(view);
    }


    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        final ViewHolder viewHolder = (ViewHolder)holder;
        final Posts.Post post = mPosts.get(position);
        viewHolder.describe.setText(post.tagline);
        viewHolder.title.setText(post.name);
        viewHolder.upvotes.setText(post.votesCount+"");
            Glide
                    .with(mActivity)
                    .load(post.thumbnail.imageUrl)
                    .error(R.drawable.error)
                    .into(viewHolder.img);
        viewHolder.rootView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FPost fPost = new FPost();
                fPost.setPost(post);
                mActivity.getSupportFragmentManager().beginTransaction()
                        .addToBackStack(null).replace(R.id.main_container,fPost)
                        .commitAllowingStateLoss();
            }
        });
    }

    @Override
    public int getItemCount() {
       // return vKparser.getPosts().size();
        return mPosts.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        public TextView title;
        public ImageView img;
        public View rootView;
        public TextView upvotes;
        public TextView describe;

        public ViewHolder(View itemView) {
            super(itemView);
            rootView = itemView;
            title = itemView.findViewById(R.id.item_title);
            img = itemView.findViewById(R.id.item_img);
            upvotes = itemView.findViewById(R.id.item_likes);
            describe = itemView.findViewById(R.id.describe);
        }
    }
}
