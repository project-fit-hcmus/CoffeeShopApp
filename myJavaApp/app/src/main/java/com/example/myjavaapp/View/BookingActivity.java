package com.example.myjavaapp.View;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.utils.widget.ImageFilterButton;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myjavaapp.R;
import com.example.myjavaapp.View.ProfileAction.ChooseLocationActivity;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class BookingActivity extends AppCompatActivity implements View.OnClickListener{
    private TextView txtTitle;
    private ImageFilterButton btnReturn;
    private RecyclerView listItem;
    private TextView txtAddr;
    private TextInputEditText edtPhone, edtNote;
    private final int CHOOSE_ADDRESS_REQUEST = 121212;
    private ImageButton btnChooseAddress;
    private Button btnAutoFill;
    private FirebaseUser user;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.booking_screen);
        txtTitle = findViewById(R.id.mainTitle);
        btnReturn = findViewById(R.id.btnBack);
        listItem = findViewById(R.id.bookingItems);
        edtPhone = findViewById(R.id.bookingPhone);
        edtNote = findViewById(R.id.bookingNote);
        txtAddr = findViewById(R.id.bookingAddress);
        btnChooseAddress = findViewById(R.id.btnChooseAddress);
        btnAutoFill = findViewById(R.id.btnAutoFill);

        user = FirebaseAuth.getInstance().getCurrentUser();


        btnAutoFill.setOnClickListener(this);
        btnReturn.setOnClickListener(this);
        btnChooseAddress.setOnClickListener(this);





        txtTitle.setText("Booking");

    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.btnBack){
            setResult(RESULT_OK);
            finish();
        }
        if(v.getId() == R.id.btnChooseAddress){
            startActivityForResult(new Intent(BookingActivity.this,ChooseLocationActivity.class),CHOOSE_ADDRESS_REQUEST);
        }
        if(v.getId() == R.id.btnAutoFill){
            // đọc data trong realtime để hiển thị
            DatabaseReference ref = FirebaseDatabase.getInstance().getReference("users").child(user.getUid());
            ref.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    String phone = snapshot.child("phoneNumber").getValue(String.class);
                    String address = snapshot.child("location").getValue(String.class);
                    txtAddr.setText(address);
                    edtPhone.setText(phone);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == CHOOSE_ADDRESS_REQUEST){
            if(resultCode == RESULT_OK){
                Bundle bundle = data.getBundleExtra("data");
                String addr = bundle.getString("address");
                Double longitude = bundle.getDouble("longitude");
                Double latitude = bundle.getDouble("latitude");
                txtAddr.setText(addr);
            }else{
                Toast.makeText(BookingActivity.this,"There are something wrong",Toast.LENGTH_SHORT).show();
            }
        }
    }
}
