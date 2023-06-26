package com.example.news.models.NewsCountModel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.HashMap;

public class DateHistogram {
    @SerializedName("field")
    @Expose
    private String field;
    @SerializedName("calendar_interval")
    @Expose
    private String calendarInterval;

    public DateHistogram(String field, String calendarInterval) {
        this.field = field;
        this.calendarInterval = calendarInterval;
    }

    public String getField() {
        return field;
    }

    public void setField(String field) {
        this.field = field;
    }

    public String getCalendarInterval() {
        return calendarInterval;
    }

    public void setCalendarInterval(String calendarInterval) {
        this.calendarInterval = calendarInterval;
    }
}
