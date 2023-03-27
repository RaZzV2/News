package com.example.news.activities;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.news.ApiClient;
import com.example.news.ApiInterface;
import com.example.news.R;
import com.example.news.models.CountryCountModel.Bucket;
import com.example.news.models.CountryCountModel.CountryRequestBody;
import com.example.news.models.CountryCountModel.CountryResult;
import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AnalyticsActivity extends AppCompatActivity {

    ImageView back;

    PieChart languageNews;

    PieChart categoriesNews;

    public void createPieChart(String filter, PieChart chart) {
        ApiInterface apiInterface = ApiClient.getApiClient().create(ApiInterface.class);
        CountryRequestBody.Aggs.Types.Terms terms = new CountryRequestBody.Aggs.Types.Terms(filter+".keyword");
        CountryRequestBody.Aggs.Types types = new CountryRequestBody.Aggs.Types(terms);
        CountryRequestBody.Aggs aggs = new CountryRequestBody.Aggs(types);
        CountryRequestBody requestBody = new CountryRequestBody(aggs);
        Call<CountryResult> call = apiInterface.getCountryResult(requestBody);
        call.enqueue(new Callback<CountryResult>() {
            @Override
            public void onResponse(@NonNull Call<CountryResult> call, @NonNull Response<CountryResult> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<Bucket> countriesNewsList;
                    List<PieEntry> countryEntries = new ArrayList<>();
                    countriesNewsList = response.body().getAggregations().getTypes().getBuckets();
                    for(Bucket buckets : countriesNewsList) {
                        countryEntries.add(new PieEntry(buckets.getDoc_count(), buckets.getKey()));
                    }
                    PieDataSet dataSet = new PieDataSet(countryEntries, "Countries News Label");
                    dataSet.setSliceSpace(3f);
                    dataSet.setSelectionShift(5f);
                    dataSet.setColors(ColorTemplate.VORDIPLOM_COLORS);

                    PieData data = new PieData(dataSet);
                    data.setValueTextSize(10f);
                    data.setValueTextColor(Color.YELLOW);
                    chart.setData(data);

                    chart.setCenterText("News Countries Chart");
                    chart.setCenterTextSize(16f);
                    chart.getLegend().setEnabled(false);
                    chart.getDescription().setEnabled(false);
                    chart.animateY(1000, Easing.EaseInOutCubic);
                    chart.invalidate();

                }
            }
            @Override
            public void onFailure(@NonNull Call<CountryResult> call, @NonNull Throwable t) {
                // Handle failure
            }
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_analytics);

        back = findViewById(R.id.back);
        languageNews = findViewById(R.id.chart);
        categoriesNews = findViewById(R.id.chart1);

        createPieChart("country", languageNews);
        createPieChart("category",categoriesNews);
        




        back.setOnClickListener(v -> {
            startActivity(new Intent(AnalyticsActivity.this, HomeActivity.class));
            finish();
        });
    }
}