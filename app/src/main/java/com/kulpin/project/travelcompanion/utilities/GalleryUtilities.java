package com.kulpin.project.travelcompanion.utilities;

import android.app.AlertDialog;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.util.TypedValue;
import android.view.Display;
import android.view.WindowManager;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;
import java.util.Locale;

public class GalleryUtilities {

    private Context context;

    public GalleryUtilities(Context context) {
        this.context = context;
    }

    public ArrayList<String> getFilePaths() {
        ArrayList<String> filePaths = new ArrayList<String>();

        File directory = new File(
                android.os.Environment.getExternalStorageDirectory()
                        + File.separator + Constants.PHOTOS_DIRECTORY);

        if (directory.isDirectory()) {
            File[] listFiles = directory.listFiles();

            if (listFiles.length > 0) {
                for (int i = 0; i < listFiles.length; i++) {
                    String filePath = listFiles[i].getAbsolutePath();
                    if (IsSupportedFile(filePath)) {
                        filePaths.add(filePath);
                    }
                }
            } else {
                Toast.makeText(context, Constants.PHOTOS_DIRECTORY
                                + " is empty. Please load some images in it !",
                        Toast.LENGTH_LONG).show();
            }
        } else {
            AlertDialog.Builder alert = new AlertDialog.Builder(context);
            alert.setTitle("Error!");
            alert.setMessage(Constants.PHOTOS_DIRECTORY
                    + " directory path is not valid!");
            alert.setPositiveButton("OK", null);
            alert.show();
        }

        return filePaths;
    }

    private boolean IsSupportedFile(String filePath) {
        String ext = filePath.substring((filePath.lastIndexOf(".") + 1),
                filePath.length());

        if (Constants.FILE_EXTENSIONS.contains(ext.toLowerCase(Locale.getDefault())))
            return true;
        else
            return false;
    }

    public int getScreenWidth() {
        int columnWidth;
        WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Display display = windowManager.getDefaultDisplay();

        final Point point = new Point();
        try {
            display.getSize(point);
        } catch (java.lang.NoSuchMethodError ignore) { // Older device
            point.x = display.getWidth();
            point.y = display.getHeight();
        }
        columnWidth = point.x;
        return columnWidth;
    }

    public float convertDIPtoPXL(int valueInDPs){
        Resources resources = context.getResources();
        float padding = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, valueInDPs, resources.getDisplayMetrics());
        return padding;
    }

    public int getColumnWidth(){
        int columnWidth = (int) ((getScreenWidth() - ((Constants.NUM_COLUMNS + 1) * convertDIPtoPXL(Constants.GRID_PADDING))) / Constants.NUM_COLUMNS);
        return columnWidth;
    }

    public static Bitmap decodeBitmapFromResource(String filePath, int requiredHeight, int requiredWidth){
        File file = new File(filePath);

        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(filePath, options);
        options.inSampleSize = calculateImageSize(options, requiredHeight, requiredWidth);
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeFile(filePath, options);
    }

    public static int calculateImageSize(BitmapFactory.Options options, int requiredHeight, int requiredWidth){
        int scale = 1;
        while (options.outWidth / scale / 2 >= requiredWidth && options.outHeight / scale / 2 >= requiredHeight)
            scale *= 2;

        return scale;
    }
}
