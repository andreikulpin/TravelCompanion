package com.kulpin.project.travelcompanion.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.kulpin.project.travelcompanion.AppController;
import com.kulpin.project.travelcompanion.Constants;
import com.kulpin.project.travelcompanion.R;
import com.kulpin.project.travelcompanion.adapter.EventListAdapter;
import com.kulpin.project.travelcompanion.dto.EventDTO;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class EventListFragment extends TabFragment{
    private static final int LAYOUT = R.layout.fragment_timeline_main;

    private Context context;
    private View view;
    private EventListAdapter adapter;
    private List<EventDTO> list;

    /*public static EventListFragment getInstance(Context context){
        Bundle args = new Bundle();
        EventListFragment fragment = new EventListFragment();
        fragment.setArguments(args);
        fragment.setContext(context);
        return fragment;
    }*/

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(LAYOUT, container, false);
        list = new ArrayList<>();
        //list = createMockEventListData();
        RecyclerView rv = (RecyclerView)view.findViewById(R.id.recycleView);
        rv.setLayoutManager(new LinearLayoutManager(context));
        adapter = new EventListAdapter(list, getActivity());
        rv.setAdapter(adapter);
        syncEventList();
        return view;
    }

    public void syncEventList(){
        String URL = Constants.URL.GET_ALL_EVENTS + getArguments().getLong("journeyId");
        JsonArrayRequest jsonArrayRequest=new JsonArrayRequest(URL, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                list.clear();
                for(int i=0; i<response.length(); i++){
                    try{
                        JSONObject object = response.getJSONObject(i);
                        EventDTO item = new EventDTO();
                        item.setId(object.getLong("id"));
                        item.setTitle(object.getString("title"));
                        item.setPlace(object.getString("place"));
                        item.setDistance(object.getInt("distance"));
                        item.setEventDate(new Date(object.getLong("eventDate")));
                        list.add(item);
                    }catch(JSONException e){
                        e.printStackTrace();
                    }
                }
                adapter.notifyDataSetChanged();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("myLOG", "error syncronization event list = " + error);
            }
        });
        AppController.getInstance().addToRequestQueue(jsonArrayRequest);
    }

    public void addNewEvent(EventDTO newEvent){
        String URL = Constants.URL.ADD_EVENT;
        JSONObject object = new JSONObject();

        try {
            object.accumulate("journeyId", getArguments().getLong("journeyId"));
            object.accumulate("userId", 0);
            object.accumulate("title", newEvent.getTitle());
            object.accumulate("place", newEvent.getPlace());
            object.accumulate("eventDate", newEvent.getEventDate().getTime());
            object.accumulate("distance", newEvent.getDistance());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, URL, object, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.d("myLOG", "new event created = " + response);
                syncEventList();
            }

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("myLOG", "error creating new event = " + error
                        /*+ ">>" + error.networkResponse.statusCode
                        + ">>" + error.networkResponse.data*/
                        + ">>" + error.getCause()
                        + ">>" + error.getMessage());
            }

        });
        AppController.getInstance().addToRequestQueue(request);

    }

    public void deleteEvent(long eventId){
        if (list.isEmpty()) return;
        eventId = list.get(list.size() - 1).getId();
        String URL = Constants.URL.DELETE_EVENT + eventId;
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.DELETE, URL, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.d("myLOG", "event deleted: " + response);
                syncEventList();
            }

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("myLOG", "error deleting event = " + error);
                syncEventList();
            }

        });
        AppController.getInstance().addToRequestQueue(request);

    }

    private List<EventDTO> createMockEventListData() {
        List<EventDTO> list = new ArrayList<>();
        for (int i =1; i <= 10; i ++) {
            list.add(new EventDTO("Event Title " + i, "Place " + i, new Date(), i * 10));
        }
        return list;
    }
}
