package com.example.myjavaapp.View;

import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelStoreOwner;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myjavaapp.Model.LocalViewModel.LocalFavoriteViewModel;
import com.example.myjavaapp.Model.entity.Beverage;
import com.example.myjavaapp.R;
import com.example.myjavaapp.View.Adapter.FavoriteAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.squareup.picasso.Picasso;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class FavoriteFragment extends Fragment {
    private RecyclerView recyclerView;
    private FirebaseUser user;
    private LocalFavoriteViewModel favoriteViewModel;
    private CircleImageView userAvatar;



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

        favoriteViewModel = new ViewModelProvider((ViewModelStoreOwner) getContext()).get(LocalFavoriteViewModel.class);
        favoriteViewModel.getAllBeverageFromFavorite().observe((LifecycleOwner) getContext(), new Observer<List<Beverage>>() {
            @Override
            public void onChanged(List<Beverage> beverages) {
                FavoriteAdapter adapter = new FavoriteAdapter(getContext(),beverages);
                recyclerView.setAdapter(adapter);
                recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
            }
        });

        return v;
    }
}
