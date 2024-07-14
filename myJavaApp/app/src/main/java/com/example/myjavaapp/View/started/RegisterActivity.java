package com.example.myjavaapp.View.started;

import static android.content.ContentValues.TAG;

import android.content.Intent;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import com.example.myjavaapp.Model.entity.User;
import com.example.myjavaapp.R;
import com.example.myjavaapp.Model.entity.Cart;
import com.facebook.CallbackManager;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Random;

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
                    //sign in success, updated UI with user's information
                    Log.d("ADD ACCOUNT","createUserWithEmailAndPassword:Success");
                    FirebaseUser user=mAuth.getCurrentUser();
                    saveToRealtimeDatabase();
                    updateUI(user);
                }
                else{
                    //if singin if failed,display a message to user
                    Log.w(TAG,"createUserWithEmailAndPassword:Failed", task.getException());
                    Toast.makeText(RegisterActivity.this, "Authentication Failed!!! ---- " + task.getException().getMessage(),Toast.LENGTH_SHORT).show();
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

    public void saveToRealtimeDatabase(){
        Log.d("REGISTER: ", "enter save to realtime database");

        FirebaseUser acc = FirebaseAuth.getInstance().getCurrentUser();
        if (acc != null) {
            User userData = new User();
            userData.setUserName("");
            userData.setUserPhone("");
            userData.setUserEmail(acc.getEmail());
            userData.setUserLocation("");
            userData.setUserLongitude(0f);
            userData.setUserLatitude(0f);
            FirebaseDatabase.getInstance().getReference("users")
                    .child(acc.getUid()).setValue(userData);
            Toast.makeText(RegisterActivity.this,"SUCCESS: User is NOT null!!",Toast.LENGTH_SHORT).show();

            //add cart for user into firebase
            Cart userCart = new Cart();
            String id = randomGenerator();
            userCart.setCartId(id);
            userCart.setCartUser(acc.getUid());
            FirebaseDatabase.getInstance().getReference("carts")
                    .child(id).setValue(userCart);



        }
        else{
            Toast.makeText(RegisterActivity.this,"FAILED: User is null!!",Toast.LENGTH_SHORT).show();
        }
    }

    public static String randomGenerator(){
        String alphabet = "ABCDEFGHIJKLMNOPRSTUWXYZ";
        String numbers = "1234567890";
        Random random = new Random();
        StringBuilder strBuilder = new StringBuilder();
        for(int i = 0; i < 3; ++i){
            int index = random.nextInt(alphabet.length());
            strBuilder.append(alphabet.charAt(index));
        }
        for(int i = 0; i < 2; ++i){
            int index = random.nextInt(numbers.length());
            strBuilder.append(numbers.charAt(index));
        }
        return strBuilder.toString();
    }
}
