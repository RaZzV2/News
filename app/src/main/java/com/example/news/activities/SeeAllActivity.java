package com.example.news.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.bumptech.glide.request.RequestOptions;
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
import com.example.news.models.SearchQuery.SearchQuery;
import com.example.news.models.SearchQuery.SearchResponse;

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

public class SeeAllActivity extends AppCompatActivity implements OnItemClickListener {

    RecyclerView recyclerView;

    LinearLayoutManager layoutManager;

    boolean isLoading = false;

    HashSet<String> stopWords;

    RealtimeDatabaseManager realtimeDatabaseManager;

    SearchAdapter searchAdapter;

    List<Article> articleList = new ArrayList<>();

    int size = 3;

    int currentPage = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_see_all);
        recyclerView = findViewById(R.id.seeAllView);
        layoutManager = new LinearLayoutManager(SeeAllActivity.this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setNestedScrollingEnabled(false);
        stopWords = new HashSet<>(Arrays.asList(getResources().getStringArray(R.array.stop_words)));
        loadRecommendedNews();
        searchAdapter = new SearchAdapter(articleList, this, this);
        recyclerView.setAdapter(searchAdapter);

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
                    loadRecommendedNews();
                }
            }
        });
    }

    void loadRecommendedNews() {
        realtimeDatabaseManager = new RealtimeDatabaseManager();
        String uid = realtimeDatabaseManager.getCurrentUserUid();
        ApiInterface apiInterface = ApiClient.getApiClient().create(ApiInterface.class);
        SearchQuery searchQuery = new SearchQuery(size,
                new SearchQuery.Query(
                        new SearchQuery.Query.FunctionScore(
                                new SearchQuery.Query.FunctionScore.TermQuery(
                                        new SearchQuery.Query.FunctionScore.TermQuery.UidTerm(uid)
                                ),
                                new SearchQuery.Query.FunctionScore.RandomScore()
                        )
                )
        );
        Call<SearchResponse> call = apiInterface.getRandomDocument(searchQuery);
        call.enqueue(new Callback<SearchResponse>() {
            @Override
            public void onResponse(@NonNull Call<SearchResponse> call, @NonNull Response<SearchResponse> response) {
                if (response.isSuccessful()) {
                    SearchResponse searchResponse = response.body();
                    assert searchResponse != null;
                    for(int index=0;index<searchResponse.getHits().getHits().length;++index){
                        getRecommendedArticles(searchResponse.getHits().getHits()[index].getSource().getQuery(),currentPage,size);
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<SearchResponse> call, @NonNull Throwable t) {
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

    public void getRecommendedArticles(String title, int from, int size) {
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