package com.example.news.models.NewsModel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class SourceSite {
    @Expose
    @SerializedName("id")
    String id;

    @Expose
    @SerializedName("name")
    String name;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
