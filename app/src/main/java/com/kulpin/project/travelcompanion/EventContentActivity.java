package com.kulpin.project.travelcompanion;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.support.design.widget.AppBarLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.kulpin.project.travelcompanion.adapter.DocumentListAdapter;
import com.kulpin.project.travelcompanion.adapter.GridViewAdapter;
import com.kulpin.project.travelcompanion.dto.Document;
import com.kulpin.project.travelcompanion.dto.EventDTO;
import com.kulpin.project.travelcompanion.dto.Photo;
import com.kulpin.project.travelcompanion.utilities.Constants;
import com.kulpin.project.travelcompanion.utilities.DBHelper;
import com.kulpin.project.travelcompanion.utilities.EventController;
import com.kulpin.project.travelcompanion.utilities.FilePath;
import com.kulpin.project.travelcompanion.utilities.GalleryUtilities;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

public class EventContentActivity extends AppCompatActivity {

    private static final int LAYOUT = R.layout.activity_event_content;
    private long eventId;
    private EventController eventController;
    private EventDTO event;
    private AppBarLayout appBarLayout;
    private Toolbar toolbar;
    private ProgressBar progressBar;
    private TextView mTextPlace;
    private TextView mTextDate;
    private ImageView image;
    private CardView attachDocument;
    private RecyclerView recyclerView;
    private CardView gallery;
    private GridView gridView;
    private CardView allPhotos;
    private CardView attachPhoto;

    private ArrayList<Document> documentList;
    private DocumentListAdapter documentListAdapter;
    private ArrayList<Photo> photoList;
    private GalleryUtilities galleryUtilities;
    private GridViewAdapter gridViewAdapter;

    private DBHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(LAYOUT);

