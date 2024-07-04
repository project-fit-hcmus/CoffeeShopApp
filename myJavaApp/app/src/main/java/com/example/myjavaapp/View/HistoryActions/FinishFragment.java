package com.example.myjavaapp.View.HistoryActions;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelStoreOwner;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myjavaapp.Model.LocalViewModel.LocalOrderViewModel;
import com.example.myjavaapp.Model.entity.Order;
import com.example.myjavaapp.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.List;

public class FinishFragment extends Fragment {
    private RecyclerView recyclerView;
    private FirebaseUser user;
    private LocalOrderViewModel orderViewModel;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.single_history_order_screen, container,false);
        Toast.makeText(getContext(),"Finish Screen",Toast.LENGTH_SHORT).show();
        recyclerView = view.findViewById(R.id.historyRecyclerView);
        user = FirebaseAuth.getInstance().getCurrentUser();
        orderViewModel = new ViewModelProvider((ViewModelStoreOwner) getContext()).get(LocalOrderViewModel.class);

        orderViewModel.getAllOrders(user.getUid()).observe(getViewLifecycleOwner(), new Observer<List<Order>>() {
            @Override
            public void onChanged(List<Order> orders) {
                SingleItemAdapter adapter = new SingleItemAdapter(getContext(), orders);
                recyclerView.setAdapter(adapter);
                recyclerView.setLayoutManager(new GridLayoutManager(getContext(),1));
            }
        });

        return view;
    }
}
