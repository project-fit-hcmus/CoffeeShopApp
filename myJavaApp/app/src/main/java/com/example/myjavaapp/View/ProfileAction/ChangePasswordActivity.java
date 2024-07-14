package com.example.myjavaapp.View.ProfileAction;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import com.example.myjavaapp.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserInfo;

import java.util.List;

public class ChangePasswordActivity extends AppCompatActivity implements View.OnClickListener {
    private TextInputEditText edtOldPass, edtNewPass, edtRepeat;
    private AppCompatButton btnSave, btnCancel, btnBack;
    private TextView txtNotification, txtTitle;
    private FirebaseUser user;
    private TextInputLayout repeatLayout;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.change_password_screen);
        //definition
        edtOldPass = (TextInputEditText) findViewById(R.id.txtOldPass);
        edtNewPass = (TextInputEditText) findViewById(R.id.txtNewPass);
        edtRepeat = (TextInputEditText) findViewById(R.id.txtRepeatPass);
        btnSave = (AppCompatButton) findViewById(R.id.btnSave);
        btnCancel = (AppCompatButton) findViewById(R.id.btnCancel);
        btnBack = (AppCompatButton) findViewById(R.id.btnBack);
        txtNotification = (TextView) findViewById(R.id.txtNoti);
        repeatLayout = (TextInputLayout) findViewById(R.id.newPasswordRepeat);
        txtTitle = (TextView) findViewById(R.id.HeadTitle);
        user = FirebaseAuth.getInstance().getCurrentUser();
        btnBack.setOnClickListener(this);

        txtTitle.setText("Manage Password");

        // kiểm tra và vô hiệu hóa nếu người dùng đăng nhập bằng Google hay Facebook
        String provideId  = null;
        List<? extends UserInfo> providers = user.getProviderData();
        for(UserInfo userInfo: providers){
            String id = userInfo.getProviderId();
            if(id.equals("google.com")){
                provideId = "google.com";
            }
            if(id.equals("password")){
                provideId = "password";
            }
            if(id.equals("facebook.com")){
                provideId = "facebook.com";
            }
        }
        Toast.makeText(ChangePasswordActivity.this,provideId,Toast.LENGTH_SHORT).show();

        if(provideId.equals("google.com") || provideId.equals("facebook.com")){
            edtNewPass.setEnabled(false);
            edtRepeat.setEnabled(false);
            edtOldPass.setEnabled(false);
            btnCancel.setEnabled(false);
            btnSave.setEnabled(false);
            txtNotification.setText("You can not change password because of your authentication!!!");
        }else{
            edtNewPass.setEnabled(true);
            edtRepeat.setEnabled(true);
            edtOldPass.setEnabled(true);
            btnCancel.setEnabled(true);
            btnSave.setEnabled(true);
            btnSave.setOnClickListener(this);
            btnCancel.setOnClickListener(this);

        }

    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.btnBack){
            setResult(RESULT_OK);
            finish();

        }
        if(v.getId() == R.id.btnSave){
            changePassword();
        }
        if(v.getId() == R.id.btnCancel){
            edtOldPass.setText("");
            edtNewPass.setText("");
            edtRepeat.setText("");
        }
    }

    public void changePassword(){
        //check repeat password information
        String oldPass = edtOldPass.getText().toString();
        String newPass = edtNewPass.getText().toString();
        String repeatPass = edtRepeat.getText().toString();
        if(repeatPass.equals(newPass)){
            //change password
            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
            String email = user.getEmail();
            AuthCredential credential = EmailAuthProvider.getCredential(email,oldPass);
            user.reauthenticate(credential).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if(task.isSuccessful()){
                        user.updatePassword(newPass).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if(!task.isSuccessful()){
                                    Toast.makeText(ChangePasswordActivity.this,"Something went wrong. Please try it again!!! --- " + task.getException().getMessage(),Toast.LENGTH_SHORT).show();
                                }else{
                                    Toast.makeText(ChangePasswordActivity.this,"Password Successfully Modified!!!",Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                    }else{
                        Toast.makeText(ChangePasswordActivity.this,"FAILED: Your old password may be wrong",Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
        else{
            txtNotification.setText("**Vui lòng nhập lại đúng mật khẩu mới");
        }
    }
}
