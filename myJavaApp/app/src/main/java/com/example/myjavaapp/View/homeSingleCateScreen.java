package com.example.myjavaapp.View;

import android.app.Instrumentation;
import android.content.Context;
import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myjavaapp.R;
import com.example.myjavaapp.View.Adapter.CategoryAdapter;
import com.example.myjavaapp.View.Adapter.homeBeverageAdapter;
import com.example.myjavaapp.View.Model.database.AppDatabase;
import com.example.myjavaapp.View.Model.entity.Beverage;

import org.checkerframework.checker.units.qual.A;

import java.util.List;

public class homeSingleCateScreen extends AppCompatActivity {
    private String keywork ;
    private RecyclerView listData;
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
}
