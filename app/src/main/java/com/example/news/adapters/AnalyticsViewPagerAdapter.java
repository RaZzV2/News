package com.example.news.adapters;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.news.fragments.AllStatisticsFragment;
import com.example.news.fragments.MyStatisticsFragment;

public class AnalyticsViewPagerAdapter extends FragmentStateAdapter {


    public AnalyticsViewPagerAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        if (position == 1) {
            return new AllStatisticsFragment();
        }
        return new MyStatisticsFragment();
    }

    @Override
    public int getItemCount() {
        return 2;
    }
}
