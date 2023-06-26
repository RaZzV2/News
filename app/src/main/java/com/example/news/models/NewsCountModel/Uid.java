package com.example.news.models.NewsCountModel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Uid {
    @SerializedName("uid")
    @Expose
    private String keyword;

    public Uid(String keyword) {
        this.keyword = keyword;
    }

    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }
}
