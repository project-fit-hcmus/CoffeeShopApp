package com.example.myjavaapp.View;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.viewpager.widget.ViewPager;
import androidx.viewpager2.widget.ViewPager2;

import com.example.myjavaapp.R;
import com.example.myjavaapp.View.HistoryActions.ViewPagerAdapter;
import com.google.android.material.tabs.TabLayout;

public class OrderHistory extends AppCompatActivity implements View.OnClickListener{
    private ImageView imgBack;
    private TabLayout tabLayout;
    private ViewPager2 viewPager2;
    private ViewPagerAdapter viewPagerAdapter;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.order_history_screen);
        //definition
        imgBack = findViewById(R.id.logo);
        tabLayout = findViewById(R.id.historyTabs);
        viewPager2 =  findViewById(R.id.historyViewPager);

        viewPagerAdapter = new ViewPagerAdapter(this);
        viewPager2.setAdapter(viewPagerAdapter);

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager2.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });






        imgBack.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.logo){
            setResult(RESULT_OK);
            finish();
        }
    }
}