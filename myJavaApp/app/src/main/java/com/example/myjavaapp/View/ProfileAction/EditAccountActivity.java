package com.example.myjavaapp.View.ProfileAction;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextPaint;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.PickVisualMediaRequest;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.example.myjavaapp.Model.User;
import com.example.myjavaapp.R;
import com.example.myjavaapp.View.ProfileActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.type.LatLng;
import com.squareup.picasso.Picasso;

import java.util.UUID;

import de.hdodenhof.circleimageview.CircleImageView;

public class EditAccountActivity extends AppCompatActivity implements View.OnClickListener {
    private TextView title;
    private EditText txtUsername, txtPhoneNumber;
    private FirebaseUser user;
    private AppCompatButton btnChoosePhoto, btnChooseLocation,btnSave,btnCancel, btnBack;
    private CircleImageView userImage;
    private ImageView dropPinView;
    private int SELECT_PICTURE = 1122;
    private int REQUEST_CODE = 1133;
    private Uri choosenPic = null;
    //get image from user device
    private ActivityResultLauncher<PickVisualMediaRequest> launcher = registerForActivityResult(new ActivityResultContracts.PickVisualMedia(), new ActivityResultCallback<Uri>() {
        @Override
        public void onActivityResult(Uri o) {
            if(o != null){
                Picasso.get().load(o).into(userImage);
                choosenPic = o;

            }else{
                Toast.makeText(EditAccountActivity.this, "Faild to get Image!!!", Toast.LENGTH_SHORT).show();
                choosenPic = o;
            }
        }
    });
     @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pro_edit_account_screen);
        title = (TextView) findViewById(R.id.HeadTitle);
        txtUsername = (EditText) findViewById(R.id.editUsername);
        txtPhoneNumber = (EditText) findViewById(R.id.editPhoneNumber);
        btnChoosePhoto = (AppCompatButton)findViewById(R.id.btnAddPhoto);
        btnChooseLocation = (AppCompatButton) findViewById(R.id.btnAddLocation);
        btnSave = (AppCompatButton) findViewById(R.id.btnSave);
        btnCancel = (AppCompatButton) findViewById(R.id.btnCancel);
        userImage = (CircleImageView) findViewById(R.id.userPhoto);
        btnBack = (AppCompatButton) findViewById(R.id.btnBack);
        user = FirebaseAuth.getInstance().getCurrentUser();
        title.setText("Manage Account");
        txtUsername.setText(user.getDisplayName());
//        userImage.setImageURI(user.getPhotoUrl());
         Picasso.get().load(user.getPhotoUrl()).into(userImage);

        btnCancel.setOnClickListener(this);
        btnSave.setOnClickListener(this);
        btnChooseLocation.setOnClickListener(this);
        btnChoosePhoto.setOnClickListener(this);
        btnBack.setOnClickListener(this);





    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.btnAddPhoto){
            //request permission
            if(ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED){
                Toast.makeText(EditAccountActivity.this,"READ_EXTERNAL_STORAGE: GRANTED",Toast.LENGTH_SHORT).show();
            }
            else{
                ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},REQUEST_CODE);
            }
            getImageFromDevice();
        }
        if(v.getId() == R.id.btnAddLocation){
            // algorithm???
        }
        if(v.getId() == R.id.btnSave){
            //update user photo
            UserProfileChangeRequest change;
            if(choosenPic != null){
                saveImageShortage(choosenPic);
            }
            //update user name
            change = new UserProfileChangeRequest.Builder()
                        .setDisplayName(txtUsername.getText().toString())
                        .build();

            user.updateProfile(change).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if(task.isSuccessful()){
                        Toast.makeText(EditAccountActivity.this,"Updated Successful!!",Toast.LENGTH_SHORT).show();
                    }
                    else{
                        Toast.makeText(EditAccountActivity.this,"Updated Failed!!!",Toast.LENGTH_SHORT).show();
                    }
                }
            });
            //update phonenumber in realtime
            //get reference to realtime databse
            if(!txtPhoneNumber.getText().toString().isEmpty()){
                DatabaseReference userRef = FirebaseDatabase.getInstance().getReference("users");
                DatabaseReference userRefId = userRef.child(user.getUid());
                //update phonenumber
                userRefId.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if(snapshot.exists()){
                            User uInfo = snapshot.getValue(User.class);
                            if(uInfo != null){
                                uInfo.setPhone(txtPhoneNumber.getText().toString());
                            }
                            userRefId.setValue(uInfo);
                            Log.d("Realtime","Update phone successful!!!");
                        }
                        else{
                            Log.d("Realtime","User not found!!!");
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Log.e("firebase", "Error retrieving user information", error.toException());
                    }
                });

            }


            //trường hợp sau khi save thoát ra màn hình chính thì không cập nhập thông tin tức thời được
