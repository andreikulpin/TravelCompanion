package com.kulpin.project.travelcompanion;

import android.app.DialogFragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;

import com.kulpin.project.travelcompanion.dto.EventDTO;
import com.kulpin.project.travelcompanion.fragment.DatePickerFragment;

import java.text.SimpleDateFormat;
import java.util.GregorianCalendar;

public class AddEventActivity extends FragmentActivity {
    private static final int LAYOUT = R.layout.activity_add_event;
    private EditText addTitle;
    private EditText addPlace;
    private Button addDate;
    private GregorianCalendar calendar;
    private EditText addDistance;
    private Button addButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(LAYOUT);

        addTitle = (EditText) findViewById(R.id.addTitle);
        addPlace = (EditText) findViewById(R.id.addPlace);
        addDate = (Button) findViewById(R.id.addDate);
        addDistance = (EditText) findViewById(R.id.addDistance);
        addButton = (Button) findViewById(R.id.addButton);
        addDate.setOnClickListener(OnClickListener());
        addButton.setOnClickListener(OnClickListener());
        calendar = new GregorianCalendar();
    }

    private View.OnClickListener OnClickListener(){
        View.OnClickListener clickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.addButton : {
                        Intent intent = new Intent();
                        EventDTO event = new EventDTO(addTitle.getText().toString(), addPlace.getText().toString(), calendar.getTime(), Float.parseFloat(addDistance.getText().toString()));
                        intent.putExtra(EventDTO.class.getCanonicalName(), event);
                        setResult(RESULT_OK, intent);
                        finish();
                    }
                    break;

                    case R.id.addDate : {
                        DialogFragment dateFragment = new DatePickerFragment(){
                            @Override
                            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                                calendar.set(year, monthOfYear, dayOfMonth);
                                addDate.setText((new SimpleDateFormat("dd.MM.yyyy")).format(calendar.getTime()));
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
