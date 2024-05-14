package com.example.myjavaapp.View;

import static android.content.ContentValues.TAG;

import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import com.example.myjavaapp.R;
import com.example.myjavaapp.View.register.facebookActivity;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Arrays;

public class RegisterActivity extends AppCompatActivity {
    private TextInputEditText txtPassword;
    private TextInputEditText txtRepeatPassword;
    private TextInputEditText txtEmail;
    private AppCompatButton btnRegister;
    private SpannableString spannableString;
    private TextView txtLogin;
    private FirebaseAuth mAuth;
    private CallbackManager callbackManager;

    private static final String EMAIL = "email";



    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register_screen);
//        FacebookSdk.sdkInitialize(getApplicationContext());

        txtPassword = (TextInputEditText) findViewById(R.id.txtPassword);
        txtEmail = (TextInputEditText) findViewById(R.id.txtEmail);
        txtRepeatPassword = (TextInputEditText) findViewById(R.id.txtRepeatPassword);
        btnRegister = (AppCompatButton) findViewById(R.id.btnRegister);
        txtLogin = (TextView) findViewById(R.id.txtlogin);
        mAuth = FirebaseAuth.getInstance();
        spannableString = new SpannableString("Have an account? Login here");
        ClickableSpan clickableSpan = new ClickableSpan() {
            @Override
            public void onClick(@NonNull View widget) {
                startActivity(new Intent(RegisterActivity.this,LoginActivity.class));
            }
        };
//        facebook
//        callbackManager = CallbackManager.Factory.create();
//        btnFacebook = (LoginButton) findViewById(R.id.btnFacebook);
//        btnFacebook.setPermissions(Arrays.asList(EMAIL));

        // thiết lập cho ClickableSpan
        spannableString.setSpan(clickableSpan,23,27, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        // thiết lập span cho textview
        txtLogin.setText(spannableString);
        txtLogin.setMovementMethod(LinkMovementMethod.getInstance());


        //login with email and password
        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!CheckRegisterInformation(txtPassword.getText().toString(),txtRepeatPassword.getText().toString())) {
                    Toast.makeText(RegisterActivity.this, "The password is not match!!!", Toast.LENGTH_SHORT).show();
                }else{
                    // xử lý quá trình thêm account
                    AddAccount();
                }

            }
        });

        // login with facebook
        // Initialize Facebook Login button
        //[code here ... ]

        //callback registration
//        btnFacebook.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
//            @Override
//            public void onSuccess(LoginResult loginResult) {
//                Log.d(TAG,"facebook:Success" + loginResult);
//                handleFacebookAccessToken(loginResult.getAccessToken());
//            }
//
//            @Override
//            public void onCancel() {
//                Log.d(TAG,"facebook:Cancel");
//            }
//
//            @Override
//            public void onError(@NonNull FacebookException e) {
//                Log.d(TAG,"facebook:Error",e);
//            }
//        });


        

    }

    @Override
    protected void onStart() {
        super.onStart();
        //check if the user is sign in
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser != null){
            currentUser.reload();
        }
    }

    public boolean CheckRegisterInformation(String pass, String repeatPass){
        if(!pass.equals(repeatPass))
            return false;
        else return true;
    }
    public void AddAccount(){
        mAuth.createUserWithEmailAndPassword(txtEmail.getText().toString(), txtPassword.getText().toString()).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    //add credential

                    //sign in success, updated UI with user's information
                    Log.d(TAG,"createUserWithEmailAndPassword:Success");
                    FirebaseUser user=mAuth.getCurrentUser();
                    updateUI(user);
                }
                else{
                    //if singin if failed,display a message to user
                    Log.w(TAG,"createUserWithEmailAndPassword:Failed", task.getException());
                    Toast.makeText(RegisterActivity.this, "Authentication Failed!!!",Toast.LENGTH_SHORT).show();
                    updateUI(null);
                }
            }
        });
    }

    public void updateUI(FirebaseUser user){
        if(user == null) {
            // delete the information in InputEditText
            txtEmail.setText("");
            txtPassword.setText("");
            txtRepeatPassword.setText("");
        }else{
            //move to the sign in screen for user to sign in
            Toast.makeText(RegisterActivity.this,"Successful!!! Please log in to continue.", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
        }
    }



//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//
//        //pass the activity result back to the facebook SDK
//        callbackManager.onActivityResult(resultCode,resultCode,data);
//    }
//    private void handleFacebookAccessToken(AccessToken accessToken){
//        Log.d(TAG,"HandleFacebookAccessToken" + accessToken);
//        AuthCredential credential  = FacebookAuthProvider.getCredential(accessToken.getToken());
//
//        //ở đây phải làm hàm đăng ký chứ không phải đăng nhập(bởi vì chưa đăng ký nên sẽ không tìm thấy hashkey để đăng nhập vào)
//        mAuth.signInWithCredential(credential).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
//            @Override
//            public void onComplete(@NonNull Task<AuthResult> task) {
//                if(task.isSuccessful()){
//                    Log.d(TAG,"SigupWithFacebook:Success");
//                    FirebaseUser user = mAuth.getCurrentUser();
//                    updateFBSignUpUI(user);
//                } else{
//                    Log.d(TAG,"SignupWithFacebook:Failed");
//                    Toast.makeText(RegisterActivity.this,"Autentication Failed!!!",Toast.LENGTH_SHORT).show();
//                    updateFBSignUpUI(null);
//                }
//            }
//        });
//    }
//    public void updateFBSignUpUI(FirebaseUser user){
//        if(user != null){
//            startActivity(new Intent(RegisterActivity.this, homeActivity.class));
//        }
//    }

}
