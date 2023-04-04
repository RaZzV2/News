package com.example.news.api;

import com.example.news.models.CountryCountModel.CountryRequestBody;
import com.example.news.models.CountryCountModel.CountryResult;
import com.example.news.models.NewsModel.News;
import com.example.news.models.SearchByImageModel.ImageKnn;
import com.example.news.models.SearchByImageModel.ImageQuery;
import com.example.news.models.SearchHistoryModel.SearchHistory;
import com.example.news.models.SearchLogModel.SearchLog;

import org.json.JSONObject;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface ApiInterface {

    @GET("news_articles/_search")
    Call<News> getNews(
            @Query("q") String country
    );

    @POST("news_articles/_search?size=0")
    Call<CountryResult> getCountryResult(@Body CountryRequestBody body);

    @POST("search_history/_doc")
    Call<JSONObject> logSearch(@Body SearchLog searchLog);

    @POST("search_history/_search")
    Call<SearchHistory> getSearchHistory(
            @Body CountryRequestBody body,
            @Query("q") String query,
            @Query("size") int size);

    @POST("news_articles/_search")
    Call<News> knnSearch(@Body ImageQuery body);

}
