package com.example.myjavaapp.View;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myjavaapp.Model.entity.CartDetail;
import com.example.myjavaapp.R;
import com.example.myjavaapp.View.Adapter.homeBeverageAdapter;
import com.example.myjavaapp.Model.database.AppDatabase;
import com.example.myjavaapp.Model.entity.Beverage;
import com.example.myjavaapp.View.Interfaces.BeverageItemClickListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

public class homeSingleCateScreen extends AppCompatActivity implements BeverageItemClickListener {
    private String keywork ;
    private RecyclerView listData;
    private FirebaseUser user;

    private TextView title;
    private ImageButton btnBack;
    private LiveData<List<Beverage>> lstBeverage;
    private Observer<List<Beverage>> listBeverageObserve;
    private LiveData<String> titleCate;
    private Observer<String> observeTitle;
    private homeBeverageAdapter adapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.single_category_screen);
        // definition components
        title = (TextView) findViewById(R.id.mainTitle);
        btnBack = (ImageButton) findViewById(R.id.btnBack);
        listData = (RecyclerView) findViewById(R.id.listBeverageInSingleCate);
        user = FirebaseAuth.getInstance().getCurrentUser();


        // get data intent
        Intent receiver = getIntent();
        Bundle data = receiver.getExtras();
        keywork = data.getString("keyword");

        // tìm tên cate theo id để set title
        titleCate = AppDatabase.getDatabase(homeSingleCateScreen.this).typeDAO().getNameFromId(keywork);
        observeTitle = new Observer<String>() {
            @Override
            public void onChanged(String s) {

                if(s != null && s.isEmpty())
                    return;
                title.setText(s);

            }
        };
        titleCate.observe(this,observeTitle);

        // get list data for recycler view
        lstBeverage = AppDatabase.getDatabase(homeSingleCateScreen.this).beverageDAO().getBeveregaInType(keywork);
        listBeverageObserve = new Observer<List<Beverage>>() {
            @Override
            public void onChanged(List<Beverage> beverages) {
                if(beverages != null && beverages.isEmpty())
                    return;
                // set adapter for recycler view
                adapter = new homeBeverageAdapter(homeSingleCateScreen.this,beverages);
                adapter.setItemClickListener(homeSingleCateScreen.this);
                listData.setAdapter(adapter);
                listData.setLayoutManager(new GridLayoutManager(homeSingleCateScreen.this,2));

            }
        };
        lstBeverage.observe(this,listBeverageObserve);


        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setResult(RESULT_OK);
                finish();
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
                    LiveData<Boolean> isExistsLive = AppDatabase.getDatabase(homeSingleCateScreen.this).cartDetailDAO().isExistBeverage(id, s);
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
                                LiveData<Integer> quantityLive = AppDatabase.getDatabase(homeSingleCateScreen.this).cartDetailDAO().getQuantityOfCartDetail(s, id);
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
                                quantityLive.observe(homeSingleCateScreen.this,quantityObserver);
                                isExistsLive.removeObserver(this);
                            }else {
                                Toast.makeText(homeSingleCateScreen.this, "NOT TRUE OR FALSE", Toast.LENGTH_SHORT).show();
                            }
                        }

                    };
                    isExistsLive.observe(homeSingleCateScreen.this,isExistObserver);
                    cartIdLive.removeObserver(this);
                }
            };
            cartIdLive.observe(homeSingleCateScreen.this, cartIdObserver);
        }
        else if(action.contains("single-beverage")){
            Intent intent = new Intent(this,SingleBeverageActivity.class);
            Bundle data = new Bundle();
            data.putString("beverageId",id);
            intent.putExtras(data);
            startActivityForResult(intent, 112233);

        }
    }
}
