package com.example.dfrank.movieapp.contoller;


import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.transition.Visibility;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;

import com.example.dfrank.movieapp.BuildConfig;
import com.example.dfrank.movieapp.R;
import com.example.dfrank.movieapp.adapters.MoviesAdapter;
import com.example.dfrank.movieapp.api.Client;
import com.example.dfrank.movieapp.api.Service;
import com.example.dfrank.movieapp.model.Movies;
import com.example.dfrank.movieapp.model.MoviesResponse;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {
    RecyclerView recyclerView;
    Snackbar snackbar;
    ProgressDialog progressDialog;
    SwipeRefreshLayout refreshLayout;
    LinearLayout linear1, linear2, linear3;
    FloatingActionButton fab, fab1, fab2, fab3;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        refreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe);
        fab = (FloatingActionButton) findViewById(R.id.fab);
        fab1 = (FloatingActionButton) findViewById(R.id.fab1);
        fab2 = (FloatingActionButton) findViewById(R.id.fab2);
        fab3 = (FloatingActionButton) findViewById(R.id.fab3);
        linear1 = (LinearLayout) findViewById(R.id.linear1);
        linear2 = (LinearLayout) findViewById(R.id.linear2);
        linear3 = (LinearLayout) findViewById(R.id.linear3);
        linear1.setVisibility(View.GONE);
        linear2.setVisibility(View.GONE);
        linear3.setVisibility(View.GONE);
        Animation animation = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.bounce);
        fab.startAnimation(animation);
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                recyclerView = (RecyclerView) findViewById(R.id.recycler);
                recyclerView.setLayoutManager(new GridLayoutManager(getBaseContext(), 2));
                recyclerView.smoothScrollToPosition(0);
                loadJSON();
            }
        });
        fabButton();
    }
    private void visibility(){
        linear1.setVisibility(View.GONE);
        linear2.setVisibility(View.GONE);
        linear3.setVisibility(View.GONE);
    }
    private void fabButton(){
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (linear1.getVisibility()==View.GONE &&
                        linear2.getVisibility()==View.GONE &&
                        linear3.getVisibility() ==View.GONE){
                    linear1.setVisibility(View.VISIBLE);
                    linear2.setVisibility(View.VISIBLE);
                    linear3.setVisibility(View.VISIBLE);
                }else {
                    visibility();
                }
            }
        });
    linear1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                latest();
                visibility();
            }
        });
       linear2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                upComing();
               visibility();
            }
        });
        linear3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popular();
               visibility();
            }
        });
    }


    private void initView(){
        recyclerView = (RecyclerView) findViewById(R.id.recycler);
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Fetching Movies");
        progressDialog.setCancelable(false);
        progressDialog.show();
        recyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        recyclerView.smoothScrollToPosition(0);
        loadJSON();
    }
    private void loadJSON(){
        Client client = new Client();
        Service service = client.getClient().create(Service.class);
        Call<MoviesResponse> responseCall ;
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
        String select = preferences.getString("list", "1");
        if (select.equals("1")) {
            responseCall = service.getPopularMovies(BuildConfig.The_Movie_Api_Token);
        }else if (select.equals("2")){
            responseCall = service.getTopRatedMovies(BuildConfig.The_Movie_Api_Token);
        }else if (select.equals("3")){
            responseCall = service.getNowPlayingMovies(BuildConfig.The_Movie_Api_Token);
        }else {
            responseCall = service.getUpComingMovies(BuildConfig.The_Movie_Api_Token);
        }
        responseCall.enqueue(new Callback<MoviesResponse>() {
            @Override
            public void onResponse(Call<MoviesResponse> call, Response<MoviesResponse> response) {
                List<Movies> movies = response.body().getResults();
                recyclerView.setAdapter(new MoviesAdapter(getApplicationContext(), movies));
                recyclerView.smoothScrollToPosition(0);
                if (refreshLayout.isRefreshing()){
                    refreshLayout.setRefreshing(false);

                }
                progressDialog.dismiss();
            }

            @Override
            public void onFailure(Call<MoviesResponse> call, Throwable t) {
                progressDialog.dismiss();
                refreshLayout.setVisibility(View.GONE);
                snackbar = Snackbar.make(findViewById(R.id.LinearLayout), "Poor/No Internet", Snackbar.LENGTH_INDEFINITE)
                        .setAction("Refresh", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                initView();
                                fab.hide();
                            }
                        }); snackbar.show();
                fab.hide();
            }
        });
    }
    private void popular(){
        Client client = new Client();
        Service service = client.getClient().create(Service.class);
        Call<MoviesResponse> responseCall = service.getPopularMovies(BuildConfig.The_Movie_Api_Token);
        responseCall.enqueue(new Callback<MoviesResponse>() {
            @Override
            public void onResponse(Call<MoviesResponse> call, Response<MoviesResponse> response) {
                progressDialog.setMessage("Fetching popular movies");
                progressDialog.setCancelable(false);
                progressDialog.show();
                List<Movies> movies = response.body().getResults();
                recyclerView.setAdapter(new MoviesAdapter(getApplicationContext(), movies));
                recyclerView.smoothScrollToPosition(0);
                if (refreshLayout.isRefreshing()){
                    refreshLayout.setRefreshing(false);
                }
                progressDialog.dismiss();
            }

            @Override
            public void onFailure(Call<MoviesResponse> call, Throwable t) {
                progressDialog.dismiss();
                snackbar = Snackbar.make(findViewById(R.id.LinearLayout), "Poor/No Internet", Snackbar.LENGTH_INDEFINITE)
                        .setAction("Refresh", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                initView();
                            }
                        }); snackbar.show();
            }
        });
    }
    private void upComing(){
        Client client = new Client();
        Service service = client.getClient().create(Service.class);
        Call<MoviesResponse> responseCall = service.getUpComingMovies(BuildConfig.The_Movie_Api_Token);
        responseCall.enqueue(new Callback<MoviesResponse>() {
            @Override
            public void onResponse(Call<MoviesResponse> call, Response<MoviesResponse> response) {
                progressDialog.setMessage("Fetching upcoming movies");
                progressDialog.setCancelable(false);
                progressDialog.show();
                List<Movies> movies = response.body().getResults();
                recyclerView.setAdapter(new MoviesAdapter(getApplicationContext(), movies));
                recyclerView.smoothScrollToPosition(0);
                if (refreshLayout.isRefreshing()){
                    refreshLayout.setRefreshing(false);
                }
                progressDialog.dismiss();
            }

            @Override
            public void onFailure(Call<MoviesResponse> call, Throwable t) {
                progressDialog.dismiss();
                snackbar = Snackbar.make(findViewById(R.id.LinearLayout), "Poor/No Internet", Snackbar.LENGTH_INDEFINITE)
                        .setAction("Refresh", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                initView();
                            }
                        }); snackbar.show();
            }
        });
    }
    private void latest(){
        Client client = new Client();
        Service service = client.getClient().create(Service.class);
        Call<MoviesResponse> responseCall = service.getNowPlayingMovies(BuildConfig.The_Movie_Api_Token);
        responseCall.enqueue(new Callback<MoviesResponse>() {
            @Override
            public void onResponse(Call<MoviesResponse> call, Response<MoviesResponse> response) {
                progressDialog.setMessage("Fetching latest movies");
                progressDialog.setCancelable(false);
                progressDialog.show();
                List<Movies> movies = response.body().getResults();
                recyclerView.setAdapter(new MoviesAdapter(getApplicationContext(), movies));
                recyclerView.smoothScrollToPosition(0);
                if (refreshLayout.isRefreshing()){
                    refreshLayout.setRefreshing(false);
                }
                progressDialog.dismiss();
            }

            @Override
            public void onFailure(Call<MoviesResponse> call, Throwable t) {
                progressDialog.dismiss();
                snackbar = Snackbar.make(findViewById(R.id.LinearLayout), "Poor/No Internet", Snackbar.LENGTH_INDEFINITE)
                        .setAction("Refresh", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                initView();
                            }
                        }); snackbar.show();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId()==R.id.settings){
            Intent intent = new Intent(this, Settings.class);
            startActivity(intent);
        }
        return true;
    }

    }


