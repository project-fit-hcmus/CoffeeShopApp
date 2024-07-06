package com.example.myjavaapp.View;

import android.content.Intent;
import android.content.SharedPreferences;
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
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myjavaapp.Model.LocalViewModel.LocalBeverageViewModel;
import com.example.myjavaapp.Model.LocalViewModel.LocalCartDetailViewModel;
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
import java.util.prefs.Preferences;

public class SearchActivity extends AppCompatActivity implements BeverageItemClickListener {
    private EditText edtSearch;
    private ImageButton btnSearch;
    private TextView txtKeyword;
    private RecyclerView recyclerView;
    private ImageView imgReturn;
    private String keyword;
    private FirebaseUser user;
    private LocalBeverageViewModel beverageViewModel;
    private LocalCartDetailViewModel cartDetailViewModel;
    private homeBeverageAdapter adapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search_screen);
        imgReturn = findViewById(R.id.logo);
        edtSearch = findViewById(R.id.keywordSearchHome);
        btnSearch = findViewById(R.id.btnSearch);
        txtKeyword = findViewById(R.id.hintKeywork);
        recyclerView = findViewById(R.id.findRecyclerview);
        adapter = new homeBeverageAdapter(SearchActivity.this);


        user = FirebaseAuth.getInstance().getCurrentUser();
        beverageViewModel = new ViewModelProvider(this).get(LocalBeverageViewModel.class);
        cartDetailViewModel = new ViewModelProvider(this).get(LocalCartDetailViewModel.class);

        Intent intent = getIntent();
        Bundle data = intent.getExtras();
        keyword = data.getString("keyword");
        String factor = "%" + keyword + "%";
        txtKeyword.setText("Kết quả tìm kiếm: " + keyword);

        Toast.makeText(this,factor,Toast.LENGTH_SHORT);
        Log.d("FACTOR",factor);

        beverageViewModel.findBeverageWithKeyword(factor).observe(this, new Observer<List<Beverage>>() {
            @Override
            public void onChanged(List<Beverage> beverages) {
//                if(beverages != null && beverages.isEmpty()) {
//                    Toast.makeText(SearchActivity.this, "IS EMPTY",Toast.LENGTH_SHORT).show();
//                    adapter.setAdapterData(beverages);
//                    recyclerView.getAdapter().notifyDataSetChanged();
//                }
                adapter.setAdapterData(beverages);
                adapter.setItemClickListener(SearchActivity.this);
                recyclerView.setAdapter(adapter);
                recyclerView.setLayoutManager(new GridLayoutManager(SearchActivity.this,2));
            }
        });


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
                    edtSearch.setText("");

                    beverageViewModel.findBeverageWithKeyword(factor).observe(SearchActivity.this, new Observer<List<Beverage>>() {
                        @Override
                        public void onChanged(List<Beverage> beverages) {
//                            if(beverages != null && beverages.isEmpty()) {
//                                Toast.makeText(SearchActivity.this, "IS EMPTY",Toast.LENGTH_SHORT).show();
//                                return;
//                            }
                            adapter.setAdapterData(beverages);
                            adapter.setItemClickListener(SearchActivity.this);
                            recyclerView.setAdapter(adapter);
                            recyclerView.setLayoutManager(new GridLayoutManager(SearchActivity.this,2));
                        }
                    });
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

            // get cart user id
            SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs",MODE_PRIVATE);
            String cartId = sharedPreferences.getString("CartUserId","");
            cartDetailViewModel.checkIfBeverageIsExist(id,cartId).observe(SearchActivity.this, new Observer<Integer>() {
                @Override
                public void onChanged(Integer integer) {
                    if(integer == null){
                        // add new
                        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("cartdetails");
                        ref.child(cartId + id).setValue(new CartDetail(cartId,id,1));
                        Toast.makeText(SearchActivity.this, "Add To Cart",Toast.LENGTH_SHORT).show();
                    }
                    else {
                        //update
                        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("cartdetails");
                        ref.child(cartId + id).child("cartDetailQuantity").setValue(integer + 1);
                        Toast.makeText(SearchActivity.this, "Add To Cart --- " + integer,Toast.LENGTH_SHORT).show();
                    }
                }
            });
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
