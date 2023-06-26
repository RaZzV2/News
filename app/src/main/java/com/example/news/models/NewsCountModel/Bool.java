package com.example.news.models.NewsCountModel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Bool {
    @SerializedName("must")
    @Expose
    private List<MustElement> must;

    public Bool(List<MustElement> must) {
        this.must = must;
    }



    public List<MustElement> getMust() {
        return must;
    }

    public void setMust(List<MustElement> must) {
        this.must = must;
    }
}
