package com.example.myjavaapp.View;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.model.GlideUrl;
import com.example.myjavaapp.Model.LocalViewModel.LocalCommentViewModel;
import com.example.myjavaapp.Model.LocalViewModel.LocalOrderViewModel;
import com.example.myjavaapp.Model.entity.Comment;
import com.example.myjavaapp.Model.entity.Order;
import com.example.myjavaapp.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.protobuf.Internal;

import java.util.Calendar;
import java.util.Random;

public class ReviewActivity extends AppCompatActivity implements View.OnClickListener {
    private ImageButton btnBack;
    private TextView txtTitle, txtAddress, txtDate, txtPhone, txtPrice, txtSub;
    private ImageView cover;
    private RatingBar ratingBar;
    private EditText comment;
    private Button btnSend;
    private LocalOrderViewModel orderViewModel;
    private FirebaseUser user;
    private String orderId = "";
    private float value = 0.0f;
    private LocalCommentViewModel commentViewModel;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.review_screen);
        txtTitle = findViewById(R.id.mainTitle);
        btnBack = findViewById(R.id.btnBack);
        txtAddress = findViewById(R.id.orderAddress);
        txtDate = findViewById(R.id.orderDate);
        txtPhone = findViewById(R.id.orderPhone);
        txtPrice = findViewById(R.id.orderTotal);
        cover = findViewById(R.id.orderCover);
        ratingBar = findViewById(R.id.ratingBar);
        txtSub = findViewById(R.id.subQuality);
        comment = findViewById(R.id.mainContent);
        btnSend = findViewById(R.id.btnSend);

        Intent intent = getIntent();
        Bundle data = intent.getExtras();
        orderId = data.getString("orderId");
        txtTitle.setText("Review");
        user = FirebaseAuth.getInstance().getCurrentUser();
        commentViewModel = new ViewModelProvider(this).get(LocalCommentViewModel.class);



        orderViewModel = new ViewModelProvider(this).get(LocalOrderViewModel.class);
        orderViewModel.getOrderItem(orderId).observe(this, new Observer<Order>() {
            @Override
            public void onChanged(Order order) {
                if(order == null)
                    return;
                txtAddress.setText(order.getOrderAddress());
                txtDate.setText("Booking Date: " + order.getOrderDate());
                txtPhone.setText("Booking Phone: " + order.getOrderPhone());
                txtPrice.setText("$" + String.valueOf(order.getOrderCost()));

                StorageReference storageReference = FirebaseStorage.getInstance().getReference("images/beverages/" + order.getOrderCover());
                Task<Uri> downloadUrlTask = storageReference.getDownloadUrl();
                downloadUrlTask = downloadUrlTask.addOnCompleteListener(new OnCompleteListener<Uri>() {
                    @Override
                    public void onComplete(@NonNull Task<Uri> task) {
                        if (task.isSuccessful()) {
                            Uri downloadUrl = task.getResult();
                            GlideUrl glideUrl = new GlideUrl(downloadUrl.toString());

                            // Load image using GlideUrl
                            // ở đây phải sử dụng getApplicationContext nếu không sẽ xảy ra lỗi "You cannot start a load for a destroyed activity in relativelayout image using glide"
                            Glide.with(ReviewActivity.this)
                                    .load(glideUrl)
                                    .into(cover);
                        } else {
                            // Handle download URL retrieval error
                        }
                    }
                });
            }
        });
        ratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                value = rating;
                if(rating < 1.0){
                    txtSub.setText("Very Bad");
                    txtSub.setTextColor(getResources().getColor(R.color.black));
                }
                else if(rating < 2.0){
                    txtSub.setText("Bad");
                    txtSub.setTextColor(getResources().getColor(R.color.black));
                }else if(rating < 3.0){
                    txtSub.setText("Normal");
                    txtSub.setTextColor(getResources().getColor(R.color.main_color));
                }else if(rating < 4.0){
                    txtSub.setText("Good");
                    txtSub.setTextColor(getResources().getColor(R.color.btnColor));
                }else{
                    txtSub.setText("Wonderful");
                    txtSub.setTextColor(getResources().getColor(R.color.btnColor));
                }
            }
        });



        btnBack.setOnClickListener(this);
        btnSend.setOnClickListener(this);




    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.btnBack){
            setResult(RESULT_CANCELED);
            finish();
        }
        if(v.getId() == R.id.btnSend){
            String id = randomId();
            Comment item = new Comment(id, user.getUid(), orderId, comment.getText().toString(), value, getCurrentDate());
            DatabaseReference ref = FirebaseDatabase.getInstance().getReference("comments");
            ref.child(id).setValue(item).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if(task.isSuccessful()){
                        commentViewModel.insert(item);
                    }else{
                        Toast.makeText(ReviewActivity.this, "Failed to review this order!!!",Toast.LENGTH_SHORT).show();
                    }
                }
            });
            setResult(RESULT_OK);
            finish();
        }

    }

    public static String randomId() {
        StringBuilder str = new StringBuilder();
        Random rand = new Random();
        String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        String numbers = "0123456789";
        for (int i = 0; i < 3; ++i) {
            str.append(characters.charAt(rand.nextInt(characters.length())));
        }
        for (int i = 0; i < 2; ++i) {
            str.append(numbers.charAt(rand.nextInt(numbers.length())));
        }
        return str.toString();
    }
    public static String getCurrentDate() {

        Calendar calendar = Calendar.getInstance();
        String str = "";
        str += String.valueOf(calendar.get(Calendar.DAY_OF_MONTH)) + "/"
                + String.valueOf(calendar.get(Calendar.MONTH) + 1) + "/" + String.valueOf(calendar.get(Calendar.YEAR));
        return str;
    }
}
