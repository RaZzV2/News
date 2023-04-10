package com.example.news.activities;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.news.R;
import com.example.news.adapters.SearchAdapter;
import com.example.news.api.ApiClient;
import com.example.news.api.ApiInterface;
import com.example.news.datastream.LoadMoreNewsAsyncTask;
import com.example.news.firebasemanager.RealtimeDatabaseManager;
import com.example.news.interfaces.LoaderCallbackInterface;
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

public class SearchActivity extends AppCompatActivity implements LoaderCallbackInterface {

    RecyclerView recyclerView;

    int currentPage = 0;

    int size = 25;

    RealtimeDatabaseManager realtimeDatabaseManager;
    LinearLayoutManager layoutManager;

    private List<Article> articleList = new ArrayList<>();

    HashSet<String> stopWords;

    SearchAdapter searchAdapter;

    ImageView back;

    SearchView searchBar;

    public void onLoadMoreComplete() {
        currentPage +=1;
    }

    @Override
    public void loadJson(String title, int from, int size) {
        ApiInterface apiInterface = ApiClient.getApiClient().create(ApiInterface.class);
        Call<News> call = apiInterface.getNews("title:" + title, from * size, size);

        call.enqueue(new Callback<News>() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onResponse(@NonNull Call<News> call, @NonNull Response<News> response) {
                if (response.isSuccessful() && Objects.requireNonNull(response.body()).getHits() != null) {
                    if (!articleList.isEmpty()) {
                        articleList.clear();
                    }
                    articleList = response.body().getHits().getArticleList();
                    searchAdapter = new SearchAdapter(articleList, SearchActivity.this, SearchActivity.this);
                    recyclerView.setAdapter(searchAdapter);
                    searchAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onFailure(@NonNull Call<News> call, @NonNull Throwable t) {

            }
        });
    }

    void logSearch(String title) {
        realtimeDatabaseManager = new RealtimeDatabaseManager();
        String[] words = title.split(" ");

        for(String word : words) {
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

//    void filterList(String name) {
//        filteredPlayers = new ArrayList<>();
//        for(Player player : players){
//            if(player.getName().toLowerCase().contains(name.toLowerCase())){
//                filteredPlayers.add(player);
//            }
//        }
//        playerAdapter.setFilteredList(filteredPlayers);
//    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        recyclerView = findViewById(R.id.recyclerView);
        layoutManager = new LinearLayoutManager(SearchActivity.this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setNestedScrollingEnabled(false);
        searchBar = findViewById(R.id.searchBar);

        loadJson("*", currentPage, size);

        searchBar.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                currentPage = 0;
                if (newText.equals(""))
                    newText = "*";
                loadJson(newText, currentPage, size);
                return true;
            }
        });


        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                layoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
                assert layoutManager != null;
                int lastVisibleItemPosition = layoutManager.findLastVisibleItemPosition();
                int totalItemCount = layoutManager.getItemCount();
                if (lastVisibleItemPosition == totalItemCount - 1 && articleList.size() == size) {
                    if (searchBar.getQuery().length() == 0) {
                        new LoadMoreNewsAsyncTask(SearchActivity.this, "*", currentPage, size).execute();
                        //loadJson("*", currentPage, size);
                    } else {
                        new LoadMoreNewsAsyncTask(SearchActivity.this, searchBar.getQuery().toString(), currentPage, size).execute();
                        //loadJson(searchBar.getQuery().toString(), currentPage, size);
                    }
                    //currentPage += 1;
                }
            }
        });


        stopWords = new HashSet<>(Arrays.asList(getResources().getStringArray(R.array.stop_words)));
        back = findViewById(R.id.back);
        searchBar = findViewById(R.id.searchBar);
        searchBar.requestFocus();

        back.setOnClickListener(v -> {
            Intent intent = new Intent(SearchActivity.this, HomeActivity.class);
            startActivity(intent);
        });
    }

//    @Override
//    public void logSearch(String title) {
//        realtimeDatabaseManager = new RealtimeDatabaseManager();
//        String[] words = title.split(" ");
//
//        for(String word : words) {
//            if(!stopWords.contains(word)) {
//                ApiInterface apiInterface = ApiClient.getApiClient().create(ApiInterface.class);
//                SearchLog searchLog = new SearchLog();
//                searchLog.setUid(realtimeDatabaseManager.getCurrentUserUid());
//                searchLog.setQuery(word);
//
//                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ", Locale.UK);
//                String formattedDate = sdf.format(Calendar.getInstance().getTime());
//
//                searchLog.setDate(formattedDate);
//                Call<JSONObject> call = apiInterface.logSearch(searchLog);
//
//                call.enqueue(new Callback<JSONObject>() {
//                    @SuppressLint("NotifyDataSetChanged")
//                    @Override
//                    public void onResponse(@NonNull Call<JSONObject> call, @NonNull Response<JSONObject> response) {
//                    }
//
//                    @Override
//                    public void onFailure(@NonNull Call<JSONObject> call, @NonNull Throwable t) {
//
//                    }
//                });
//            }
//        }
//    }
}