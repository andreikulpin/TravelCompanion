package com.kulpin.project.travelcompanion.controller;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.util.Base64;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.kulpin.project.travelcompanion.BasicActivity;
import com.kulpin.project.travelcompanion.dto.Photo;
import com.kulpin.project.travelcompanion.utilities.Constants;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class PhotoController {
    private Bitmap bitmap;
    private BasicActivity activity;

    public PhotoController(BasicActivity activity) {
        this.activity = activity;
    }

    public void downloadAllPhotos(final long eventId){
        String URL = Constants.URL.GET_ALL_PHOTOS + eventId;
        final JsonArrayRequest request = new JsonArrayRequest(URL, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                try {
                    for (int i = 0; i < response.length(); i++) {
                        JSONObject jsonObject = response.getJSONObject(i);
                        Photo photo = new Photo();
                        photo.setId(jsonObject.getLong("id"));
                        photo.setEventId(jsonObject.getLong("eventId"));
                        photo.setJourneyId(jsonObject.getLong("journeyId"));
                        photo.setTitle(jsonObject.getString("photoTitle"));

                        String temp = (String)jsonObject.get("lob");
                        //temp.replaceAll("\\\\", "");
                        byte[] byteImage = Base64.decode(temp, Base64.DEFAULT);
                        bitmap = BitmapFactory.decodeByteArray(byteImage, 0, byteImage.length);

                        if (activity.isPhotoExistsLocally(photo.getId())) continue;

                        String root = Environment.getExternalStorageDirectory().toString();
                        File dir = new File(root + "/saved");
                        dir.mkdirs();
                        File file = new File(dir, photo.getId() + ".png");
                        if (file.exists()) {
                            Log.d("tclog", file.getAbsolutePath() + " exists");
                            file.delete();
                        }

                        photo.setPhotoPath(dir + "/" + photo.getId() + ".png");

                        FileOutputStream fileOutputStream = new FileOutputStream(file);
                        bitmap.compress(Bitmap.CompressFormat.PNG, 100, fileOutputStream);
                        fileOutputStream.flush();
                        fileOutputStream.close();

                        activity.addPhotoToLocalDB(photo);
                    }
                } catch (JSONException e){}
                catch (FileNotFoundException e){}
                catch (IOException e){}
                activity.updatePhotos();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("tclog", "error downloading images = " + error
                        + ">>" + error.networkResponse.statusCode
                        //+ ">>" + error.networkResponse.data
                        //+ ">>" + error.getCause()
                        + ">>" + error.getMessage());
            }
        });
        AppController.getInstance().addToRequestQueue(request);
    }


    public void uploadImage(final Photo photo){
        String URL = Constants.URL.ADD_PHOTO;
        BitmapFactory.Options options = new BitmapFactory.Options();
        bitmap = BitmapFactory.decodeFile(photo.getPhotoPath());

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG,100, baos);
        byte[] byteImage = baos.toByteArray();
        String temp= Base64.encodeToString(byteImage, Base64.DEFAULT);

        JSONObject jsonObject = new JSONObject();

        try{
            jsonObject.accumulate("eventId", photo.getEventId());
            jsonObject.accumulate("journeyId", 1);
            jsonObject.accumulate("lob", temp);
            jsonObject.accumulate("photoTitle", photo.getTitle());
        } catch (JSONException e){e.printStackTrace();}

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, URL, jsonObject,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("tclog", "new image created = " + response);
                        try{
                            photo.setId(response.getLong("id"));
                            photo.setJourneyId(response.getLong("journeyId"));
                        }
                        catch (JSONException e){}
                        activity.addPhotoToLocalDB(photo);
                        activity.updatePhotos();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("tclog", "error creating new image = " + error
                        + ">>" + error.networkResponse.statusCode
                        //+ ">>" + error.networkResponse.data
                        //+ ">>" + error.getCause()
                        + ">>" + error.getMessage());
            }
        });
        AppController.getInstance().addToRequestQueue(request);
    }

    public void delete(long id){
        String URL = Constants.URL.DELETE_PHOTO + id;
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.DELETE, URL, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("tclog", "photo deleted: " + response);
                    }

                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("myLOG", "error deleting the photo = " + error);
            }

        });
        AppController.getInstance().addToRequestQueue(request);

    }

    public void deleteAllPhotos(long eventId){
        String URL = Constants.URL.DELETE_ALL_PHOTOS + eventId;
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.DELETE, URL, null,
                new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.d("tclog", "all photos deleted: " + response);
            }

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("myLOG", "error deleting photos = " + error);
            }

        });
        AppController.getInstance().addToRequestQueue(request);

    }
}
