package com.example.myjavaapp.View;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myjavaapp.Model.LocalViewModel.LocalCartDetailViewModel;
import com.example.myjavaapp.Model.entity.CartDetail;
import com.example.myjavaapp.R;
import com.example.myjavaapp.View.Adapter.homeBeverageAdapter;
import com.example.myjavaapp.Model.database.AppDatabase;
import com.example.myjavaapp.Model.entity.Beverage;
import com.example.myjavaapp.View.Interfaces.BeverageItemClickListener;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
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
    private LocalCartDetailViewModel cartDetailViewModel;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.single_category_screen);
        // definition components
        title = (TextView) findViewById(R.id.mainTitle);
        btnBack = (ImageButton) findViewById(R.id.btnBack);
        listData = (RecyclerView) findViewById(R.id.listBeverageInSingleCate);
        user = FirebaseAuth.getInstance().getCurrentUser();
        cartDetailViewModel = new ViewModelProvider(this).get(LocalCartDetailViewModel.class);
        adapter = new homeBeverageAdapter(homeSingleCateScreen.this);


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
                adapter.setAdapterData(beverages);
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

            //get CartId
            SharedPreferences sharedPreferences = homeSingleCateScreen.this.getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
            String cartId = sharedPreferences.getString("CartUserId","");
            Toast.makeText(homeSingleCateScreen.this,cartId,Toast.LENGTH_SHORT).show();

            handleUpdateData(id, cartId);
        }
        else if(action.contains("single-beverage")){
            Intent intent = new Intent(this,SingleBeverageActivity.class);
            Bundle data = new Bundle();
            data.putString("beverageId",id);
            intent.putExtras(data);
            startActivityForResult(intent, 112233);

        }
    }


    public void handleUpdateData(String id, String cartId){
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("cartdetails");
        LiveData<Boolean> isExistsLive = AppDatabase.getDatabase(homeSingleCateScreen.this).cartDetailDAO().isExistBeverage(id, cartId);
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
                                Toast.makeText(homeSingleCateScreen.this,"There are some error when add item to cart!!!", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                    isExistsLive.removeObserver(this);
                }else if(aBoolean == true){
                    Log.d("RESULT CHECK", "true");
                    //update for cardetail
                    LiveData<Integer> quantityLive = AppDatabase.getDatabase(homeSingleCateScreen.this).cartDetailDAO().getQuantityOfCartDetail(cartId, id);
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
                                        Toast.makeText(homeSingleCateScreen.this,"There are some errors when update item!!!",Toast.LENGTH_SHORT).show();
                                }
                            });
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

    }
}
