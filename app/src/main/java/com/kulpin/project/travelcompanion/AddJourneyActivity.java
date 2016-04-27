package com.kulpin.project.travelcompanion;

import android.app.DialogFragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;

import com.kulpin.project.travelcompanion.dto.EventDTO;
import com.kulpin.project.travelcompanion.dto.JourneyDTO;
import com.kulpin.project.travelcompanion.fragment.DatePickerFragment;

import java.text.SimpleDateFormat;
import java.util.GregorianCalendar;

public class AddJourneyActivity extends FragmentActivity{
    private static final int LAYOUT = R.layout.activity_add_journey;
    private EditText addTitle;
    private Button addStartDate;
    private Button addEndDate;
    private GregorianCalendar startDate, endDate;
    private Button addButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(LAYOUT);

        addTitle = (EditText) findViewById(R.id.addJourneyTitle);
        addStartDate = (Button) findViewById(R.id.addStartDate);
        addEndDate = (Button) findViewById(R.id.addEndDate);
        addButton = (Button) findViewById(R.id.addButton);
        addStartDate.setOnClickListener(OnClickListener());
        addEndDate.setOnClickListener(OnClickListener());
        addButton.setOnClickListener(OnClickListener());
        startDate = new GregorianCalendar();
        endDate = new GregorianCalendar();
    }

    private View.OnClickListener OnClickListener(){
        View.OnClickListener clickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.addButton : {
                        Intent intent = new Intent();
                        JourneyDTO journey = new JourneyDTO(addTitle.getText().toString(), startDate.getTime(), endDate.getTime());
                        intent.putExtra(JourneyDTO.class.getCanonicalName(), journey);
                        setResult(RESULT_OK, intent);
                        finish();
                    }
                    break;

                    case R.id.addStartDate : {
                        DialogFragment dateFragment = new DatePickerFragment(){
                            @Override
                            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                                startDate.set(year, monthOfYear, dayOfMonth);
                                addStartDate.setText((new SimpleDateFormat("dd.MM.yyyy")).format(startDate.getTime()));
                            }
                        };
                        dateFragment.show(getFragmentManager(), "DatePicker");
                    }
                    break;

                    case R.id.addEndDate : {
                        DialogFragment dateFragment = new DatePickerFragment(){
                            @Override
                            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                                endDate.set(year, monthOfYear, dayOfMonth);
                                addEndDate.setText((new SimpleDateFormat("dd.MM.yyyy")).format(endDate.getTime()));
                            }
                        };
                        dateFragment.show(getFragmentManager(), "DatePicker");
                    }
                    break;
                }
            }
        };
        return clickListener;
    }
}
