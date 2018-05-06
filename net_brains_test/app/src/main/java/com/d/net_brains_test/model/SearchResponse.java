package com.d.net_brains_test.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Алексей on 06.05.2018.
 */

public class SearchResponse implements Serializable{
    @SerializedName("meta")
    @Expose
    public Meta meta;

    @SerializedName("search-results")
    @Expose
    public List<Result> search_results;

    public class Meta implements Serializable{
        @SerializedName("page")
        @Expose
        public int page;

        @SerializedName("has_next")
        @Expose
        public boolean has_next;

        @SerializedName("has_previous")
        @Expose
        public boolean has_previous;
    }

    public class Result implements Serializable{

        @SerializedName("id")
        @Expose
        public int id;

        @SerializedName("position")
        @Expose
        public int position;

        @SerializedName("score")
        @Expose
        public float score;

        @SerializedName("target_id")
        @Expose
        public int target_id;

        @SerializedName("target_type")
        @Expose
        public String target_type;

        @SerializedName("course")
        @Expose
        public int course;

        @SerializedName("course_owner")
        @Expose
        public int course_owner;

        @SerializedName("course_title")
        @Expose
        public String course_title;

        @SerializedName("course_slug")
        @Expose
        public String course_slug;

        @SerializedName("course_cover")
        @Expose
        public String course_cover;

        @SerializedName("course_authors")
        @Expose
        public List<Integer> course_authors;

    }
}
