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

import com.example.myjavaapp.Model.entity.Cart;
import com.example.myjavaapp.Model.entity.User;
import com.example.myjavaapp.View.mainActivity;
import com.facebook.AccessToken;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.Login;
import com.facebook.login.LoginResult;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.firebase.Firebase;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import com.example.myjavaapp.R;
import com.facebook.CallbackManager;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;


import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.Random;

public class LoginActivity extends AppCompatActivity {

    private TextInputEditText txtEmail;
    private TextInputEditText txtPassword;
    private AppCompatButton btnLogin;
    private TextView txtRegister;
    private SpannableString spannableString;
    private ClickableSpan clickableSpan;
    private FirebaseAuth mAuth;
    private LoginButton btnFacebook;
    private CallbackManager callbackManager;
    private SignInButton btnGoogle;
    private GoogleSignInClient googleSignInClient;
    private static final int RC_SIGN_IN = 11111;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_screen);

        //definition
        txtEmail = (TextInputEditText) findViewById(R.id.txtEmail);
        txtPassword = (TextInputEditText) findViewById(R.id.txtPassword);
        btnLogin = (AppCompatButton) findViewById(R.id.btnLogin);
        txtRegister = (TextView) findViewById(R.id.txtRegister);
        btnGoogle = (SignInButton) findViewById(R.id.btnGoogle);
        btnFacebook = (LoginButton) findViewById(R.id.btnFacebook);




        spannableString = new SpannableString("Didn't have account? Register here");
        clickableSpan = new ClickableSpan() {
            @Override
            public void onClick(@NonNull View widget) {
                startActivity(new Intent(LoginActivity.this,RegisterActivity.class));
            }
        };
        spannableString.setSpan(clickableSpan,30,34, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        txtRegister.setText(spannableString);
        txtRegister.setMovementMethod(LinkMovementMethod.getInstance());
        mAuth = FirebaseAuth.getInstance();


        //login with email and password
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // kiểm tra xem email và password có rỗng không
                if(txtEmail.getText().toString().isEmpty() || txtPassword.getText().toString().isEmpty()){
                    Toast.makeText(LoginActivity.this,"Please enter Password and Email!!!",Toast.LENGTH_SHORT).show();
                }
                else{
                    //sign in
                    LoginToAccount();
                }
            }
        });

        // GOOGLE LOGIN
        // delete sign in cache(only apply for google sign in)
        DeleteCache();
        //tạo thực thể yêu cầu đăng nhập bằng google
        GoogleSignInOptions gso =  new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        googleSignInClient = GoogleSignIn.getClient(this, gso);

        btnGoogle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = googleSignInClient.getSignInIntent();
                startActivityForResult(intent,RC_SIGN_IN);
                Log.d("LOGIN ACTIVITY","google login");
            }
        });


        // FACEBOOK LOGIN
        FirebaseAuth.getInstance().signOut();
        callbackManager = CallbackManager.Factory.create();
        btnFacebook.setReadPermissions("email","public_profile");
        btnFacebook.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                Log.d(TAG, "Facebook: onSuccess" + loginResult);
                handleFaceBookAccessToken(loginResult.getAccessToken());
            }

            @Override
            public void onCancel() {
                Log.d(TAG, "Facebook: onCancel");
            }

            @Override
            public void onError(@NonNull FacebookException e) {
                Log.d(TAG, "Facebook: onError " + e.getMessage() );
            }
        });

    }


    @Override
    protected void onStart() {
        super.onStart();
        // Check for existing Google Sign In account, if the user is already signed in
        // the GoogleSignInAccount will be non-null.
        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(this);
        if(account != null) {
            Toast.makeText(LoginActivity.this, "Already login!!!", Toast.LENGTH_SHORT).show();
        }

    }

    //login with email và password
    public void LoginToAccount(){
        mAuth.signInWithEmailAndPassword(txtEmail.getText().toString(),txtPassword.getText().toString()).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    //trường hợp thông tin đăng nhập hợp lệ
                    Log.d(TAG,"SUCCESS: SignInWithEmailAndPassword");
                    FirebaseUser user = mAuth.getCurrentUser();

                    updateUI(user);
                }
                else{
                    //trường hợp thông tin đăng nhập không hợp lệ
                    Log.d(TAG,"FAILED: SingInWithEmailAndPassword");
                    Toast.makeText(LoginActivity.this,"Email or Password may be wrong!!",Toast.LENGTH_SHORT).show();
                    updateUI(null);
                }
            }
        });
    }

    public void updateUI(FirebaseUser user){
        if(user == null){
            txtPassword.setText("");
            txtEmail.setText("");
        }else{
            Intent intent = new Intent(LoginActivity.this, mainActivity.class);
            startActivity(intent);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == RC_SIGN_IN){          // result for login by Google
            // The Task returned from this call is always completed, no need to attach
            // a listener.
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            if(task.isSuccessful()) {
                handleSignInResult(task);
            }else{
                Toast.makeText(LoginActivity.this,"Failed to log in with google!!!",Toast.LENGTH_SHORT).show();
            }

        }else  // result for login by Facebook
            callbackManager.onActivityResult(resultCode,resultCode,data);

    }



    // for google login
    public void handleSignInResult(Task<GoogleSignInAccount> completedTask) {
        try {
            GoogleSignInAccount account = completedTask.getResult(ApiException.class);
            String idToken = account.getIdToken();
            String name = account.getDisplayName();
            String email = account.getEmail();
            Log.d("LOGIN ACTIVITY - name",name);
            Log.d("LOGIN ACTIVITY - email", email);
            Log.d("LOGIN ACTIVITY - id token", idToken);
            CreadentialWithGoogleInfo(idToken);


        } catch (ApiException e) {
            // The ApiException status code indicates the detailed failure reason.
            // Please refer to the GoogleSignInStatusCodes class reference for more information.
            Log.w(TAG, "signInResult:failed code=" + e.getStatusCode());
            updateUI(null);
        }
    }

    //xác thực thông tin người dùng với IdToken từ Google
    public void CreadentialWithGoogleInfo(String idToken){
        AuthCredential credential = GoogleAuthProvider.getCredential(idToken,null);
        Log.d("LOGIN ACTIVITY - credential with gg", "START");

        FirebaseAuth.getInstance().signInWithCredential(credential).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d("FAILED LOGIN WITH GG"," Signin with credential google" + e.getMessage());
                Toast.makeText(LoginActivity.this,"FAILED: Signin with credential google",Toast.LENGTH_SHORT).show();
            }
        }).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    Log.d("SUCCESS LOG IN WITH GG","Signin with credential google");
                    saveToRealtimeDatabase();
                    Toast.makeText(LoginActivity.this,"SUCCESS: Signin with credential google",Toast.LENGTH_SHORT).show();

                    // Signed in successfully, show authenticated UI.
                    Intent intent= new Intent(LoginActivity.this, mainActivity.class);
                    startActivity(intent);
                }else{
                    Log.d("FAILED LOGIN WITH GG"," Signin with credential google");
                }
            }
        });
    }

    //lưu thông tin vào realtime firebase
    public void saveToRealtimeDatabase(){
        Log.d("BEGIN","ENTER TO SAVE TO REALTIME DATABASE ");

        FirebaseUser acc = FirebaseAuth.getInstance().getCurrentUser();
        if (acc != null) {
            User userData = new User(acc.getUid(), acc.getDisplayName(), acc.getEmail(), "","",0,0);
            Log.d("SAVE DATA USER",acc.getUid());
            Log.d("SAVE DATA USER",acc.getEmail());
            Log.d("SAVE DATA USER",acc.getDisplayName());

            // user cart infomation
            String id = randomGenerator();
            Cart userCart = new Cart(id, acc.getUid());

            // kiểm tra xem userId đã tồn tại trong realtime firebase hay chưa?
            checkExistInRealtime(acc.getUid(), new ExistCallback() {
                @Override
                public void onResult(boolean exist) {
                    if(!exist){
                        Log.d("CHECK EXISTS","FALSE");
                        FirebaseDatabase.getInstance().getReference("users")
                                .child(acc.getUid()).setValue(userData).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if(task.isSuccessful()){
                                            Log.d("ADD TO DATABASE REALTIME","SUCCESS");
                                        }
                                        else{
                                            Log.d("ADD TO DATABASE REALTIME","FAILED");
                                        }
                                    }
                                });
                        FirebaseDatabase.getInstance().getReference("carts")
                                .child(id).setValue(userCart);
                        Log.d("SAVE DATA USER", "user IS NOT EXISTS");
                    }else{
                        Log.d("CHECK EXISTS","TRUE");
                        Log.d("SAVE DATA USER","user IS EXISTS");
                    }
                }
            });



        }
        else{
//            Toast.makeText(LoginActivity.this,"FAILED: User is null!!",Toast.LENGTH_SHORT).show();
            Log.d("SAVE DATA USER","user is null");
        }
    }

    public interface ExistCallback{
        void onResult(boolean exist);
    }

    public void checkExistInRealtime(String key, ExistCallback callback){
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("users").child(key);
        Log.d("CHECK EXIST IN REALTIME", key);
//        Query query = ref.orderByChild("userId").equalTo(key);
        Query query = ref.limitToFirst(1);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    Toast.makeText(LoginActivity.this,"EXISTS", Toast.LENGTH_SHORT).show();
                    callback.onResult(true);
                }
                else{
                    Toast.makeText(LoginActivity.this,"NOT EXISTS", Toast.LENGTH_SHORT).show();
                    callback.onResult(false);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

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

    public void DeleteCache(){
        GoogleSignInClient signInClient = GoogleSignIn.getClient(this, GoogleSignInOptions.DEFAULT_SIGN_IN);
        //sign out for current account
        signInClient.signOut();
        //delete log in information former
        signInClient.revokeAccess();
    }


    private void handleFaceBookAccessToken(AccessToken token){
        Log.d(TAG, "handle facebook Access token : " + token);
        AuthCredential credential = FacebookAuthProvider.getCredential(token.getToken());
        Log.d(TAG, "get Token" + token.getToken());
        Log.d(TAG, "handle facebook create credential: "  + credential);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            Log.d(TAG,"signInWithCredential: Success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            saveToRealtimeDatabase();
                            Intent intent= new Intent(LoginActivity.this, mainActivity.class);
                            startActivity(intent);
                        }else{
                            Log.w(TAG,"SignInWithCredential : Failed");
                            Log.w(TAG,"SignInWithCredential Error Detail: " + task.getException());
                            Toast.makeText(LoginActivity.this,"Authentication failed",Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

}



