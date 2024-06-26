package com.example.myjavaapp.View;

import static android.content.Context.MODE_PRIVATE;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.Fragment;

import com.example.myjavaapp.R;
import com.example.myjavaapp.View.ProfileAction.ChangePasswordActivity;
import com.example.myjavaapp.View.ProfileAction.EditAccountActivity;
import com.example.myjavaapp.View.started.LoginActivity;
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

public class ProfileFragment extends Fragment implements View.OnClickListener{
    private FirebaseUser user;
    private CircleImageView userAvatar;
    private TextView userName;
    private TextView txtEmail;
    private AppCompatButton btnLogout;
    private AppCompatButton btnManageAcc, btnPayment, btnManagePass, btnMyOrder, btnPolicy;
    private TextView userPhoneNumber, userLocation;
    private BottomNavigationView bottomNavigationView;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.profile_screen,container,false);


        //definition
        user = FirebaseAuth.getInstance().getCurrentUser();
        userAvatar = (CircleImageView) v.findViewById(R.id.userPhoto);
        userName = (TextView) v.findViewById(R.id.userName);
        txtEmail = (TextView) v.findViewById(R.id.txtUserEmail);
        btnLogout = (AppCompatButton) v.findViewById(R.id.btnLogout);
        btnPayment = (AppCompatButton) v.findViewById(R.id.btnPayment);
        btnManagePass = (AppCompatButton) v.findViewById(R.id.btnPassword);
        btnPolicy = (AppCompatButton) v.findViewById(R.id.btnPolicy);
        btnManageAcc = (AppCompatButton) v.findViewById(R.id.btnEditAccount);
        btnMyOrder = (AppCompatButton) v.findViewById(R.id.btnOrder);
        userPhoneNumber = (TextView) v.findViewById(R.id.txtUserPhoneNumber);
        userLocation = (TextView) v.findViewById(R.id.txtUserLocation);
        Uri photo = user.getPhotoUrl();

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

        return v;
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.btnLogout){
            FirebaseAuth.getInstance().signOut();
            //xóa trạng thái đăng nhập của người dùng
            SharedPreferences sharedPreferences = getActivity().getSharedPreferences("MyPrefs", MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.remove("isLogin"); // Xóa giá trị "isLogin"
            editor.apply();
            startActivityForResult(new Intent(getContext(), LoginActivity.class), 11111);
        }
        if(v.getId() == R.id.btnEditAccount){
            //chỉnh sửa nội dung, thông tin tài khoản người dùng(tên người dùng, userphoto, địa chỉ location)
            startActivityForResult(new Intent(getContext(), EditAccountActivity.class),11112);
        }
        if(v.getId() == R.id.btnOrder){
            //xem thông tin đơn đặt hàng(đã giao, đang giao, đã hủy)
        }
        if(v.getId() == R.id.btnPassword){
            //thay đổi mật khẩu ...
            startActivityForResult(new Intent(getContext(), ChangePasswordActivity.class),11113);
        }
        if(v.getId() == R.id.btnPayment){
            //quản lý phương thức thanh toán
        }
        if(v.getId() == R.id.btnPolicy){
            //xem thông tin về chính sách của ứng dụng
        }
    }
}
