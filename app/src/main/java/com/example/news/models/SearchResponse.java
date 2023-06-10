package com.example.news.models;

import com.google.gson.annotations.SerializedName;

public class SearchResponse {
    @SerializedName("hits")
    private Hits hits;

    public Hits getHits() {
        return hits;
    }

    public static class Hits {
        @SerializedName("hits")
        private Hit[] hits;

        public Hit[] getHits() {
            return hits;
        }
    }

    public static class Hit {
        @SerializedName("_source")
        private Source source;

        public Source getSource() {
            return source;
        }
    }

    public static class Source {
        @SerializedName("date")
        private String date;
        @SerializedName("query")
        private String query;
        @SerializedName("uid")
        private String uid;

        public String getDate() {
            return date;
        }

        public String getQuery() {
            return query;
        }

        public String getUid() {
            return uid;
        }
    }
}
