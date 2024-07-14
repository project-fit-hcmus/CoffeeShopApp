package com.example.myjavaapp.View.HistoryActions;

import android.content.Context;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.model.GlideUrl;
import com.example.myjavaapp.Model.entity.Order;
import com.example.myjavaapp.R;
import com.example.myjavaapp.View.Interfaces.OrderItemClickListener;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.List;

public class SingleItemAdapter extends RecyclerView.Adapter<SingleItemAdapter.MyViewHolder> implements OrderItemClickListener {
    private Context context;
    private List<Order> data;
    private OrderItemClickListener listener;

    public SingleItemAdapter(Context mainCtx, List<Order> data){
        this.context = mainCtx;
        this.data = data;
    }
    public void setListener(OrderItemClickListener listener){
        this.listener = listener;
    }

    @NonNull
    @Override
    public SingleItemAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.order_history_item,parent,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SingleItemAdapter.MyViewHolder holder, int position) {
        Order item = data.get(position);
        holder.txtStatus.setText(item.getOrderStatus());
        holder.txtPhone.setText(item.getOrderPhone());
        holder.txtAddress.setText(item.getOrderAddress());
        holder.txtCost.setText(item.getOrderCost().toString() + "$");
        holder.txtDate.setText(item.getOrderDate());
        Log.d("PHONE",item.getOrderPhone());
        Log.d("STATUS",item.getOrderStatus());
        Log.d("COST",String.valueOf(item.getOrderCost()));
        String name = item.getOrderCover();
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
        holder.itemView.setOnClickListener(v -> {
            this.listener.OnClickListener(item.getOrderId());
        });
    }

    @Override
    public int getItemCount() {
        return data.size() ;
    }

    @Override
    public void OnClickListener(String orderId) {
        this.listener.OnClickListener(orderId);

    }

    public class MyViewHolder extends RecyclerView.ViewHolder{
        private TextView txtAddress, txtCost, txtDate, txtPhone, txtStatus;
        private ImageView icon;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            txtAddress = itemView.findViewById(R.id.address);
            txtCost = itemView.findViewById(R.id.cost);
            txtDate = itemView.findViewById(R.id.date);
            txtPhone = itemView.findViewById(R.id.phone);
            txtStatus = itemView.findViewById(R.id.status);
            icon = itemView.findViewById(R.id.image);
        }
    }

}
