package com.example.myjavaapp.View;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import com.example.myjavaapp.R;
import com.example.myjavaapp.View.ProfileAction.ChangePasswordActivity;
import com.example.myjavaapp.View.ProfileAction.EditAccountActivity;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

public class ProfileActivity extends AppCompatActivity implements View.OnClickListener {

    private FirebaseUser user;
    private CircleImageView userAvatar;
    private TextView userName;
    private TextView txtEmail;
    private AppCompatButton btnLogout;
    private AppCompatButton btnManageAcc, btnPayment, btnManagePass, btnMyOrder, btnPolicy;
    private TextView userPhoneNumber, userLocation;
    private BottomNavigationView bottomNavigationView;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.profile_screen);
        //definition
        user = FirebaseAuth.getInstance().getCurrentUser();
        userAvatar = (CircleImageView) findViewById(R.id.userPhoto);
        userName = (TextView)findViewById(R.id.userName);
        txtEmail = (TextView) findViewById(R.id.txtUserEmail);
        btnLogout = (AppCompatButton) findViewById(R.id.btnLogout);
        btnPayment = (AppCompatButton) findViewById(R.id.btnPayment);
        btnManagePass = (AppCompatButton) findViewById(R.id.btnPassword);
        btnPolicy = (AppCompatButton) findViewById(R.id.btnPolicy);
        btnManageAcc = (AppCompatButton) findViewById(R.id.btnEditAccount);
        btnMyOrder = (AppCompatButton) findViewById(R.id.btnOrder);
        userPhoneNumber = (TextView) findViewById(R.id.txtUserPhoneNumber);
        userLocation = (TextView) findViewById(R.id.txtUserLocation);
        Uri photo = user.getPhotoUrl();
        bottomNavigationView = (BottomNavigationView) findViewById(R.id.ProfileBottomNav);

        // bottom navigation action
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                if(item.getItemId() == R.id.page_profile){
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
                    startActivity(new Intent(ProfileActivity.this, homeActivity.class));
                    return true;
                }
                return false;
            }
        });


        if(photo != null){
            // convert to drawable image
            Picasso.get().load(photo).into(userAvatar);
        }
        else{
//            Toast.makeText(ProfileActivity.this, "No photo profile", Toast.LENGTH_SHORT).show();
        }
        userName.setText(user.getDisplayName());
        txtEmail.setText(user.getEmail());

        // đọc và hiển thị thông tin số điện thoại/địa chỉ của người dùng
        DatabaseReference userRef = FirebaseDatabase.getInstance().getReference("users").child(user.getUid());
        userRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String phone = snapshot.child("phoneNumber").getValue(String.class);
                String location = snapshot.child("location").getValue(String.class);
                userPhoneNumber.setText(phone);
                userLocation.setText(location);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        btnLogout.setOnClickListener(this);
        btnPolicy.setOnClickListener(this);
        btnPayment.setOnClickListener(this);
        btnManagePass.setOnClickListener(this);
        btnMyOrder.setOnClickListener(this);
        btnManageAcc.setOnClickListener(this);


    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.btnLogout){
            FirebaseAuth.getInstance().signOut();
            //xóa trạng thái đăng nhập của người dùng
            SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.remove("isLogin"); // Xóa giá trị "isLogin"
            editor.apply();
            startActivity(new Intent(ProfileActivity.this, LoginActivity.class));
        }
        if(v.getId() == R.id.btnEditAccount){
            //chỉnh sửa nội dung, thông tin tài khoản người dùng(tên người dùng, userphoto, địa chỉ location)
            startActivity(new Intent(ProfileActivity.this, EditAccountActivity.class));
        }
        if(v.getId() == R.id.btnOrder){
            //xem thông tin đơn đặt hàng(đã giao, đang giao, đã hủy)
        }
        if(v.getId() == R.id.btnPassword){
            //thay đổi mật khẩu ...
            startActivity(new Intent(ProfileActivity.this, ChangePasswordActivity.class));
        }
        if(v.getId() == R.id.btnPayment){
            //quản lý phương thức thanh toán
        }
        if(v.getId() == R.id.btnPolicy){
            //xem thông tin về chính sách của ứng dụng
        }

    }
}
