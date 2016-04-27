package com.kulpin.project.travelcompanion.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.kulpin.project.travelcompanion.R;

/**
 * Created by Andrei on 08.04.2016.
 */
public class EventContentFragment extends Fragment{
    private static final int LAYOUT = R.layout.fragment_event_content;

    private int index;
    private Context context;
    private View view;
    private TextView textView;

    @Override
    public void setArguments(Bundle args) {
        super.setArguments(args);
        index = args.getInt("Index");
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(LAYOUT, container, false);

        textView = (TextView)view.findViewById(R.id.textTitle);
        textView.setText("Item" + index);

        return view;
    }
}
