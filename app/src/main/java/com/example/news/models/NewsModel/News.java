package com.example.news.models.NewsModel;

import com.example.news.models.NewsModel.Hits;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class News {

    @SerializedName("hits")
    @Expose
    Hits hits;

    public Hits getHits() {
        return hits;
    }

    public void setHits(Hits hits) {
        this.hits = hits;
    }
}
