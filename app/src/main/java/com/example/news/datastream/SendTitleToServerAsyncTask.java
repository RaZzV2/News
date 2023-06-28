package com.example.news.datastream;

import android.os.AsyncTask;

import com.example.news.interfaces.OnTaskCompleteListener;
import com.example.news.models.SearchByImageModel.ImageKnn;
import com.example.news.models.SearchByImageModel.ImageQuery;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class SendTitleToServerAsyncTask extends AsyncTask<String, Void, ImageQuery> {
    private OnTaskCompleteListener.OnTaskCompletedListener<ImageQuery> onTaskCompletedListener;

    int from, size;

    public void setOnTaskCompletedListener(OnTaskCompleteListener.OnTaskCompletedListener<ImageQuery> listener) {
        this.onTaskCompletedListener = listener;
    }

    public int getFrom() {
        return from;
    }

    public void setFrom(int from) {
        this.from = from;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    @Override
    protected ImageQuery doInBackground(String... strings) {
        OkHttpClient client = new OkHttpClient();
        JSONObject json = new JSONObject();
        try {
            json.put("text", strings[0]);
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
        String jsonStr = json.toString();
        MediaType mediaType = MediaType.parse("application/json; charset=utf-8");
        RequestBody requestBody = RequestBody.create(jsonStr,mediaType);
        Request request = new Request.Builder()
                .url("http://192.168.0.102:7000/text_identify")
                .post(requestBody)
                .build();

        Response response;
        try {
            response = client.newCall(request).execute();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        String responseBody;
        try {
            assert response.body() != null;
            responseBody = response.body().string();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        String[] stringValues = responseBody.replaceAll("[\\[\\]]", "").split(",");
        float[] floatArray = new float[stringValues.length];

        for (int i = 0; i < stringValues.length; i++) {
            floatArray[i] = Float.parseFloat(stringValues[i]);
        }

        ImageKnn knn = new ImageKnn("textFeatureVector", floatArray, 100, 100);

        return new ImageQuery(knn, from, size);
    }

    @Override
    protected void onPostExecute(ImageQuery result) {
        if (onTaskCompletedListener != null) {
            onTaskCompletedListener.onTaskCompleted(result);
        }
    }
}
