package com.example.dfrank.movieapp.contoller;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.dfrank.movieapp.BuildConfig;
import com.example.dfrank.movieapp.R;
import com.example.dfrank.movieapp.adapters.TrailerAdapter;
import com.example.dfrank.movieapp.api.Client;
import com.example.dfrank.movieapp.api.Service;
import com.example.dfrank.movieapp.model.Movies;
import com.example.dfrank.movieapp.model.TrailerResponse;
import com.example.dfrank.movieapp.model.Trailers;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by dfrank on 7/6/17.
 */

public class DetailActivity extends AppCompatActivity {
    private TextView movieName, plot, ratings, releaseDate;
    private ImageView imageView;
    private int movie_id;
    private RecyclerView recycler;
    TrailerAdapter adapter;
    Context context;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.detail_layout);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        initViews();
        initCollapsingToolbar();
        imageView = (ImageView) findViewById(R.id.imageView);
        movieName = (TextView) findViewById(R.id.movieName);
        plot = (TextView) findViewById(R.id.plot);
        releaseDate = (TextView) findViewById(R.id.releaseDate);
        ratings = (TextView) findViewById(R.id.vote);
        String name = getIntent().getStringExtra("Title");
        String overView = getIntent().getStringExtra("overview");
        String voteCount = getIntent().getStringExtra("ratings");
        String thumbnail = getIntent().getStringExtra("image");
        String date = getIntent().getStringExtra("release_date");
        String poster = "https://image.tmdb.org/t/p/w500" + thumbnail;
        Glide.with(getApplicationContext()).load(poster).into(imageView);
        movieName.setText(name);
        plot.setText(overView);
        ratings.setText(voteCount);
        releaseDate.setText(date);
    }
    private void initViews(){
        loadJSON();
        recycler = (RecyclerView) findViewById(R.id.detailRecycler);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recycler.setLayoutManager(mLayoutManager);
//        adapter.notifyDataSetChanged();


    }
    private void initCollapsingToolbar(){
        final CollapsingToolbarLayout collapsingToolbarLayout =
                (CollapsingToolbarLayout) findViewById(R.id.collapsing);
        collapsingToolbarLayout.setTitle(" ");
        AppBarLayout appBarLayout = (AppBarLayout) findViewById(R.id.appbar);
        appBarLayout.setExpanded(true);

        appBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener(){
            boolean isShow = false;
            int scrollRange = -1;

            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset){
                if (scrollRange == -1){
                    scrollRange = appBarLayout.getTotalScrollRange();
                }
                if (scrollRange + verticalOffset == 0){
                    collapsingToolbarLayout.setTitle(getString(R.string.movie_details));
                    isShow = true;
                }else if (isShow){
                    collapsingToolbarLayout.setTitle(" ");
                    isShow = false;
                }
            }
        });
    }
    private void loadJSON(){
        movie_id = getIntent().getExtras().getInt("id");
        Client client = new Client();
        Service service = client.getClient().create(Service.class);
        Call<TrailerResponse> call = service.getTrailers(movie_id, BuildConfig.The_Movie_Api_Token);
        call.enqueue(new Callback<TrailerResponse>() {
            @Override
            public void onResponse(Call<TrailerResponse> call, Response<TrailerResponse> response) {
              List<Trailers> trailer = response.body().getResults();
                recycler.setAdapter(new TrailerAdapter(context, trailer));
                recycler.smoothScrollToPosition(0);

            }

            @Override
            public void onFailure(Call<TrailerResponse> call, Throwable t) {
                Toast.makeText(DetailActivity.this, "Error Fetching data", Toast.LENGTH_SHORT).show();

            }
        });
    }
}
