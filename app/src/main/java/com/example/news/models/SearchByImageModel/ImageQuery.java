package com.example.news.models.SearchByImageModel;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class ImageQuery implements Parcelable {
    @SerializedName("knn")
    @Expose
    ImageKnn imageKnn;

    public ImageQuery(ImageKnn imageKnn) {
        this.imageKnn = imageKnn;
    }

    protected ImageQuery(Parcel in) {
        imageKnn = in.readParcelable(ImageKnn.class.getClassLoader());
    }

    public static final Creator<ImageQuery> CREATOR = new Creator<ImageQuery>() {
        @Override
        public ImageQuery createFromParcel(Parcel in) {
            return new ImageQuery(in);
        }

        @Override
        public ImageQuery[] newArray(int size) {
            return new ImageQuery[size];
        }
    };

    public ImageKnn getImageKnn() {
        return imageKnn;
    }

    public void setImageKnn(ImageKnn imageKnn) {
        this.imageKnn = imageKnn;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {
        dest.writeParcelable(imageKnn, flags);
    }
}
