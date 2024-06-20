package com.example.myjavaapp.View.Adapter;

import android.content.Context;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.model.GlideUrl;
import com.example.myjavaapp.R;
import com.example.myjavaapp.View.Model.entity.Beverage;
import com.example.myjavaapp.View.Model.entity.Type;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.List;

public class homeBeverageAdapter extends  RecyclerView.Adapter<homeBeverageAdapter.ViewHolder>{
    private Context context;
    private List<Beverage> lstData;
    private int itemClicked = -1;

    public homeBeverageAdapter(Context main, List<Beverage> data ){
        this.context = main;
        this.lstData = data;
    }
    @NonNull
    @Override
    public homeBeverageAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.home_sub_item,parent,false);
        return new homeBeverageAdapter.ViewHolder(view);
    }


    //failed on handle click
    @Override
    public void onBindViewHolder(@NonNull homeBeverageAdapter.ViewHolder holder, int position) {
        Beverage beverage = lstData.get(position);
        String name = beverage.getBeverageImage();
        StorageReference storageReference = FirebaseStorage.getInstance().getReference("images/beverages/" + name);

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
        holder.name.setText(beverage.getBeverageName());
        holder.price.setText(beverage.getBeverageCost());


    }

    @Override
    public int getItemCount() {
        return lstData.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        private ImageView icon;
        private TextView name;
        private TextView price;
        private ImageButton btnAdd;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            icon = itemView.findViewById(R.id.itemBeverageImg);
            name = itemView.findViewById(R.id.itemBeverageName);
            price = itemView.findViewById(R.id.itemBeverageCost);
            btnAdd = itemView.findViewById(R.id.itemBtnAdd);
        }
    }
}
