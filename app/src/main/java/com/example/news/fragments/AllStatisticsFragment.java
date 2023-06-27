package com.example.news.fragments;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.news.api.ApiClient;
import com.example.news.api.ApiInterface;
import com.example.news.R;
import com.example.news.firebasemanager.RealtimeDatabaseManager;
import com.example.news.models.Bucket;
import com.example.news.models.CountryCountModel.CountryRequestBody;
import com.example.news.models.CountryCountModel.CountryResult;
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

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AllStatisticsFragment extends Fragment {

    PieChart languageNews;

    PieChart categoriesNews;

    BarChart barChart;

    RealtimeDatabaseManager realtimeDatabaseManager;

    public void createBarChart(String time) {
        ApiInterface apiInterface = ApiClient.getApiClient().create(ApiInterface.class);
        realtimeDatabaseManager = new RealtimeDatabaseManager();
        List<MustElement> mustElements = new ArrayList<>();
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

                    dataSet.setValueFormatter(new ValueFormatter() {
                        @Override
                        public String getFormattedValue(float value) {
                            return String.valueOf((int) value);
                        }
                    });

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

    public void createPieChart(String filter, PieChart chart, String title) {
        ApiInterface apiInterface = ApiClient.getApiClient().create(ApiInterface.class);
        CountryRequestBody.Aggs.Types.Terms terms = new CountryRequestBody.Aggs.Types.Terms(filter + ".keyword");
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
                    for (Bucket buckets : countriesNewsList) {
                        countryEntries.add(new PieEntry(buckets.getDoc_count(), buckets.getKey()));
                    }
                    PieDataSet dataSet = new PieDataSet(countryEntries, "Countries News Label");
                    dataSet.setSliceSpace(3f);
                    dataSet.setSelectionShift(5f);
                    dataSet.setColors(ColorTemplate.VORDIPLOM_COLORS);

                    dataSet.setValueFormatter(new ValueFormatter() {
                        @Override
                        public String getFormattedValue(float value) {
                            return String.valueOf((int) value);
                        }
                    });

                    PieData data = new PieData(dataSet);
                    data.setValueTextSize(10f);
                    data.setValueTextColor(Color.YELLOW);
                    chart.setData(data);
                    chart.setCenterText(title);
                    chart.setCenterTextSize(16f);
                    chart.getLegend().setEnabled(false);
                    chart.getDescription().setEnabled(false);
                    chart.animateY(1000, Easing.EaseInOutCubic);
                    chart.invalidate();

                }
            }

            @Override
            public void onFailure(@NonNull Call<CountryResult> call, @NonNull Throwable t) {

            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_all_statistics, container, false);

        languageNews = view.findViewById(R.id.chart);
        categoriesNews = view.findViewById(R.id.chart1);
        barChart = view.findViewById(R.id.barChart);

        createPieChart("country", languageNews, "News Languages Chart");
        createPieChart("category", categoriesNews, "News Categories Chart");
        createBarChart("7");


        return view;
    }
}