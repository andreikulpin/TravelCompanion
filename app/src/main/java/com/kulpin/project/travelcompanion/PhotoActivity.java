package com.kulpin.project.travelcompanion;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.kulpin.project.travelcompanion.controller.AppController;
import com.kulpin.project.travelcompanion.controller.DocumentController;
import com.kulpin.project.travelcompanion.utilities.Constants;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class PhotoActivity extends AppCompatActivity {

    private ImageView imageView;
    private Bitmap bitmap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo);

        findViewById(R.id.download_photo).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                downloadImage();
            }
        });

        findViewById(R.id.upload_photo).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                uploadImage();
            }
        });

        imageView = (ImageView) findViewById(R.id.image_photo);

        findViewById(R.id.save_file).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DocumentController.test();
            }
        });

    }

    public void downloadImage(){
        String URL = Constants.localURL.GET_PHOTO + "1";
        JsonObjectRequest request = new JsonObjectRequest(URL, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                try {
                    String string = (String)response.get("lob");
                    string.replaceAll("\\\\", "");
                    byte[] byteImage = Base64.decode(string, Base64.DEFAULT);

                    bitmap = BitmapFactory.decodeByteArray(byteImage, 0, byteImage.length);
                    imageView.setImageBitmap(bitmap);

                    String root = Environment.getExternalStorageDirectory().toString();
                    File dir = new File(root + "/saved");
                    dir.mkdirs();
                    File file = new File(dir, "image.png");
                    if (file.exists()) {
                        file.delete();
                    }
                    try {
                        FileOutputStream fileOutputStream = new FileOutputStream(file);
                        bitmap.compress(Bitmap.CompressFormat.PNG, 100, fileOutputStream);
                        fileOutputStream.flush();
                        fileOutputStream.close();
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                    catch (IOException e){}
                } catch (JSONException e){}
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("tclog", "error syncronization image = " + error);
            }
        });
        AppController.getInstance().addToRequestQueue(request);
    }

    public void uploadImage(){
        String URL = Constants.localURL.ADD_PHOTO;
        if (bitmap == null)
            bitmap = Bitmap.createBitmap(BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher));
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG,100, baos);
        byte[] byteImage = baos.toByteArray();
        String temp=Base64.encodeToString(byteImage, Base64.DEFAULT);

        JSONObject jsonObject = new JSONObject();

        try{
            jsonObject.accumulate("eventId", 1);
            jsonObject.accumulate("journeyId", 1);
            jsonObject.accumulate("lob", temp);
            jsonObject.accumulate("photoTitle", "photo");
        } catch (JSONException e){e.printStackTrace();}

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, URL, jsonObject,
                new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.d("tclog", "new image created = " + response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("myLOG", "error creating new image = " + error
                        + ">>" + error.networkResponse.statusCode
                        + ">>" + error.networkResponse.data
                        + ">>" + error.getCause()
                        + ">>" + error.getMessage());
            }
        });
        AppController.getInstance().addToRequestQueue(request);
    }
}
