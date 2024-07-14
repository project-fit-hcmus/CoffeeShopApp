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
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelStoreOwner;

import com.example.myjavaapp.Model.LocalViewModel.LocalUserViewModel;
import com.example.myjavaapp.Model.entity.User;
import com.example.myjavaapp.R;
import com.example.myjavaapp.View.ProfileAction.ChangePasswordActivity;
import com.example.myjavaapp.View.ProfileAction.EditAccountActivity;
import com.example.myjavaapp.View.started.LoginActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

public class ProfileFragment extends Fragment implements View.OnClickListener{
    private FirebaseUser user;
    private CircleImageView userAvatar;
    private TextView userName;
    private TextView txtEmail;
    private AppCompatButton btnLogout;
    private AppCompatButton btnManageAcc, btnPayment, btnManagePass, btnMyOrder, btnPolicy;
    private static final int LOGOUT_ACTION = 11111;
    private static final int EDIT_ACCOUNT_ACTION = 11112;
    private static final int ORDER_HISTORY_ACTION = 11113;
    private static final int CHANGE_PASSWORD_ACTION = 11114;
    private static final int PAYMENT_METHOD_ACTION = 11115;
    private static final int POLICY_ACTION = 11116;
    private TextView userPhoneNumber, userLocation;
    private LocalUserViewModel userViewModel;
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
        }
        userName.setText(user.getDisplayName());
        txtEmail.setText(user.getEmail());

        // đọc và hiển thị thông tin số điện thoại/địa chỉ của người dùng
        userViewModel = new ViewModelProvider((ViewModelStoreOwner) getContext()).get(LocalUserViewModel.class);
        userViewModel.getUserWithId(user.getUid()).observe(getViewLifecycleOwner(), new Observer<User>() {
            @Override
            public void onChanged(User us) {
                if(us == null)
                    return;
                userPhoneNumber.setText(us.getUserPhone());
                userLocation.setText(us.getUserLocation());
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
            startActivityForResult(new Intent(getContext(), LoginActivity.class), LOGOUT_ACTION);
            FirebaseAuth.getInstance().signOut();

        }
        if(v.getId() == R.id.btnEditAccount){
            //chỉnh sửa nội dung, thông tin tài khoản người dùng(tên người dùng, userphoto, địa chỉ location)
            startActivityForResult(new Intent(getContext(), EditAccountActivity.class),EDIT_ACCOUNT_ACTION);
        }
        if(v.getId() == R.id.btnOrder){
            //xem thông tin đơn đặt hàng(đã giao, đang giao, đã hủy)
            startActivityForResult(new Intent(getContext(), OrderHistory.class),ORDER_HISTORY_ACTION);
        }
        if(v.getId() == R.id.btnPassword){
            //thay đổi mật khẩu ...
            startActivityForResult(new Intent(getContext(), ChangePasswordActivity.class),CHANGE_PASSWORD_ACTION);
        }
        if(v.getId() == R.id.btnPayment){
            //quản lý phương thức thanh toán
        }
        if(v.getId() == R.id.btnPolicy){
            startActivityForResult(new Intent(getContext(),PrivacyActivity.class), POLICY_ACTION);
        }
    }
}
