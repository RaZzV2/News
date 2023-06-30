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

public class SendImageToServerAsyncTask extends AsyncTask<String, Void, ImageQuery> {

    private OnTaskCompleteListener.OnTaskCompletedListener<ImageQuery> onTaskCompletedListener;

    int from, size;

    public SendImageToServerAsyncTask(int from, int size) {
        super();
        this.from = from;
        this.size = size;
    }

    public void setOnTaskCompletedListener(OnTaskCompleteListener.OnTaskCompletedListener<ImageQuery> listener) {
        this.onTaskCompletedListener = listener;
    }

    @Override
    protected ImageQuery doInBackground(String... strings) {
        OkHttpClient client = new OkHttpClient();
        JSONObject json = new JSONObject();
        try {
            json.put("im_b64_1", strings[0]);
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
        String jsonStr = json.toString();
        MediaType mediaType = MediaType.parse("application/json; charset=utf-8");
        RequestBody requestBody = RequestBody.create(jsonStr,mediaType);
        Request request = new Request.Builder()
                .url("http://192.168.196.1:5000/identify")
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

        try {
            for (int i = 0; i < stringValues.length; i++) {
                floatArray[i] = Float.parseFloat(stringValues[i]);
            }
        }
        catch (Exception ignored) {
            return null;
        }

        ImageKnn knn = new ImageKnn("featureVector", floatArray, 100, 100);
        return new ImageQuery(knn);
    }

    @Override
    protected void onPostExecute(ImageQuery result) {
        if (onTaskCompletedListener != null) {
            onTaskCompletedListener.onTaskCompleted(result);
        }
    }
}
