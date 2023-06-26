package com.example.news.models.NewsCountModel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class SearchResponse {

    @SerializedName("aggregations")
    @Expose
    private Aggregations aggregations;

    public Aggregations getAggregations() {
        return aggregations;
    }

    public void setAggregations(Aggregations aggregations) {
        this.aggregations = aggregations;
    }

    public static class Hit {
        @SerializedName("_source")
        @Expose
        private Source source;

        public Source getSource() {
            return source;
        }

        public void setSource(Source source) {
            this.source = source;
        }
    }

    public static class Source {
        @SerializedName("uid.keyword")
        @Expose
        private String uid;
        @Expose
        @SerializedName("date")
        private String date;

        public String getUid() {
            return uid;
        }

        public void setUid(String uid) {
            this.uid = uid;
        }

        public String getDate() {
            return date;
        }

        public void setDate(String date) {
            this.date = date;
        }
    }

    public static class SearchesPerDay {
        @SerializedName("buckets")
        @Expose
        private List<Bucket> buckets;

        public List<Bucket> getBuckets() {
            return buckets;
        }

        public void setBuckets(List<Bucket> buckets) {
            this.buckets = buckets;
        }
    }

    public static class Aggregations {
        @SerializedName("searches_per_day")
        @Expose
        private SearchesPerDay searchesPerDay;

        public SearchesPerDay getSearchesPerDay() {
            return searchesPerDay;
        }

        public void setSearchesPerDay(SearchesPerDay searchesPerDay) {
            this.searchesPerDay = searchesPerDay;
        }
    }


    public static class Bucket {
        @SerializedName("key_as_string")
        @Expose
        private String key;
        @SerializedName("doc_count")
        @Expose
        private int docCount;

        public String getKey() {
            return key;
        }

        public void setKey(String key) {
            this.key = key;
        }

        public int getDocCount() {
            return docCount;
        }

        public void setDocCount(int docCount) {
            this.docCount = docCount;
        }
    }
}
