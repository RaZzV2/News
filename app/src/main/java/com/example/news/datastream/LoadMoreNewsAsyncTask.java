package com.example.news.datastream;

import android.os.AsyncTask;

import com.example.news.activities.SearchActivity;

import java.lang.ref.WeakReference;

public class LoadMoreNewsAsyncTask extends AsyncTask<Void, Void, Void> {
    private final WeakReference<SearchActivity> activityReference;
    private final String query;
    private final int currentPage;
    private final int size;

    public LoadMoreNewsAsyncTask(SearchActivity activity, String query, int currentPage, int size) {
        activityReference = new WeakReference<>(activity);
        this.query = query;
        this.currentPage = currentPage;
        this.size = size;
    }

    @Override
    protected Void doInBackground(Void... voids) {
        SearchActivity activity = activityReference.get();
        if (activity == null || activity.isFinishing()) return null;
        activity.loadNews(query, currentPage, size);
        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        SearchActivity activity = activityReference.get();
        if (activity == null || activity.isFinishing()) return;
        activity.onLoadMoreComplete();
    }
}
