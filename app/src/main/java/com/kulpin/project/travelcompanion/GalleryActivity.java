package com.kulpin.project.travelcompanion;

import android.content.res.Resources;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.TypedValue;
import android.widget.GridView;

import com.kulpin.project.travelcompanion.adapter.GridViewAdapter;
import com.kulpin.project.travelcompanion.utilities.Constants;
import com.kulpin.project.travelcompanion.utilities.GalleryUtilities;

import java.util.ArrayList;

public class GalleryActivity extends AppCompatActivity {
    private static final int LAYOUT = R.layout.activity_gallery;
    private Toolbar toolbar;
    private GridView gridView;
    private GridViewAdapter gridViewAdapter;
    private GalleryUtilities galleryUtilities;
    private ArrayList<String> filePaths;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(LAYOUT);

        bindActivity();
        initGallery();
    }

    private void initGallery() {
        filePaths = galleryUtilities.getFilePaths();

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

    private void bindActivity(){
        toolbar = (Toolbar) findViewById(R.id.toolbar_content);
        gridView = (GridView) findViewById(R.id.grid_view_gallery);
        galleryUtilities = new GalleryUtilities(this);

    }


}
