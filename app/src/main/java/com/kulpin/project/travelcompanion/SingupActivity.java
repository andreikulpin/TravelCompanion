package com.kulpin.project.travelcompanion;

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
import com.kulpin.project.travelcompanion.controller.AppController;
import com.kulpin.project.travelcompanion.utilities.Constants;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;

public class SingupActivity extends AppCompatActivity {

    private User user;
    private EditText usernameText;
    private EditText emailText;
    private EditText passwordText;
    private Button signupButton;
    private TextView loginLink;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_singup);
        bindActivity();
    }

    private void bindActivity(){
        usernameText = (EditText) findViewById(R.id.input_username);
        emailText = (EditText) findViewById(R.id.input_email);
        passwordText = (EditText)findViewById(R.id.input_password);
        signupButton = (Button) findViewById(R.id.button_signup);
        loginLink = (TextView) findViewById(R.id.link_login);

        signupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signup();
            }
        });

        loginLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    public void signup(){
        if (!validate()) {
            onSignupFailed();
            return;
        }

        signupButton.setEnabled(false);
        createAccount();
    }

    public void onSignupSuccess() {
        signupButton.setEnabled(true);
        SharedPreferences sharedPreferences = getSharedPreferences("TCPrefs", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putLong("userId", user.getId());
        editor.putString("username", user.getUsername());
        editor.putString("email", user.getEmail());
        editor.commit();

        setResult(RESULT_OK, null);
        finish();
    }

    public void onSignupFailed() {
        Toast.makeText(getBaseContext(), getString(R.string.signup_failed), Toast.LENGTH_SHORT).show();
        signupButton.setEnabled(true);
    }

    public boolean validate(){
        boolean isValid = true;
        String username = usernameText.getText().toString();
        String email = emailText.getText().toString();
        String password = passwordText.getText().toString();

        if (username.isEmpty() || username.length() < 4){
            usernameText.setError(getString(R.string.signup_invalid_username));
            isValid = false;
        }else {
            emailText.setError(null);
        }

        if (email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            emailText.setError(getString(R.string.signup_invalid_email));
            isValid = false;
        } else {
            emailText.setError(null);
        }

        if (password.isEmpty() || password.length() < 6 || password.length() > 20) {
            passwordText.setError(getString(R.string.signup_invalid_password));
            isValid = false;
        } else {
            passwordText.setError(null);
        }
        return isValid;
    }

    private void createAccount(){
        user = new User(usernameText.getText().toString(), passwordText.getText().toString(),
                emailText.getText().toString());

        String URL = Constants.URL.CREATE_ACCOUNT;
        JSONObject object = new JSONObject();

        try{
            object.accumulate("username", user.getUsername());
            object.accumulate("password", user.getPassword());
            object.accumulate("email", user.getEmail());
        } catch (JSONException e){
            e.printStackTrace();
        }

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, URL, object, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    user.setId(response.getLong("id"));
                } catch (JSONException e){
                    e.printStackTrace();
                }
                Log.d("tclog", "account created, id = " + user.getId());
                Toast.makeText(getBaseContext(), getString(R.string.signup_successful), Toast.LENGTH_SHORT).show();
                onSignupSuccess();
            }

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                String errorBody;
                if (error.networkResponse.statusCode == Constants.HttpStatus.CONFLICT)
                    if(error.networkResponse.data!=null) {
                        try {
                            errorBody = new String(error.networkResponse.data,"UTF-8");
                            if (Integer.parseInt(errorBody) == Constants.HttpMessageCodes.USERNAME_EXISTS) {
                                Log.d("tclog", "Username already exists");
                                Toast.makeText(getBaseContext(), getString(R.string.signup_username_exists), Toast.LENGTH_SHORT).show();
                            }
                            if (Integer.parseInt(errorBody) == Constants.HttpMessageCodes.EMAIL_EXISTS){
                                Log.d("tclog", "Email exists");
                                Toast.makeText(getBaseContext(), getString(R.string.signup_email_exists), Toast.LENGTH_SHORT).show();
                            }
                        } catch (UnsupportedEncodingException e) {
                            e.printStackTrace();
                        }
                    }
            }

        });
        AppController.getInstance().addToRequestQueue(request);
    }

}
