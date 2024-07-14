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
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myjavaapp.Model.LocalViewModel.LocalODAndBEViewModel;
import com.example.myjavaapp.Model.LocalViewModel.LocalOrderDetailViewModel;
import com.example.myjavaapp.Model.LocalViewModel.LocalOrderViewModel;
import com.example.myjavaapp.Model.LocalViewModel.LocalUserViewModel;
import com.example.myjavaapp.Model.entity.Order;
import com.example.myjavaapp.Model.entity.OrderDetailAndBeverage;
import com.example.myjavaapp.Model.entity.User;
import com.example.myjavaapp.R;
import com.example.myjavaapp.View.Adapter.RepurchaseItemAdapter;
import com.example.myjavaapp.View.Interfaces.ReOrderItemListener;
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

public class RepurchaseActivity extends AppCompatActivity implements ReOrderItemListener, View.OnClickListener {
    private TextView txtTitle, txtQuantity, txtTotalCost, txtAddress;
    private Button btnAutofill, btnConfirm;
    private RecyclerView recyclerView;
    private TextInputEditText edtPhone, edtNote;
    private ImageButton btnChooseAddr, btnBack;
    private String orderId = "";
    private LocalOrderViewModel orderViewModel;
    private LocalOrderDetailViewModel orderDetailViewModel;
    private LocalODAndBEViewModel ODAndBEViewModel;
    private RepurchaseItemAdapter adapter;
    private LocalUserViewModel userViewModel;
    private FirebaseUser user;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.repurchase_screen);
        //definitions
        txtTitle = findViewById(R.id.mainTitle);
        txtQuantity = findViewById(R.id.quantityContent);
        txtTotalCost = findViewById(R.id.totalPriceContent);
        txtAddress = findViewById(R.id.repurchaseAddress);
        btnAutofill = findViewById(R.id.btnAutoFill);
        btnChooseAddr = findViewById(R.id.btnChooseAddress);
        btnConfirm = findViewById(R.id.btnConfirm);
        recyclerView = findViewById(R.id.listItem);
        edtNote = findViewById(R.id.repurchaseNote);
        edtPhone = findViewById(R.id.repurchasePhone);
        btnBack = findViewById(R.id.btnBack);

        Intent receiver = getIntent();
        Bundle data = receiver.getExtras();
        orderId = data.getString("orderId");
        txtTitle.setText("Repurchase");

        user = FirebaseAuth.getInstance().getCurrentUser();
        orderViewModel = new ViewModelProvider(this).get(LocalOrderViewModel.class);
        orderDetailViewModel = new ViewModelProvider(this).get(LocalOrderDetailViewModel.class);
        ODAndBEViewModel = new ViewModelProvider(this).get(LocalODAndBEViewModel.class);
        userViewModel = new ViewModelProvider(this).get(LocalUserViewModel.class);
        LiveData<List<OrderDetailAndBeverage>> listLive = ODAndBEViewModel.getAllItemsInOrder(orderId);
        listLive.observe(this, new Observer<List<OrderDetailAndBeverage>>() {
            @Override
            public void onChanged(List<OrderDetailAndBeverage> orderDetailAndBeverageList) {
                if(orderDetailAndBeverageList != null && orderDetailAndBeverageList.isEmpty())
                    return;
                adapter = new RepurchaseItemAdapter(RepurchaseActivity.this, orderDetailAndBeverageList);
                adapter.setListener(RepurchaseActivity.this);
                recyclerView.setAdapter(adapter);
                recyclerView.setLayoutManager(new LinearLayoutManager(RepurchaseActivity.this, LinearLayoutManager.VERTICAL, false));
                listLive.removeObserver(this);
            }
        });

        btnBack.setOnClickListener(this);
        btnAutofill.setOnClickListener(this);
        btnConfirm.setOnClickListener(this);



    }



    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.btnBack){
            setResult(RESULT_CANCELED);
            finish();
        }
        if(v.getId() == R.id.btnAutoFill){
            userViewModel.getUserWithId(user.getUid()).observe(RepurchaseActivity.this, new Observer<User>() {
                @Override
                public void onChanged(User us) {
                    if(us == null)
                        return;
                    edtPhone.setText(us.getUserPhone());
                    txtAddress.setText(us.getUserLocation());
                }
            });
        }
        if(v.getId() == R.id.btnConfirm){
            String orderId = randomId();
            List<Boolean> checkItems = adapter.getCheckList();
            List<OrderDetailAndBeverage> data = adapter.getData();
            List<OrderDetailAndBeverage> choosen = new ArrayList<>();
            // check if exist item is selected
            for(int i = 0 ; i < checkItems.size(); ++i)
                if(checkItems.get(i) == true) {
                    data.get(i).orderDetail.setOrderDetailId(orderId);
                    choosen.add(data.get(i));
                }

            if(choosen.size() == 0) {
                Toast.makeText(RepurchaseActivity.this, "Please choose at least 1 item to make an order!!!", Toast.LENGTH_SHORT).show();
                return;
            }else if(edtPhone.getText().toString().isEmpty()){
                Toast.makeText(RepurchaseActivity.this, "Please fill enough information!!!", Toast.LENGTH_SHORT).show();
                return;
            }else if(txtAddress.getText().toString().isEmpty()){
                Toast.makeText(RepurchaseActivity.this, "Please fill enough information!!!", Toast.LENGTH_SHORT).show();
                return;
            }
            else{
                String orderDate = getCurrentDate();
                String cost = txtTotalCost.getText().toString();
                cost = cost.substring(0, cost.length()-1);
                Order order = new Order(orderId, user.getUid(), edtPhone.getText().toString(), edtNote.getText().toString(), txtAddress.getText().toString(), Integer.parseInt(cost), "Delivering", orderDate, false,  choosen.get(0).beverage.getBeverageImage());
                DatabaseReference refOrder = FirebaseDatabase.getInstance().getReference("orders");
                DatabaseReference refDetail = FirebaseDatabase.getInstance().getReference("orderdetails");
                refOrder.child(orderId).setValue(order).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){
                            orderViewModel.Insert(order);
                            for(int i = 0; i < choosen.size(); ++i){
                                OrderDetailAndBeverage item = choosen.get(i);
                                refDetail.child(orderId + item.beverage.getBeverageId()).setValue(item.orderDetail).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if(task.isSuccessful()){
                                            orderDetailViewModel.insert(item.orderDetail);
                                        }
                                    }
                                });
                            }
                        }
                    }
                });
                setResult(RESULT_OK);
                finish();
            }
        }
    }

    @Override
    public void onClickListener(String action, int quantity, String cost) {
        int curQuantity = Integer.parseInt(txtQuantity.getText().toString());
        String price = txtTotalCost.getText().toString();
        price = price.substring(0, price.length()-1);
        int curPrice = Integer.parseInt(price);
        String inputCost = "";
        int changePrice = 0;
        if(!cost.isEmpty()) {
            inputCost = cost.substring(0, cost.length() - 1);
            changePrice = Integer.parseInt(inputCost);
        }

        if(action.equals("plus")){
            // to do nothing
            adapter.notifyDataSetChanged();
        }else if(action.equals("substract")){
            // to do nothing
            adapter.notifyDataSetChanged();
        }else if(action.equals("check")){
            txtQuantity.setText(String.valueOf(curQuantity + quantity));
            txtTotalCost.setText(String.valueOf(curPrice + quantity * changePrice) + "$");
        }else if(action.equals("uncheck")){
            txtQuantity.setText(String.valueOf(curQuantity - quantity));
            txtTotalCost.setText(String.valueOf(curPrice - quantity * changePrice) + "$");
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
