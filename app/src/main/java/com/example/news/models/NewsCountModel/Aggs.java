package com.example.news.models.NewsCountModel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Aggs {
    @SerializedName("searches_per_day")
    @Expose
    private SearchesPerDay searchesPerDay;

    public Aggs(SearchesPerDay searchesPerDay) {
        this.searchesPerDay = searchesPerDay;
    }

    public SearchesPerDay getSearchesPerDay() {
        return searchesPerDay;
    }

    public void setSearchesPerDay(SearchesPerDay searchesPerDay) {
        this.searchesPerDay = searchesPerDay;
    }
}
