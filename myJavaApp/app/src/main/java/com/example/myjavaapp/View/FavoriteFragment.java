package com.example.myjavaapp.View;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelStoreOwner;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myjavaapp.Model.LocalViewModel.LocalCartDetailViewModel;
import com.example.myjavaapp.Model.LocalViewModel.LocalFavoriteViewModel;
import com.example.myjavaapp.Model.database.AppDatabase;
import com.example.myjavaapp.Model.entity.BeverageAndType;
import com.example.myjavaapp.Model.entity.CartDetail;
import com.example.myjavaapp.R;
import com.example.myjavaapp.View.Adapter.FavoriteAdapter;
import com.example.myjavaapp.View.Interfaces.BeverageItemClickListener;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class FavoriteFragment extends Fragment implements BeverageItemClickListener {
    private RecyclerView recyclerView;
    private FirebaseUser user;
    private LocalFavoriteViewModel favoriteViewModel;
    private CircleImageView userAvatar;
    private FavoriteAdapter adapter;
    private static final int SINGLE_SCREEN_INTENT = 112299;
    private LocalCartDetailViewModel cartDetailViewModel;




    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.favorite_screen,container,false);
        recyclerView = v.findViewById(R.id.favoriteRecyclerView);
        userAvatar = (CircleImageView) v.findViewById(R.id.userImg);

        user = FirebaseAuth.getInstance().getCurrentUser();
        Uri photo = user.getPhotoUrl();
        if(photo != null){
            Picasso.get().load(photo).into(userAvatar);
        }

        cartDetailViewModel = new ViewModelProvider((ViewModelStoreOwner) getContext()).get(LocalCartDetailViewModel.class);
        favoriteViewModel = new ViewModelProvider((ViewModelStoreOwner) getContext()).get(LocalFavoriteViewModel.class);

        favoriteViewModel.getAllBeverageFromFavoriteWithType(user.getUid()).observe(getViewLifecycleOwner(), new Observer<List<BeverageAndType>>() {
            @Override
            public void onChanged(List<BeverageAndType> beverageAndTypes) {
                adapter = new FavoriteAdapter(getContext(),beverageAndTypes);
                adapter.setListener(FavoriteFragment.this);
                recyclerView.setAdapter(adapter);
                recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
            }
        });

        return v;
    }

    @Override
    public void onBeverageClick(Integer position, String id, String action) {
        if(action.equals("single-beverage")){
            Intent intent = new Intent(getContext(), SingleBeverageActivity.class);
            Bundle data = new Bundle();
            data.putString("beverageId", id);
            intent.putExtras(data);
            startActivityForResult(intent,SINGLE_SCREEN_INTENT);
        }
        else if(action.equals("add-to-cart")){
            //get CartId
            SharedPreferences sharedPreferences = getContext().getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
            String cartId = sharedPreferences.getString("CartUserId","");

            handleUpdateData(id, cartId);
        }else{
            Log.d("FAVORITE FRAGMENT", "Nothing");
        }
    }

    public void handleUpdateData(String id, String cartId){
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("cartdetails");
        LiveData<Boolean> isExistsLive = AppDatabase.getDatabase(getContext()).cartDetailDAO().isExistBeverage(id, cartId);
        Observer<Boolean> isExistObserver = new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                if(aBoolean == false){
                    Log.d("RESULT CHECK", "false");
                    //add new cart detail
                    CartDetail cartItem = new CartDetail(cartId,id,1);
                    ref.child(cartId + id).setValue(cartItem).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful()){
                                cartDetailViewModel.insert(cartItem);
                                Toast.makeText(getContext(),"Added",Toast.LENGTH_SHORT).show();
                            }
                            else{
                                Toast.makeText(getContext(),"There are some error when add item to cart!!!", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                    isExistsLive.removeObserver(this);
                }else if(aBoolean == true){
                    Log.d("RESULT CHECK", "true");
                    //update for cardetail
                    LiveData<Integer> quantityLive = AppDatabase.getDatabase(getContext()).cartDetailDAO().getQuantityOfCartDetail(cartId, id);
                    Observer<Integer> quantityObserver = new Observer<Integer>() {
                        @Override
                        public void onChanged(Integer integer) {
                            Log.d("VALUE", String.valueOf(integer));
                            DatabaseReference ref = FirebaseDatabase.getInstance().getReference("cartdetails");
                            String child = cartId + id;
                            Integer number = integer + 1;
                            ref.child(child).child("cartDetailQuantity").setValue(number).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if(task.isSuccessful()) {
                                        cartDetailViewModel.UpdateQuantityOfAnItem(number, cartId, id);
                                        Toast.makeText(getContext(),"Added",Toast.LENGTH_SHORT).show();
                                    }
                                    else
                                        Toast.makeText(getContext(),"There are some errors when update item!!!",Toast.LENGTH_SHORT).show();
                                }
                            });
                            quantityLive.removeObserver(this);
                        }
                    };
                    quantityLive.observe(getViewLifecycleOwner(),quantityObserver);
                    isExistsLive.removeObserver(this);
                }else {
                    Toast.makeText(getContext(), "Fail to add to cart", Toast.LENGTH_SHORT).show();
                }
            }

        };
        isExistsLive.observe(getViewLifecycleOwner(),isExistObserver);

    }
}
