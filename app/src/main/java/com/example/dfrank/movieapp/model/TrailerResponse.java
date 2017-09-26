package com.example.dfrank.movieapp.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by dfrank on 7/9/17.
 */

public class TrailerResponse {
    @SerializedName("id")
    private int id;
    @SerializedName("results")
    private List<Trailers> results;

    public TrailerResponse(int id, List<Trailers> results) {
        this.id = id;
        this.results = results;
    }

    public Integer getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public List<Trailers> getResults() {
        return results;
    }

    public void setResults(List<Trailers> results) {
        this.results = results;
    }
}
