package com.example.myjavaapp.View;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.myjavaapp.R;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class homeActivity extends AppCompatActivity {
    private TextView txtMail;
    private TextView txtName;
    private TextView txtIdToken;
    private GoogleSignInClient gClient;
    private GoogleSignInOptions gOptions;
    FirebaseUser user;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_screen);
        txtMail = (TextView) findViewById(R.id.txtMail);
        txtName = (TextView) findViewById(R.id.txtName);
        txtIdToken = (TextView) findViewById(R.id.txtIdToken);





        Intent intent = getIntent();
        Bundle data = intent.getExtras();
//        String type = data.getString("type");
        if(data != null){
            String name = data.getString("name");
            String idToken = data.getString("idtoken");
            String mail = data.getString("email");
            txtName.setText(name);
            txtIdToken.setText(idToken);
            txtMail.setText(mail);
        }


//         Lưu trạng thái đã đăng nhập của ngươi dùng
        SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs",MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean("isLogin",true);
        editor.apply();

        // lấy thông tin của user hiện tai
        user = FirebaseAuth.getInstance().getCurrentUser();
        if(user != null){
            String email = user.getEmail();
            //kiểm tra mail có được xác thực hay chưa
            boolean verifiedMail = user.isEmailVerified();
            String userID = user.getUid();
        }
    }
}
