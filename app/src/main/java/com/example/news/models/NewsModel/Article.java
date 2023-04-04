package com.example.news.models.NewsModel;

import com.example.news.classes.Source;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Article {

     @SerializedName("_source")
     @Expose
     Source source;

     @SerializedName("_score")
     @Expose
     double score;

     public Source getSource() {
          return source;
     }

     public void setSource(Source source) {
          this.source = source;
     }

     public double getScore() {
          return score;
     }

     public void setScore(double score) {
          this.score = score;
     }
}
