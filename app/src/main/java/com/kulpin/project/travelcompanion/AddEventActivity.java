package com.kulpin.project.travelcompanion;

import android.app.DialogFragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;

import com.kulpin.project.travelcompanion.dto.EventDTO;
import com.kulpin.project.travelcompanion.fragment.DatePickerFragment;
import com.kulpin.project.travelcompanion.utilities.Constants;

import java.text.SimpleDateFormat;
import java.util.GregorianCalendar;

public class AddEventActivity extends FragmentActivity {
    private static final int LAYOUT = R.layout.activity_add_event;
    private EditText addTitle;
    private EditText addPlace;
    private Button addDate;
    private GregorianCalendar eventDate;
    private EditText addDistance;
    private Toolbar toolbar;
    private EventDTO newEvent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(LAYOUT);
        bindActivity();
        initToolbar();
        if (getIntent().getAction() != null && getIntent().getAction().equals(Constants.Actions.EDIT_EVENT_ACTION))
            fillFieds();
    }

    private void fillFieds() {
        EventDTO event = getIntent().getParcelableExtra(EventDTO.class.getCanonicalName());
        addTitle.setText(event.getTitle());
        addPlace.setText(event.getPlace());
        addDate.setText((new SimpleDateFormat("dd.MM.yyyy")).format(event.getEventDate()));
        addDistance.setText(((Float) event.getDistance()).toString());
        eventDate.setTime(event.getEventDate());
        newEvent.setId(event.getId());
    }

    public void bindActivity(){
        addTitle = (EditText) findViewById(R.id.addTitle);
        addPlace = (EditText) findViewById(R.id.addPlace);
        addDate = (Button) findViewById(R.id.addDate);
        addDistance = (EditText) findViewById(R.id.addDistance);
        addDate.setOnClickListener(OnClickListener());
        eventDate = new GregorianCalendar();
        newEvent = new EventDTO();
    }

    private void initToolbar(){
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.inflateMenu(R.menu.menu_create);
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.done: {
                        Intent intent = new Intent();
                        newEvent.setTitle(addTitle.getText().toString());
                        newEvent.setPlace(addPlace.getText().toString());
                        newEvent.setEventDate(eventDate.getTime());
                        newEvent.setDistance(Float.parseFloat(addDistance.getText().toString()));
                        intent.putExtra(EventDTO.class.getCanonicalName(), newEvent);
                        setResult(RESULT_OK, intent);
                        finish();
                    }
                    break;
                }
                return false;
            }
        });

        toolbar.setNavigationIcon(R.mipmap.ic_arrow_left_white_24dp);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    private View.OnClickListener OnClickListener(){
        View.OnClickListener clickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.addDate : {
                        DialogFragment dateFragment = new DatePickerFragment(){
                            @Override
                            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                                eventDate.set(year, monthOfYear, dayOfMonth);
                                addDate.setText((new SimpleDateFormat("dd.MM.yyyy")).format(eventDate.getTime()));
                            }
                        };
                        dateFragment.show(getFragmentManager(), "DatePicker");
                    }
                }
            }
        };
        return clickListener;
    }
}
