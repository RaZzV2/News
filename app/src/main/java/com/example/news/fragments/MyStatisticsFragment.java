package com.example.news.fragments;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.news.R;
import com.example.news.api.ApiClient;
import com.example.news.api.ApiInterface;
import com.example.news.firebasemanager.RealtimeDatabaseManager;
import com.example.news.models.Bucket;
import com.example.news.models.CountryCountModel.CountryRequestBody;
import com.example.news.models.SearchHistoryModel.SearchHistory;
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

public class MyStatisticsFragment extends Fragment {

    PieChart pieChart;
    RealtimeDatabaseManager realtimeDatabaseManager;

    public void createPieChart(String time) {
        ApiInterface apiInterface = ApiClient.getApiClient().create(ApiInterface.class);
        realtimeDatabaseManager = new RealtimeDatabaseManager();
        CountryRequestBody.Aggs.Types.Terms terms = new CountryRequestBody.Aggs.Types.Terms( "query.keyword");
        CountryRequestBody.Aggs.Types types = new CountryRequestBody.Aggs.Types(terms);
        CountryRequestBody.Aggs aggs = new CountryRequestBody.Aggs(types);
        CountryRequestBody requestBody = new CountryRequestBody(aggs);
        realtimeDatabaseManager = new RealtimeDatabaseManager();
//        Call<SearchHistory> call = apiInterface.getSearchHistory(requestBody, realtimeDatabaseManager.getCurrentUserUid() + " AND date:[now-1h TO now]", 10000);
        Call<SearchHistory> call = apiInterface.getSearchHistory(requestBody, realtimeDatabaseManager.getCurrentUserUid() + " AND date:[now-"+time+" TO now]", 10000);
        call.enqueue(new Callback<SearchHistory>() {
            @Override
            public void onResponse(@NonNull Call<SearchHistory> call, @NonNull Response<SearchHistory> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<Bucket> buckets;
                    List<PieEntry> lastNewsEntries = new ArrayList<>();
                    buckets = response.body().getAggregations().getTypes().getBuckets();
                    for (Bucket buckets1 : buckets) {
                        lastNewsEntries.add(new PieEntry(buckets1.getDoc_count(), buckets1.getKey()));
                    }
                    PieDataSet dataSet = new PieDataSet(lastNewsEntries, "Countries News Label");
                    dataSet.setSliceSpace(3f);
                    dataSet.setSelectionShift(5f);
                    dataSet.setColors(ColorTemplate.VORDIPLOM_COLORS);

                    PieData data = new PieData(dataSet);
                    data.setValueTextSize(10f);
                    data.setValueTextColor(Color.YELLOW);
                    pieChart.setData(data);

                    pieChart.setCenterText("News Countries Chart");
                    pieChart.setCenterTextSize(16f);
                    pieChart.getLegend().setEnabled(false);
                    pieChart.getDescription().setEnabled(false);
                    pieChart.animateY(1000, Easing.EaseInOutCubic);
                    pieChart.invalidate();
                }
            }

            @Override
            public void onFailure(@NonNull Call<SearchHistory> call, @NonNull Throwable t) {

            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_my_statistics, container, false);

        pieChart = view.findViewById(R.id.chart);
//        calendarNews = view.findViewById(R.id.calendarNews);
        createPieChart("1h");





        return view;
    }
}