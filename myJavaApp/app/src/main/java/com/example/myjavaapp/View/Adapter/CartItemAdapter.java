package com.example.myjavaapp.View.Adapter;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.model.GlideUrl;
import com.example.myjavaapp.Model.entity.BeverageAndCartDetail;
import com.example.myjavaapp.R;
import com.example.myjavaapp.View.Interfaces.BeverageItemClickListener;
import com.example.myjavaapp.View.Interfaces.CartItemClickListener;
import com.example.myjavaapp.View.Interfaces.ItemClickListener;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.List;

public class CartItemAdapter extends RecyclerView.Adapter<CartItemAdapter.ViewHolder> implements CartItemClickListener {
    private Context mainCtx;
    private List<BeverageAndCartDetail> data;
    private List<Boolean> check;
    private CartItemClickListener itemClickListener;

    public void setClickListener(CartItemClickListener clickListener) {
        this.itemClickListener = clickListener;
    }

    public CartItemAdapter(Context context, List<BeverageAndCartDetail> input){
        this.mainCtx = context;
        this.data = input;
        this.check = new ArrayList<>(input.size());
        for(int i = 0; i < input.size(); ++i){
            check.add(false);
        }
    }
    @NonNull
    @Override
    public CartItemAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cart_item,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CartItemAdapter.ViewHolder holder, int position) {
        BeverageAndCartDetail item = data.get(position);
//        String name = item.beverage.getBeverageImage();
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
                    Glide.with(mainCtx.getApplicationContext())
                            .load(glideUrl)
                            .into(holder.icon);
                } else {
                    // Handle download URL retrieval error
                }
            }
        });

//        holder.name.setText(item.beverage.getBeverageName());
        holder.name.setText(item.beverage.getBeverageName());
//        holder.price.setText(item.beverage.getBeverageCost());
        holder.price.setText(item.beverage.getBeverageCost());
        holder.type.setText(item.type.getTypeName());

        holder.quantity.setText(item.cartDetail.getCartDetailQuantity().toString());

        holder.btnAdd.setOnClickListener(v -> {
            itemClickListener.onCartItemClick(position,item.cartDetail.getCartDetailId(),item.cartDetail.getCartDetailBeverage(),item.cartDetail.getCartDetailQuantity() + 1,item);
        });
        holder.btnSub.setOnClickListener(v -> {
            itemClickListener.onCartItemClick(position,item.cartDetail.getCartDetailId(),item.cartDetail.getCartDetailBeverage(),item.cartDetail.getCartDetailQuantity() - 1,item);
        });

        holder.checkbox.setOnClickListener(v -> {
            if(holder.checkbox.isChecked()){
                itemClickListener.onCartItemClick(position,"plus",item.beverage.getBeverageCost(),item.cartDetail.getCartDetailQuantity(),item);
                check.set(position,true);
            }
            else{
                itemClickListener.onCartItemClick(position,"subs",item.beverage.getBeverageCost(),item.cartDetail.getCartDetailQuantity(),item);
                check.set(position,false);
            }
        });

    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public List<Boolean> getCheckList(){
        return this.check;
    }



    @Override
    public void onCartItemClick(Integer position, String id, String beverage,Integer number, BeverageAndCartDetail item) {
        this.itemClickListener.onCartItemClick(position,id,beverage,number, item);
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
