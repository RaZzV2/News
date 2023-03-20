package com.example.news.activities;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.news.Adapter;
import com.example.news.ApiClient;
import com.example.news.ApiInterface;
import com.example.news.R;
import com.example.news.Utils;
import com.example.news.models.Article;
import com.example.news.models.News;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.logging.Logger;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SearchActivity extends AppCompatActivity {

    public static final String API_KEY = "b6f70243230a4674a4df8612f71fa0e8";
    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    private List<Article> articleList = new ArrayList<>();
    Adapter adapter;

    ImageView back;

    EditText searchBar;

    public void loadJson() {
        ApiInterface apiInterface = ApiClient.getApiClient().create(ApiInterface.class);
        Call<News> call = apiInterface.getNews("title:" + searchBar.getText().toString());

        call.enqueue(new Callback<News>() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onResponse(@NonNull Call<News> call, @NonNull Response<News> response) {
                if(response.isSuccessful() && Objects.requireNonNull(response.body()).getHits() != null) {
                    if(!articleList.isEmpty()){
                        articleList.clear();
                    }
                    articleList = response.body().getHits().getArticleList();
                    adapter = new Adapter(articleList, SearchActivity.this);
                    recyclerView.setAdapter(adapter);
                    adapter.notifyDataSetChanged();
                }
                else {
                    Toast.makeText(SearchActivity.this, "No result!", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<News> call, @NonNull Throwable t) {

            }
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        recyclerView = findViewById(R.id.recyclerView);
        layoutManager = new LinearLayoutManager(SearchActivity.this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setNestedScrollingEnabled(false);


        back = findViewById(R.id.back);
        searchBar = findViewById(R.id.searchBar);
        searchBar.requestFocus();

        searchBar.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                loadJson();
                return true;
            }
            return false;
        });

        back.setOnClickListener(v -> {
            Intent intent = new Intent(SearchActivity.this, HomeActivity.class);
            startActivity(intent);
        });
    }
}