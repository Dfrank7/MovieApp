package com.example.dfrank.movieapp.adapters;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.dfrank.movieapp.R;
import com.example.dfrank.movieapp.contoller.Settings;
import com.example.dfrank.movieapp.model.Trailers;

import java.util.List;

/**
 * Created by dfrank on 7/9/17.
 */

public class TrailerAdapter extends RecyclerView.Adapter<TrailerAdapter.viewHolder>{
    private Context context;
    private List<Trailers> trailer;
    public TrailerAdapter(Context context, List<Trailers> trailer) {
        this.context = context;
        this.trailer = trailer;
    }
    @Override
    public TrailerAdapter.viewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.trailercardview, parent, false);
        return new viewHolder(view);
    }

    @Override
    public void onBindViewHolder(TrailerAdapter.viewHolder holder, int position) {
        holder.name.setText(trailer.get(position).getName());
    }

    @Override
    public int getItemCount() {
        return trailer.size();
    }
    public class viewHolder extends RecyclerView.ViewHolder{
        TextView name;
        public viewHolder(final View view){
            super(view);
            name = (TextView) view.findViewById(R.id.trailer_name);
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    if (position!=RecyclerView.NO_POSITION){
                        String video = trailer.get(position).getKey().toString();
                        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.youtube.com/watch?v="+video));
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        view.getContext().startActivity(intent);

                        //context.startActivity(intent);
                    }
                }
            });
        }
    }
}
