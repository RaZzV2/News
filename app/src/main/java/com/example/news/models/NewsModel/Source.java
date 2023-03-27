package com.example.news.models.NewsModel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Source {

    @SerializedName("publishedAt")
    @Expose
    String publishedAt;

    @SerializedName("author")
    @Expose
    String author;

    @SerializedName("description")
    @Expose
    String description;

    @SerializedName("urlToImage")
    @Expose
    String urlToImage;

    @SerializedName("source")
    SourceSite sourceSite;

    @SerializedName("country")
    String country;

    public String getPublishedAt() {
        return publishedAt;
    }

    public void setPublishedAt(String publishedAt) {
        this.publishedAt = publishedAt;
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

    public String getUrlToImage() {
        return urlToImage;
    }

    public void setUrlToImage(String urlToImage) {
        this.urlToImage = urlToImage;
    }

    public SourceSite getSourceSite() {
        return sourceSite;
    }

    public void setSourceSite(SourceSite sourceSite) {
        this.sourceSite = sourceSite;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }
}
