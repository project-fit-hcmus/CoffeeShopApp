package com.example.myjavaapp.View;

import static android.app.Activity.RESULT_CANCELED;
import static android.app.Activity.RESULT_OK;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
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
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelStoreOwner;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myjavaapp.Model.LocalViewModel.LocalBeAndCartDetailViewModel;
import com.example.myjavaapp.Model.LocalViewModel.LocalCartDetailViewModel;
import com.example.myjavaapp.Model.LocalViewModel.LocalCartViewModel;
import com.example.myjavaapp.Model.dao.CartDetailDAO;
import com.example.myjavaapp.Model.database.AppDatabase;
import com.example.myjavaapp.Model.entity.BeverageAndCartDetail;
import com.example.myjavaapp.Model.entity.CartDetail;
import com.example.myjavaapp.R;
import com.example.myjavaapp.View.Adapter.CartItemAdapter;
import com.example.myjavaapp.View.Interfaces.CartItemClickListener;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
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
    private CartItemAdapter adapter;

    private List<BeverageAndCartDetail> listData;
    private CartDetailDAO cartDetailDAO;
    private String userId;
    private LocalCartViewModel cartViewModel;
    private LocalBeAndCartDetailViewModel beAndCartDetailViewModel;
    private LocalCartDetailViewModel cartDetailViewModel;
    private final int REQUEST_BOOKING = 11110;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Log.d("CART FRAGMENT","on create view");
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

        SharedPreferences sharedPreferences = getContext().getSharedPreferences("MyPrefs",Context.MODE_PRIVATE);
        String cartId = sharedPreferences.getString("CartUserId","");
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("cartdetails");

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
                adapter = new CartItemAdapter(getContext(), maps);
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
            Log.d("CHECKBOX",beverage + "----" + number);
            if(id.equals("plus")){
                String curQuantity = quantity.getText().toString();
                String curPrice = totalPrice.getText().toString();
                quantity.setText(String.valueOf(Integer.parseInt(curQuantity) + number));
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

        if(number == 0) {
            ref.child(id + beverage).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if(task.isSuccessful()){
                        Log.d("INFO","success delete on realtime, start delete in local");
                        cartDetailViewModel.deleteItemInCartDetail(beverage, id);
                    }else{
                        Log.d("INFO","something wrong on delete on realtime");
                    }
                }
            });
        }else{
            ref.child(id + beverage).child("cartDetailQuantity").setValue(number).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    cartDetailViewModel.UpdateQuantityOfAnItem(number,id,beverage);
                }
            });
        }


        quantity.setText(String.valueOf(0));
        totalPrice.setText("0$");
    }


    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.btnCheckout){
            List<Boolean> result = adapter.getCheckList();
            String output = encodeChechoutList(result);
            if(output.isEmpty()){
                Toast.makeText(getContext(),"Please choose item to check out!!!", Toast.LENGTH_SHORT).show();
                return;
            }
            // result là danh sách các item được chọn
            Intent send = new Intent(getContext(), BookingActivity.class);
            Bundle data = new Bundle();
            data.putString("listChoosen",output);
            data.putString("quantity",quantity.getText().toString());
            data.putString("totalCost",totalPrice.getText().toString());
            send.putExtras(data);
            startActivityForResult(send, REQUEST_BOOKING);
        }
    }

    public void updateInRealtime(String id, String beverage){
        cartDetailViewModel.getAllItems().observe(getViewLifecycleOwner(), new Observer<List<CartDetail>>() {
            @Override
            public void onChanged(List<CartDetail> cartDetails) {
                DatabaseReference ref = FirebaseDatabase.getInstance().getReference("cartdetails");
                for(CartDetail i  : cartDetails){
                    ref.child(i.getCartDetailId() + i.getCartDetailBeverage()).setValue(i);
                }
                ref.child(id + beverage).removeValue();
            }
        });
    }

    public String encodeChechoutList(List<Boolean> result){
        String str = "";
        for(int i =0; i < result.size(); ++i){
            if(result.get(i) == true) {
                if (!str.isEmpty())
                    str += "-";
                str += String.valueOf(i);
            }
        }
        return str;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == REQUEST_BOOKING ){
            if(resultCode == RESULT_OK){

            }
            else if(resultCode == RESULT_CANCELED){
                quantity.setText("0");
                totalPrice.setText("0$");
            }
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        Log.d("CART FRAGMENT","on destroy view");
    }

    @Override
    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
        Log.d("CART FRAGMENT", "on view state restored");
    }
}
