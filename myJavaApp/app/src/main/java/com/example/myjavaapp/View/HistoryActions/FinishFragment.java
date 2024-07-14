package com.example.myjavaapp.View.HistoryActions;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

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

import com.example.myjavaapp.Model.LocalViewModel.LocalCommentViewModel;
import com.example.myjavaapp.Model.LocalViewModel.LocalODAndBEViewModel;
import com.example.myjavaapp.Model.LocalViewModel.LocalOrderViewModel;
import com.example.myjavaapp.Model.entity.Comment;
import com.example.myjavaapp.Model.entity.Order;
import com.example.myjavaapp.Model.entity.OrderDetailAndBeverage;
import com.example.myjavaapp.R;
import com.example.myjavaapp.View.Interfaces.OrderItemClickListener;
import com.example.myjavaapp.View.RepurchaseActivity;
import com.example.myjavaapp.View.ReviewActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.List;

public class FinishFragment extends Fragment implements OrderItemClickListener {
    private RecyclerView recyclerView;
    private FirebaseUser user;
    private LocalOrderViewModel orderViewModel;
    private LocalODAndBEViewModel ODAndBeViewModel;
    private static final int INTENT_REVIEW_ACTIVITY = 121212;
    private LocalCommentViewModel commentViewModel;
    private static final int MOVE_TO_REPURCHASE = 101010;



    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.single_history_order_screen, container,false);
        recyclerView = view.findViewById(R.id.historyRecyclerView);
        user = FirebaseAuth.getInstance().getCurrentUser();
        orderViewModel = new ViewModelProvider((ViewModelStoreOwner) getContext()).get(LocalOrderViewModel.class);
        ODAndBeViewModel = new ViewModelProvider((ViewModelStoreOwner) getContext()).get(LocalODAndBEViewModel.class);
        commentViewModel = new ViewModelProvider((ViewModelStoreOwner) getContext()).get(LocalCommentViewModel.class);

        orderViewModel.getAllOrderBaseOnStatus(user.getUid(), "Finish").observe(getViewLifecycleOwner(), new Observer<List<Order>>() {
            @Override
            public void onChanged(List<Order> orders) {
                SingleItemAdapter adapter = new SingleItemAdapter(getContext(), orders);
                adapter.setListener(FinishFragment.this);
                recyclerView.setAdapter(adapter);
                recyclerView.setLayoutManager(new GridLayoutManager(getContext(),1));
            }
        });


        return view;
    }

    @Override
    public void OnClickListener(String orderId) {
        RecyclerView recyclerViewDialog;
        Button  btnPurchase, btnReview;
        // show dialog
        AlertDialog.Builder dialog = new AlertDialog.Builder(getContext());
        View view = LayoutInflater.from(getContext()).inflate(R.layout.history_order_dialog, (ViewGroup)getView(), false);
        recyclerViewDialog = view.findViewById(R.id.recyclerViewDialog);
        btnPurchase = view.findViewById(R.id.btnPurchase);
        btnReview = view.findViewById(R.id.btnReview);
        ODAndBeViewModel.getAllItemsInOrder(orderId).observe(getViewLifecycleOwner(), new Observer<List<OrderDetailAndBeverage>>() {
            @Override
            public void onChanged(List<OrderDetailAndBeverage> orderDetailAndBeverages) {
                if(orderDetailAndBeverages != null && orderDetailAndBeverages.isEmpty())
                    return;
                SingleBeverageAdapter adapter = new SingleBeverageAdapter(getContext(), orderDetailAndBeverages);
                recyclerViewDialog.setAdapter(adapter);
                recyclerViewDialog.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
            }
        });

        commentViewModel.getCommentOfOrder(orderId).observe(getViewLifecycleOwner(), new Observer<Comment>() {
            @Override
            public void onChanged(Comment comment) {
                if(comment != null) {
                    btnReview.setClickable(false);
                    btnReview.setBackground(getResources().getDrawable(R.drawable.botron_button_unclick));
                }
                else{
                    btnReview.setClickable(true);
                    btnReview.setBackground(getResources().getDrawable(R.drawable.botron_button));
                }
            }
        });

        btnReview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), ReviewActivity.class);
                Bundle data = new Bundle();
                data.putString("orderId",orderId);
                intent.putExtras(data);
                startActivityForResult(intent,INTENT_REVIEW_ACTIVITY);
            }
        });
        btnPurchase.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), RepurchaseActivity.class);
                Bundle data = new Bundle();
                data.putString("orderId",orderId);
                intent.putExtras(data);
                startActivityForResult(intent, MOVE_TO_REPURCHASE);
            }
        });

        dialog.setView(view);
        dialog.show();

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == MOVE_TO_REPURCHASE){
        }
    }
}
