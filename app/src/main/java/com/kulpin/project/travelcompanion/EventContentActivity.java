package com.kulpin.project.travelcompanion;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.kulpin.project.travelcompanion.adapter.GridViewAdapter;
import com.kulpin.project.travelcompanion.dto.EventDTO;
import com.kulpin.project.travelcompanion.utilities.AppController;
import com.kulpin.project.travelcompanion.utilities.Constants;
import com.kulpin.project.travelcompanion.utilities.DBHelper;
import com.kulpin.project.travelcompanion.utilities.FilePath;
import com.kulpin.project.travelcompanion.utilities.GalleryUtilities;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class EventContentActivity extends AppCompatActivity {

    private static final int LAYOUT = R.layout.activity_event_content;
    private long eventId;
    private EventDTO event;
    private DrawerLayout drawerLayout;
    private AppBarLayout appBarLayout;
    private Toolbar toolbar;
    private TextView mTextPlace;
    private TextView mTextDate;
    private TextView mTextDistance;
    private ImageView image;
    private GridView gridView;

    private ArrayList<String> filePaths;
    private GalleryUtilities galleryUtilities;
    private GridViewAdapter gridViewAdapter;

    private DBHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(LAYOUT);

        bindActivity();
        initToolbar();
        //initNavigationView();
        syncEvent();
        initGallery();
    }

    public static void start(Context context, long eventId){
        Intent intent = new Intent(context, EventContentActivity.class);
        intent.putExtra("eventId", eventId);
        context.startActivity(intent);
    }

    private void bindActivity(){
        this.eventId = getIntent().getLongExtra("eventId", 0);
        //filePaths = new ArrayList<String>();
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout_content);
        appBarLayout = (AppBarLayout) findViewById(R.id.appBar_content);
        toolbar = (Toolbar) findViewById(R.id.toolbar_content);
        mTextPlace = (TextView)findViewById(R.id.textPlace);
        mTextDate = (TextView)findViewById(R.id.textDate);
        mTextDistance = (TextView)findViewById(R.id.textDistance);
        image = (ImageView)findViewById(R.id.image);
        //image.setImageResource(R.drawable.image_1);
        gridView = (GridView) findViewById(R.id.grid_view);
        dbHelper = new DBHelper(this);
        filePaths = dbHelper.getPhotosByEventId(eventId);
        galleryUtilities = new GalleryUtilities(this);

        Button getImagesButton = (Button)findViewById(R.id.button_get_image);
        getImagesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("*/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select Picture"), Constants.RequestCodes.PICK_IMAGE_REQUEST);
            }
        });

        Button deleteAllImagesButton = (Button) findViewById(R.id.button_delete_image);
        deleteAllImagesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dbHelper.deleteAllPhotos();
                filePaths.clear();
                gridViewAdapter.notifyDataSetChanged();
                appBarLayout.setExpanded(false);
                image.setImageBitmap(null);
            }
        });
    }

    public void initToolbar(){
        if (filePaths.isEmpty()) {
            appBarLayout.setExpanded(false);
        } else {
            setCover();
        }
        toolbar.setNavigationIcon(R.mipmap.ic_arrow_left_white_24dp);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    private void initGallery() {
        float padding = galleryUtilities.getPadding(Constants.GRID_PADDING);
        int columnWidth = galleryUtilities.getColumnWidth();

        gridView.setNumColumns(Constants.NUM_COLUMNS);
        gridView.setColumnWidth(columnWidth);
        gridView.setStretchMode(GridView.NO_STRETCH);
        gridView.setPadding((int) padding, (int) padding, (int) padding, (int) padding);
        gridView.setHorizontalSpacing((int) padding);
        gridView.setVerticalSpacing((int) padding);

        gridViewAdapter = new GridViewAdapter(this, filePaths, columnWidth);
        gridView.setAdapter(gridViewAdapter);
    }

    private void setCover(){
        Bitmap bitmap = GalleryUtilities.decodeBitmapFromResource(filePaths.get(0), galleryUtilities.getScreenWidth(), galleryUtilities.getScreenWidth());
        image.setImageBitmap(bitmap);
        appBarLayout.setExpanded(true);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == Constants.RequestCodes.PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {

            /*Uri uri = data.getData();
            String path = uri.getPath();
            Log.d("tclog", path);*/

            String selectedImagePath;
            Uri selectedImageUri = data.getData();
            selectedImagePath = FilePath.getPath(getApplicationContext(), selectedImageUri);
            Log.d("tclog", "path = " + selectedImagePath);
            dbHelper.insertPhotoPath(eventId, selectedImagePath);
            filePaths.add(selectedImagePath);
            gridViewAdapter.notifyDataSetChanged();
            setCover();
        }
    }

    public void syncEvent(){
        String URL = Constants.URL.GET_EVENT + eventId;
        JsonObjectRequest request = new JsonObjectRequest(URL, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    toolbar.setTitle(response.getString("title"));
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


}
