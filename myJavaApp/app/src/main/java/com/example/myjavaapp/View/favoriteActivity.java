package com.example.myjavaapp.View;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.myjavaapp.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class favoriteActivity extends AppCompatActivity {
    private BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.favorite_screen);
//        bottomNavigationView = (BottomNavigationView) findViewById(R.id.favoriteBottomNav);



        // bottom navigation action
//        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
//            @Override
//            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
//                if(item.getItemId() == R.id.page_profile){
//                    startActivity(new Intent(favoriteActivity.this, ProfileActivity.class));
//                    return true;
//                }
//                if(item.getItemId() == R.id.page_favorite){
//                    //to do something
//                    return true;
//                }
//                if(item.getItemId() == R.id.page_cart){
//                    startActivity(new Intent(favoriteActivity.this, cartActivity.class));
//                    return true;
//                }
//                if(item.getItemId() == R.id.page_home){
//                    //to do something
//                    startActivity(new Intent(favoriteActivity.this, homeActivity.class));
//                    return true;
//                }
//                return false;
//            }
//        });
    }
}
