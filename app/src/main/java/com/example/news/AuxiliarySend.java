package com.example.news;

import android.os.AsyncTask;
import android.util.Log;

import androidx.annotation.NonNull;

import com.example.news.api.ApiClient;
import com.example.news.api.ApiInterface;
import com.example.news.models.NewsModel.News;
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
import retrofit2.Call;
import retrofit2.Callback;

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

        String responseBody = null;
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

        ImageKnn knn = new ImageKnn("featureVector", floatArray, 10, 100);
        ImageQuery imageQuery = new ImageQuery(knn);

        ApiInterface apiInterface = ApiClient.getApiClient().create(ApiInterface.class);
        Call<News> call = apiInterface.knnSearch(imageQuery);
        call.enqueue(new Callback<News>() {
            @Override
            public void onResponse(@NonNull Call<News> call, @NonNull retrofit2.Response<News> response) {
                Log.i("TAG", "test");
            }

            @Override
            public void onFailure(@NonNull Call<News> call, @NonNull Throwable t) {

            }
        });
        assert response.body() != null;
        return null;
    }
}
