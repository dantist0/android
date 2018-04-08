package com.d.test_for_job.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Алексей on 08.04.2018.
 */

public class Topics {
    @SerializedName("topics")
    @Expose
    public List<Topic> topics;


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

        @SerializedName("description")
        @Expose
        public String description;

    }


}
