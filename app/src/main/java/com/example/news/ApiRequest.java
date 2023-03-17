package com.example.news;

import java.io.IOException;
import java.util.Objects;

import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class ApiRequest {
    String endpoint = "http://localhost:9200";
    String indexName = "news_articles";

    OkHttpClient client = new OkHttpClient();
    HttpUrl url = Objects.requireNonNull(HttpUrl.parse(endpoint)).newBuilder()
            .addPathSegment(indexName)
            .build();
    Request request = new Request.Builder()
            .url(url).build();

    Response response = client.newCall(request).execute();

    public ApiRequest() throws IOException {
    }
}
