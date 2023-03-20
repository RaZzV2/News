package com.example.news;

import com.example.news.models.News;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface ApiInterface {

    @GET("news_articles/_search")
    Call<News> getNews (
            @Query("q") String country
    );


}
