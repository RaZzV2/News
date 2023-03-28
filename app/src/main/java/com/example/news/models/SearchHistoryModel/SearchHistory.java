package com.example.news.models.SearchHistoryModel;

import com.example.news.models.Aggregations;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class SearchHistory {
    @SerializedName("hits")
    @Expose
    private Hits hits;

    public Hits getHits() {
        return hits;
    }

    public void setHits(Hits hits) {
        this.hits = hits;
    }

    @SerializedName("aggregations")
    @Expose
    private Aggregations aggregations;

    public Aggregations getAggregations() {
        return aggregations;
    }

    public void setAggregations(Aggregations aggregations) {
        this.aggregations = aggregations;
    }
}
