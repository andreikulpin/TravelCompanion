package com.kulpin.project.travelcompanion.adapter;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.util.Log;

import com.kulpin.project.travelcompanion.fragment.JourneyListFragment;
import com.kulpin.project.travelcompanion.fragment.TabFragment;

import java.util.HashMap;
import java.util.Map;

public class ViewPagerAdapter extends FragmentPagerAdapter {
    private Map<Integer, TabFragment> tabs;
    private Context context;

    public ViewPagerAdapter(Context context, FragmentManager fragmentManager) {
        super(fragmentManager);
        this.context = context;
        initTabsMap(context);
    }

    private void initTabsMap(Context context) {
        tabs = new HashMap<>();
        tabs.put(0, JourneyListFragment.getInstance(context, "active"));
        tabs.put(1, JourneyListFragment.getInstance(context, "last"));
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return tabs.get(position).getArguments().getString("title");
    }

    @Override
    public Fragment getItem(int position) {
        return tabs.get(position);
    }

    @Override
    public int getCount() {
        return tabs.size();
    }

    public void onRefresh(){
        ((JourneyListFragment)tabs.get(0)).syncJourneyList();
        ((JourneyListFragment)tabs.get(1)).syncJourneyList();
    }

    public void onDelete(int position){
        switch (position){
            case 0: ((JourneyListFragment)tabs.get(0)).deleteJourney(0);
                break;
            case 1: ((JourneyListFragment)tabs.get(1)).deleteJourney(0);
                break;
        }
    }
}
