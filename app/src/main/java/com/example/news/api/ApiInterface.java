package com.example.news.api;

import com.example.news.models.CountryCountModel.CountryRequestBody;
import com.example.news.models.CountryCountModel.CountryResult;
import com.example.news.models.NewsModel.News;
import com.example.news.models.SearchByImageModel.ImageQuery;
import com.example.news.models.SearchHistoryModel.SearchHistory;
import com.example.news.models.SearchLogModel.SearchLog;
import com.example.news.models.SearchQuery.SearchQuery;
import com.example.news.models.SearchQuery.SearchResponse;

import org.json.JSONObject;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface ApiInterface {

    @GET("news_articles/_search")
    Call<News> getNews(
            @Query("q") String country,
            @Query("from") int from,
            @Query("size") int size
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

    @POST("input_history/_doc")
    Call<JSONObject> logInput(@Body SearchLog searchLog);

    @POST("input_history/_search")
    Call<SearchResponse> getRandomDocument(@Body SearchQuery searchQuery);

}
