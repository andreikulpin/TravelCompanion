package com.kulpin.project.travelcompanion.controller;


import android.app.Activity;
import android.util.Log;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.kulpin.project.travelcompanion.EventContentActivity;
import com.kulpin.project.travelcompanion.controller.AppController;
import com.kulpin.project.travelcompanion.dto.EventDTO;
import com.kulpin.project.travelcompanion.utilities.Constants;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Date;

public class EventController {
    private EventDTO event;
    private Activity activity;

    public EventController(Activity activity) {
        this.activity = activity;
    }

    public EventDTO getEventById(final long eventId){
        event = new EventDTO();
        String URL = Constants.URL.GET_EVENT + eventId;
        JsonObjectRequest request = new JsonObjectRequest(URL, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    //Log.d("tclog", "onResponse");
                    event.setId(response.getLong("id"));
                    event.setType(response.getInt("type"));
                    event.setTitle(response.getString("title"));
                    event.setPlace(response.getString("place"));
                    event.setDeparturePlace(response.getString("departurePlace"));
                    event.setDestinationPlace(response.getString("destinationPlace"));
                    event.setStartDate(new Date(response.getLong("startDate")));
                    event.setStartTime(new Date(response.getLong("startTime")));
                    event.setEndDate(new Date(response.getLong("endDate")));
                    event.setEndTime(new Date(response.getLong("endTime")));
                } catch (JSONException e){}

                ((EventContentActivity) activity).setEvent(event);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("tclog", "error syncronization event = " + error);
            }
        });
        AppController.getInstance().addToRequestQueue(request);
        return event;
    }


}
