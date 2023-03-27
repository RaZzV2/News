package com.example.news.models.CountryCountModel;

import com.google.gson.annotations.SerializedName;

public class CountryRequestBody {
    @SerializedName("aggs")
    private Aggs aggs;

    public CountryRequestBody(Aggs aggs) {
        this.aggs = aggs;
    }

    public static class Aggs {
        @SerializedName("types")
        private Types types;

        public Aggs(Types types) {
            this.types = types;
        }

        public static class Types {
            @SerializedName("terms")
            private Terms terms;

            public Types(Terms terms) {
                this.terms = terms;
            }

            public static class Terms {
                @SerializedName("field")
                private String field;

                public Terms(String field) {
                    this.field = field;
                }
            }
        }
    }
}
