package com.example.news.models.NewsModel;

import com.example.news.classes.Source;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Article {

     @SerializedName("_source")
     @Expose
     Source source;

     public Source getSource() {
          return source;
     }

     public void setSource(Source source) {
          this.source = source;
     }
}
