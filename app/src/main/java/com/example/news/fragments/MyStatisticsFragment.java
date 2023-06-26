package com.example.news.fragments;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.news.R;
import com.example.news.api.ApiClient;
import com.example.news.api.ApiInterface;
import com.example.news.firebasemanager.RealtimeDatabaseManager;
import com.example.news.models.Bucket;
import com.example.news.models.CountryCountModel.CountryRequestBody;
import com.example.news.models.NewsCountModel.Aggs;
import com.example.news.models.NewsCountModel.Bool;
import com.example.news.models.NewsCountModel.DateHistogram;
import com.example.news.models.NewsCountModel.DateRange;
import com.example.news.models.NewsCountModel.MustElement;
import com.example.news.models.NewsCountModel.Query;
import com.example.news.models.NewsCountModel.Range;
import com.example.news.models.NewsCountModel.RangeElement;
import com.example.news.models.NewsCountModel.SearchRequest;
import com.example.news.models.NewsCountModel.SearchResponse;
import com.example.news.models.NewsCountModel.SearchesPerDay;
import com.example.news.models.NewsCountModel.Term;
import com.example.news.models.NewsCountModel.TermElement;
import com.example.news.models.NewsCountModel.Uid;
import com.example.news.models.SearchHistoryModel.SearchHistory;
import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.google.common.collect.ImmutableList;
import com.google.gson.Gson;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MyStatisticsFragment extends Fragment {

    PieChart pieChart;
    BarChart barChart;
    RealtimeDatabaseManager realtimeDatabaseManager;
    Spinner timeSpinner;

    public void createPieChart(String time) {
        ApiInterface apiInterface = ApiClient.getApiClient().create(ApiInterface.class);
        realtimeDatabaseManager = new RealtimeDatabaseManager();
        CountryRequestBody.Aggs.Types.Terms terms = new CountryRequestBody.Aggs.Types.Terms( "query.keyword");
        CountryRequestBody.Aggs.Types types = new CountryRequestBody.Aggs.Types(terms);
        CountryRequestBody.Aggs aggs = new CountryRequestBody.Aggs(types);
        CountryRequestBody requestBody = new CountryRequestBody(aggs);
        realtimeDatabaseManager = new RealtimeDatabaseManager();
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
                    dataSet.setValueFormatter(new ValueFormatter() {
                        @Override
                        public String getFormattedValue(float value) {
                            return String.valueOf((int) value);
                        }
                    });

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

    public void createBarChart(String time) {
        ApiInterface apiInterface = ApiClient.getApiClient().create(ApiInterface.class);
        realtimeDatabaseManager = new RealtimeDatabaseManager();
        List<MustElement> mustElements = new ArrayList<>();
        mustElements.add(new TermElement(new Term(realtimeDatabaseManager.getCurrentUserUid())));
        mustElements.add(new RangeElement(new Range(new DateRange("now-"+time+"d", "now"))));
        SearchRequest searchRequest = new SearchRequest(
                new Query(
                        new Bool(mustElements
                                )
                        )
                ,
                0,
                new Aggs(new SearchesPerDay(new DateHistogram("date", "day")))
        );
        Call<SearchResponse> searchResponseCall = apiInterface.search(searchRequest);

        searchResponseCall.enqueue(new Callback<SearchResponse>() {
            @Override
            public void onResponse(@NonNull Call<SearchResponse> call, @NonNull Response<SearchResponse> response) {
                if(response.isSuccessful() && response.body()!=null){
                    List<SearchResponse.Bucket> buckets = response.body().getAggregations().getSearchesPerDay().getBuckets();
                    List<BarEntry> entries = new ArrayList<>();
                    List<String> dates = new ArrayList<>();
                    for(int index=0;index<buckets.size();++index){
                        String inputDate = buckets.get(index).getKey();
                        DateTimeFormatter inputFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
                        DateTimeFormatter outputFormatter = DateTimeFormatter.ofPattern("dd MMMM yyyy");
                        LocalDateTime dateTime = LocalDateTime.parse(inputDate, inputFormatter);
                        String outputDate = dateTime.format(outputFormatter);
                        dates.add(outputDate);
                        entries.add(new BarEntry(index, buckets.get(index).getDocCount()));
                    }
                    BarDataSet dataSet = new BarDataSet(entries, "Searches Count");

                    dataSet.setColor(Color.BLUE);

                    BarData barData = new BarData(dataSet);

                    XAxis xAxis = barChart.getXAxis();
                    xAxis.setValueFormatter(new IndexAxisValueFormatter(dates));
                    xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
                    xAxis.setGranularity(1);
                    xAxis.setGranularityEnabled(true);
                    barChart.getDescription().setEnabled(false);
                    barChart.setData(barData);
                    barChart.invalidate();
                }
            }

            @Override
            public void onFailure(@NonNull Call<SearchResponse> call, @NonNull Throwable t) {
            }
        });
        
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_my_statistics, container, false);

        pieChart = view.findViewById(R.id.chart);
        barChart = view.findViewById(R.id.barChart);
        createPieChart("1h");
        createBarChart("7");
        timeSpinner = view.findViewById(R.id.spinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(view.getContext(), R.array.time_options, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        timeSpinner.setAdapter(adapter);
        timeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedOption = parent.getItemAtPosition(position).toString();
                Toast.makeText(view.getContext(), "Selected option: " + selectedOption, Toast.LENGTH_SHORT).show();

                switch (selectedOption) {
                    case "Last 24h":
                        createPieChart("1d");
                        break;
                    case "Last 7 days":
                        createPieChart("7d");
                        break;
                    default:
                        createPieChart("1h");
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        return view;
    }
}