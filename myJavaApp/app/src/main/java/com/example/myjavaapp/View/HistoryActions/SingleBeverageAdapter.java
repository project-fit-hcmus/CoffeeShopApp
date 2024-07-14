package com.example.myjavaapp.View.HistoryActions;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.model.GlideUrl;
import com.example.myjavaapp.Model.entity.OrderDetail;
import com.example.myjavaapp.Model.entity.OrderDetailAndBeverage;
import com.example.myjavaapp.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.List;

public class SingleBeverageAdapter extends RecyclerView.Adapter<SingleBeverageAdapter.MyViewHolder> {
    private Context context;
    private List<OrderDetailAndBeverage> data;


    public SingleBeverageAdapter(Context ctx, List<OrderDetailAndBeverage> input){
        this.context = ctx;
        this.data = input;
    }

    @NonNull
    @Override
    public SingleBeverageAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.history_order_dialog_item,parent,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SingleBeverageAdapter.MyViewHolder holder, int position) {
        OrderDetailAndBeverage item = data.get(position);

        holder.txtName.setText(item.beverage.getBeverageName());
        holder.txtType.setText(item.type.getTypeName());
        holder.txtCost.setText(item.beverage.getBeverageCost());
        holder.txtQuantity.setText(String.valueOf(item.orderDetail.getOrderDetailQuantity()));

        String name = item.beverage.getBeverageImage();
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
                    Glide.with(context.getApplicationContext())
                            .load(glideUrl)
                            .into(holder.icon);
                } else {
                    // Handle download URL retrieval error
                }
            }
        });


    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{
        private TextView txtName, txtCost, txtQuantity, txtType;
        private ImageView icon;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            txtName = itemView.findViewById(R.id.itemName);
            txtCost = itemView.findViewById(R.id.itemCost);
            txtQuantity = itemView.findViewById(R.id.itemQuantity);
            txtType = itemView.findViewById(R.id.itemType);
            icon = itemView.findViewById(R.id.itemImage);
        }
    }
}
