package com.kulpin.project.travelcompanion.fragment;

import android.app.DialogFragment;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;

import com.kulpin.project.travelcompanion.R;
import com.kulpin.project.travelcompanion.dto.EventDTO;
import com.kulpin.project.travelcompanion.dto.JourneyDTO;
import com.kulpin.project.travelcompanion.utilities.Constants;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;

public class CreateLocalEventFragment extends BasicCreateFragment {

    private static final int LAYOUT = R.layout.fragment_event_local;

    private EditText editTitle;
    private EditText editPlace;
    private Button editStartDate;
    private Button editStartTime;
    private Button editEndTime;
    private GregorianCalendar startDate;
    private GregorianCalendar startTime;
    private GregorianCalendar endTime;
    private EventDTO newEvent;
    private EventDTO currentEvent;
    private JourneyDTO journey;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(LAYOUT, container, false);

        editTitle = (EditText) view.findViewById(R.id.edit_title);
        editPlace = (EditText) view.findViewById(R.id.edit_place);
        editStartDate = (Button) view.findViewById(R.id.edit_start_date);
        editStartTime = (Button) view.findViewById(R.id.edit_start_time);
        editEndTime = (Button) view.findViewById(R.id.edit_end_time);

        editStartDate.setOnClickListener(OnClickListener());
        editStartTime.setOnClickListener(OnClickListener());
        editEndTime.setOnClickListener(OnClickListener());

        newEvent = new EventDTO();
        startDate = new GregorianCalendar();
        startTime = new GregorianCalendar();
        endTime = new GregorianCalendar();

        journey  = getArguments().getParcelable(JourneyDTO.class.getCanonicalName());
        currentEvent = getArguments().getParcelable(EventDTO.class.getCanonicalName());
        if (getArguments().getString("action").equals(Constants.Actions.EDIT_EVENT_ACTION)){
            fillFields();
        }
        return view;
    }

    public EventDTO getNewEvent(){
        newEvent.setTitle(editTitle.getText().toString());
        newEvent.setPlace(editPlace.getText().toString());
        newEvent.setStartDate(startDate.getTime());
        newEvent.setStartTime(startTime.getTime());
        newEvent.setEndTime(endTime.getTime());

        newEvent.setDeparturePlace(editPlace.getText().toString());
        newEvent.setDestinationPlace(editPlace.getText().toString());
        newEvent.setEndDate(startDate.getTime());
        return newEvent;
    }

    private void fillFields(){
        newEvent.setId(currentEvent.getId());
        newEvent.setType(currentEvent.getType());
        editTitle.setText(currentEvent.getTitle());
        editPlace.setText(currentEvent.getPlace());
        editStartDate.setText((new SimpleDateFormat("dd.MM.yyyy")).format(currentEvent.getStartDate()));
        editStartTime.setText((new SimpleDateFormat("HH:mm")).format(currentEvent.getStartTime()));
        editEndTime.setText((new SimpleDateFormat("HH:mm")).format(currentEvent.getEndTime()));
        startDate.setTime(currentEvent.getStartDate());
        startTime.setTime(currentEvent.getStartTime());
        endTime.setTime(currentEvent.getEndTime());
    }

    public boolean checkValues(){
        setMock();
        if (editTitle.getText().toString().length() == 0){
            Toast.makeText(getActivity(), getString(R.string.create_error_enter_title), Toast.LENGTH_LONG).show();
            return false;
        }
        if (editPlace.getText().toString().length() == 0){
            Toast.makeText(getActivity(), getString(R.string.create_error_enter_place), Toast.LENGTH_LONG).show();
            return false;
        }
        if (editStartDate.getText().toString().equals(getResources().getString(R.string.create_choose_start_date))) {
            Toast.makeText(getActivity(), getString(R.string.create_error_enter_start_date), Toast.LENGTH_LONG).show();
            return false;
        }
        if (editStartTime.getText().toString().equals(getResources().getString(R.string.create_choose_start_time))) {
            Toast.makeText(getActivity(), getString(R.string.create_error_enter_start_time), Toast.LENGTH_LONG).show();
            return false;
        }
        if (editEndTime.getText().toString().equals(getResources().getString(R.string.create_choose_end_time))) {
            Toast.makeText(getActivity(), getString(R.string.create_error_enter_end_time), Toast.LENGTH_LONG).show();
            return false;
        }
        return true;
    }

    private void setMock(){

    }

    private View.OnClickListener OnClickListener(){
        View.OnClickListener clickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.edit_start_date : {
                        DialogFragment dateFragment = new DatePickerFragment(){
                            @Override
                            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                                startDate.set(year, monthOfYear, dayOfMonth);
                                if (startDate.getTimeInMillis() < journey.getStartDate().getTime() ||
                                        startDate.getTimeInMillis() > journey.getEndDate().getTime()){
                                    Toast.makeText(getActivity().getBaseContext(), getString(R.string.create_error_date_out_of_bounds) + "(" +
                                            (new SimpleDateFormat("dd.MM.yyyy")).format(journey.getStartDate().getTime()) + " - " +
                                            (new SimpleDateFormat("dd.MM.yyyy")).format(journey.getEndDate().getTime()) + ")", Toast.LENGTH_LONG).show();
                                } else {
                                    editStartDate.setText((new SimpleDateFormat("dd.MM.yyyy")).format(startDate.getTime()));
                                }
                            }
                        };
                        dateFragment.show(getActivity().getFragmentManager(), "DatePicker");
                    }
                    break;

                    case R.id.edit_start_time : {
                        TimePickerDialog timePicker = new TimePickerDialog(getActivity(), new TimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                                startTime.set(Calendar.HOUR_OF_DAY, hourOfDay);
                                startTime.set(Calendar.MINUTE, minute);
                                editStartTime.setText((new SimpleDateFormat("HH:mm")).format(startTime.getTime()));

                            }
                        },
                                Calendar.getInstance().get(Calendar.HOUR_OF_DAY),
                                Calendar.getInstance().get(Calendar.MINUTE), true);
                        timePicker.show();
                    }
                    break;

                    case R.id.edit_end_time : {
                        TimePickerDialog timePicker = new TimePickerDialog(getActivity(), new TimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                                endTime.set(Calendar.HOUR_OF_DAY, hourOfDay);
                                endTime.set(Calendar.MINUTE, minute);
                                editEndTime.setText((new SimpleDateFormat("HH:mm")).format(endTime.getTime()));
                            }
                        },
                                Calendar.getInstance().get(Calendar.HOUR_OF_DAY),
                                Calendar.getInstance().get(Calendar.MINUTE), true);
                        timePicker.show();
                    }
                    break;
                }
            }
        };
        return clickListener;
    }
}
