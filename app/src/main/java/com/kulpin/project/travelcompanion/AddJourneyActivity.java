package com.kulpin.project.travelcompanion;

import android.app.DialogFragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;

import com.kulpin.project.travelcompanion.dto.EventDTO;
import com.kulpin.project.travelcompanion.dto.JourneyDTO;
import com.kulpin.project.travelcompanion.fragment.DatePickerFragment;
import com.kulpin.project.travelcompanion.utilities.Constants;

import java.text.SimpleDateFormat;
import java.util.GregorianCalendar;

public class AddJourneyActivity extends AppCompatActivity{
    private static final int LAYOUT = R.layout.activity_add_journey;
    private EditText addTitle;
    private Button addStartDate;
    private Button addEndDate;
    private GregorianCalendar startDate, endDate;
    private Toolbar toolbar;
    private JourneyDTO newJourney;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(LAYOUT);
        bindActivity();
        initToolbar();

        if (getIntent().getAction() != null && getIntent().getAction().equals(Constants.Actions.EDIT_JOURNEY_ACTION))
            fillFieds();
    }

    private void bindActivity(){
        addTitle = (EditText) findViewById(R.id.addJourneyTitle);
        addStartDate = (Button) findViewById(R.id.addStartDate);
        addEndDate = (Button) findViewById(R.id.addEndDate);
        addStartDate.setOnClickListener(OnClickListener());
        addEndDate.setOnClickListener(OnClickListener());
        startDate = new GregorianCalendar();
        endDate = new GregorianCalendar();
        newJourney = new JourneyDTO();
    }

    private void fillFieds() {
        JourneyDTO journey = getIntent().getParcelableExtra(JourneyDTO.class.getCanonicalName());
        addTitle.setText(journey.getTitle());
        addStartDate.setText((new SimpleDateFormat("dd.MM.yyyy")).format(journey.getStartDate()));
        addEndDate.setText((new SimpleDateFormat("dd.MM.yyyy")).format(journey.getEndDate()));
        startDate.setTime(journey.getStartDate());
        endDate.setTime(journey.getEndDate());
        newJourney.setId(journey.getId());
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
                        newJourney.setTitle(addTitle.getText().toString());
                        newJourney.setStartDate(startDate.getTime());
                        newJourney.setEndDate(endDate.getTime());
                        intent.putExtra(JourneyDTO.class.getCanonicalName(), newJourney);
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
