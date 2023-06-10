package com.example.news.activities;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.bumptech.glide.request.RequestOptions;
import com.example.news.R;
import com.example.news.api.ApiClient;
import com.example.news.api.ApiInterface;
import com.example.news.datastream.SendTitleToServerAsyncTask;
import com.example.news.firebasemanager.RealtimeDatabaseManager;
import com.example.news.interfaces.OnTaskCompleteListener;
import com.example.news.models.NewsModel.Article;
import com.example.news.models.NewsModel.News;
import com.example.news.models.SearchByImageModel.ImageQuery;
import com.example.news.models.SearchQuery.SearchQuery;
import com.example.news.models.SearchQuery.SearchResponse;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeActivity extends AppCompatActivity {

    de.hdodenhof.circleimageview.CircleImageView shortCircleImageView1;
    TextView shortNewsTitle1;
    TextView shortNewsContent1;

    de.hdodenhof.circleimageview.CircleImageView shortCircleImageView2;
    TextView shortNewsTitle2;
    TextView shortNewsContent2;

    de.hdodenhof.circleimageview.CircleImageView shortCircleImageView3;
    TextView shortNewsTitle3;
    TextView shortNewsContent3;


    DrawerLayout drawerLayout;

    RealtimeDatabaseManager realtimeDatabaseManager;

    LinearLayout photoSearch;

    TextView seeAll;

    List<Article> articleList = new ArrayList<>();

    TextView searchBar;

    ImageView profilePicture;

    Toolbar toolbar;
    NavigationView navigationView;
    View headerView;
    TextView currentUsername;


    public void logOut() {
        realtimeDatabaseManager = new RealtimeDatabaseManager();
        realtimeDatabaseManager.logOut();
        Intent intent = new Intent(HomeActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    public void setCurrentUsername() {
        realtimeDatabaseManager = new RealtimeDatabaseManager();
        realtimeDatabaseManager.getUsersReference().child(realtimeDatabaseManager.getCurrentUserUid()).child("username").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    String username = snapshot.getValue(String.class);
                    currentUsername.setText(username);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }

        });
    }

    protected void onResume() {
        super.onResume();
        realtimeDatabaseManager = new RealtimeDatabaseManager();
        if (realtimeDatabaseManager.getCurrentUser() == null) {
            realtimeDatabaseManager.logOut();
        }
        loadRecommendedNews();
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        searchBar = findViewById(R.id.searchBar);
        toolbar = findViewById(R.id.toolbar);
        drawerLayout = findViewById(R.id.drawerLayout);
        navigationView = findViewById(R.id.navigationView);
        headerView = navigationView.getHeaderView(0);
        currentUsername = headerView.findViewById(R.id.usernameNavigation);
        profilePicture = headerView.findViewById(R.id.imageProfile);
        photoSearch = findViewById(R.id.photoSearch);

        shortCircleImageView2 = findViewById(R.id.shortNewsImageView2);
        shortNewsTitle2 = findViewById(R.id.shortNewsTitle2);
        shortNewsContent2 = findViewById(R.id.shortNewsContent2);

        shortCircleImageView3 = findViewById(R.id.shortNewsImageView3);
        shortNewsTitle3 = findViewById(R.id.shortNewsTitle3);
        shortNewsContent3 = findViewById(R.id.shortNewsContent3);

        shortCircleImageView1 = findViewById(R.id.shortNewsImageView1);
        shortNewsTitle1 = findViewById(R.id.shortNewsTitle1);
        shortNewsContent1 = findViewById(R.id.shortNewsContent1);

        seeAll = findViewById(R.id.seeAll);

        seeAll.setOnClickListener(v -> {
            Intent intent = new Intent(HomeActivity.this, SeeAllActivity.class);
            startActivity(intent);
        });

        findViewById(R.id.menu).setOnClickListener(v -> drawerLayout.openDrawer(GravityCompat.START));

        searchBar.setOnClickListener(v -> startActivity(new Intent(HomeActivity.this, SearchActivity.class)));

        setCurrentUsername();
        loadRecommendedNews();

        navigationView.setNavigationItemSelectedListener(item -> {
            Intent intent;
            switch (item.getItemId()) {
                case R.id.menuLogout:
                    logOut();
                    break;

                case R.id.menuProfile:
                    intent = new Intent(this, ProfileActivity.class);
                    startActivity(intent);
                    break;

                case R.id.menuAnalytics:
                    startActivity(new Intent(this, AnalyticsActivity.class));
                    break;

            }
            return true;
        });


        realtimeDatabaseManager.getCurrentUserReference().child("image").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    Glide.with(HomeActivity.this)
                            .load(snapshot.getValue(String.class))
                            .into(profilePicture);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        photoSearch.setOnClickListener(v -> startActivity(new Intent(HomeActivity.this, ImageSenderActivity.class)));

    }

    void loadRecommendedNews() {
        realtimeDatabaseManager = new RealtimeDatabaseManager();
        String uid = realtimeDatabaseManager.getCurrentUserUid();
        int size = 3;
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
                    if (searchResponse.getHits().getHits().length != 0)
                        getRecommendedArticles(searchResponse.getHits().getHits()[0].getSource().getQuery(), 0, 3);
                }
            }

            @Override
            public void onFailure(@NonNull Call<SearchResponse> call, @NonNull Throwable t) {
            }
        });
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

                            if (articleList.size() >= 3) {
                                Glide.with(HomeActivity.this)
                                        .load(articleList.get(0).getSource().getUrlToImage())
                                        .apply(RequestOptions.bitmapTransform(new CircleCrop()))
                                        .into(shortCircleImageView1);
                                shortNewsTitle1.setText(articleList.get(0).getSource().getTitle());
                                shortNewsContent1.setText(articleList.get(0).getSource().getDescription());

                                Glide.with(HomeActivity.this)
                                        .load(articleList.get(1).getSource().getUrlToImage())
                                        .apply(RequestOptions.bitmapTransform(new CircleCrop()))
                                        .into(shortCircleImageView2);
                                shortNewsTitle2.setText(articleList.get(1).getSource().getTitle());
                                shortNewsContent2.setText(articleList.get(1).getSource().getDescription());

                                Glide.with(HomeActivity.this)
                                        .load(articleList.get(2).getSource().getUrlToImage())
                                        .apply(RequestOptions.bitmapTransform(new CircleCrop()))
                                        .into(shortCircleImageView3);
                                shortNewsTitle3.setText(articleList.get(2).getSource().getTitle());
                                shortNewsContent3.setText(articleList.get(2).getSource().getDescription());
                            }
                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call<News> call, @NonNull Throwable t) {
                    }
                });
            }

            @Override
            public void onError(Exception e) {
            }
        });
    }


}