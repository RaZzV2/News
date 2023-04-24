package com.example.news.activities;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.news.R;
import com.example.news.adapters.SearchAdapter;
import com.example.news.api.ApiClient;
import com.example.news.api.ApiInterface;
import com.example.news.interfaces.OnItemClickListener;
import com.example.news.models.NewsModel.Article;
import com.example.news.models.NewsModel.News;
import com.example.news.models.SearchByImageModel.ImageQuery;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;

public class SearchByImageActivity extends AppCompatActivity implements OnItemClickListener {

    RecyclerView recyclerView;

    ImageQuery imageQuery;

    RecyclerView.LayoutManager layoutManager;

    SearchAdapter searchAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_by_image);

        recyclerView = findViewById(R.id.searchByImageView);
        layoutManager = new LinearLayoutManager(SearchByImageActivity.this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setNestedScrollingEnabled(false);
        imageQuery = getIntent().getParcelableExtra("imageQuery");

        ApiInterface apiInterface = ApiClient.getApiClient().create(ApiInterface.class);
        Call<News> call = apiInterface.knnSearch(imageQuery);
        call.enqueue(new Callback<News>() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onResponse(@NonNull Call<News> call, @NonNull retrofit2.Response<News> response) {
                if(response.isSuccessful()) {
                    assert response.body() != null;
                    List<Article> articleList = response.body().getHits().getArticleList();
                    searchAdapter = new SearchAdapter(articleList, SearchByImageActivity.this, SearchByImageActivity.this);
                    recyclerView.setAdapter(searchAdapter);
                    searchAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onFailure(@NonNull Call<News> call, @NonNull Throwable t) {

            }
        });
    }

    @Override
    public void onItemClick(int position) {
        TextView title = findViewById(R.id.titleItem);
        String url = title.getTag().toString();
        //String titleContent = title.getText().toString();
        Intent intent = new Intent(getApplicationContext(), WebViewActivity.class);
        intent.putExtra("url", url);
        startActivity(intent);
    }
}