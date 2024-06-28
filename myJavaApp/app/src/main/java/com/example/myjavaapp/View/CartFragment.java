package com.example.myjavaapp.View;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelStoreOwner;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myjavaapp.Model.LocalViewModel.LocalBeAndCartDetailViewModel;
import com.example.myjavaapp.Model.LocalViewModel.LocalBeverageViewModel;
import com.example.myjavaapp.Model.LocalViewModel.LocalCartDetailViewModel;
import com.example.myjavaapp.Model.LocalViewModel.LocalCartViewModel;
import com.example.myjavaapp.Model.dao.CartDetailDAO;
import com.example.myjavaapp.Model.database.AppDatabase;
import com.example.myjavaapp.Model.entity.Beverage;
import com.example.myjavaapp.Model.entity.BeverageAndCartDetail;
import com.example.myjavaapp.Model.entity.CartDetail;
import com.example.myjavaapp.R;
import com.example.myjavaapp.View.Adapter.CartItemAdapter;
import com.example.myjavaapp.View.Interfaces.CartItemClickListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class CartFragment extends Fragment implements CartItemClickListener, View.OnClickListener {
    private TextView quantity, totalPrice;
    private Button btnCheckout;
    private RecyclerView recyclerView;
    private FirebaseUser user;
    private CircleImageView userAvatar;

    private List<BeverageAndCartDetail> listData;
    private CartDetailDAO cartDetailDAO;
    private String userId;
    private LocalCartViewModel cartViewModel;
    private LocalBeAndCartDetailViewModel beAndCartDetailViewModel;
    private LocalCartDetailViewModel cartDetailViewModel;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.cart_screen,container,false);
        quantity = (TextView) v.findViewById(R.id.quantityContent);
        totalPrice = (TextView) v.findViewById(R.id.totalPriceContent);
        recyclerView = (RecyclerView) v.findViewById(R.id.listItem);
        btnCheckout = (Button) v.findViewById(R.id.btnCheckout);
        userAvatar = (CircleImageView) v.findViewById(R.id.userImg);


        user = FirebaseAuth.getInstance().getCurrentUser();
        Uri photo = user.getPhotoUrl();
        if(photo != null){
            Picasso.get().load(photo).into(userAvatar);
        }

        // fetch data
        userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        cartDetailDAO = AppDatabase.getDatabase(getContext()).cartDetailDAO();
        cartViewModel = new ViewModelProvider((ViewModelStoreOwner) getContext()).get(LocalCartViewModel.class);
        beAndCartDetailViewModel = new ViewModelProvider((ViewModelStoreOwner) getContext()).get(LocalBeAndCartDetailViewModel.class);
        cartDetailViewModel = new ViewModelProvider((ViewModelStoreOwner) getContext()).get(LocalCartDetailViewModel.class);
        beAndCartDetailViewModel.getBeverageInCartDetail(userId).observe(getViewLifecycleOwner(), new Observer<List<BeverageAndCartDetail>>() {
            @Override
            public void onChanged(List<BeverageAndCartDetail> maps) {
                if (maps != null && maps.isEmpty())
                    return;
                listData = maps;
                // set adapter
                CartItemAdapter adapter = new CartItemAdapter(getContext(), maps);
                adapter.setClickListener(CartFragment.this);
                recyclerView.setAdapter(adapter);
                recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
            }
        });


        btnCheckout.setOnClickListener(this);

        return v;
    }

    @Override
    public void onCartItemClick(Integer position, String id, String beverage, Integer number, BeverageAndCartDetail item) {
        if(id.equals("plus") || id.equals("subs")){
            Toast.makeText(getContext(), beverage + "----" + number, Toast.LENGTH_SHORT).show();
            Log.d("CHECKBOX",beverage + "----" + number);
            if(id.equals("plus")){
                String curQuantity = quantity.getText().toString();
                String curPrice = totalPrice.getText().toString();
                quantity.setText(String.valueOf(Integer.parseInt(curQuantity) + number));
                Toast.makeText(getContext(), curPrice.substring(0,curPrice.length()-1), Toast.LENGTH_SHORT).show();
                totalPrice.setText(String.valueOf(Integer.parseInt(curPrice.substring(0,curPrice.length()-1)) + Integer.parseInt(beverage.substring(0,beverage.length()-1))*number) + "$");
            }else if(id.equals("subs")){
                String curQuantity = quantity.getText().toString();
                String curPrice = totalPrice.getText().toString();
                quantity.setText(String.valueOf(Integer.parseInt(curQuantity) - number));
                totalPrice.setText(String.valueOf(Integer.parseInt(curPrice.substring(0,curPrice.length()-1)) - Integer.parseInt(beverage.substring(0,beverage.length()-1))*number) + "$");
            }
            return;
        }

        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("cartdetails");

        if(number == 0){            //ERROR
            Toast.makeText(getContext(),"cart id: " + id + "--- beverage: " + beverage, Toast.LENGTH_SHORT).show();
            cartDetailViewModel.deleteItemInCartDetail(beverage,id);
//            cartDetailViewModel.DeleteItemInCartDetail(item.cartDetail);
            ref.child(id+beverage).removeValue();
        }else {
            ref.child(id + beverage).child("cartDetailQuantity").setValue(number);
        }


        quantity.setText(String.valueOf(0));
        totalPrice.setText("0$");
    }


    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.btnCheckout){
            startActivityForResult(new Intent(getContext(),BookingActivity.class), 11110);
        }
    }
}
