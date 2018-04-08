package com.d.test_for_job.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.HashMap;
import java.util.List;

/**
 * Created by Алексей on 08.04.2018.
 */

public class Posts {
    @SerializedName("posts")
    @Expose
    public List<Post> posts;

    public class Post{
        @SerializedName("name")
        @Expose
        public String name;

        @SerializedName("slug")
        @Expose
        public String slug;


        @SerializedName("tagline")
        @Expose
        public String tagline;

        @SerializedName("votes_count")
        @Expose
        public int votesCount;

        @SerializedName("thumbnail")
        @Expose
        public Thumbnail thumbnail;

        @SerializedName("topics")
        @Expose
        public List<Topic> topic;

        @SerializedName("screenshot_url")
        @Expose
        public Screenshot screenshot;

        @SerializedName("description")
        @Expose
        public String description;

        public class Screenshot{
            @SerializedName("300px")
            @Expose
            public String url300px;

            @SerializedName("850px")
            @Expose
            public String url850px;
        }


        public class Thumbnail {
            @SerializedName("id")
            @Expose
            public int id;

            @SerializedName("image_url")
            @Expose
            public String imageUrl;

            @SerializedName("media_type")
            @Expose
            public String mediaType;
        }
        public class Topic{

            @SerializedName("id")
            @Expose
            public int id;

            @SerializedName("name")
            @Expose
            public String name;

            @SerializedName("slug")
            @Expose
            public String slug;
        }
    }

}
