package com.example.news.models.CountryCountModel;

import com.example.news.models.Aggregations;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class CountryResult {
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
