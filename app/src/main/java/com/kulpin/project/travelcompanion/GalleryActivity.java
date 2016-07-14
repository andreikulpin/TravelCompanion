package com.kulpin.project.travelcompanion;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;

import com.kulpin.project.travelcompanion.adapter.GridViewAdapter;
import com.kulpin.project.travelcompanion.controller.PhotoController;
import com.kulpin.project.travelcompanion.dto.Photo;
import com.kulpin.project.travelcompanion.utilities.Constants;
import com.kulpin.project.travelcompanion.utilities.DBHelper;
import com.kulpin.project.travelcompanion.utilities.FilePath;
import com.kulpin.project.travelcompanion.utilities.GalleryUtilities;

import java.util.ArrayList;

public class GalleryActivity extends BasicActivity {
    private static final int LAYOUT = R.layout.activity_gallery;
    private long eventId;
    private String title;
    private Toolbar toolbar;
    private GridView gridView;
    private GridViewAdapter gridViewAdapter;
    private GalleryUtilities galleryUtilities;
    private ArrayList<Photo> photoList;
    private DBHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(LAYOUT);
        bindActivity();
        initToolbar();
    }

    @Override
    protected void onResume() {
        super.onResume();
        initGallery();
    }

    public static void start(Context context, long eventId, String title){
        Intent intent = new Intent(context, GalleryActivity.class);
        intent.putExtra("eventId", eventId);
        intent.putExtra("title", title);
        context.startActivity(intent);
    }

    private void bindActivity(){
        this.eventId = getIntent().getLongExtra("eventId", 0);
        this.title = getIntent().getStringExtra("title");
        dbHelper = new DBHelper(this);
        toolbar = (Toolbar) findViewById(R.id.toolbar_gallery);
        gridView = (GridView) findViewById(R.id.grid_view_gallery);
        galleryUtilities = new GalleryUtilities(this);
        photoController = new PhotoController(this);
    }

    private void initGallery() {
        photoList = dbHelper.getPhotosByEventId(eventId);

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
        registerForContextMenu(gridView);
        /*
        * setOnItemClickListener and setOnItemLongClickListener here, because in case of
        * implementation of it in gridViewAdapter such methods doesn't work for first
        * element in GridView.
        * */
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Log.d("tclog", "onItemClick: position = " + position);
            }
        });
        gridView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                Log.d("tclog", "onItemLongClick: position = " + position);
                gridViewAdapter.setSelectedPosition(position);
                return false;
            }
        });
    }

    public void initToolbar(){
        toolbar.setTitle(title + " " + getString(R.string.gallery_photos));
        toolbar.setNavigationIcon(R.mipmap.ic_arrow_left_white_24dp);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        toolbar.inflateMenu(R.menu.menu_gallery);
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
                        photoController.deleteAllPhotos(eventId);
                        dbHelper.deleteAllPhotos(eventId);
                        photoList.clear();
                        gridViewAdapter.notifyDataSetChanged();
                        onBackPressed();
                    }
                    break;

                    case R.id.clear_local_gallery:
                    {
                        dbHelper.deleteAllPhotos(eventId);
                        photoList.clear();
                        gridViewAdapter.notifyDataSetChanged();
                        onBackPressed();
                    }
                }
                return false;
            }
        });
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == Constants.RequestCodes.PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            String selectedImagePath;
            Uri selectedImageUri = data.getData();
            selectedImagePath = FilePath.getPath(getApplicationContext(), selectedImageUri);
            Photo photo = new Photo(eventId, "new", selectedImagePath);
            //dbHelper.insertPhoto(photo);
            photoController.uploadImage(photo);
        }
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        menu.setHeaderTitle(getString(R.string.options));
        getMenuInflater().inflate(R.menu.menu_context, menu);
        menu.removeItem(R.id.edit_context);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.delete_context:{
                int position = gridViewAdapter.getSelectedPosition();
                photoController.delete(photoList.get(position).getId());
                dbHelper.deletePhoto(photoList.get(position).getId());
                photoList.remove(position);
                gridViewAdapter.notifyDataSetChanged();
            }
            break;
        }
        return super.onContextItemSelected(item);
    }

    @Override
    public void addPhotoToLocalDB(Photo photo) {
        dbHelper.insertPhoto(photo);
    }

    @Override
    public void updatePhotos() {
        initGallery();
    }

    @Override
    public boolean isPhotoExistsLocally(long photoId) {
        return dbHelper.isPhotoExists(photoId);
    }
}
