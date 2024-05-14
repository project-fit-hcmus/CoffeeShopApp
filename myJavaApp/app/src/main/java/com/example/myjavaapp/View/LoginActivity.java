package com.example.myjavaapp.View;


import static android.content.ContentValues.TAG;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import com.example.myjavaapp.R;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
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


import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.Base64;

import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.firebase.auth.GoogleAuthProvider;

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

        //login with facebook
        printHashKey();
        btnFacebook = (LoginButton) findViewById(R.id.btnFacebook);
        callbackManager = CallbackManager.Factory.create();

        //test
        btnFacebook.setPermissions("email","public_profile");
        btnFacebook.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                Log.d(TAG,"facebook login: Sucess" + loginResult);
                handleFacebookAccessToken(loginResult.getAccessToken());
            }

            @Override
            public void onCancel() {
                Log.d(TAG,"facebook login: Cancel" );

            }

            @Override
            public void onError(@NonNull FacebookException e) {
                Log.d(TAG,"facebook login: Error",e);


            }
        });

        //test

//        AccessToken accessToken = AccessToken.getCurrentAccessToken();
//        if(accessToken != null && !accessToken.isExpired()){
//            Intent intent = new Intent(LoginActivity.this, homeActivity.class);
//            Bundle data = new Bundle();
//            data.putString("type","another");
//            intent.putExtras(data);
//            startActivity(intent);
//        }
//        LoginManager.getInstance().registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
//            @Override
//            public void onSuccess(LoginResult loginResult) {
//                try {
//                    PackageInfo packageInfo = getPackageManager().getPackageInfo(getPackageName(), PackageManager.GET_SIGNATURES);
//                    for (Signature signature : packageInfo.signatures) {
//                        MessageDigest messageDigest = MessageDigest.getInstance("SHA");
//                        messageDigest.update(signature.toByteArray());
//                        String hashKey = Base64.encodeToString(messageDigest.digest(), Base64.DEFAULT);
//                        Log.d("Hash Key", hashKey);
//                        System.out.println(hashKey);
//                    }
//                } catch (PackageManager.NameNotFoundException e) {
//                    e.printStackTrace();
//                } catch (NoSuchAlgorithmException e) {
//                    e.printStackTrace();
//                }
//                Intent intent = new Intent(LoginActivity.this, homeActivity.class);
//                Bundle data = new Bundle();
//                data.putString("type","another");
//                intent.putExtras(data);
//                startActivity(intent);
//
////                startActivity(new Intent(LoginActivity.this, homeActivity.class));
//                finish();
//            }
//
//            @Override
//            public void onCancel() {
//                Log.d("RESULT","CANCEL");
//                System.out.println("CANCEL");
//            }
//
//            @Override
//            public void onError(@NonNull FacebookException e) {
//                Log.d("RESULT","ERROR");
//                System.out.println("ERROR");
//            }
//        });
//
//        btnFacebook.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                LoginManager.getInstance().logInWithReadPermissions(LoginActivity.this, Arrays.asList("public_profile","email"));
//            }
//        });


        // google login
        //tạo thực thể yêu cầu đăng nhập bằng google
        GoogleSignInOptions gso =  new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        googleSignInClient = GoogleSignIn.getClient(this, gso);

        btnGoogle = (SignInButton) findViewById(R.id.btnGoogle);
        btnGoogle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = googleSignInClient.getSignInIntent();
                startActivityForResult(intent,RC_SIGN_IN);
            }
        });

    }



    @Override
    protected void onStart() {
        super.onStart();
        // Check for existing Google Sign In account, if the user is already signed in
        // the GoogleSignInAccount will be non-null.
        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(this);
        //updateUI(account);
        if(account != null)
            Toast.makeText(LoginActivity.this,"Already login!!!",Toast.LENGTH_SHORT).show();
    }

    public void LoginToAccount(){
        mAuth.signInWithEmailAndPassword(txtEmail.getText().toString(),txtPassword.getText().toString()).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    //trường hợp thông tin đăng nhập hợp lệ
                    Log.d(TAG,"SignInWithEmailAndPassword:Success");
                    FirebaseUser user = mAuth.getCurrentUser();
                    updateUI(user);
                }
                else{
                    //trường hợp thông tin đăng nhập không hợp lệ
                    Log.d(TAG,"SingInWithEmailAndPassword:Failed");
                    Toast.makeText(LoginActivity.this,"Failed to login!!!",Toast.LENGTH_SHORT).show();
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
            //move to home screen
            Intent intent = new Intent(LoginActivity.this, homeActivity.class);
            Bundle data = new Bundle();
            data.putString("type","another");
            intent.putExtras(data);
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

    // for facebook login
    public void handleFacebookAccessToken(AccessToken accessToken){
        Log.d(TAG,"HandleFacebookAccessToken:  " + accessToken);
        AuthCredential credential = FacebookAuthProvider.getCredential(accessToken.getToken());
        mAuth.signInWithCredential(credential).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    //đăng nhập thành công bằng facebook
                    Log.d(TAG,"LoginByFacebook:Success");
                    FirebaseUser user = mAuth.getCurrentUser();
                    // xử lý updateUI( gửi intent tới với homeActivity
                    Intent intent = new Intent(LoginActivity.this, homeActivity.class);
                    Bundle data = new Bundle();
                    data.putString("name",user.getDisplayName());
                    data.putString("email",user.getEmail());
                    intent.putExtras(data);
                    startActivity(intent);
                }else{
                    //trường hợp đăng nhập bằng facebook thất bại
                    Log.d(TAG,"LoginByFacebook:Failed");
                    Toast.makeText(LoginActivity.this,"Something Wrong!!!",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public void printHashKey(){
        try{
            PackageInfo info = getPackageManager().getPackageInfo("com.android.facebookloginsample",PackageManager.GET_SIGNATURES);
            for (Signature signature: info.signatures){
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
//                Log.d("KeyHash:", Base64.getEncoder().encodeToString(md.digest(),Base64.DEFAULT));
            }
        }catch(PackageManager.NameNotFoundException e){
        }catch(NoSuchAlgorithmException e){
        }
    }



    // for google login
    public void handleSignInResult(Task<GoogleSignInAccount> completedTask) {
        try {
            GoogleSignInAccount account = completedTask.getResult(ApiException.class);
            String idToken = account.getIdToken();
            String name = account.getDisplayName();
            String email = account.getEmail();
//            String photo = account.getPhotoUrl().toString();
            Toast.makeText(LoginActivity.this,"email: " + email, Toast.LENGTH_SHORT).show();
            Toast.makeText(LoginActivity.this,"idtoken: " + idToken,Toast.LENGTH_SHORT).show();
            CreadentialWithGoogleInfo(idToken);

            Bundle bundle = new Bundle();
            bundle.putString("type","google");
            bundle.putString("email",email);
            bundle.putString("name",name);
            bundle.putString("idtoken",idToken);
//            bundle.putString("photo",photo);
            Intent intent= new Intent(LoginActivity.this, homeActivity.class);
            intent.putExtras(bundle);
            // Signed in successfully, show authenticated UI.
            // updateUI(account);
            startActivity(intent);
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
        FirebaseAuth.getInstance().signInWithCredential(credential).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    //đăng nhập thành công, lưu thông tin vào firebase
                    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                    String userID = user.getUid();
                    String userName = user.getDisplayName();
                    String userMail = user.getEmail();
                    String userPhoto = user.getPhotoUrl().toString();
                    //lưu thông tin vào database
                    DatabaseReference userRef = FirebaseDatabase.getInstance().getReference().child("users").child(userID);
                    userRef.child("name").setValue(userName);
                    userRef.child("email").setValue(userMail);
                    userRef.child("photoURL").setValue(userPhoto);
                }
                else{
                    Log.w(TAG, "signInResult:failed" );

                }
            }
        });
    }


}



//problem key hash in authentication facebook: https://github.com/facebookarchive/react-native-fbsdk/issues/424