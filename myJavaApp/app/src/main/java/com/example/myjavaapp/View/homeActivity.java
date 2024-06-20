package com.example.myjavaapp.View;


import android.content.ClipData;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.ItemTouchUIUtil;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import com.example.myjavaapp.View.Adapter.CategoryAdapter;
import com.example.myjavaapp.View.Adapter.homeBeverageAdapter;
import com.example.myjavaapp.View.Model.Category;
import com.example.myjavaapp.R;
import com.example.myjavaapp.View.Model.dao.BeverageDAO;
import com.example.myjavaapp.View.Model.dao.TypeDAO;
import com.example.myjavaapp.View.Model.database.AppDatabase;
import com.example.myjavaapp.View.Model.database.HandleDataToRoom;
import com.example.myjavaapp.View.Model.entity.Beverage;
import com.example.myjavaapp.View.Model.entity.Type;
import com.example.myjavaapp.ViewModel.TypeViewModel;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.squareup.picasso.Picasso;


import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.internal.cache.DiskLruCache;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.concurrent.RecursiveAction;

public class homeActivity extends AppCompatActivity implements ItemClickListener {

    private GoogleSignInClient gClient;
    private GoogleSignInOptions gOptions;
    private FirebaseUser user;
    private BottomNavigationView bottomNavigationView;
    private TextView txtGreeting;
    private CircleImageView userAvatar;
    private RecyclerView category, lstBeverages;

    //some data
    private ArrayList<Type> listType;
    private TypeViewModel typeViewModel;
    private TypeDAO typeDao;
    private LiveData<List<Beverage>> allBeverages;
    private Observer<List<Beverage>> beverageObserve;
    private LiveData<List<Type>> allTypes;
    private Observer<List<Type>> typesObserve;
    private HandleDataToRoom handleDataToRoom;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_screen);
        //definition
        bottomNavigationView = (BottomNavigationView) findViewById(R.id.homeBottomNav);
        user = FirebaseAuth.getInstance().getCurrentUser();
        txtGreeting = (TextView) findViewById(R.id.greetingHome);
        userAvatar = (CircleImageView) findViewById(R.id.userImg);
        category = (RecyclerView) findViewById(R.id.recyclerCategory);
        lstBeverages = (RecyclerView) findViewById(R.id.homeSubMainItems);

        //get user data
        user = FirebaseAuth.getInstance().getCurrentUser();
        Uri photo = user.getPhotoUrl();
        if(photo != null){
            Picasso.get().load(photo).into(userAvatar);
        }
        //get system time
        String gretting = getTime();
        gretting += " " + user.getDisplayName() + ",";
        txtGreeting.setText(gretting);

        //get Type ViewModel and set display on Recycle View
        typeViewModel = new ViewModelProvider(this).get(TypeViewModel.class);
        listType = new ArrayList<>();
        LiveData<DataSnapshot> liveData = typeViewModel.getDataSnapshotLiveData();
        if(typeDao == null)
            typeDao = AppDatabase.getDatabase(this).typeDAO();

        liveData.observe(this, new Observer<DataSnapshot>() {
            @Override
            public void onChanged(DataSnapshot dataSnapshot) {
                if(dataSnapshot != null){
                    for(DataSnapshot iterator : dataSnapshot.getChildren()){
                        listType.add(new Type(iterator.child("type_id").getValue(String.class), iterator.child("type_name").getValue(String.class), iterator.child("type_image").getValue(String.class)));
                    }
//                    CategoryAdapter adapter = new CategoryAdapter(homeActivity.this, listType);
//                    category.setAdapter(adapter);
//                    category.setLayoutManager(new LinearLayoutManager(homeActivity.this, LinearLayoutManager.HORIZONTAL,true));
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            typeDao.insertAllType(listType);
                        }
                    }).start();
                }
            }
        });

        allTypes = AppDatabase.getDatabase(this).typeDAO().getAllType();
        typesObserve = new Observer<List<Type>>() {
            @Override
            public void onChanged(List<Type> types) {
                if(types != null && types.isEmpty())
                    return;
                listType = new ArrayList<>(types);
                CategoryAdapter adapter = new CategoryAdapter(homeActivity.this, listType);
                adapter.setClickListener(homeActivity.this);
                category.setAdapter(adapter);
                category.setLayoutManager(new LinearLayoutManager(homeActivity.this, LinearLayoutManager.HORIZONTAL,true));
            }
        };
        allTypes.observe(this,typesObserve);
        // lấy danh sách các best seller của quán
        // thêm 1 item trong category là all
        // mở ứng dụng thì toàn bộ các thông tin cần thiết sẽ được lưu về local
        //=> khi cáo cập nhật thì update lại trong local và update trong realtime
        handleDataToRoom = new HandleDataToRoom(this);
        handleDataToRoom.getAllBeverage();

        allBeverages = AppDatabase.getDatabase(this).beverageDAO().getBestSellerBeverage();
        beverageObserve = new Observer<List<Beverage>>() {
            @Override
            public void onChanged(List<Beverage> beverages) {
                if(beverages != null && beverages.isEmpty())
                    return;
                homeBeverageAdapter adapter = new homeBeverageAdapter(homeActivity.this, beverages);
                lstBeverages.setAdapter(adapter);
                lstBeverages.setLayoutManager(new GridLayoutManager(homeActivity.this,2));

            }
        };
        allBeverages.observe(this,beverageObserve);











        //navigate with bottom navigation
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                if(item.getItemId() == R.id.page_profile){
                    startActivity(new Intent(homeActivity.this, ProfileActivity.class));
                    Toast.makeText(homeActivity.this, "Move to Profile Screen",Toast.LENGTH_SHORT).show();
                    return true;
                }
                if(item.getItemId() == R.id.page_profile){
                    //to do something
                    return true;
                }
                if(item.getItemId() == R.id.page_cart){
                    //to do something
                    return true;
                }
                if(item.getItemId() == R.id.page_home){
                    //to do something
                    return true;
                }
                return false;
            }
        });

//         Lưu trạng thái đã đăng nhập của ngươi dùng
        SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs",MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean("isLogin",true);
        editor.apply();

    }

    public String getTime(){

        Calendar calendar = Calendar.getInstance();
        int currentHour = calendar.get(Calendar.HOUR_OF_DAY);
        Toast.makeText(homeActivity.this,"giờ hiện tại là: " + currentHour , Toast.LENGTH_SHORT).show();
        if(currentHour >= 0 && currentHour <= 12 )
            return "Good Morning";
        else if(currentHour <= 17){
            return "Good Afternoon";
        }else if(currentHour <= 22){
            return "Good Evening";
        }else return "Good Night";
    }

    private TypeDAO getTypeDao(){
        if(typeDao == null)
            typeDao = AppDatabase.getDatabase(this).typeDAO();
        return typeDao;
    }


    @Override
    public void onItemClick(int position, String id) {
        Bundle data = new Bundle();
        data.putString("keyword",id);
        Intent intent = new Intent(homeActivity.this, homeSingleCateScreen.class);
        intent.putExtras(data);
        startActivityForResult(intent, 11111);
    }
}


//reference: https://www.globallogic.com/latam/insights/blogs/viewmodel-firebase-database/

