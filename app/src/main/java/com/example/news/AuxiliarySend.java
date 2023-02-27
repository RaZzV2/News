package com.example.news;

import android.os.AsyncTask;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class AuxiliarySend extends AsyncTask<String, Void, String> {
    @Override
    protected String doInBackground(String... strings) {
        OkHttpClient client = new OkHttpClient();
        JSONObject json = new JSONObject();
        try {
            json.put("im_b64_1", strings[0]);
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
        try {
            json.put("im_b64_2", strings[0]);
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
        String jsonStr = json.toString();
        MediaType mediaType = MediaType.parse("application/json; charset=utf-8");
        RequestBody requestBody = RequestBody.create(jsonStr,mediaType);
        Request request = new Request.Builder()
                .url("http://192.168.0.104:5000/identify")
                .post(requestBody)
                .build();

        Response response;
        try {
            response = client.newCall(request).execute();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        String x;
        assert response.body() != null;
        try {
             x = response.body().string();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return null;
    }
}
