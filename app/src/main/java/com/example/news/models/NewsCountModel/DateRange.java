package com.example.news.models.NewsCountModel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class DateRange {
    @SerializedName("gte")
    @Expose
    private String greaterThanEqual;
    @SerializedName("lte")
    @Expose
    private String lessThanEqual;

    public DateRange(String greaterThanEqual, String lessThanEqual) {
        this.greaterThanEqual = greaterThanEqual;
        this.lessThanEqual = lessThanEqual;
    }

    public String getGreaterThanEqual() {
        return greaterThanEqual;
    }

    public void setGreaterThanEqual(String greaterThanEqual) {
        this.greaterThanEqual = greaterThanEqual;
    }

    public String getLessThanEqual() {
        return lessThanEqual;
    }

    public void setLessThanEqual(String lessThanEqual) {
        this.lessThanEqual = lessThanEqual;
    }
}