//            startActivity(new Intent(EditAccountActivity.this, ProfileActivity.class));

        }
        if(v.getId() == R.id.btnCancel){
            txtUsername.setText(user.getDisplayName());
            String phone = user.getPhoneNumber();
            if(phone != null)
                txtPhoneNumber.setText(phone);
            else txtPhoneNumber.setText("Your phone number");

            Picasso.get().load(user.getPhotoUrl()).into(userImage);
//            startActivity(new Intent(EditAccountActivity.this, ProfileActivity.class));

        }
        if(v.getId() == R.id.btnBack){
            startActivity(new Intent(EditAccountActivity.this, ProfileActivity.class));
        }
    }

    public void getImageFromDevice(){
        launcher.launch(new PickVisualMediaRequest.Builder().setMediaType(ActivityResultContracts.PickVisualMedia.ImageOnly.INSTANCE).build());
    }

    public void saveImageShortage(Uri choosen){
         //check images folder
        StorageReference imagesRef = FirebaseStorage.getInstance().getReference().child("images");

         //tạo ngẫu nhiên tên cho hình ảnh
         String filename = UUID.randomUUID().toString();
         //tạo đường dẫn lưu trong firebase shortage
        StorageReference storageReference = FirebaseStorage.getInstance().getReference().child("images/" +filename);
        //tải hình ảnh lên firebase shortage
        storageReference.putFile(choosen).addOnSuccessListener(taskSnapshot -> {
            // lấy URL của hình ảnh tải lên thành công
            storageReference.getDownloadUrl().addOnSuccessListener(uri -> {
               String photoUrl = uri.toString();
               //tiếp tục lưu url này vào firebase user
                savePhotoUrlToFirebaseUser(photoUrl);

            }).addOnFailureListener(exception->{
                Toast.makeText(EditAccountActivity.this,"ERROR: Không lấy được URL của hình ảnh!!!",Toast.LENGTH_SHORT).show();
            });
        }).addOnFailureListener(exception->{
            String errorMessage = exception.getMessage();
            Toast.makeText(EditAccountActivity.this, "ERROR:  Tải hình ảnh lên Firebase User không thành công  --- " + errorMessage, Toast.LENGTH_SHORT).show();
        });
    }

    public void savePhotoUrlToFirebaseUser(String url){
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                .setPhotoUri(Uri.parse(url))
                .build();
        if(user != null){
            user.updateProfile(profileUpdates)
                    .addOnSuccessListener(aVoid->{
                        Toast.makeText(EditAccountActivity.this, "SUCCESS: Lưu URL thành công", Toast.LENGTH_SHORT).show();
                    }).addOnFailureListener(exception -> {
                        Toast.makeText(EditAccountActivity.this, "ERROR: Lưu URL thất bại", Toast.LENGTH_SHORT).show();
                    });
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode == REQUEST_CODE){
            if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                Toast.makeText(EditAccountActivity.this,"SUCCESS: Quyền được cấp thành công",Toast.LENGTH_SHORT).show();
            }
            else{
                Toast.makeText(EditAccountActivity.this,"ERROR: Quyền không được cấp",Toast.LENGTH_SHORT).show();
            }
        }
    }
}
