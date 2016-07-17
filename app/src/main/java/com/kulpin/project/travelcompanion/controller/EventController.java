package com.kulpin.project.travelcompanion.controller;


import android.app.Activity;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.kulpin.project.travelcompanion.BasicActivity;
import com.kulpin.project.travelcompanion.EventContentActivity;
import com.kulpin.project.travelcompanion.controller.AppController;
import com.kulpin.project.travelcompanion.dto.EventDTO;
import com.kulpin.project.travelcompanion.dto.Link;
import com.kulpin.project.travelcompanion.utilities.Constants;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Date;

public class EventController {
    private EventDTO event;
    private BasicActivity activity;

    public EventController(BasicActivity activity) {
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

    public void uploadLink(final Link newLink){
        String URL = Constants.URL.ADD_LINK;
        JSONObject jsonObject = new JSONObject();

        try{
            jsonObject.accumulate("eventId", newLink.getEventId());
            jsonObject.accumulate("title", newLink.getTitle());
            jsonObject.accumulate("address", newLink.getAddress());
        } catch (JSONException e){e.printStackTrace();}

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, URL, jsonObject,
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("tclog", "new link created = " + response);
                        try {
                            newLink.setId(response.getLong("id"));
                        }catch (JSONException e){}
                        activity.addLink(newLink);

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("tclog", "error creating new link = " + error
                                + ">>" + error.networkResponse.statusCode
                                + ">>" + error.getMessage());
                    }
        });
        AppController.getInstance().addToRequestQueue(request);

    }

    public void syncLinks(final long eventId){
        String URL = Constants.URL.GET_ALL_LINKS + eventId;
        JsonArrayRequest request = new JsonArrayRequest(URL,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        try {
                            for (int i = 0; i < response.length(); i++) {
                                JSONObject jsonLink = response.getJSONObject(i);
                                Link newLink = new Link();
                                newLink.setId(jsonLink.getLong("id"));
                                newLink.setEventId(jsonLink.getLong("eventId"));
                                newLink.setTitle(jsonLink.getString("title"));
                                newLink.setAddress(jsonLink.getString("address"));
                                activity.addLink(newLink);
                            }
                        } catch (JSONException e){}
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("tclog", "error downloading links = " + error
                        + ">>" + error.networkResponse.statusCode
                        + ">>" + error.getMessage());
            }
        });
        AppController.getInstance().addToRequestQueue(request);
    }

    public void deleteLink(long id){
        String URL = Constants.URL.DELETE_LINK + id;
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.DELETE, URL, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("tclog", "link deleted: " + response);
                    }

                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("myLOG", "error deleting the link = " + error);
            }

        });
        AppController.getInstance().addToRequestQueue(request);

    }
}
