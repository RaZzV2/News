package com.example.news.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Total {

    @SerializedName("value")
    @Expose
    int value;

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }
}
