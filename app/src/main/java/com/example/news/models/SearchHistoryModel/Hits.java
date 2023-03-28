package com.example.news.models.SearchHistoryModel;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Hits {
    @SerializedName("hits")
    private List<Hit> hitList;

    public List<Hit> getHitList() {
        return hitList;
    }

    public void setHitList(List<Hit> hitList) {
        this.hitList = hitList;
    }
}
