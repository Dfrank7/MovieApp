package com.example.dfrank.movieapp.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.dfrank.movieapp.R;
import com.example.dfrank.movieapp.contoller.DetailActivity;
import com.example.dfrank.movieapp.model.Movies;

import java.util.List;

/**
 * Created by dfrank on 7/6/17.
 */

public class MoviesAdapter extends RecyclerView.Adapter<MoviesAdapter.viewHolder>{
    private Context context;
    private List<Movies> movies;
    public MoviesAdapter(Context context, List<Movies> movies){
        this.movies = movies;
        this.context = context;
    }
    @Override
    public MoviesAdapter.viewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.rowcard, parent, false);
        return new viewHolder(view);
    }

    @Override
    public void onBindViewHolder(MoviesAdapter.viewHolder holder, int position) {
        String poster = "https://image.tmdb.org/t/p/w500" + movies.get(position).getPosterPath();
        Glide.with(context).load(poster).into(holder.thumbnail);
        holder.title.setText(movies.get(position).getOriginalTitle());
        holder.ratings.setText(movies.get(position).getVoteAverage().toString());
    }

    @Override
    public int getItemCount() {
        return movies.size();
    }
    public class viewHolder extends RecyclerView.ViewHolder{
        private TextView title, ratings;
        ImageView thumbnail;
        public viewHolder(View view){
            super(view);
            title = (TextView) view.findViewById(R.id.title);
            ratings = (TextView) view.findViewById(R.id.rating);
            thumbnail = (ImageView) view.findViewById(R.id.thumbnail);

            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    if (position!=RecyclerView.NO_POSITION){
                        Intent intent = new Intent(context, DetailActivity.class);
                        intent.putExtra("image", movies.get(position).getPosterPath())
                                .putExtra("overview", movies.get(position).getOverview())
                                .putExtra("release_date", movies.get(position).getReleaseDate())
                                .putExtra("Title", movies.get(position).getOriginalTitle())
                                .putExtra("id",movies.get(position).getId())
                                .putExtra("ratings", movies.get(position).getVoteAverage().toString());
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        context.startActivity(intent);

                    }
                }
            });
        }
    }
}
