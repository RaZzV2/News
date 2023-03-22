package com.example.news.classes;

import com.example.news.models.SourceSite;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Source {
    @Expose
    @SerializedName("country")
    String country;

    @Expose
    @SerializedName("publishedAt")
    String publishedAt;

    @Expose
    @SerializedName("author")
    String author;

    @Expose
    @SerializedName("urlToImage")
    String urlToImage;

    @Expose
    @SerializedName("description")
    String description;

    @Expose
    @SerializedName("source")
    SourceSite sourceSite;

    @SerializedName("title")
    @Expose
    String title;

    @SerializedName("category")
    @Expose
    String category;


    @SerializedName("url")
    @Expose
    String url;

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

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

    public String getUrlToImage() {
        return urlToImage;
    }

    public void setUrlToImage(String urlToImage) {
        this.urlToImage = urlToImage;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public SourceSite getSourceSite() {
        return sourceSite;
    }

    public void setSourceSite(SourceSite sourceSite) {
        this.sourceSite = sourceSite;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
