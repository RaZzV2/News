package com.example.news.models.SearchByImageModel;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ImageKnn implements Parcelable {

    @SerializedName("field")
    @Expose
    String field;

    @SerializedName("query_vector")
    @Expose
    float[]queryVector;

    @SerializedName("k")
    @Expose
    private int k;

    @SerializedName("num_candidates")
    @Expose
    private int numCandidates;

    public ImageKnn(String field, float[] queryVector, int k, int numCandidates) {
        this.field = field;
        this.queryVector = queryVector;
        this.k = k;
        this.numCandidates = numCandidates;
    }

    protected ImageKnn(Parcel in) {
        field = in.readString();
        queryVector = in.createFloatArray();
        k = in.readInt();
        numCandidates = in.readInt();
    }

    public static final Creator<ImageKnn> CREATOR = new Creator<ImageKnn>() {
        @Override
        public ImageKnn createFromParcel(Parcel in) {
            return new ImageKnn(in);
        }

        @Override
        public ImageKnn[] newArray(int size) {
            return new ImageKnn[size];
        }
    };

    public String getField() {
        return field;
    }

    public void setField(String field) {
        this.field = field;
    }

    public float[] getQueryVector() {
        return queryVector;
    }

    public void setQueryVector(float[] queryVector) {
        this.queryVector = queryVector;
    }

    public int getK() {
        return k;
    }

    public void setK(int k) {
        this.k = k;
    }

    public int getNumCandidates() {
        return numCandidates;
    }

    public void setNumCandidates(int numCandidates) {
        this.numCandidates = numCandidates;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {
        dest.writeString(field);
        dest.writeFloatArray(queryVector);
        dest.writeInt(k);
        dest.writeInt(numCandidates);
    }
}
