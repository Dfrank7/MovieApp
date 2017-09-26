package com.example.dfrank.movieapp.api;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by dfrank on 7/6/17.
 */

public class Client {
    public static final String base_url ="http://api.themoviedb.org/3/";
    Retrofit retrofit =null;
    public Retrofit getClient(){
        if (retrofit == null){
            retrofit = new Retrofit.Builder()
                    .baseUrl(base_url)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }return retrofit;
    }
}
