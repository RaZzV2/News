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
import com.example.news.datastream.SendTitleToServerAsyncTask;
import com.example.news.firebasemanager.RealtimeDatabaseManager;
import com.example.news.interfaces.OnItemClickListener;
import com.example.news.interfaces.OnTaskCompleteListener;
import com.example.news.models.NewsModel.Article;
import com.example.news.models.NewsModel.News;
import com.example.news.models.SearchByImageModel.ImageQuery;
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
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SearchActivity extends AppCompatActivity implements OnItemClickListener {

    RecyclerView recyclerView;

    int currentPage = 0;

    int size = 5;

    RealtimeDatabaseManager realtimeDatabaseManager;
    LinearLayoutManager layoutManager;

    private List<Article> articleList = new ArrayList<>();

    HashSet<String> stopWords;

    SearchAdapter searchAdapter;

    boolean isLoading = false;

    Executor executor;

    ImageView back;

    SearchView searchBar;

    public void loadNews(String title, int from, int size) {
        SendTitleToServerAsyncTask sendTitleToServerAsyncTask = new SendTitleToServerAsyncTask();
        sendTitleToServerAsyncTask.setSize(size);
        sendTitleToServerAsyncTask.setFrom(from * size);
        sendTitleToServerAsyncTask.execute(title);
        sendTitleToServerAsyncTask.setOnTaskCompletedListener(new OnTaskCompleteListener.OnTaskCompletedListener<ImageQuery>() {
            @Override
            public void onTaskCompleted(ImageQuery result) {
                ApiInterface apiInterface = ApiClient.getApiClient().create(ApiInterface.class);
                Call<News> call = apiInterface.knnSearch(result);

                call.enqueue(new Callback<News>() {
                    @SuppressLint("NotifyDataSetChanged")
                    @Override
                    public void onResponse(@NonNull Call<News> call, @NonNull Response<News> response) {
                        if (response.isSuccessful() && Objects.requireNonNull(response.body()).getHits() != null) {
                            articleList.addAll(response.body().getHits().getArticleList());
                            searchAdapter.notifyDataSetChanged();
                            currentPage++;
                        }
                        isLoading = false;
                    }

                    @Override
                    public void onFailure(@NonNull Call<News> call, @NonNull Throwable t) {
                        isLoading = false;
                    }
                });
            }

            @Override
            public void onError(Exception e) {
                isLoading = false;
            }
        });
    }

    void logSearch(String title) {
        realtimeDatabaseManager = new RealtimeDatabaseManager();
        String[] words = title.split(" ");

        for (String word : words) {
            if (!stopWords.contains(word)) {
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

    void logInput(String title) {
        RealtimeDatabaseManager realtimeDatabaseManager = new RealtimeDatabaseManager();
        ApiInterface apiInterface = ApiClient.getApiClient().create(ApiInterface.class);
        SearchLog searchLog = new SearchLog();
        searchLog.setUid(realtimeDatabaseManager.getCurrentUserUid());
        searchLog.setQuery(title);

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ", Locale.UK);
        String formattedDate = sdf.format(Calendar.getInstance().getTime());

        searchLog.setDate(formattedDate);
        Call<JSONObject> call = apiInterface.logInput(searchLog);

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



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        executor = Executors.newSingleThreadExecutor();
        recyclerView = findViewById(R.id.recyclerView);
        layoutManager = new LinearLayoutManager(SearchActivity.this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setNestedScrollingEnabled(false);
        searchBar = findViewById(R.id.searchBar);
        searchAdapter = new SearchAdapter(articleList, SearchActivity.this, SearchActivity.this);
        recyclerView.setAdapter(searchAdapter);

        searchBar.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String newText) {
                if (!isLoading) {
                    currentPage = 0;
                    articleList.clear();
                    loadNews(newText, currentPage, size);
                    logInput(newText);
                }
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });


        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                layoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
                assert layoutManager != null;
                int visibleItemCount = layoutManager.getChildCount();
                int totalItemCount = layoutManager.getItemCount();
                int firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition();

                if (!isLoading && (visibleItemCount + firstVisibleItemPosition) >= totalItemCount
                        && firstVisibleItemPosition >= 0) {
                    isLoading = true;
                    loadNews(searchBar.getQuery().toString(), currentPage, size);
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

    @Override
    public void onItemClick(int position) {
        String url = articleList.get(position).getSource().getUrl();
        String titleContent = articleList.get(position).getSource().getTitle();
        logSearch(titleContent);
        Intent intent = new Intent(getApplicationContext(), WebViewActivity.class);
        intent.putExtra("url", url);
        startActivity(intent);
    }
}