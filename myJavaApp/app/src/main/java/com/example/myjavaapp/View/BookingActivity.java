package com.example.myjavaapp.View;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.utils.widget.ImageFilterButton;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myjavaapp.Model.LocalViewModel.LocalBeAndCartDetailViewModel;
import com.example.myjavaapp.Model.LocalViewModel.LocalCartDetailViewModel;
import com.example.myjavaapp.Model.LocalViewModel.LocalUserViewModel;
import com.example.myjavaapp.Model.entity.BeverageAndCartDetail;
import com.example.myjavaapp.Model.entity.Order;
import com.example.myjavaapp.Model.entity.OrderDetail;
import com.example.myjavaapp.Model.entity.User;
import com.example.myjavaapp.R;
import com.example.myjavaapp.View.Adapter.BookingItemAdapter;
import com.example.myjavaapp.View.ProfileAction.ChooseLocationActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Random;

public class BookingActivity extends AppCompatActivity implements View.OnClickListener{
    private TextView txtTitle;
    private ImageFilterButton btnReturn;
    private RecyclerView listItem;
    private TextView txtAddr, txtQuantity, txtTotalCost;
    private TextInputEditText edtPhone, edtNote;
    private final int CHOOSE_ADDRESS_REQUEST = 121212;
    private ImageButton btnChooseAddress;
    private Button btnAutoFill, btnConfirm;
    private BookingItemAdapter adapter;
    private FirebaseUser user;
    private LocalBeAndCartDetailViewModel beAndCartDetailViewModel;
    private LocalCartDetailViewModel cartDetailViewModel;
    private LocalUserViewModel userViewModel;


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
        txtQuantity = findViewById(R.id.quantityContent);
        txtTotalCost = findViewById(R.id.totalPriceContent);
        btnConfirm = findViewById(R.id.btnConfirm);

        user = FirebaseAuth.getInstance().getCurrentUser();
        Intent intent = getIntent();
        Bundle receive = intent.getExtras();
        String listChoosen = receive.getString("listChoosen");
        String quantity = receive.getString("quantity");
        String totalCost = receive.getString("totalCost");
        List<Integer> result = decodeCheckoutList(listChoosen);

        beAndCartDetailViewModel = new ViewModelProvider(this).get(LocalBeAndCartDetailViewModel.class);
        cartDetailViewModel = new ViewModelProvider(this).get(LocalCartDetailViewModel.class);
        userViewModel = new ViewModelProvider(this).get(LocalUserViewModel.class);
        LiveData<List<BeverageAndCartDetail>> live = beAndCartDetailViewModel.getBeverageInCartDetail(user.getUid());
        live.observe(this, new Observer<List<BeverageAndCartDetail>>() {
            @Override
            public void onChanged(List<BeverageAndCartDetail> beverageAndCartDetails) {
                if(beverageAndCartDetails != null && beverageAndCartDetails.isEmpty()) {
                    Toast.makeText(BookingActivity.this, "Empty",Toast.LENGTH_SHORT).show();
                    return;
                }
                List<BeverageAndCartDetail> rawData = new ArrayList<>();
                for(int i = 0; i < result.size(); ++i){
                    rawData.add(beverageAndCartDetails.get(result.get(i)));
                }
                adapter = new BookingItemAdapter(BookingActivity.this, rawData);
                listItem.setAdapter(adapter);
                listItem.setLayoutManager(new GridLayoutManager(BookingActivity.this, 1));
                live.removeObserver(this);

            }
        });

        txtQuantity.setText(quantity);
        txtTotalCost.setText(totalCost);
        txtTitle.setText("Booking");



        btnAutoFill.setOnClickListener(this);
        btnReturn.setOnClickListener(this);
        btnChooseAddress.setOnClickListener(this);
        btnConfirm.setOnClickListener(this);


    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.btnBack){
            setResult(RESULT_CANCELED);
            finish();
        }
        if(v.getId() == R.id.btnChooseAddress){
            startActivityForResult(new Intent(BookingActivity.this,ChooseLocationActivity.class),CHOOSE_ADDRESS_REQUEST);
        }
        if(v.getId() == R.id.btnAutoFill){
            userViewModel.getUserWithId(user.getUid()).observe(BookingActivity.this, new Observer<User>() {
                @Override
                public void onChanged(User us) {
                    if(us != null){
                        txtAddr.setText(us.getUserLocation());
                        edtPhone.setText(us.getUserPhone());
                    }
                }
            });


        }
        if(v.getId() == R.id.btnConfirm){

            // create new order
            String orderId = randomId();


            Order order = new Order(orderId, user.getUid(),edtPhone.getText().toString(),edtNote.getText().toString(),txtAddr.getText().toString(),Integer.parseInt(txtTotalCost.getText().toString().substring(0,txtTotalCost.getText().toString().indexOf("$"))),"Delivering",getCurrentDate(),adapter.getData().get(0).beverage.getBeverageImage(),false,adapter.getData().get(0).beverage.getBeverageImage() );
            DatabaseReference ref = FirebaseDatabase.getInstance().getReference("orders");
            ref.child(orderId).setValue(order);
            //create order detail
            List<BeverageAndCartDetail> data = adapter.getData();
            DatabaseReference ref2 = FirebaseDatabase.getInstance().getReference("orderdetails");
            DatabaseReference refCart = FirebaseDatabase.getInstance().getReference("cartdetails");
            for(BeverageAndCartDetail i : data){
                //xóa các item trong cart
                refCart.child(i.cartDetail.getCartDetailId() + i.cartDetail.getCartDetailBeverage()).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){
                            Toast.makeText(BookingActivity.this,"delete success!!!",Toast.LENGTH_SHORT).show();
                            cartDetailViewModel.deleteItemInCartDetail(i.beverage.getBeverageId(),i.cartDetail.getCartDetailId());

                        }
                        else{
                            Toast.makeText(BookingActivity.this,"delete failed!!!",Toast.LENGTH_SHORT).show();
                        }
                    }
                });

                ref2.child(orderId + i.beverage.getBeverageId()).setValue(new OrderDetail(orderId,i.beverage.getBeverageId(),i.cartDetail.getCartDetailQuantity()));

            }

            AlertDialog.Builder dialog = new AlertDialog.Builder(BookingActivity.this);
            dialog.setView(R.layout.booking_success_dialog);
            dialog.show();
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    dialog.setCancelable(true);
                    setResult(RESULT_OK);
                    finish();
                }
            }, 5000);





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

    public List<Integer> decodeCheckoutList(String input){
        List<Integer> output = new ArrayList<>();
        String temp = input;
        int pos = temp.indexOf("-");
        if(pos == -1)
            output.add(Integer.parseInt(temp));
        else{
            while(true){
                output.add(Integer.parseInt(temp.substring(0, pos)));
                if (pos < temp.length())
                    temp = temp.substring(pos + 1);
                else
                    temp = temp.substring(pos);
                if (temp.isEmpty())
                    break;
                pos = temp.indexOf("-");
                if (pos == -1)
                    pos = temp.length();
            }
        }
        return  output;
    }
    public static String getCurrentDate() {

        Calendar calendar = Calendar.getInstance();
        String str = "";
        str += String.valueOf(calendar.get(Calendar.DAY_OF_MONTH)) + "/"
                + String.valueOf(calendar.get(Calendar.MONTH) + 1) + "/" + String.valueOf(calendar.get(Calendar.YEAR));
        return str;
    }
}
