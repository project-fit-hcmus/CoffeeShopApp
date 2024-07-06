package com.example.myjavaapp.View.Adapter;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.model.GlideUrl;
import com.example.myjavaapp.Model.entity.Beverage;
import com.example.myjavaapp.Model.entity.BeverageAndCartDetail;
import com.example.myjavaapp.Model.entity.BeverageAndType;
import com.example.myjavaapp.Model.entity.FavoriteAndBeverage;
import com.example.myjavaapp.Model.entity.Type;
import com.example.myjavaapp.R;
import com.example.myjavaapp.View.Interfaces.BeverageItemClickListener;
import com.example.myjavaapp.View.Interfaces.ItemClickListener;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.List;

public class FavoriteAdapter extends RecyclerView.Adapter<FavoriteAdapter.MyViewHolder> implements BeverageItemClickListener {

    private Context main;
    private List<BeverageAndType> data;
    private BeverageItemClickListener listener;

    public FavoriteAdapter(Context context, List<BeverageAndType> input){
        this.data = input;
        this.main = context;
    }
    public void setListener(BeverageItemClickListener click){
        this.listener = click;
    }

    @NonNull
    @Override
    public FavoriteAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.favorite_item,parent,false);
        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Beverage item = data.get(position).beverage;
        Type type = data.get(position).type;
        String name = item.getBeverageImage();
        StorageReference storageReference = FirebaseStorage.getInstance().getReference("images/beverages/" + name);
        Task<Uri> downloadUrlTask = storageReference.getDownloadUrl();
        downloadUrlTask = downloadUrlTask.addOnCompleteListener(new OnCompleteListener<Uri>() {
            @Override
            public void onComplete(@NonNull Task<Uri> task) {
                if (task.isSuccessful()) {
                    Uri downloadUrl = task.getResult();
                    GlideUrl glideUrl = new GlideUrl(downloadUrl.toString());

                    // Load image using GlideUrl
                    // ở đây phải sử dụng getApplicationContext nếu không sẽ xảy ra lỗi "You cannot start a load for a destroyed activity in relativelayout image using glide"
                    Glide.with(main.getApplicationContext())
                            .load(glideUrl)
                            .into(holder.icon);
                } else {
                    // Handle download URL retrieval error
                }
            }
        });

        holder.name.setText(item.getBeverageName());
        holder.price.setText(item.getBeverageCost());
        holder.type.setText(type.getTypeName());
        holder.itemView.setOnClickListener(v -> {
            listener.onBeverageClick(position, item.getBeverageId(), "single-beverage");
        });
        holder.btnAddToCart.setOnClickListener(v -> {
            listener.onBeverageClick(position, item.getBeverageId(), "add-to-cart");
        });


    }

    @Override
    public int getItemCount() {
        return data.size();
    }



    @Override
    public void onBeverageClick(Integer position, String id, String action) {

    }

    public class MyViewHolder extends RecyclerView.ViewHolder{
        private ImageView icon;
        private TextView name, type, price;
        private Button btnAddToCart;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            icon = itemView.findViewById(R.id.mainImg);
            name = itemView.findViewById(R.id.favoriteName);
            type = itemView.findViewById(R.id.favoriteType);
            price = itemView.findViewById(R.id.favoritePrice);
            btnAddToCart = itemView.findViewById(R.id.btnAddToCart);
        }
    }
}
