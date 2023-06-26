package com.example.news.models.NewsCountModel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class TermElement implements MustElement {
    @SerializedName("term")
    @Expose
    Term term;

    public TermElement(Term term) {
        this.term = term;
    }
}
