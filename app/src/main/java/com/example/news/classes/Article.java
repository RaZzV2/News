package com.example.news.classes;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Article {

     @SerializedName("source")
     @Expose
     Source source;

     @SerializedName("title")
     @Expose
     String title;

     @SerializedName("author")
     @Expose
     String author;

     @SerializedName("description")
     @Expose
     String description;


     @SerializedName("url")
     @Expose
     String url;

     @SerializedName("urlToImage")
     @Expose
     String urlToImage;

     @SerializedName("publishedAt")
     @Expose
     String publishedAt;

     public Source getSource() {
          return source;
     }

     public void setSource(Source source) {
          this.source = source;
     }

     public String getTitle() {
          return title;
     }

     public void setTitle(String title) {
          this.title = title;
     }

     public String getAuthor() {
          return author;
     }

     public void setAuthor(String author) {
          this.author = author;
     }

     public String getDescription() {
          return description;
     }

     public void setDescription(String description) {
          this.description = description;
     }

     public String getUrl() {
          return url;
     }

     public void setUrl(String url) {
          this.url = url;
     }

     public String getUrlToImage() {
          return urlToImage;
     }

     public void setUrlToImage(String urlToImage) {
          this.urlToImage = urlToImage;
     }

     public String getPublishedAt() {
          return publishedAt;
     }

     public void setPublishedAt(String publishedAt) {
          this.publishedAt = publishedAt;
     }
}
