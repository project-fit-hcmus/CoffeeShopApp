package com.example.myjavaapp.View;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.model.GlideUrl;
import com.example.myjavaapp.Model.database.AppDatabase;
import com.example.myjavaapp.Model.entity.Beverage;
import com.example.myjavaapp.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class SingleBeverageActivity extends AppCompatActivity implements View.OnClickListener {
    private ImageView beverageIcon;
    private ImageButton btnBack, btnFavorite, btnAdd, btnSub;
    private Button btnAddToCart;
    private TextView txtRating, txtPrice, txtDescription, txtName, txtQuantity;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.single_beverage_screen);

        beverageIcon = findViewById(R.id.mainImage);
        btnBack = findViewById(R.id.btnBack);
        btnFavorite = findViewById(R.id.btnFavorite);
        btnAdd = findViewById(R.id.btnPlus);
        btnSub = findViewById(R.id.btnSubstract);
        btnAddToCart = findViewById(R.id.btnAddToCart);
        txtRating = findViewById(R.id.txtRating);
        txtPrice = findViewById(R.id.txtPrice);
        txtDescription = findViewById(R.id.beverageDescp);
        txtName = findViewById(R.id.txtBeverageName);
        txtQuantity = findViewById(R.id.quantityResult);

        Intent intent = getIntent();
        Bundle data = intent.getExtras();
        String beverageId = data.getString("beverageId");
        // get a beverage from Id
        LiveData<Beverage> beverageLive = AppDatabase.getDatabase(this).beverageDAO().getBeverageFromId(beverageId);
        Observer<Beverage> beverageObserver = new Observer<Beverage>() {
            @Override
            public void onChanged(Beverage beverage) {
                if(beverage == null)
                    return;
                String name = beverage.getBeverageImage();
                StorageReference storageReference = FirebaseStorage.getInstance().getReference("images/beverages/" + name);
                Task<Uri> downloadUrlTask = storageReference.getDownloadUrl();
                downloadUrlTask = downloadUrlTask.addOnCompleteListener(new OnCompleteListener<Uri>() {
                    @Override
                    public void onComplete(@NonNull Task<Uri> task) {
                        if (task.isSuccessful()) {
                            Uri downloadUrl = task.getResult();
                            GlideUrl glideUrl = new GlideUrl(downloadUrl.toString());

                            // Load image using GlideUrl
                            // ở đây phải sử dụng getApplicationContext nếu không sẽ xảy ra lỗi "You cannot start a load for a destroyed activity in relativelayout image using glide"
                            Glide.with(SingleBeverageActivity.this)
                                    .load(glideUrl)
                                    .into(beverageIcon);
                        } else {
                            // Handle download URL retrieval error
                        }
                    }
                });
                txtName.setText(beverage.getBeverageName());
                txtPrice.setText(beverage.getBeverageCost());
                txtRating.setText(String.valueOf(beverage.getBeverageRating()));
                txtQuantity.setText("0");
                txtDescription.setText(beverage.getBeverageDescription());


            }
        };
        beverageLive.observe(this,beverageObserver);


        btnBack.setOnClickListener(this);
        btnFavorite.setOnClickListener(this);
        btnAdd.setOnClickListener(this);
        btnSub.setOnClickListener(this);
        btnAddToCart.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.btnBack){
            setResult(RESULT_OK);
            finish();
        }
        if(v.getId() == R.id.btnPlus){
            txtQuantity.setText(String.valueOf(Integer.parseInt(txtQuantity.getText().toString()) + 1));
        }
        if(v.getId() == R.id.btnSubstract){
             txtQuantity.setText(String.valueOf(Integer.parseInt(txtQuantity.getText().toString()) - 1));

        }
        if(v.getId() == R.id.btnAddToCart){
            //code here
        }
        if(v.getId() == R.id.btnFavorite){
            btnFavorite.setImageResource(R.drawable.baseline_favorite_24);
        }
    }
}
