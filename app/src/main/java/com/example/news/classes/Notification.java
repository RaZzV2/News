package com.example.news.classes;

public class Notification {

    String title;
    String date;
    String body;

    public Notification(String title, String date, String body) {
        this.title = title;
        this.date = date;
        this.body = body;
    }

    public String getTitle() {
        return title;
    }

    public String getDate() {
        return date;
    }

    public String getBody() {
        return body;
    }
}
