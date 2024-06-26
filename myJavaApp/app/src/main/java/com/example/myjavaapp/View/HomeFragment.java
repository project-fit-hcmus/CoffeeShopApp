package com.example.myjavaapp.View;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myjavaapp.Model.dao.TypeDAO;
import com.example.myjavaapp.Model.database.AppDatabase;
import com.example.myjavaapp.Model.entity.Beverage;
import com.example.myjavaapp.Model.entity.CartDetail;
import com.example.myjavaapp.Model.entity.Type;
import com.example.myjavaapp.R;
import com.example.myjavaapp.View.Adapter.CategoryAdapter;
import com.example.myjavaapp.View.Adapter.homeBeverageAdapter;
import com.example.myjavaapp.View.Interfaces.BeverageItemClickListener;
import com.example.myjavaapp.View.Interfaces.ItemClickListener;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class HomeFragment extends Fragment implements ItemClickListener, BeverageItemClickListener {
    private FirebaseUser user;
    private TextView txtGreeting;
    private EditText edtSearch;
    private ImageButton btnSearch;
    private CircleImageView userAvatar;
    private RecyclerView category, lstBeverages;
    private LiveData<List<Beverage>> allBeverages;
    private Observer<List<Beverage>> beverageObserve;
    private LiveData<List<Type>> allTypes;
    private Observer<List<Type>> typesObserve;
    private ArrayList<Type> listType;



    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.home_screen,container,false);
        user = FirebaseAuth.getInstance().getCurrentUser();
        txtGreeting = (TextView) v.findViewById(R.id.greetingHome);
        userAvatar = (CircleImageView) v.findViewById(R.id.userImg);
        category = (RecyclerView) v.findViewById(R.id.recyclerCategory);
        lstBeverages = (RecyclerView) v.findViewById(R.id.homeSubMainItems);
        edtSearch = (EditText) v.findViewById(R.id.keywordSearchHome);
        btnSearch = (ImageButton) v.findViewById(R.id.btnSearch);


        //get user data
        user = FirebaseAuth.getInstance().getCurrentUser();

        Uri photo = user.getPhotoUrl();
        if(photo != null){
            Picasso.get().load(photo).into(userAvatar);
        }
        //get system time
        String gretting = getTime();
        gretting += " " + user.getDisplayName() + ",";
        txtGreeting.setText(gretting);

        allTypes = AppDatabase.getDatabase(getContext()).typeDAO().getAllType();
        typesObserve = new Observer<List<Type>>() {
            @Override
            public void onChanged(List<Type> types) {
                if(types != null && types.isEmpty())
                    return;
                listType = new ArrayList<>(types);
                CategoryAdapter adapter = new CategoryAdapter(getContext(), listType);
                adapter.setClickListener(HomeFragment.this);
                category.setAdapter(adapter);
                category.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL,false));
            }
        };
        allTypes.observe((LifecycleOwner) getContext(),typesObserve);

        //get hot beverage
        allBeverages = AppDatabase.getDatabase(getContext()).beverageDAO().getBestSellerBeverage();
        beverageObserve = new Observer<List<Beverage>>() {
            @Override
            public void onChanged(List<Beverage> beverages) {
                if(beverages != null && beverages.isEmpty())
                    return;
                homeBeverageAdapter adapter = new homeBeverageAdapter(getContext(), beverages);
                adapter.setItemClickListener(HomeFragment.this);
                lstBeverages.setAdapter(adapter);
                lstBeverages.setLayoutManager(new GridLayoutManager(getContext(),2));

            }
        };
        allBeverages.observe((LifecycleOwner) getContext(),beverageObserve);

        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String keywork = edtSearch.getText().toString();
                if(keywork.isEmpty())
                    Toast.makeText(getContext(),"Please enter keyword to search",Toast.LENGTH_SHORT).show();
                else{
                    Intent intent = new Intent(getContext(),SearchActivity.class);
                    Bundle data = new Bundle();
                    data.putString("keyword",keywork);
                    intent.putExtras(data);
                    startActivityForResult(intent,12345);
                    edtSearch.setHint("Search...");
                    edtSearch.setText("");
                }

            }
        });



        return v;
    }

    public String getTime(){

        Calendar calendar = Calendar.getInstance();
        int currentHour = calendar.get(Calendar.HOUR_OF_DAY);
        if(currentHour >= 0 && currentHour <= 12 )
            return "Good Morning";
        else if(currentHour <= 17){
            return "Good Afternoon";
        }else if(currentHour <= 22){
            return "Good Evening";
        }else return "Good Night";
    }

    @Override
    public void onItemClick(int position, String id) {
        Bundle data = new Bundle();
        data.putString("keyword",id);
        Intent intent = new Intent(getContext(), homeSingleCateScreen.class);
        intent.putExtras(data);
        startActivityForResult(intent, 11111);
    }

    @Override
    public void onBeverageClick(Integer position, String id, String action) {
        Log.d("ACTION",action);
        Log.d("BEVERAGE",id);
        if (action.contains("add-to-cart")) {
            Toast.makeText(getContext(),"Added",Toast.LENGTH_SHORT).show();
            String Uid = user.getUid();
            Log.d("USER ID",Uid);
            LiveData<String> cartIdLive = AppDatabase.getDatabase(getContext()).cartDAO().getCartIdFromUser(Uid);
            Observer<String> cartIdObserver = new Observer<String>() {
                @Override
                public void onChanged(String s) {
                    Log.d("cart id",s);
                    Log.d("beverage id",id);
                    if (s != null && s.isEmpty())
                        return;
                    LiveData<Boolean> isExistsLive = AppDatabase.getDatabase(getContext()).cartDetailDAO().isExistBeverage(id, s);
                    Observer<Boolean> isExistObserver = new Observer<Boolean>() {
                        @Override
                        public void onChanged(Boolean aBoolean) {
                            if(aBoolean == false){
                                Log.d("RESULT CHECK", "false");
                                //thêm mới cho cartdetail
                                CartDetail cartItem = new CartDetail(s,id,1);
                                DatabaseReference ref = FirebaseDatabase.getInstance().getReference("cartdetails");
                                ref.child(s + id).setValue(cartItem);
                                isExistsLive.removeObserver(this);
                            }else if(aBoolean == true){
                                Log.d("RESULT CHECK", "true");
                                //update trong cardetail
                                LiveData<Integer> quantityLive = AppDatabase.getDatabase(getContext()).cartDetailDAO().getQuantityOfCartDetail(s, id);
                                Observer<Integer> quantityObserver = new Observer<Integer>() {
                                    @Override
                                    public void onChanged(Integer integer) {
                                        Log.d("VALUE", String.valueOf(integer));
                                        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("cartdetails");
                                        String child = s + id;
                                        ref.child(child).child("cartDetailQuantity").setValue(integer + 1);
                                        quantityLive.removeObserver(this);
                                    }
                                };
                                quantityLive.observe(getViewLifecycleOwner(),quantityObserver);
                                isExistsLive.removeObserver(this);
                            }else {
                                Toast.makeText(getContext(), "NOT TRUE OR FALSE", Toast.LENGTH_SHORT).show();
                            }
                        }

                    };
                    isExistsLive.observe(getViewLifecycleOwner(),isExistObserver);
                    cartIdLive.removeObserver(this);
                }
            };
            cartIdLive.observe((LifecycleOwner) getContext(), cartIdObserver);
        }
        else if(action.contains("single-beverage")){
            Intent intent = new Intent(getContext(),SingleBeverageActivity.class);
            Bundle data = new Bundle();
            data.putString("beverageId",id);
            intent.putExtras(data);
            startActivityForResult(intent, 112233);
        }
    }
}
