package com.secretcitylabs.digitaldna;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> {
    private Context context;
    private ArrayList sample_id, date_collected,exact_site;

    public MyAdapter(Context context, ArrayList sample_id, ArrayList date_collected, ArrayList exact_site) {
        this.context = context;
        this.sample_id = sample_id;
        this.date_collected = date_collected;
        this.exact_site = exact_site;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.dataentry,parent,false);
        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.sample_id.setText(String.valueOf(sample_id.get(position)));
        holder.date_collected.setText(String.valueOf(date_collected.get(position)));
        holder.exact_site.setText(String.valueOf(exact_site.get(position)));
    }

    @Override
    public int getItemCount() {
        return sample_id.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView sample_id, date_collected, exact_site;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            sample_id = itemView.findViewById(R.id.sampleID_);
            date_collected = itemView.findViewById(R.id.dateCollected_);
            exact_site = itemView.findViewById(R.id.exactSite_);
        }
    }
}
