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
import com.example.news.datastream.SendImageToServerAsyncTask;
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

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SearchByImageActivity extends AppCompatActivity implements OnItemClickListener {

    RecyclerView recyclerView;

    ImageQuery imageQuery;

    HashSet<String> stopWords;

    RealtimeDatabaseManager realtimeDatabaseManager;

    int currentPage = 0;

    int size = 5;

    List<Article> articleList = new ArrayList<>();

    boolean isLoading = false;

    LinearLayoutManager layoutManager;

    SearchAdapter searchAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_by_image);
        stopWords = new HashSet<>(Arrays.asList(getResources().getStringArray(R.array.stop_words)));
        recyclerView = findViewById(R.id.searchByImageView);
        layoutManager = new LinearLayoutManager(SearchByImageActivity.this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setNestedScrollingEnabled(false);
        imageQuery = getIntent().getParcelableExtra("imageQuery");
        searchAdapter = new SearchAdapter(articleList, SearchByImageActivity.this, SearchByImageActivity.this);
        recyclerView.setAdapter(searchAdapter);
        loadNews(currentPage,size);


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
                    loadNews(currentPage, size);
                }
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


    public void loadNews(int from, int size) {
        ApiInterface apiInterface = ApiClient.getApiClient().create(ApiInterface.class);
        Call<News> call = apiInterface.knnSearch(new ImageQuery(imageQuery.getImageKnn(),from*size,size));
        call.enqueue(new Callback<News>() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onResponse(@NonNull Call<News> call, @NonNull retrofit2.Response<News> response) {
                if (response.isSuccessful()) {
                    assert response.body() != null;
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
    public void onItemClick(int position) {
        String url = articleList.get(position).getSource().getUrl();
        String titleContent = articleList.get(position).getSource().getTitle();
        logSearch(titleContent);
        Intent intent = new Intent(getApplicationContext(), WebViewActivity.class);
        intent.putExtra("url", url);
        startActivity(intent);
    }
}