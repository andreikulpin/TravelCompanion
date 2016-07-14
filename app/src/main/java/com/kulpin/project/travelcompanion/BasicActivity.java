package com.kulpin.project.travelcompanion;

import android.support.v7.app.AppCompatActivity;

import com.kulpin.project.travelcompanion.controller.PhotoController;
import com.kulpin.project.travelcompanion.dto.Photo;
import com.kulpin.project.travelcompanion.utilities.DBHelper;
import com.kulpin.project.travelcompanion.utilities.GalleryUtilities;

import java.util.ArrayList;

/**
 * Created by Andrei on 14.07.2016.
 */
public abstract class BasicActivity extends AppCompatActivity {
    protected ArrayList<Photo> photoList;
    protected PhotoController photoController;
    protected GalleryUtilities galleryUtilities;
    protected DBHelper dbHelper;

    public abstract void addPhotoToLocalDB(final Photo photo);
    public abstract void updatePhotos();
    public abstract boolean isPhotoExistsLocally(final long photoId);

}
