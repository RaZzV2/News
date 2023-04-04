package com.example.news.interfaces;

public interface OnTaskCompleteListener {
    public interface OnTaskCompletedListener<T> {
        void onTaskCompleted(T result);
        void onError(Exception e);
    }
}
