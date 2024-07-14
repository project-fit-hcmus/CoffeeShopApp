package com.example.myjavaapp.View;

import android.Manifest;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.viewpager.widget.ViewPager;

import com.example.myjavaapp.Model.LocalViewModel.LocalCartDetailViewModel;
import com.example.myjavaapp.Model.LocalViewModel.LocalCartViewModel;
import com.example.myjavaapp.Model.LocalViewModel.LocalOrderViewModel;
import com.example.myjavaapp.Model.LocalViewModel.LocalUserViewModel;
import com.example.myjavaapp.Model.database.HandleDataToRoom;
import com.example.myjavaapp.R;
import com.example.myjavaapp.View.Adapter.ViewPagerAdapter;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import java.util.Random;


public class mainActivity extends AppCompatActivity {

    private FirebaseUser account;
    private BottomNavigationView bottomNavigationView;
    private ViewPager viewPager;
    private int REQUEST_CODE = 1133;
    private int LOCATION_PERMISSION_REQUEST = 2;



    private HandleDataToRoom handleDataToRoom = new HandleDataToRoom(this);
    private LocalCartViewModel cartViewModel;
    private LocalCartDetailViewModel cartDetailViewModel;
    private LocalUserViewModel userViewModel;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_screen);

        account = FirebaseAuth.getInstance().getCurrentUser();
        Toast.makeText(mainActivity.this,account.getUid(),Toast.LENGTH_SHORT).show();
        Log.d("MAIN ACTIVITY", account.getUid());
        //         Lưu trạng thái đã đăng nhập của ngươi dùng
        SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs",MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean("isLogin",true);
        editor.putString("UserId",account.getUid());
        editor.apply();


        //get data from realtime firebase
        handleDataToRoom = new HandleDataToRoom(this);
        handleDataToRoom.getAllTypes();
        handleDataToRoom.getAllBeverage();
        handleDataToRoom.getAllCart();
        handleDataToRoom.getAllCartDetail();
        handleDataToRoom.getAllFavoriteItems(account.getUid());
        handleDataToRoom.getAllOrder(account.getUid());
        handleDataToRoom.getAllOrderDetail();
        handleDataToRoom.getAllComment();
        handleDataToRoom.getUser(account.getUid());


        // Request Permissions
        ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},REQUEST_CODE);
        ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.ACCESS_FINE_LOCATION,Manifest.permission.ACCESS_COARSE_LOCATION},LOCATION_PERMISSION_REQUEST);



        cartViewModel = new ViewModelProvider(this).get(LocalCartViewModel.class);
        cartDetailViewModel = new ViewModelProvider(this).get(LocalCartDetailViewModel.class);
        userViewModel = new ViewModelProvider(this).get(LocalUserViewModel.class);


        bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottomNav);
        viewPager = (ViewPager) findViewById(R.id.viewPager);
        setupViewPager();
        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                if (item.getItemId() == R.id.page_home) {
                    viewPager.setCurrentItem(0);
                    Log.d("ON CLICK", String.valueOf(0));

                } else if (item.getItemId() == R.id.page_favorite) {
                    Log.d("ON CLICK", String.valueOf(1));
                    viewPager.setCurrentItem(1);
                } else if (item.getItemId() == R.id.page_cart) {
                    Log.d("ON CLICK", String.valueOf(2));
                    viewPager.setCurrentItem(2);
                } else {
                    Log.d("ON CLICK", String.valueOf(3));
                    viewPager.setCurrentItem(3);
                }
                return true;
            }
        });



        cartViewModel.getCartIdFromUser(account.getUid()).observe(this, new Observer<String>() {
            @Override
            public void onChanged(String s) {
                editor.putString("CartUserId",s);
                editor.apply();
            }
        });

    }

    public void setupViewPager(){
        ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager(), FragmentStatePagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        viewPager.setAdapter(viewPagerAdapter);

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }
            @Override
            public void onPageSelected(int position) {
                switch (position){
                    case 0:
                        bottomNavigationView.getMenu().findItem(R.id.page_home).setChecked(true);
                        break;
                    case 1:
                        bottomNavigationView.getMenu().findItem(R.id.page_favorite).setChecked(true);
                        break;
                    case 2:
                        bottomNavigationView.getMenu().findItem(R.id.page_cart).setChecked(true);
                        break;
                    case 3:
                        bottomNavigationView.getMenu().findItem(R.id.page_profile).setChecked(true);
                        break;
                }

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }
    public static String randomGenerator(){
        String alphabet = "ABCDEFGHIJKLMNOPRSTUWXYZ";
        String numbers = "1234567890";
        Random random = new Random();
        StringBuilder strBuilder = new StringBuilder();
        for(int i = 0; i < 3; ++i){
            int index = random.nextInt(alphabet.length());
            strBuilder.append(alphabet.charAt(index));
        }
        for(int i = 0; i < 2; ++i){
            int index = random.nextInt(numbers.length());
            strBuilder.append(numbers.charAt(index));
        }
        return strBuilder.toString();
    }
}
