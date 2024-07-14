package com.example.myjavaapp.View.Adapter;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresPermission;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.model.GlideUrl;
import com.example.myjavaapp.Model.entity.BeverageAndCartDetail;
import com.example.myjavaapp.Model.entity.OrderDetailAndBeverage;
import com.example.myjavaapp.R;
import com.example.myjavaapp.View.Interfaces.CartItemClickListener;
import com.example.myjavaapp.View.Interfaces.ReOrderItemListener;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.List;

public class RepurchaseItemAdapter extends RecyclerView.Adapter<RepurchaseItemAdapter.ViewHolder> implements ReOrderItemListener {
    private Context context;
    private List<OrderDetailAndBeverage> data;
    private ReOrderItemListener listener;
    private List<Boolean> check;

    public RepurchaseItemAdapter(Context main, List<OrderDetailAndBeverage> input){
        this.context = main;
        this.data = input;
        check = new ArrayList<>(input.size());
        for(int i = 0; i < input.size(); ++i)
            check.add(false);
    }
    public void setListener(ReOrderItemListener listen){
        this.listener = listen;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cart_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        OrderDetailAndBeverage item = data.get(position);
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
        holder.name.setText(item.beverage.getBeverageName());
        holder.price.setText(item.beverage.getBeverageCost());
        holder.type.setText(item.beverage.getBeverageType());
        holder.quantity.setText(item.orderDetail.getOrderDetailQuantity().toString());
        holder.btnAdd.setOnClickListener(v -> {
            listener.onClickListener("plus",0,"");
            item.orderDetail.setOrderDetailQuantity(item.orderDetail.getOrderDetailQuantity() + 1);

        });
        holder.btnSub.setOnClickListener(v -> {
            listener.onClickListener("substract",0,"");
            item.orderDetail.setOrderDetailQuantity(item.orderDetail.getOrderDetailQuantity() - 1);
        });

        holder.checkbox.setOnClickListener(v -> {
            if(holder.checkbox.isChecked()){
                listener.onClickListener("check",item.orderDetail.getOrderDetailQuantity(), item.beverage.getBeverageCost());
                check.set(position,true);
            }
            else{
                listener.onClickListener("uncheck", item.orderDetail.getOrderDetailQuantity(), item.beverage.getBeverageCost());
                check.set(position,false);
            }
        });

    }

    @Override
    public int getItemCount() {
        return data.size();
    }
    public List<Boolean> getCheckList(){
        return check;
    }
    public List<OrderDetailAndBeverage> getData(){
        return this.data;
    }

    @Override
    public void onClickListener(String action, int quantity, String cost) {
        listener.onClickListener(action, quantity, cost);
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        private ImageView icon;
        private TextView name;
        private TextView type;
        private TextView price;
        private ImageButton btnAdd, btnSub;
        private TextView quantity;
        private CheckBox checkbox;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            icon = (ImageView) itemView.findViewById(R.id.itemIcon);
            name = (TextView) itemView.findViewById(R.id.itemName);
            type = (TextView) itemView.findViewById(R.id.itemType);
            price = (TextView) itemView.findViewById(R.id.itemCost);
            btnAdd = (ImageButton) itemView.findViewById(R.id.btnPlus);
            btnSub = (ImageButton) itemView.findViewById(R.id.btnSubstract);
            quantity = (TextView) itemView.findViewById(R.id.quantityResult);
            checkbox = (CheckBox) itemView.findViewById(R.id.itemCheckbox);


        }
    }
}
