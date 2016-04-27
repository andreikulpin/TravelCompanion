package com.kulpin.project.travelcompanion.fragment;


import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.kulpin.project.travelcompanion.R;
import com.kulpin.project.travelcompanion.adapter.ViewPagerAdapter;

public class PagesContainerFragment extends Fragment{
    private static final int LAYOUT = R.layout.fragment_tabs_container;
    private Context context;
    private View view;
    private ViewPager viewPager;
    private ViewPagerAdapter viewPagerAdapter;
    private TabLayout tabLayout;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Log.d("myLOG", "onCreate " + this);
        viewPagerAdapter = new ViewPagerAdapter(getActivity().getApplicationContext(), getChildFragmentManager());
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(LAYOUT, container, false);
        //Log.d("myLOG", "onCreateView " + toString());
        initTabs();
        return view;
    }

    private void initTabs(){
        viewPager = (ViewPager) view.findViewById(R.id.viewPager);
        viewPager.setAdapter(viewPagerAdapter);

        tabLayout = (TabLayout)view.findViewById(R.id.tabLayout);
        tabLayout.setupWithViewPager(viewPager);
    }

    public ViewPagerAdapter getViewPagerAdapter() {
        return viewPagerAdapter;
    }

    public TabLayout getTabLayout() {
        return tabLayout;
    }

    public void onRefresh(){
        viewPagerAdapter.onRefresh();
    }

    public void onDelete(){
        viewPagerAdapter.onDelete(tabLayout.getSelectedTabPosition());
    }

}
