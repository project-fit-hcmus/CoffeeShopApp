package com.example.myjavaapp.View.HistoryActions;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myjavaapp.Model.entity.Order;
import com.example.myjavaapp.R;

import java.util.List;

public class SingleItemAdapter extends RecyclerView.Adapter<SingleItemAdapter.MyViewHolder> {
    private Context context;
    private List<Order> data;

    public SingleItemAdapter(Context mainCtx, List<Order> data){
        this.context = mainCtx;
        this.data = data;
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
        holder.txtStatus.setText(item.getOrderStatus());
        holder.txtAddress.setText(item.getOrderAddress());
        holder.txtCost.setText(item.getOrderCost().toString() + "$");
    }

    @Override
    public int getItemCount() {
        return data.size() ;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{
        private TextView txtAddress, txtCost, txtDate, txtPhone, txtStatus;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            txtAddress = itemView.findViewById(R.id.address);
            txtCost = itemView.findViewById(R.id.cost);
            txtDate = itemView.findViewById(R.id.date);
            txtPhone = itemView.findViewById(R.id.phone);
            txtStatus = itemView.findViewById(R.id.status);
        }
    }

}
