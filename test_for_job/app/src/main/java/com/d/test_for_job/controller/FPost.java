package com.d.test_for_job.controller;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.d.test_for_job.MainActivity;
import com.d.test_for_job.R;
import com.d.test_for_job.model.C;
import com.d.test_for_job.model.DescPost;
import com.d.test_for_job.model.Posts;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Алексей on 09.04.2018.
 */

public class FPost extends Fragment{
    private View mRootView;
    private ImageView mImage;
    private TextView mVotes;
    private TextView mDescribe;
    private Posts.Post mPost;
    private Handler mHandler;
    public void setPost(Posts.Post post){
        mPost = post;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mHandler = new Handler();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mRootView = inflater.inflate(R.layout.post, null);
        mImage = mRootView.findViewById(R.id.image);
        mVotes = mRootView.findViewById(R.id.item_upvotes);
        mDescribe = mRootView.findViewById(R.id.describe);
        ((MainActivity) getActivity()).toolbar.findViewById(R.id.title).setVisibility(View.VISIBLE);
        ((MainActivity) getActivity()).toolbar.findViewById(R.id.spinner).setVisibility(View.GONE);
        ((TextView)((MainActivity) getActivity()).toolbar.findViewById(R.id.title)).setText(mPost.name);
        Glide
             .with(getActivity())
             .load(mPost.screenshot.url850px)
                .listener(new RequestListener<String, GlideDrawable>() {
                    @Override
                    public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                        mRootView.findViewById(R.id.progress_bar).setVisibility(View.GONE);
                        return false;
                    }
                }).error(R.drawable.error)
             .into(mImage);
        mVotes.setText(mPost.votesCount+"");
        mRootView.findViewById(R.id.button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Intent.ACTION_VIEW);
                PackageManager pm = getActivity().getPackageManager();
                PackageInfo pi = null;
                try {
                    pi = pm.getPackageInfo("com.android.chrome", 0);
                } catch (PackageManager.NameNotFoundException e) {
                    e.printStackTrace();
                }
                if (pi != null) {
                    i.setPackage("com.android.chrome");
                }

                i.setData(Uri.parse( "https://www.producthunt.com/posts/" + mPost.slug));
                startActivity(i);
            }
        });
        if(mPost.description != null) {
            mDescribe.setText(mPost.description);
            mRootView.findViewById(R.id.progress_bar_text).setVisibility(View.GONE);
        }
        else {
            mDescribe.setText(mPost.tagline);
            loadPostText();
        }
        return mRootView;
    }
    private void loadPostText(){
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(C.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        Api api = retrofit.create(Api.class);
        final Call<DescPost> call = api.getPost(mPost.slug, C.TOKEN);
        call.enqueue(new Callback<DescPost>() {
            @Override
            public void onResponse(Call<DescPost> call, Response<DescPost> response) {
                Log.e("MY", "POST LOADED" + response.body().post.description);
                Log.e("MY", "POST LOADED " + response.raw());
                Log.e("MY", "POST LOADED " + response.body().post.name);

                mPost.description = response.body().post.description;
                if(getContext() != null)
                    mDescribe.setText(mPost.tagline + "\n\n" + mPost.description);
                mRootView.findViewById(R.id.progress_bar_text).setVisibility(View.GONE);
            }

            @Override
            public void onFailure(Call<DescPost> call, Throwable t) {
                Log.e("MY", "POST FAIL");
                mHandler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        loadPostText();
                    }
                }, 200);
            }
        });
    }
}
