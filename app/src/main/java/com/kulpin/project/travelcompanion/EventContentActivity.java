package com.kulpin.project.travelcompanion;

import android.content.Context;
import android.content.Intent;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;

public class EventContentActivity extends AppCompatActivity {

    private static final int LAYOUT = R.layout.activity_event_content;
    private static long eventId;
    private CollapsingToolbarLayout mToolbar;
    private TextView mTextPlace;
    private TextView mTextDate;
    private TextView mTextDistance;
    private ImageView image;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(LAYOUT);

        bindActivity();
        syncEvent();
    }

    public static void start(Context context, long eventId){
        context.startActivity(new Intent(context, EventContentActivity.class));
        EventContentActivity.eventId = eventId;
    }

    public void syncEvent(){
        String URL = Constants.URL.GET_EVENT + eventId;
        JsonObjectRequest request = new JsonObjectRequest(URL, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    mToolbar.setTitle(response.getString("title"));
                    mTextPlace.setText(response.getString("place"));
                    mTextDate.setText((new SimpleDateFormat("dd.MM.yyyy")).format(new Date(response.getLong("eventDate"))));
                    mTextDistance.setText(response.getString("distance") + " km");
                } catch (JSONException e){}
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("myLOG", "error syncronization event = " + error);
            }
        });
        AppController.getInstance().addToRequestQueue(request);
    }

    private void bindActivity(){
        mToolbar = (CollapsingToolbarLayout) findViewById(R.id.collapsingToolbar);
        mTextPlace = (TextView)findViewById(R.id.textPlace);
        mTextDate = (TextView)findViewById(R.id.textDate);
        mTextDistance = (TextView)findViewById(R.id.textDistance);
        image = (ImageView)findViewById(R.id.image);
        image.setImageResource(R.drawable.image_1);
    }
}
