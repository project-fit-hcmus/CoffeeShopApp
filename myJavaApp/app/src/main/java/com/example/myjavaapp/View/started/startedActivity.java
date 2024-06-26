package com.example.myjavaapp.View.started;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import com.example.myjavaapp.R;
import com.example.myjavaapp.View.mainActivity;
//import com.example.myjavaapp.View.homeActivity;

public class startedActivity extends AppCompatActivity {
    private AppCompatButton btnStart;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // kiểm tra người dùng đã đăng nhập thành công trước đó hay chưa
        SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs",MODE_PRIVATE);
        boolean isLogin = sharedPreferences.getBoolean("isLogin",false);
        if (isLogin){
            //nguười dùng đã đăng nhập => chuyển tới màn hình home
//            startActivity(new Intent(startedActivity.this, homeActivity.class));
            startActivity(new Intent(startedActivity.this, mainActivity.class));

        }else{
            //người dùng chưa đăng nhập, ở lại trang hiện tại
        }

        setContentView(R.layout.started_screen);
        btnStart = (AppCompatButton) findViewById(R.id.btnStarted);
        btnStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(startedActivity.this,RegisterActivity.class));
            }
        });
    }
}
