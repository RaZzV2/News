package com.example.news.models;

import com.google.gson.annotations.SerializedName;

public class SearchQuery {
    @SerializedName("size")
    private int size;
    @SerializedName("query")
    private Query query;

    public SearchQuery(int size, Query query) {
        this.size = size;
        this.query = query;
    }

    public static class Query {
        @SerializedName("function_score")
        private FunctionScore functionScore;

        public Query(FunctionScore functionScore) {
            this.functionScore = functionScore;
        }

        public static class FunctionScore {
            @SerializedName("query")
            private TermQuery termQuery;
            @SerializedName("random_score")
            private RandomScore randomScore;

            public FunctionScore(TermQuery termQuery, RandomScore randomScore) {
                this.termQuery = termQuery;
                this.randomScore = randomScore;
            }

            public static class TermQuery {
                @SerializedName("term")
                private UidTerm uidTerm;

                public TermQuery(UidTerm uidTerm) {
                    this.uidTerm = uidTerm;
                }

                public static class UidTerm {
                    @SerializedName("uid.keyword")
                    private String uidKeyword;

                    public UidTerm(String uidKeyword) {
                        this.uidKeyword = uidKeyword;
                    }
                }
            }

            public static class RandomScore {
                // No specific fields needed
            }
        }
    }
}
