package com.example.news;

import com.example.news.models.CountryCountModel.CountryRequestBody;
import com.example.news.models.CountryCountModel.CountryResult;
import com.example.news.models.NewsModel.News;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface ApiInterface {

    @GET("news_articles/_search")
    Call<News> getNews (
            @Query("q") String country
    );

    @POST("news_articles/_search?size=0")
    Call<CountryResult> getCountryResult(@Body CountryRequestBody body);

}
