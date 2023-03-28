package com.example.news.activities;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.news.adapters.SearchAdapter;
import com.example.news.api.ApiClient;
import com.example.news.api.ApiInterface;
import com.example.news.R;
import com.example.news.firebasemanager.RealtimeDatabaseManager;
import com.example.news.models.NewsModel.Article;
import com.example.news.models.NewsModel.News;
import com.example.news.models.SearchLogModel.SearchLog;

import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SearchActivity extends AppCompatActivity {

    RecyclerView recyclerView;

    RealtimeDatabaseManager realtimeDatabaseManager;
    RecyclerView.LayoutManager layoutManager;
    private List<Article> articleList = new ArrayList<>();

    HashSet<String> stopWords;
    SearchAdapter searchAdapter;

    ImageView back;

    EditText searchBar;

    public void loadJson() {
        ApiInterface apiInterface = ApiClient.getApiClient().create(ApiInterface.class);
        Call<News> call = apiInterface.getNews("title:" + searchBar.getText().toString());

        call.enqueue(new Callback<News>() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onResponse(@NonNull Call<News> call, @NonNull Response<News> response) {
                if (response.isSuccessful() && Objects.requireNonNull(response.body()).getHits() != null) {
                    if (!articleList.isEmpty()) {
                        articleList.clear();
                    }
                    articleList = response.body().getHits().getArticleList();
                    searchAdapter = new SearchAdapter(articleList, SearchActivity.this);
                    recyclerView.setAdapter(searchAdapter);
                    searchAdapter.notifyDataSetChanged();
                } else {
                    Toast.makeText(SearchActivity.this, "No result!", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<News> call, @NonNull Throwable t) {

            }
        });
    }

    void logSearch() {
        realtimeDatabaseManager = new RealtimeDatabaseManager();
        String[] words = searchBar.getText().toString().split(" ");

        for(String word :words) {
            if(!stopWords.contains(word)) {
                ApiInterface apiInterface = ApiClient.getApiClient().create(ApiInterface.class);
                SearchLog searchLog = new SearchLog();
                searchLog.setUid(realtimeDatabaseManager.getCurrentUserUid());
                searchLog.setQuery(word);

                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ", Locale.UK);
                String formattedDate = sdf.format(Calendar.getInstance().getTime());

                searchLog.setDate(formattedDate);
                Call<JSONObject> call = apiInterface.logSearch(searchLog);

                call.enqueue(new Callback<JSONObject>() {
                    @SuppressLint("NotifyDataSetChanged")
                    @Override
                    public void onResponse(@NonNull Call<JSONObject> call, @NonNull Response<JSONObject> response) {
                    }

                    @Override
                    public void onFailure(@NonNull Call<JSONObject> call, @NonNull Throwable t) {

                    }
                });
            }
        }
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


        stopWords = new HashSet<>(Arrays.asList(getResources().getStringArray(R.array.stop_words)));
        back = findViewById(R.id.back);
        searchBar = findViewById(R.id.searchBar);
        searchBar.requestFocus();

        searchBar.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                loadJson();
                logSearch();
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