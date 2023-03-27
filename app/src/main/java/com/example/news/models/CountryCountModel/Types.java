package com.example.news.models.CountryCountModel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Types {
    @SerializedName("buckets")
    @Expose
    private List<Bucket> buckets;

    public List<Bucket> getBuckets() {
        return buckets;
    }

    public void setBuckets(List<Bucket> buckets) {
        this.buckets = buckets;
    }
}
