package com.kulpin.project.travelcompanion.fragment;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.view.View;

public class TabFragment extends Fragment{

    private String title;
    protected Context context;
    protected View view;

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTitle() {
        return title;
    }
}
