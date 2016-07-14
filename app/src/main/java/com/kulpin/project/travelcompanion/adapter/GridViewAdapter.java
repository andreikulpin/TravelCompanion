package com.kulpin.project.travelcompanion.adapter;

import android.app.Activity;
import android.graphics.Bitmap;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

import com.kulpin.project.travelcompanion.dto.Photo;
import com.kulpin.project.travelcompanion.utilities.GalleryUtilities;

import java.util.ArrayList;

/**
 * Created by Andrei on 28.04.2016.
 */
public class GridViewAdapter extends BaseAdapter{

    private Activity activity;
    private ArrayList<Photo> photoList;
    private int imageWidth;
    private int selectedPosition;

    public GridViewAdapter(Activity activity, ArrayList<Photo> photoList, int imageWidth) {
        this.activity = activity;
        this.photoList = photoList;
        this.imageWidth = imageWidth;
    }

    @Override
    public int getCount() {
        return photoList.size();
    }

    @Override
    public Object getItem(int position) {
        return photoList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ImageView imageView;
        if (convertView == null) {
            imageView = new ImageView(activity);
        } else {
            imageView = (ImageView) convertView;
        }

        Bitmap image = GalleryUtilities.decodeBitmapFromResource(photoList.get(position).getPhotoPath(), imageWidth, imageWidth);
        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        imageView.setLayoutParams(new GridView.LayoutParams(imageWidth, imageWidth));
        imageView.setImageBitmap(image);
        return imageView;
    }

    public int getSelectedPosition() {
        return selectedPosition;
    }

    public void setSelectedPosition(int selectedPosition) {
        this.selectedPosition = selectedPosition;
    }
}
