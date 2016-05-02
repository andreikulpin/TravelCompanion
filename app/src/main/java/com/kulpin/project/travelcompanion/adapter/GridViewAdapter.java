package com.kulpin.project.travelcompanion.adapter;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

import com.kulpin.project.travelcompanion.utilities.GalleryUtilities;

import java.io.File;
import java.util.ArrayList;

/**
 * Created by Andrei on 28.04.2016.
 */
public class GridViewAdapter extends BaseAdapter{

    private Activity activity;
    private ArrayList<String> filePaths;
    private int imageWidth;

    public GridViewAdapter(Activity activity, ArrayList<String> filePaths, int imageWidth) {
        this.activity = activity;
        this.filePaths = filePaths;
        this.imageWidth = imageWidth;
    }

    @Override
    public int getCount() {
        return filePaths.size();
    }

    @Override
    public Object getItem(int position) {
        return filePaths.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ImageView imageView;
        if (convertView == null) {
            imageView = new ImageView(activity);
        } else {
            imageView = (ImageView) convertView;
        }

        Bitmap image = GalleryUtilities.decodeBitmapFromResource(filePaths.get(position), imageWidth, imageWidth);
        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        imageView.setLayoutParams(new GridView.LayoutParams(imageWidth, imageWidth));
        imageView.setImageBitmap(image);

        return imageView;
    }
}
