package com.example.myjavaapp.View.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myjavaapp.R;

import java.util.List;

public class PrivacyAdapter extends RecyclerView.Adapter<PrivacyAdapter.MyViewHolder> {
    private Context mainCtx;
    private List<String> headers;
    private List<String> contents;
    private List<Integer> images;

    public PrivacyAdapter(Context context, List<String> headers, List<String> contents, List<Integer> images){
        this.mainCtx = context;
        this.headers = headers;
        this.contents = contents;
        this.images = images;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.privacy_item,parent,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.header.setText(headers.get(position));
        holder.content.setText(contents.get(position));
    }

    @Override
    public int getItemCount() {
        return headers.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{
        TextView header, content;
        ImageView image;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            header = itemView.findViewById(R.id.header);
            content = itemView.findViewById(R.id.content);
            image = itemView.findViewById(R.id.image);

        }
    }
}
