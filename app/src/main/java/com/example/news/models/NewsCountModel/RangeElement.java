package com.example.news.models.NewsCountModel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class RangeElement implements MustElement {
    @SerializedName("range")
    @Expose
    Range range;

    public RangeElement(Range range) {
        this.range = range;
    }
}
