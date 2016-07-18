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
import com.kulpin.project.travelcompanion.dto.Document;
import com.kulpin.project.travelcompanion.dto.Photo;
import com.kulpin.project.travelcompanion.utilities.Constants;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class DocumentController {

    private BasicActivity activity;

    public DocumentController(BasicActivity activity) {
        this.activity = activity;
    }

    public void uploadImage(final Document document){
        String URL = Constants.URL.ADD_DOCUMENT;

        File file = new File(document.getFilePath());
        byte[] byteFile = new byte[(int)file.length()];
        try{
            FileInputStream fis = new FileInputStream(file);
            fis.read(byteFile);
            fis.close();
        } catch (FileNotFoundException e){}
        catch (IOException e){}
        String temp = Base64.encodeToString(byteFile, Base64.DEFAULT);

        JSONObject jsonObject = new JSONObject();
        try{
            jsonObject.accumulate("eventId", document.getEventId());
            jsonObject.accumulate("lob", temp);
            jsonObject.accumulate("title", document.getTitle());
        } catch (JSONException e){e.printStackTrace();}

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, URL, jsonObject,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("tclog", "new document uploaded = " + response);
                        try{
                            document.setIdServer(response.getLong("id"));
                        }
                        catch (JSONException e){}
                        activity.addDocumentToLocalDB(document);
                        activity.updateDocuments();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("tclog", "error uploading document = " + error
                        + ">>" + error.networkResponse.statusCode
                        + ">>" + error.getMessage());
            }
        });
        AppController.getInstance().addToRequestQueue(request);
    }

    public void downloadAllDocuments(final long eventId){
        String URL = Constants.URL.GET_ALL_DOCUMENTS + eventId;
        final JsonArrayRequest request = new JsonArrayRequest(URL, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                try {
                    for (int i = 0; i < response.length(); i++) {
                        JSONObject jsonObject = response.getJSONObject(i);
                        Document document = new Document();
                        //document.setId(jsonObject.getLong("id"));
                        document.setIdServer(jsonObject.getLong("id"));
                        document.setEventId(jsonObject.getLong("eventId"));
                        document.setTitle(jsonObject.getString("title"));

                        String temp = (String)jsonObject.get("lob");
                        byte[] byteFile = Base64.decode(temp, Base64.DEFAULT);

                        if (activity.doesDocumentExistsLocally(document.getIdServer())) continue;

                        String root = Environment.getExternalStorageDirectory().toString();
                        File dir = new File(root + "/TravelCompanion/saved_documents");
                        dir.mkdirs();
                        File file = new File(dir, document.getTitle());
                        if (file.exists()) {
                            Log.d("tclog", file.getAbsolutePath() + " exists");
                            file.delete();
                        }

                        document.setFilePath(dir + "/" + document.getTitle());

                        try{
                            FileOutputStream fos = new FileOutputStream(file);
                            fos.write(byteFile);
                            fos.close();
                        } catch (FileNotFoundException e){}
                        catch (IOException e){}

                        activity.addDocumentToLocalDB(document);
                    }
                } catch (JSONException e){}
                activity.updateDocuments();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("tclog", "error downloading documents = " + error
                        + ">>" + error.networkResponse.statusCode
                        + ">>" + error.getMessage());
            }
        });
        AppController.getInstance().addToRequestQueue(request);
    }

    public void delete(long idServer){
        Log.d("tclog", "deleting the document id = " + idServer);
        String URL = Constants.URL.DELETE_DOCUMENT + idServer;
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.DELETE, URL, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("tclog", "document deleted: " + response);
                    }

                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("tclog", "error deleting the document = " + error);
            }

        });
        AppController.getInstance().addToRequestQueue(request);

    }

    public void deleteAllPhotos(long eventId){
        String URL = Constants.URL.DELETE_ALL_DOCUMENTS + eventId;
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.DELETE, URL, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("tclog", "all documents deleted: " + response);
                    }

                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("tclog", "error deleting documents = " + error);
            }

        });
        AppController.getInstance().addToRequestQueue(request);
    }

    public static void test(){
        String filePath = Environment.getExternalStorageDirectory().toString() + "/Download/1.txt";
        File file = new File(filePath);
        byte[] byteFile = new byte[(int)file.length()];
        try{
            FileInputStream fis = new FileInputStream(file);
            fis.read(byteFile);
            fis.close();
        } catch (FileNotFoundException e){}
        catch (IOException e){}

        String temp = Base64.encodeToString(byteFile, Base64.DEFAULT);
        save(temp);
    }

    public static void save(String temp){
        byte[] byteFile = Base64.decode(temp, Base64.DEFAULT);
        String root = Environment.getExternalStorageDirectory().toString();
        File dir = new File(root + "/saved_file");
        dir.mkdirs();
        File file = new File(dir, "2.txt");
        try{
            FileOutputStream fos = new FileOutputStream(file);
            fos.write(byteFile);
            fos.close();
        } catch (FileNotFoundException e){}
        catch (IOException e){}
    }

}
