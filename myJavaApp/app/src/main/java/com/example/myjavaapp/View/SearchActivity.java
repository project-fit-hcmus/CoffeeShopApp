package com.example.myjavaapp.View;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myjavaapp.Model.database.AppDatabase;
import com.example.myjavaapp.Model.entity.Beverage;
import com.example.myjavaapp.Model.entity.CartDetail;
import com.example.myjavaapp.R;
import com.example.myjavaapp.View.Adapter.homeBeverageAdapter;
import com.example.myjavaapp.View.Interfaces.BeverageItemClickListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

public class SearchActivity extends AppCompatActivity implements BeverageItemClickListener {
    private EditText edtSearch;
    private ImageButton btnSearch;
    private TextView txtKeyword;
    private RecyclerView recyclerView;
    private ImageView imgReturn;
    private String keyword;
    private FirebaseUser user;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search_screen);
        imgReturn = findViewById(R.id.logo);
        edtSearch = findViewById(R.id.keywordSearchHome);
        btnSearch = findViewById(R.id.btnSearch);
        txtKeyword = findViewById(R.id.hintKeywork);
        recyclerView = findViewById(R.id.findRecyclerview);

        user = FirebaseAuth.getInstance().getCurrentUser();

        Intent intent = getIntent();
        Bundle data = intent.getExtras();
        keyword = data.getString("keyword");
        String factor = "%" + keyword + "%";
        txtKeyword.setText("Kết quả tìm kiếm: " + keyword);

        Toast.makeText(this,factor,Toast.LENGTH_SHORT);
        Log.d("FACTOR",factor);

        LiveData<List<Beverage>> beverageLive = AppDatabase.getDatabase(this).beverageDAO().findBeverageWithKeyword(factor);
        Observer<List<Beverage>> beverageObserver = new Observer<List<Beverage>>() {
            @Override
            public void onChanged(List<Beverage> beverages) {
                if(beverages != null && beverages.isEmpty()) {
                    Toast.makeText(SearchActivity.this, "IS EMPTY",Toast.LENGTH_SHORT).show();
                    return;
                }
                homeBeverageAdapter adapter = new homeBeverageAdapter(SearchActivity.this, beverages);
                adapter.setItemClickListener(SearchActivity.this);
                recyclerView.setAdapter(adapter);
                recyclerView.setLayoutManager(new GridLayoutManager(SearchActivity.this,2));

            }
        };
        beverageLive.observe(SearchActivity.this, beverageObserver);

        imgReturn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setResult(RESULT_OK);
                finish();
            }
        });

        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(edtSearch.getText().toString().isEmpty()){
                    Toast.makeText(SearchActivity.this,"Please enter keyword to search!!!",Toast.LENGTH_SHORT).show();
                }
                else{
                    String key = edtSearch.getText().toString();
                    txtKeyword.setText("Kết quả tìm kiếm: " + key);
                    String factor = "%" + key + "%";
                    beverageLive.removeObserver(beverageObserver);
                    LiveData<List<Beverage>> beverageInsideLive = AppDatabase.getDatabase(SearchActivity.this).beverageDAO().findBeverageWithKeyword(factor);
                    Observer<List<Beverage>> beverageInsideObserver = new Observer<List<Beverage>>() {
                        @Override
                        public void onChanged(List<Beverage> beverages) {
                            if(beverages != null && beverages.isEmpty()) {
                                Toast.makeText(SearchActivity.this, "IS EMPTY",Toast.LENGTH_SHORT).show();
                                return;
                            }
                            homeBeverageAdapter adapter = new homeBeverageAdapter(SearchActivity.this, beverages);
                            adapter.setItemClickListener(SearchActivity.this);
                            recyclerView.setAdapter(adapter);
                            recyclerView.setLayoutManager(new GridLayoutManager(SearchActivity.this,2));

                        }
                    };
                    beverageInsideLive.observe(SearchActivity.this, beverageInsideObserver);
                }
            }
        });


    }

    @Override
    public void onBeverageClick(Integer position, String id, String action) {
        Log.d("ACTION",action);
        Log.d("BEVERAGE",id);
        if (action.contains("add-to-cart")) {
            Toast.makeText(this,"Added",Toast.LENGTH_SHORT).show();
            String Uid = user.getUid();
            Log.d("USER ID",Uid);
            LiveData<String> cartIdLive = AppDatabase.getDatabase(this).cartDAO().getCartIdFromUser(Uid);
            Observer<String> cartIdObserver = new Observer<String>() {
                @Override
                public void onChanged(String s) {
                    Log.d("cart id",s);
                    Log.d("beverage id",id);
                    if (s != null && s.isEmpty())
                        return;
                    LiveData<Boolean> isExistsLive = AppDatabase.getDatabase(SearchActivity.this).cartDetailDAO().isExistBeverage(id, s);
                    Observer<Boolean> isExistObserver = new Observer<Boolean>() {
                        @Override
                        public void onChanged(Boolean aBoolean) {
                            if(aBoolean == false){
                                Log.d("RESULT CHECK", "false");
                                //thêm mới cho cartdetail
                                CartDetail cartItem = new CartDetail(s,id,1);
                                DatabaseReference ref = FirebaseDatabase.getInstance().getReference("cartdetails");
                                ref.child(s + id).setValue(cartItem);
                                isExistsLive.removeObserver(this);
                            }else if(aBoolean == true){
                                Log.d("RESULT CHECK", "true");
                                //update trong cardetail
                                LiveData<Integer> quantityLive = AppDatabase.getDatabase(SearchActivity.this).cartDetailDAO().getQuantityOfCartDetail(s, id);
                                Observer<Integer> quantityObserver = new Observer<Integer>() {
                                    @Override
                                    public void onChanged(Integer integer) {
                                        Log.d("VALUE", String.valueOf(integer));
                                        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("cartdetails");
                                        String child = s + id;
                                        ref.child(child).child("cartDetailQuantity").setValue(integer + 1);
                                        quantityLive.removeObserver(this);
                                    }
                                };
                                quantityLive.observe(SearchActivity.this,quantityObserver);
                                isExistsLive.removeObserver(this);
                            }else {
                                Toast.makeText(SearchActivity.this, "NOT TRUE OR FALSE", Toast.LENGTH_SHORT).show();
                            }
                        }

                    };
                    isExistsLive.observe(SearchActivity.this,isExistObserver);
                    cartIdLive.removeObserver(this);
                }
            };
            cartIdLive.observe((LifecycleOwner) SearchActivity.this, cartIdObserver);
        }
        else if(action.contains("single-beverage")){
            Intent intent = new Intent(SearchActivity.this,SingleBeverageActivity.class);
            Bundle data = new Bundle();
            data.putString("beverageId",id);
            intent.putExtras(data);
            startActivityForResult(intent, 112233);
        }
    }
}
