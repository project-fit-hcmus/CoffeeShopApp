package com.example.myjavaapp.View;

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
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.myjavaapp.Model.LocalViewModel.LocalBeverageViewModel;
import com.example.myjavaapp.Model.LocalViewModel.LocalCartDetailViewModel;
import com.example.myjavaapp.Model.LocalViewModel.LocalCateViewModel;
import com.example.myjavaapp.Model.dao.TypeDAO;
import com.example.myjavaapp.Model.database.AppDatabase;
import com.example.myjavaapp.Model.entity.Beverage;
import com.example.myjavaapp.Model.entity.CartDetail;
import com.example.myjavaapp.Model.entity.Type;
import com.example.myjavaapp.R;
import com.example.myjavaapp.View.Adapter.CategoryAdapter;
import com.example.myjavaapp.View.Adapter.homeBeverageAdapter;
import com.example.myjavaapp.View.Interfaces.BeverageItemClickListener;
import com.example.myjavaapp.View.Interfaces.ItemClickListener;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class HomeFragment extends Fragment implements ItemClickListener, BeverageItemClickListener {
    private FirebaseUser user;
    private TextView txtGreeting;
    private EditText edtSearch;
    private ImageButton btnSearch;
    private CircleImageView userAvatar;
    private RecyclerView category, lstBeverages;
    private ArrayList<Type> listType;
    private LocalCateViewModel cateViewModel;
    private LocalBeverageViewModel beverageViewModel;
    private LocalCartDetailViewModel cartDetailViewModel;



    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.home_screen,container,false);
        user = FirebaseAuth.getInstance().getCurrentUser();
        txtGreeting = (TextView) v.findViewById(R.id.greetingHome);
        userAvatar = (CircleImageView) v.findViewById(R.id.userImg);
        category = (RecyclerView) v.findViewById(R.id.recyclerCategory);
        lstBeverages = (RecyclerView) v.findViewById(R.id.homeSubMainItems);
        edtSearch = (EditText) v.findViewById(R.id.keywordSearchHome);
        btnSearch = (ImageButton) v.findViewById(R.id.btnSearch);


        //get user data
        user = FirebaseAuth.getInstance().getCurrentUser();
        beverageViewModel = new ViewModelProvider(this).get(LocalBeverageViewModel.class);
        cateViewModel = new ViewModelProvider(this).get(LocalCateViewModel.class);
        cartDetailViewModel = new ViewModelProvider(this).get(LocalCartDetailViewModel.class);


        Uri photo = user.getPhotoUrl();
        if(photo != null){
            Picasso.get().load(photo).into(userAvatar);
        }
        //get system time
        String gretting = getTime();
        gretting += " " + user.getDisplayName() + ",";
        txtGreeting.setText(gretting);

        cateViewModel.getAllTypes().observe((LifecycleOwner) getContext(), new Observer<List<Type>>() {
            @Override
            public void onChanged(List<Type> types) {
                if(types != null && types.isEmpty())
                    return;
                listType = new ArrayList<>(types);
                CategoryAdapter adapter = new CategoryAdapter(getContext(), listType);
                adapter.setClickListener(HomeFragment.this);
                category.setAdapter(adapter);
                category.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL,false));
            }
        });

        //get hot beverage

        beverageViewModel.getListBestSeller().observe(getViewLifecycleOwner(), new Observer<List<Beverage>>() {
            @Override
            public void onChanged(List<Beverage> beverages) {
                if(beverages != null && beverages.isEmpty())
                    return;
                homeBeverageAdapter adapter = new homeBeverageAdapter(getContext(), beverages);
                adapter.setItemClickListener(HomeFragment.this);
                lstBeverages.setAdapter(adapter);
                lstBeverages.setLayoutManager(new GridLayoutManager(getContext(),2));
            }
        });


        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String keywork = edtSearch.getText().toString();
                if(keywork.isEmpty())
                    Toast.makeText(getContext(),"Please enter keyword to search",Toast.LENGTH_SHORT).show();
                else{
                    Intent intent = new Intent(getContext(),SearchActivity.class);
                    Bundle data = new Bundle();
                    data.putString("keyword",keywork);
                    intent.putExtras(data);
                    startActivityForResult(intent,12345);
                    edtSearch.setHint("Search...");
                    edtSearch.setText("");
                }

            }
        });



        return v;
    }

    public String getTime(){

        Calendar calendar = Calendar.getInstance();
        int currentHour = calendar.get(Calendar.HOUR_OF_DAY);
        if(currentHour >= 0 && currentHour <= 12 )
            return "Good Morning";
        else if(currentHour <= 17){
            return "Good Afternoon";
        }else if(currentHour <= 22){
            return "Good Evening";
        }else return "Good Night";
    }

    @Override
    public void onItemClick(int position, String id) {
        Bundle data = new Bundle();
        data.putString("keyword",id);
        Intent intent = new Intent(getContext(), homeSingleCateScreen.class);
        intent.putExtras(data);
        startActivityForResult(intent, 11111);
    }

    @Override
    public void onBeverageClick(Integer position, String id, String action) {
        Log.d("ACTION",action);
        Log.d("BEVERAGE",id);
        if (action.contains("add-to-cart")) {
            Toast.makeText(getContext(),"Added",Toast.LENGTH_SHORT).show();
            String Uid = user.getUid();
            Log.d("USER ID",Uid);

            //get CartId
            SharedPreferences sharedPreferences = getContext().getSharedPreferences("MyPrefs",Context.MODE_PRIVATE);
            String cartId = sharedPreferences.getString("CartUserId","");
            Toast.makeText(getContext(),cartId,Toast.LENGTH_SHORT).show();

            handleUpdateData(id, cartId);



        }
        else if(action.contains("single-beverage")){
            Intent intent = new Intent(getContext(),SingleBeverageActivity.class);
            Bundle data = new Bundle();
            data.putString("beverageId",id);
            intent.putExtras(data);
            startActivityForResult(intent, 112233);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Glide.with(this).pauseRequests();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == RESULT_OK)
        if(requestCode == 112233){
            Toast.makeText(getContext(),"112233",Toast.LENGTH_SHORT).show();

        }
        if(requestCode == 111111){
            Toast.makeText(getContext(),"111111",Toast.LENGTH_SHORT).show();

        }
    }

    public void handleUpdateData(String id, String cartId){
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("cartdetails");
        LiveData<Boolean> isExistsLive = AppDatabase.getDatabase(getContext()).cartDetailDAO().isExistBeverage(id, cartId);
        Observer<Boolean> isExistObserver = new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                if(aBoolean == false){
                    Log.d("RESULT CHECK", "false");
                    //add new cart detail
                    CartDetail cartItem = new CartDetail(cartId,id,1);
                    ref.child(cartId + id).setValue(cartItem).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful()){
                                cartDetailViewModel.insert(cartItem);
                            }
                            else{
                                Toast.makeText(getContext(),"There are some error when add item to cart!!!", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                    isExistsLive.removeObserver(this);
                }else if(aBoolean == true){
                    Log.d("RESULT CHECK", "true");
                    //update for cardetail
                    LiveData<Integer> quantityLive = AppDatabase.getDatabase(getContext()).cartDetailDAO().getQuantityOfCartDetail(cartId, id);
                    Observer<Integer> quantityObserver = new Observer<Integer>() {
                        @Override
                        public void onChanged(Integer integer) {
                            Log.d("VALUE", String.valueOf(integer));
                            DatabaseReference ref = FirebaseDatabase.getInstance().getReference("cartdetails");
                            String child = cartId + id;
                            Integer number = integer + 1;
                            ref.child(child).child("cartDetailQuantity").setValue(number).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if(task.isSuccessful())
                                        cartDetailViewModel.UpdateQuantityOfAnItem(number, cartId, id);
                                    else
                                        Toast.makeText(getContext(),"There are some errors when update item!!!",Toast.LENGTH_SHORT).show();
                                }
                            });
                            quantityLive.removeObserver(this);
                        }
                    };
                    quantityLive.observe(getViewLifecycleOwner(),quantityObserver);
                    isExistsLive.removeObserver(this);
                }else {
                    Toast.makeText(getContext(), "NOT TRUE OR FALSE", Toast.LENGTH_SHORT).show();
                }
            }

        };
        isExistsLive.observe(getViewLifecycleOwner(),isExistObserver);

    }
}
