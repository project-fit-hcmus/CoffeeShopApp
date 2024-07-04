package com.example.myjavaapp.View.HistoryActions;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelStoreOwner;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myjavaapp.Model.LocalViewModel.LocalODAndBEViewModel;
import com.example.myjavaapp.Model.LocalViewModel.LocalOrderViewModel;
import com.example.myjavaapp.Model.entity.Order;
import com.example.myjavaapp.Model.entity.OrderDetailAndBeverage;
import com.example.myjavaapp.R;
import com.example.myjavaapp.View.Interfaces.OrderItemClickListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.List;

public class DeliveringFragment extends Fragment implements OrderItemClickListener {
    private FirebaseUser user;

    private RecyclerView recyclerView;
    private LocalOrderViewModel orderViewModel;
    private LocalODAndBEViewModel  ODAndBeViewModel;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.single_history_order_screen, container,false );
        Toast.makeText(getContext(),"Delivering screen",Toast.LENGTH_SHORT).show();
        recyclerView = view.findViewById(R.id.historyRecyclerView);
        user = FirebaseAuth.getInstance().getCurrentUser();
        orderViewModel = new ViewModelProvider((ViewModelStoreOwner) getContext()).get(LocalOrderViewModel.class);
        ODAndBeViewModel = new ViewModelProvider((ViewModelStoreOwner) getContext()).get(LocalODAndBEViewModel.class);

        orderViewModel.getAllOrderBaseOnStatus(user.getUid(), "Delivering").observe(getViewLifecycleOwner(), new Observer<List<Order>>() {
            @Override
            public void onChanged(List<Order> orders) {
                SingleItemAdapter adapter = new SingleItemAdapter(getContext(),orders);
                adapter.setListener(DeliveringFragment.this);
                recyclerView.setAdapter(adapter);
                recyclerView.setLayoutManager(new GridLayoutManager(getContext(),1));
            }
        });



        return  view;
    }

    @Override
    public void OnClickListener(String orderId) {
        //show dialog
        RecyclerView recyclerViewDialog;
        Button btnExit, btnPurchase;
        // show dialog
        AlertDialog.Builder dialog = new AlertDialog.Builder(getContext());
        View view = LayoutInflater.from(getContext()).inflate(R.layout.history_order_dialog, (ViewGroup)getView(), false);
        recyclerViewDialog = view.findViewById(R.id.recyclerViewDialog);
        btnExit = view.findViewById(R.id.btnExit);
        btnPurchase = view.findViewById(R.id.btnPurchase);
        ODAndBeViewModel.getAllItemsInOrder(orderId).observe(getViewLifecycleOwner(), new Observer<List<OrderDetailAndBeverage>>() {
            @Override
            public void onChanged(List<OrderDetailAndBeverage> orderDetailAndBeverages) {
                if(orderDetailAndBeverages != null && orderDetailAndBeverages.isEmpty())
                    return;
                Toast.makeText(getContext(),String.valueOf(orderDetailAndBeverages.size()),Toast.LENGTH_SHORT).show();
                SingleBeverageAdapter adapter = new SingleBeverageAdapter(getContext(), orderDetailAndBeverages);
                recyclerViewDialog.setAdapter(adapter);
                recyclerViewDialog.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
            }
        });


        btnExit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.setCancelable(true);         // ERROR
            }
        });

        dialog.setView(view);
        dialog.show();
    }
}
