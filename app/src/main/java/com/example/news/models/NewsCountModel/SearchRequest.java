package com.example.news.models.NewsCountModel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class SearchRequest {
    @SerializedName("query")
    @Expose
    private Query query;
    @Expose
    @SerializedName("size")
    private int size;
    @Expose
    @SerializedName("aggs")
    private Aggs aggs;

    public SearchRequest(Query query, int size, Aggs aggs) {
        this.query = query;
        this.size = size;
        this.aggs = aggs;
    }

    public Query getQuery() {
        return query;
    }

    public void setQuery(Query query) {
        this.query = query;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public Aggs getAggs() {
        return aggs;
    }

    public void setAggs(Aggs aggs) {
        this.aggs = aggs;
    }
}
