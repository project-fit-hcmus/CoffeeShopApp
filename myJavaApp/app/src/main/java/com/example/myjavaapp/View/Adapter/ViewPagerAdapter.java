package com.example.myjavaapp.View.Adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.example.myjavaapp.View.CartFragment;
import com.example.myjavaapp.View.FavoriteFragment;
import com.example.myjavaapp.View.HomeFragment;
import com.example.myjavaapp.View.ProfileFragment;

public class ViewPagerAdapter extends FragmentStatePagerAdapter {
    public ViewPagerAdapter(@NonNull FragmentManager fm, int behavior) {
        super(fm, behavior);
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position){
            case 0:
                return new HomeFragment();
            case 1:
                return new FavoriteFragment();
            case 2:
                return new CartFragment();
            case 3:
                return new ProfileFragment();
            default:
                return new ProfileFragment();
        }
    }

    @Override
    public int getCount() {
        return 4;
    }
}
