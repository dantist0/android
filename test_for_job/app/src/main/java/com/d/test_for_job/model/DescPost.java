package com.d.test_for_job.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Алексей on 09.04.2018.
 */

public class DescPost {
    @SerializedName("post")
    @Expose
    public Posts.Post post;
}