        bindActivity();
        initToolbar();
        initDocuments();
        //initNavigationView();
        Log.d("tclog", "onCreate");
    }

    @Override
    protected void onResume() {
        super.onResume();
        initGallery();
        setActivityCover();
        Log.d("tclog", "onResume");
    }

    public static void start(Context context, long eventId){
        Intent intent = new Intent(context, EventContentActivity.class);
        intent.putExtra("eventId", eventId);
        context.startActivity(intent);
    }

    private void bindActivity(){
        this.eventId = getIntent().getLongExtra("eventId", 0);
        eventController = new EventController(this);
        eventController.getEventById(eventId);
        progressBar = (ProgressBar) findViewById(R.id.progress_content);
        progressBar.setVisibility(View.VISIBLE);
        appBarLayout = (AppBarLayout) findViewById(R.id.appBar_content);
        toolbar = (Toolbar) findViewById(R.id.toolbar_content);
        mTextPlace = (TextView)findViewById(R.id.textPlace);
        mTextDate = (TextView)findViewById(R.id.textDate);
        dbHelper = new DBHelper(this);
        galleryUtilities = new GalleryUtilities(this);
        image = (ImageView)findViewById(R.id.image);
        gallery = (CardView) findViewById(R.id.cardview_gallery_content);
        gridView = (GridView) findViewById(R.id.grid_view);

        attachDocument = (CardView) findViewById(R.id.cardView_attach_document);
        attachDocument.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("*/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select File"), Constants.RequestCodes.PICK_FILE_REQUEST);
            }
        });
        allPhotos = (CardView) findViewById(R.id.cardView_all_photos);
        allPhotos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startGalleryActivity();
            }
        });
        attachPhoto = (CardView) findViewById(R.id.cardView_attach_photo);
        attachPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("*/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select Picture"), Constants.RequestCodes.PICK_IMAGE_REQUEST);
            }
        });
    }

    private void startGalleryActivity(){
        GalleryActivity.start(this, eventId, event.getTitle());
    }

    public void initToolbar(){
        toolbar.setNavigationIcon(R.mipmap.ic_arrow_left_white_24dp);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        toolbar.inflateMenu(R.menu.menu_content);
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.attach_photo: {
                        Intent intent = new Intent();
                        intent.setType("*/*");
                        intent.setAction(Intent.ACTION_GET_CONTENT);
                        startActivityForResult(Intent.createChooser(intent, "Select Picture"), Constants.RequestCodes.PICK_IMAGE_REQUEST);
                    }
                    break;

                    case R.id.clear_gallery: {
                        dbHelper.deleteAllPhotos(eventId);
                        photoList.clear();
                        gridViewAdapter.notifyDataSetChanged();
                        setActivityCover();
                    }
                    break;

                    case R.id.refresh_content: {
                        eventController.getEventById(eventId);
                        progressBar.setVisibility(View.VISIBLE);
                    }
                    break;

                    case R.id.clear_documents:{
                        dbHelper.deleteAllDocuments(eventId);
                        documentList.clear();
                        documentListAdapter.notifyDataSetChanged();
                    }
                    break;

                }
                return false;
            }
        });
    }

    /*setting of TextViews with the data from event*/
    public void setEvent(EventDTO event){
        this.event = event;
        toolbar.setTitle(event.getTitle());
        mTextPlace.setText(event.getPlace());
        mTextDate.setText((new SimpleDateFormat("dd.MM.yyyy")).format(event.getEventDate().getTime()));
        progressBar.setVisibility(View.INVISIBLE);
    }

    /*document list initialization*/
    private void initDocuments(){
        documentList = dbHelper.getDocumentsByEventId(eventId);
        recyclerView = (RecyclerView) findViewById(R.id.recycleView_documents);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        documentListAdapter = new DocumentListAdapter(documentList, this);
        recyclerView.setAdapter(documentListAdapter);
    }

    /*photos grid initialization*/
    private void initGallery() {
        photoList = dbHelper.getPhotosByEventId(eventId);
        while(photoList.size() > 3) photoList.remove(photoList.size() - 1);

        float padding = galleryUtilities.convertDIPtoPXL(Constants.GRID_PADDING);
        int columnWidth = galleryUtilities.getColumnWidth();

        gridView.setNumColumns(Constants.NUM_COLUMNS);
        gridView.setColumnWidth(columnWidth);
        gridView.setStretchMode(GridView.NO_STRETCH);
        gridView.setPadding((int) padding, (int) padding, (int) padding, (int) padding);
        gridView.setHorizontalSpacing((int) padding);
        gridView.setVerticalSpacing((int) padding);

        gridViewAdapter = new GridViewAdapter(this, photoList, columnWidth);
        gridView.setAdapter(gridViewAdapter);
    }

    private void setActivityCover(){
        if(photoList.isEmpty()){
            appBarLayout.setExpanded(false);
            gallery.setVisibility(View.INVISIBLE);
            allPhotos.setVisibility(View.INVISIBLE);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            attachPhoto.setLayoutParams(params);
            image.setImageBitmap(null);
            return;
        } else {
            appBarLayout.setExpanded(true);
            gallery.setVisibility(View.VISIBLE);
            allPhotos.setVisibility(View.VISIBLE);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.MATCH_PARENT, 1);
            attachPhoto.setLayoutParams(params);
        }

        Bitmap bitmap = GalleryUtilities.decodeBitmapFromResource(photoList.get(0).getFilePath(), galleryUtilities.getScreenWidth(), galleryUtilities.getScreenWidth());
        image.setImageBitmap(bitmap);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == Constants.RequestCodes.PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            String selectedImagePath;
            Uri selectedImageUri = data.getData();
            selectedImagePath = FilePath.getPath(getApplicationContext(), selectedImageUri);
            Photo photo = new Photo(eventId, "photo", selectedImagePath);
            dbHelper.insertPhoto(photo);
        }

        if (requestCode == Constants.RequestCodes.PICK_FILE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            String selectedFilePath;
            Uri selectedFileUri = data.getData();

            selectedFilePath = FilePath.getPath(getApplicationContext(), selectedFileUri);
            Document document = new Document(eventId, selectedFileUri.getLastPathSegment(), selectedFilePath);
            documentList.add(document);
            dbHelper.insertDocument(document);
            documentListAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        getMenuInflater().inflate(R.menu.menu_context, menu);
        menu.removeItem(R.id.edit_context);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.delete_context:{
                int position = documentListAdapter.getSelectedPosition();
                dbHelper.deleteDocument(documentList.get(position).getId());
                documentList.remove(position);
                documentListAdapter.notifyDataSetChanged();
            }
            break;
        }
        return super.onContextItemSelected(item);
    }
}
