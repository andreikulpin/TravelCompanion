package com.kulpin.project.travelcompanion.fragment;

import android.support.v4.app.Fragment;

import com.kulpin.project.travelcompanion.dto.EventDTO;

/**
 * Created by Andrei on 16.07.2016.
 */
public abstract class BasicCreateFragment extends Fragment {
    public abstract boolean checkValues();
    public abstract EventDTO getNewEvent();
}
