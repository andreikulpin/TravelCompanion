package com.kulpin.project.travelcompanion;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.kulpin.project.travelcompanion.dto.User;
import com.kulpin.project.travelcompanion.utilities.AppController;
import com.kulpin.project.travelcompanion.utilities.Constants;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;

public class LoginActivity extends AppCompatActivity {

    private User user;

    private EditText emailText;
    private EditText passwordText;
    private Button loginButton;
    private TextView signupLink;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        bindActivity();
    }

    private void bindActivity(){
        emailText = (EditText) findViewById(R.id.input_email);
        passwordText = (EditText)findViewById(R.id.input_password);
        loginButton = (Button) findViewById(R.id.button_login);
        signupLink = (TextView) findViewById(R.id.link_signup);

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                login();
            }
        });

        signupLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getBaseContext(), SingupActivity.class);
                startActivityForResult(intent, Constants.RequestCodes.SIGNUP_REQUEST);
            }
        });
    }

    public void login(){
        if (!validate()){
            onLoginFailed();
            return;
        }

        loginButton.setEnabled(false);
        loginByEmail();
    }

    @Override
    public void onBackPressed() {
        moveTaskToBack(true);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == Constants.RequestCodes.SIGNUP_REQUEST)
            if (resultCode == RESULT_OK){
                setResult(RESULT_OK);
                finish();
            }
    }

    public void onLoginSuccess() {
        loginButton.setEnabled(true);

        SharedPreferences sharedPreferences = getSharedPreferences("TCPrefs", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putLong("userId", user.getId());
        editor.putString("username", user.getUsername());
        editor.putString("email", user.getEmail());
        editor.commit();

        setResult(RESULT_OK, new Intent());
        finish();
    }

    public void onLoginFailed() {
        Toast.makeText(getBaseContext(), "Login failed", Toast.LENGTH_SHORT).show();
        loginButton.setEnabled(true);
    }

    public boolean validate(){
        boolean isValid = true;
        String email = emailText.getText().toString();
        String password = passwordText.getText().toString();

        if (email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            emailText.setError("Enter a valid email address");
            isValid = false;
        } else {
            emailText.setError(null);
        }

        if (password.isEmpty() || password.length() < 6 || password.length() > 20) {
            passwordText.setError("Enter between 6 and 20 alphanumeric characters");
            isValid = false;
        } else {
            passwordText.setError(null);
        }
        return isValid;
    }

    private void loginByEmail(){
        user = new User("", passwordText.getText().toString(), emailText.getText().toString());
        final String URL = Constants.URL.LOGIN_BY_EMAIL;
        JSONObject object = new JSONObject();
        try{
            object.accumulate("email", user.getEmail());
            object.accumulate("password", user.getPassword());
        } catch (JSONException e){
            e.printStackTrace();
        }
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, URL, object, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    user.setId(response.getLong("id"));
                    user.setUsername(response.getString("username"));
                    user.setEmail(response.getString("email"));
                } catch (JSONException e){}
                Log.d("tclog", "Login successful");
                Toast.makeText(getBaseContext(), "Login successful", Toast.LENGTH_SHORT).show();
                onLoginSuccess();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if (error.networkResponse.statusCode == Constants.HttpStatus.NOT_FOUND)
                    if(error.networkResponse.data!=null) {
                        try {
                            String errorBody = new String(error.networkResponse.data,"UTF-8");
                            if (Integer.parseInt(errorBody) == Constants.HttpMessageCodes.EMAIL_NOT_FOUND)
                                Log.d("tclog", "Email not found ");
                            Toast.makeText(getBaseContext(), "Email not found", Toast.LENGTH_SHORT).show();
                        } catch (UnsupportedEncodingException e) {
                            e.printStackTrace();
                        }
                    }

                if (error.networkResponse.statusCode == Constants.HttpStatus.FORBIDDEN)
                    if(error.networkResponse.data!=null) {
                        try {
                            String errorBody = new String(error.networkResponse.data,"UTF-8");
                            if (Integer.parseInt(errorBody) == Constants.HttpMessageCodes.INCORRECT_PASSWORD)
                                Log.d("tclog", "Incorrect password");
                            Toast.makeText(getBaseContext(), "Incorrect password", Toast.LENGTH_SHORT).show();
                        } catch (UnsupportedEncodingException e) {
                            e.printStackTrace();
                        }
                    }
                onLoginFailed();
            }
        });
        AppController.getInstance().addToRequestQueue(request);
    }

    private void loginByUsername(final String username){
        user = new User(username, "", "");
        final String URL = Constants.URL.LOGIN_BY_USERNAME;
        JSONObject object = new JSONObject();
        try{
            object.accumulate("username", user.getUsername());
            object.accumulate("password", user.getPassword());
        } catch (JSONException e){
            e.printStackTrace();
        }
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, URL, object, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    user.setId(response.getLong("id"));
                    user.setUsername(response.getString("username"));
                    user.setEmail(response.getString("email"));
                } catch (JSONException e){}
                Log.d("tclog", "Login successful");
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if (error.networkResponse.statusCode == Constants.HttpStatus.NOT_FOUND)
                    if(error.networkResponse.data!=null) {
                        try {
                            String errorBody = new String(error.networkResponse.data,"UTF-8");
                            if (Integer.parseInt(errorBody) == Constants.HttpMessageCodes.USERNAME_NOT_FOUND)
                                Log.d("tclog", "Username not found");
                        } catch (UnsupportedEncodingException e) {
                            e.printStackTrace();
                        }
                    }

                if (error.networkResponse.statusCode == Constants.HttpStatus.FORBIDDEN)
                    if(error.networkResponse.data!=null) {
                        try {
                            String errorBody = new String(error.networkResponse.data,"UTF-8");
                            if (Integer.parseInt(errorBody) == Constants.HttpMessageCodes.INCORRECT_PASSWORD)
                                Log.d("tclog", "Incorrect password");
                        } catch (UnsupportedEncodingException e) {
                            e.printStackTrace();
                        }
                    }
            }
        });
        AppController.getInstance().addToRequestQueue(request);
    }
}
