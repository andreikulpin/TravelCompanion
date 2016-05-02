package com.kulpin.project.travelcompanion.utilities;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;

public class DBHelper extends SQLiteOpenHelper{

    private SQLiteDatabase db;
    private ContentValues contentValues;
    Cursor cursor;

    public DBHelper (Context context){
        super(context, Constants.DBNAME, null, Constants.DBVERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table photos ("
        + "id integer primary key autoincrement,"
        + "eventId long,"
        + "title text,"
        + "filepath text" + ");");
        Log.d("tclog", "Local database created");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public void insertPhotoPath(long eventId, String filepath){
        db = getWritableDatabase();
        contentValues = new ContentValues();
        contentValues.put("eventId", eventId);
        contentValues.put("title", "photo");
        contentValues.put("filepath", filepath);
        long rowId = db.insert("photos", null, contentValues);
        Log.d("tclog", "row inserted, id = " + rowId);
        this.close();
    }

    public void getAllPhotoPaths(ArrayList<String> filePaths){
        contentValues = new ContentValues();
        db = getWritableDatabase();
        cursor = db.query("photos", null, null, null, null, null, null);

        if (cursor.moveToFirst()){
            int eventIdColIndex = cursor.getColumnIndex("eventId");
            int titleColIndex = cursor.getColumnIndex("title");
            int filepathColIndex = cursor.getColumnIndex("filepath");

            do{
                filePaths.add(cursor.getString(filepathColIndex));
                Log.d("tclog", cursor.getString(filepathColIndex) + " " + cursor.getString(titleColIndex) + " " + cursor.getLong(eventIdColIndex));
            } while (cursor.moveToNext());
            //gridViewAdapter.notifyDataSetChanged();
        } else Log.d("tclog", "no photos");
        cursor.close();
        this.close();
    }

    public ArrayList<String> getPhotosByEventId(long eventId){
        ArrayList<String> filePaths = new ArrayList<>();
        contentValues = new ContentValues();
        db = getWritableDatabase();
        db.beginTransaction();
        try {
            String selection = "eventId = ?";
            String[] selectionArgs = new String[] {((Long) eventId).toString() };
            cursor = db.query("photos", null, selection, selectionArgs, null, null, null);
            db.setTransactionSuccessful();
        } finally {
            db.endTransaction();
        }

        if (cursor != null)
            if (cursor.moveToFirst()) {
                int eventIdColIndex = cursor.getColumnIndex("eventId");
                int titleColIndex = cursor.getColumnIndex("title");
                int filepathColIndex = cursor.getColumnIndex("filepath");
                do {
                    filePaths.add(cursor.getString(filepathColIndex));
                    Log.d("tclog", cursor.getString(filepathColIndex) + " title=" + cursor.getString(titleColIndex) + " eventId=" + cursor.getLong(eventIdColIndex));
                } while (cursor.moveToNext());
            } else Log.d("tclog", "No photos attached to event");
        return filePaths;
    }

    public void deleteAllPhotos(){
        SQLiteDatabase db = getWritableDatabase();
        int deleteCount = db.delete("photos", null, null);
        Log.d("tclog", deleteCount + " photos deleted");
    }

}
