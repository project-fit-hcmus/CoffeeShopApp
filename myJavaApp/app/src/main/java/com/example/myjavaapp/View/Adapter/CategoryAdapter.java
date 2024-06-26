package com.example.myjavaapp.View.Adapter;

import android.content.Context;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.model.GlideUrl;
import com.example.myjavaapp.View.Interfaces.ItemClickListener;
import com.example.myjavaapp.Model.entity.Type;
import com.example.myjavaapp.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.ViewHolder> implements ItemClickListener {
    private Context context;
    private  ArrayList<Type> lstData;
    private ItemClickListener itemClickListener;
    public void setClickListener(ItemClickListener clickListener) {
        this.itemClickListener = clickListener;
    }
    public CategoryAdapter(Context main, ArrayList<Type> data ){
        this.context = main;
        this.lstData = data;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.category_item,parent,false);
        return new ViewHolder(view);
    }


    //failed on handle click
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Type type = lstData.get(position);
        Log.d("CHECK", type.getTypeImage());
        String name = type.getTypeImage();
        StorageReference storageReference = FirebaseStorage.getInstance().getReference("images/types/" + name);
        Log.d("REFERENCES ",type.getTypeImage());


        Task<Uri> downloadUrlTask = storageReference.getDownloadUrl();
        downloadUrlTask.addOnCompleteListener(new OnCompleteListener<Uri>() {
            @Override
            public void onComplete(@NonNull Task<Uri> task) {
                if (task.isSuccessful()) {
                    Uri downloadUrl = task.getResult();
                    GlideUrl glideUrl = new GlideUrl(downloadUrl.toString());

                    // Load image using GlideUrl
                    // ở đây phải sử dụng getApplicationContext nếu không sẽ xảy ra lỗi "You cannot start a load for a destroyed activity in relativelayout image using glide"
                    Glide.with(context.getApplicationContext())
                            .load(glideUrl)
                            .into(holder.icon);
                } else {
                    // Handle download URL retrieval error
                }
            }
        });
        int temp = position;
        holder.name.setText(type.getTypeName());

        holder.itemView.setOnClickListener(v -> {
            if(itemClickListener != null)
                itemClickListener.onItemClick(position, type.getTypeId());
        });

    }

    @Override
    public int getItemCount() {
        return lstData.size();
    }

    @Override
    public void onItemClick(int position, String id) {
        itemClickListener.onItemClick(position,id);
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView icon;
        private TextView name;
        private LinearLayout bg;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            icon = itemView.findViewById(R.id.imgCategory);
            name = itemView.findViewById(R.id.nameCategory);
            bg = itemView.findViewById(R.id.coverCategory);
        }
    }
}


// click action in recycler view: https://stackoverflow.com/questions/49969278/recyclerview-item-click-listener-the-right-way

