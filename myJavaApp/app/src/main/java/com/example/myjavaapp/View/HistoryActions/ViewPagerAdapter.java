package com.example.myjavaapp.View.HistoryActions;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

public class ViewPagerAdapter extends FragmentStateAdapter {


    public ViewPagerAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        try{
            switch (position){
                case 0:
                    return new DeliveringFragment();
                case 1:
                    return new FinishFragment();
                default:
                    throw new IllegalArgumentException("Invalid position: " + position);

            }
        }catch(Exception e){
            e.printStackTrace();
            return new DeliveringFragment();
        }

    }

    @Override
    public int getItemCount() {
        return 2;
    }
}
