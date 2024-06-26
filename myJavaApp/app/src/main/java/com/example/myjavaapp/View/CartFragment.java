package com.example.myjavaapp.View;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myjavaapp.Model.dao.CartDetailDAO;
import com.example.myjavaapp.Model.database.AppDatabase;
import com.example.myjavaapp.Model.entity.BeverageAndCartDetail;
import com.example.myjavaapp.Model.entity.CartDetail;
import com.example.myjavaapp.R;
import com.example.myjavaapp.View.Adapter.CartItemAdapter;
import com.example.myjavaapp.View.Interfaces.CartItemClickListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

public class CartFragment extends Fragment implements CartItemClickListener {
    private TextView quantity, totalPrice;
    private Button btnCheckout;
    private RecyclerView recyclerView;
    private LiveData<List<BeverageAndCartDetail>> rawData;
    private Observer<List<BeverageAndCartDetail>> rawDataObserver;
    private List<BeverageAndCartDetail> listData;
    private LiveData<String> cartIdLive;
    private Observer<String> cartIdObserver;
    private String cartId;
    private CartDetailDAO cartDetailDAO;

    private String userId;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.cart_screen,container,false);
        quantity = (TextView) v.findViewById(R.id.quantityContent);
        totalPrice = (TextView) v.findViewById(R.id.totalPriceContent);
        recyclerView = (RecyclerView) v.findViewById(R.id.listItem);
        btnCheckout = (Button) v.findViewById(R.id.btnCheckout);

        // fetch data
        userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        cartIdLive = AppDatabase.getDatabase(getContext()).cartDAO().getCartIdFromUser(userId);
        cartDetailDAO = AppDatabase.getDatabase(getContext()).cartDetailDAO();
        cartIdObserver = new Observer<String>() {
            @Override
            public void onChanged(String s) {
                if(s != null && s.isEmpty())
                    return;
                cartId = s;
                Log.d("CART ID ", s);
                rawData = AppDatabase.getDatabase(getContext()).cartDetailAndBeverageDAO().getBeverageInCartDetail(s);
                rawDataObserver = new Observer<List<BeverageAndCartDetail>>() {
                    @Override
                    public void onChanged(List<BeverageAndCartDetail> maps) {
                        if(maps != null && maps.isEmpty())
                            return;
                        listData = maps;
                        // set adapter
                        CartItemAdapter adapter = new CartItemAdapter(getContext(),maps);
                        adapter.setClickListener(CartFragment.this);
                        recyclerView.setAdapter(adapter);
                        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(),LinearLayoutManager.VERTICAL,false));
                    }
                };
                rawData.observe((LifecycleOwner) getContext(),rawDataObserver);
            }
        };
        cartIdLive.observe((LifecycleOwner) getContext(),cartIdObserver);
        return v;
    }

    //?? xóa một item ra khỏi giỏ hàng???
    @Override
    public void onCartItemClick(Integer position, String id, String beverage, Integer number) {
        if(id.equals("plus") || id.equals("subs")){
            Toast.makeText(getContext(), beverage + "----" + number, Toast.LENGTH_SHORT).show();
            Log.d("CHECKBOX",beverage + "----" + number);
            if(id.equals("plus")){
                String curQuantity = quantity.getText().toString();
                String curPrice = totalPrice.getText().toString();
                quantity.setText(String.valueOf(Integer.parseInt(curQuantity) + number));
                Toast.makeText(getContext(), curPrice.substring(0,curPrice.length()-1), Toast.LENGTH_SHORT).show();
                totalPrice.setText(String.valueOf(Integer.parseInt(curPrice.substring(0,curPrice.length()-1)) + Integer.parseInt(beverage.substring(0,beverage.length()-1))) + "$");
            }else if(id.equals("subs")){
                String curQuantity = quantity.getText().toString();
                String curPrice = totalPrice.getText().toString();
                quantity.setText(String.valueOf(Integer.parseInt(curQuantity) - number));
                totalPrice.setText(String.valueOf(Integer.parseInt(curPrice.substring(0,curPrice.length()-1)) - Integer.parseInt(beverage.substring(0,beverage.length()-1))) + "$");
            }
            return;
        }

        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("cartdetails");
//        if(number == 0){            //ERROR
//            AppDatabase.getDatabase(getContext()).cartDetailDAO().DeleteItemInCartDetail(new CartDetail(id,beverage,1));
//            ref.child(id+beverage).removeValue();
//        }
        quantity.setText(String.valueOf(0));
        totalPrice.setText("0$");
        ref.child(id+beverage).child("cartDetailQuantity").setValue(number);
    }

}
