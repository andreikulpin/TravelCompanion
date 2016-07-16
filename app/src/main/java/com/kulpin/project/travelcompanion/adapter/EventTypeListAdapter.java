package com.kulpin.project.travelcompanion.adapter;

import android.app.Activity;
import android.content.Context;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.kulpin.project.travelcompanion.R;
import com.kulpin.project.travelcompanion.utilities.Constants;

import java.util.ArrayList;

/**
 * Created by Andrei on 16.07.2016.
 */
public class EventTypeListAdapter extends BaseAdapter {

    private ArrayList<String> eventTypeList;
    private Activity activity;
    private LayoutInflater inflater;

    public EventTypeListAdapter(Activity activity) {
        this.activity = activity;
        inflater = activity.getLayoutInflater();

        eventTypeList = new ArrayList<>();
        eventTypeList.add(activity.getString(R.string.event_type_plane));
        eventTypeList.add(activity.getString(R.string.event_type_train));
        eventTypeList.add(activity.getString(R.string.event_type_bus));
        eventTypeList.add(activity.getString(R.string.event_type_museum));
        eventTypeList.add(activity.getString(R.string.event_type_cinema));
    }

    @Override
    public int getCount() {
        return eventTypeList.size();
    }

    @Override
    public Object getItem(int position) {
        return eventTypeList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = inflater.inflate(R.layout.item_list_type_event, parent, false);

        ((TextView) view.findViewById(R.id.text_type_event)).setText(eventTypeList.get(position));
        switch (++position) {
            case Constants.EventType.TYPE_PLANE:
                ((ImageView) view.findViewById(R.id.icon_type_event)).setImageBitmap(BitmapFactory.decodeResource(activity.getResources(), R.mipmap.ic_airplane_black_24dp));
                break;
            case Constants.EventType.TYPE_TRAIN:
                ((ImageView) view.findViewById(R.id.icon_type_event)).setImageBitmap(BitmapFactory.decodeResource(activity.getResources(), R.mipmap.ic_train_black_24dp));
                break;
            case Constants.EventType.TYPE_BUS:
                ((ImageView) view.findViewById(R.id.icon_type_event)).setImageBitmap(BitmapFactory.decodeResource(activity.getResources(), R.mipmap.ic_bus_black_24dp));

            case Constants.EventType.TYPE_MUSEUM:
                ((ImageView) view.findViewById(R.id.icon_type_event)).setImageBitmap(BitmapFactory.decodeResource(activity.getResources(), R.mipmap.ic_hospital_building_black_24dp));
                break;
            case Constants.EventType.TYPE_CINEMA:
                ((ImageView) view.findViewById(R.id.icon_type_event)).setImageBitmap(BitmapFactory.decodeResource(activity.getResources(), R.mipmap.ic_filmstrip_black_24dp));
                break;
        }

        return view;
    }
}
