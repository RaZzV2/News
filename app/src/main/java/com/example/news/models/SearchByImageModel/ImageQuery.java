package com.example.news.models.SearchByImageModel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ImageQuery {
    @SerializedName("knn")
    @Expose
    ImageKnn imageKnn;

    public ImageQuery(ImageKnn imageKnn) {
        this.imageKnn = imageKnn;
    }

    public ImageKnn getImageKnn() {
        return imageKnn;
    }

    public void setImageKnn(ImageKnn imageKnn) {
        this.imageKnn = imageKnn;
    }
}
