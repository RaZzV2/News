package com.example.news.models.SearchByImageModel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ImageKnn {

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
}
