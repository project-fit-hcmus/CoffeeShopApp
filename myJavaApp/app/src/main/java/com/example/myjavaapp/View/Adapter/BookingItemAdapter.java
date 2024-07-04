package com.example.myjavaapp.View.Adapter;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.model.GlideUrl;
import com.example.myjavaapp.Model.entity.BeverageAndCartDetail;
import com.example.myjavaapp.R;
import com.example.myjavaapp.View.BookingActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.List;

public class BookingItemAdapter extends RecyclerView.Adapter<BookingItemAdapter.MyViewHolder> {

    private Context context;
    private List<BeverageAndCartDetail> data;
    public BookingItemAdapter(Context ctx, List<BeverageAndCartDetail> input){
        this.context = ctx;
        this.data = input;
    }

    @NonNull
    @Override
    public BookingItemAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.booking_item,parent,false);
        return  new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BookingItemAdapter.MyViewHolder holder, int position) {
        BeverageAndCartDetail item = data.get(position);
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

        holder.txtCost.setText(item.beverage.getBeverageCost());
        holder.txtQuantity.setText("x" + item.cartDetail.getCartDetailQuantity().toString());
        holder.txtType.setText(item.beverage.getBeverageType());
        holder.txtName.setText(item.beverage.getBeverageName());


    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public List<BeverageAndCartDetail> getData(){
        return  this.data;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{

        private ImageView icon ;
        private TextView txtName, txtType, txtQuantity, txtCost;


        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            icon = itemView.findViewById(R.id.itemIcon);
            txtName = itemView.findViewById(R.id.itemName);
            txtType = itemView.findViewById(R.id.itemType);
            txtQuantity = itemView.findViewById(R.id.itemQuantity);
            txtCost = itemView.findViewById(R.id.itemCost);
        }
    }
}
