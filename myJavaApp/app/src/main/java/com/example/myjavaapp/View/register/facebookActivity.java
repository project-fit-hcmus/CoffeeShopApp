package com.example.myjavaapp.View.register;

import static android.content.ContentValues.TAG;

import android.content.Intent;
import android.hardware.Camera;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.myjavaapp.R;
//import com.example.myjavaapp.View.homeActivity;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthCredential;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;
import java.util.Arrays;

public class facebookActivity extends AppCompatActivity {
    private CallbackManager callbackManager;
    private LoginButton btnFacebook;
    private static final String EMAIL = "email";
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        callbackManager = CallbackManager.Factory.create();
        btnFacebook = (LoginButton) findViewById(R.id.btnFacebook);
        btnFacebook.setPermissions(Arrays.asList(EMAIL));
        mAuth = FirebaseAuth.getInstance();

        //callback registration
        btnFacebook.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                Log.d(TAG,"facebook:Success" + loginResult);
                handleFacebookAccessToken(loginResult.getAccessToken());
            }

            @Override
            public void onCancel() {
                Log.d(TAG,"facebook:Cancel");
            }

            @Override
            public void onError(@NonNull FacebookException e) {
                Log.d(TAG,"facebook:Error",e);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        //pass the activity result back to the facebook SDK
        callbackManager.onActivityResult(resultCode,resultCode,data);
    }

    @Override
    protected void onStart() {
        super.onStart();
        //check if the user is signed in(not-null) and update UI accordingly
//        FirebaseUser currentUser = mAuth.getCurrentUser();
//        UpdateUI(currentUser);
    }
    private void handleFacebookAccessToken(AccessToken accessToken){
        Log.d(TAG,"HandleFacebookAccessToken" + accessToken);
        AuthCredential credential  = FacebookAuthProvider.getCredential(accessToken.getToken());
        mAuth.signInWithCredential(credential).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    Log.d(TAG,"SigupWithFacebook:Success");
                    FirebaseUser user = mAuth.getCurrentUser();
                    updateUI(user);
                } else{
                    Log.d(TAG,"SignupWithFacebook:Failed");
                    Toast.makeText(facebookActivity.this,"Autentication Failed!!!",Toast.LENGTH_SHORT).show();
                    updateUI(null);
                }
            }
        });
    }

    public void updateUI(FirebaseUser user){
        if(user != null){
//            startActivity(new Intent(facebookActivity.this, homeActivity.class));
        }
    }

}
