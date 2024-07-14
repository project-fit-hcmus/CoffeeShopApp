package com.example.myjavaapp.View;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.utils.widget.ImageFilterButton;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myjavaapp.R;
import com.example.myjavaapp.View.Adapter.PrivacyAdapter;

import java.util.ArrayList;
import java.util.List;

public class PrivacyActivity extends AppCompatActivity {
    private TextView txtTitle;
    private RecyclerView content;
    private ImageFilterButton btnBack;
    List<String> headers = new ArrayList<>();
    List<String> contents = new ArrayList<>();
    List<Integer> images = new ArrayList<>();
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.private_policy_screen);
        txtTitle = findViewById(R.id.mainTitle);
        content = findViewById(R.id.mainContent);
        btnBack = findViewById(R.id.btnBack);

        txtTitle.setText("Private Policy");
        setData();
        content.setAdapter(new PrivacyAdapter(this,headers,contents,images));
        content.setLayoutManager(new GridLayoutManager(this,1));

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setResult(RESULT_OK);
                finish();
            }
        });
    }

    public void setData(){
        headers.add("What infomation do we collect?");
        contents.add("Personal Information: Name, phone number, email address, delivery address, payment method.\n" +
                "Device Data: Device type, operating system, app version, IP address, location (if you enable location services).\n" +
                "Activity Data: How you use the App, such as order history, favorite products, etc");
        headers.add("How do we use your information?");
        contents.add("We use your Data for the following purposes:\n" +
                "\n" +
                "To provide and improve the App.\n" +
                "To process orders and payments.\n" +
                "To send you notifications about offers and promotions.\n" +
                "To provide customer support.\n" +
                "To analyze user behavior and improve the performance of the App.\n" +
                "To comply with legal requirements.");
        headers.add("Your rights");
        contents.add("You have the following rights with respect to your Data:\n" +
                "\n" +
                "The right to access your Data.\n" +
                "The right to rectify your Data.\n" +
                "The right to erase your Data.\n" +
                "The right to object to the collection or use of your Data.\n" +
                "The right to restrict the processing of your Data.");
        headers.add("Data Security");
        contents.add("We implement appropriate security measures to protect your Data from unauthorized access, use, disclosure, alteration, or destruction. However, no security measure is perfect.");
        headers.add("Data Sharing");
        contents.add("We may share your Data with the following third parties:\n" +
                "\n" +
                "Service providers: We may share your Data with service providers who help us provide the App, such as payment processors, hosting providers.\n" +
                "Government agencies: We may share your Data with government agencies when required by law.");



    }

}
