package com.example.news.models.NewsCountModel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class SearchesPerDay {
    @SerializedName("date_histogram")
    @Expose
    private DateHistogram dateHistogram;

    public SearchesPerDay(DateHistogram dateHistogram) {
        this.dateHistogram = dateHistogram;
    }

    public DateHistogram getDateHistogram() {
        return dateHistogram;
    }

    public void setDateHistogram(DateHistogram dateHistogram) {
        this.dateHistogram = dateHistogram;
    }
}
