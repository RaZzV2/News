package com.example.news.models.NewsCountModel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Range {
    @SerializedName("date")
    @Expose
    private DateRange date;

    public Range(DateRange date) {
        this.date = date;
    }

    public DateRange getDate() {
        return date;
    }

    public void setDate(DateRange date) {
        this.date = date;
    }
}
