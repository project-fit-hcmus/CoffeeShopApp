package com.example.myjavaapp.View;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.myjavaapp.Model.User;
import com.example.myjavaapp.R;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class homeActivity extends AppCompatActivity {
    private TextView txtMail;
    private TextView txtName;
    private TextView txtIdToken;
    private GoogleSignInClient gClient;
    private GoogleSignInOptions gOptions;
    private FirebaseUser user;
    private BottomNavigationView bottomNavigationView;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_screen);
        //definition
        txtMail = (TextView) findViewById(R.id.txtMail);
        txtName = (TextView) findViewById(R.id.txtName);
        txtIdToken = (TextView) findViewById(R.id.txtIdToken);
        bottomNavigationView = (BottomNavigationView) findViewById(R.id.homeBottomNav);
        user = FirebaseAuth.getInstance().getCurrentUser();

        //get phone information


        //navigate with bottom navigation
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                if(item.getItemId() == R.id.page_profile){
                    startActivity(new Intent(homeActivity.this, ProfileActivity.class));
                    Toast.makeText(homeActivity.this, "Move to Profile Screen",Toast.LENGTH_SHORT).show();
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
}
