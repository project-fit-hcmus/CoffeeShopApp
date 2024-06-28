package com.example.myjavaapp.View;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
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
import androidx.lifecycle.ViewModelProvider;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.model.GlideUrl;
import com.example.myjavaapp.Model.LocalViewModel.LocalBeverageViewModel;
import com.example.myjavaapp.Model.LocalViewModel.LocalCartDetailViewModel;
import com.example.myjavaapp.Model.LocalViewModel.LocalCartViewModel;
import com.example.myjavaapp.Model.LocalViewModel.LocalFavoriteViewModel;
import com.example.myjavaapp.Model.database.AppDatabase;
import com.example.myjavaapp.Model.entity.Beverage;
import com.example.myjavaapp.Model.entity.CartDetail;
import com.example.myjavaapp.Model.entity.Favorite;
import com.example.myjavaapp.R;
import com.google.android.gms.common.data.DataBufferObserver;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.util.Executors;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.util.List;
import java.util.Observable;
import java.util.concurrent.ExecutorService;

import de.hdodenhof.circleimageview.CircleImageView;
import kotlinx.coroutines.CoroutineScope;
import kotlinx.coroutines.Dispatchers;

public class SingleBeverageActivity extends AppCompatActivity implements View.OnClickListener {
    private ImageView beverageIcon;
    private ImageButton btnBack, btnFavorite, btnAdd, btnSub;
    private Button btnAddToCart;
    private TextView txtRating, txtPrice, txtDescription, txtName, txtQuantity;
    private CircleImageView userAvatar;

    private String beverageId;
    private FirebaseUser user;
    private LocalFavoriteViewModel favoriteViewModel;
    private LocalCartViewModel cartViewModel;
    private LocalBeverageViewModel beverageViewModel;
    private LocalCartDetailViewModel cartDetailViewModel;



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
        userAvatar = findViewById(R.id.userImg);

        user = FirebaseAuth.getInstance().getCurrentUser();
        beverageViewModel = new ViewModelProvider(this).get(LocalBeverageViewModel.class);
        favoriteViewModel = new ViewModelProvider(this).get(LocalFavoriteViewModel.class);
        cartViewModel = new ViewModelProvider(this).get(LocalCartViewModel.class);
        cartDetailViewModel = new ViewModelProvider(this).get(LocalCartDetailViewModel.class);


        Intent intent = getIntent();
        Bundle data = intent.getExtras();
        beverageId = data.getString("beverageId");

        Uri photo = user.getPhotoUrl();
        if(photo != null){
            Picasso.get().load(photo).into(userAvatar);
        }
        // get a beverage from Id

        beverageViewModel.getBeverageFromId(beverageId).observe(this, new Observer<Beverage>() {
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
        });

        btnFavorite.setImageResource(R.drawable.baseline_favorite_border_24);
        btnFavorite.setTag("unfavorite");

        favoriteViewModel.getAllFavorites().observe(this, new Observer<List<Favorite>>() {
            @Override
            public void onChanged(List<Favorite> favorites) {
                if(favorites != null && favorites.isEmpty())
                    return;
                for(Favorite i : favorites){
                    if(i.getFavoriteBeverage().equals(beverageId)){
                        btnFavorite.setImageResource(R.drawable.baseline_favorite_24);
                        btnFavorite.setTag("favorite");
                        return;
                    }
                }
            }
        });


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
            // add to realtime database
            if(txtQuantity.getText().toString().equals("0")){
                Toast.makeText(SingleBeverageActivity.this,"Please choose number of beverage to Order!!!",Toast.LENGTH_SHORT).show();
            }else {
                SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs",MODE_PRIVATE);
                String cartId = sharedPreferences.getString("CartUserId","");
                Toast.makeText(SingleBeverageActivity.this,cartId,Toast.LENGTH_SHORT).show();

                cartDetailViewModel.checkIfBeverageIsExist(beverageId, cartId).observe(SingleBeverageActivity.this, new Observer<Integer>() {
                    @Override
                    public void onChanged(Integer integer) {
                        if(integer == null){
                            //add new
                            DatabaseReference ref = FirebaseDatabase.getInstance().getReference("cartdetails");
                            ref.child(cartId + beverageId).setValue(new CartDetail(cartId,beverageId,Integer.parseInt(txtQuantity.getText().toString())));
                        }
                        else{
                            //update
                            DatabaseReference ref = FirebaseDatabase.getInstance().getReference("cartdetails");
                            ref.child(cartId + beverageId).child("cartDetailQuantity").setValue(integer + Integer.parseInt(txtQuantity.getText().toString()));
                        }
                    }
                });
            }

        }
        if(v.getId() == R.id.btnFavorite){
            if(btnFavorite.getTag().equals("favorite")){
                btnFavorite.setImageResource(R.drawable.baseline_favorite_border_24);
                Toast.makeText(SingleBeverageActivity.this,"Remove from favorite",Toast.LENGTH_SHORT).show();
                //xóa trên local (ERROR: đã xóa nhưng sau khi trở về mainFragment thì bị khôi phục lại)
                favoriteViewModel.deleteFavoriteById(beverageId);
//                favoriteViewModel.delete(beverageId);
//                //xóa trên realtime
                DatabaseReference ref = FirebaseDatabase.getInstance().getReference("favorites");
                ref.child(beverageId).removeValue();
            }else{
                Toast.makeText(SingleBeverageActivity.this,"Add to favorite",Toast.LENGTH_SHORT).show();

                btnFavorite.setImageResource(R.drawable.baseline_favorite_24);
                DatabaseReference ref = FirebaseDatabase.getInstance().getReference("favorites");
                ref.child(beverageId).setValue(new Favorite(user.getUid(),beverageId));
            }
        }
    }
}
