package com.example.androidlogin;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class RegisterActivity extends AppCompatActivity {
    private static final String TAG = RegisterActivity.class.getSimpleName();
    private static final String URL_REGISTER = "https://192.168.43.186//:8080/android_login/register.php";
    private Button btnRegister, btnLinkToLogin;
    private EditText inputName, inputEmail, inputPassword;
    private ProgressDialog pDialog;

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        inputName = findViewById(R.id.rName);
        inputEmail = findViewById(R.id.rEditEmail);
        inputPassword = findViewById(R.id.rEditPassword);
        btnRegister = findViewById(R.id.btnRegister);
        btnLinkToLogin = findViewById(R.id.btnLinkToLoginScreen);

        //progress dialog
        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);

        //login button click event
        btnRegister.setOnClickListener(new View.OnClickListener(){
            public void onClick(View view){
                String name = inputName.getText().toString().trim();
                String email = inputEmail.getText().toString().trim();
                String password = inputPassword.getText().toString().trim();

                //check if the is empty or not
                if(!name.isEmpty() && !email.isEmpty() && !password.isEmpty()){
                    registerUser(name, email, password);
                } else{
                    Toast.makeText(getApplicationContext(), "Please enter your details!", Toast.LENGTH_LONG).show();
                }
            }
        });

        btnLinkToLogin.setOnClickListener(new View.OnClickListener(){
            public void onClick(View view){
                Intent i = new Intent(RegisterActivity.this, LoginAcitivity.class);
                startActivity(i);
                finish();
            }
        });
    }

    private void registerUser(final String name, final String email, final String password){
        //Tag used to cancel the request
        String tag_string_req = "req_register";
        pDialog.setMessage("Registering ......");
        showDialog();

        StringRequest strReq = new StringRequest(Request.Method.POST, URL_REGISTER, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d(TAG, "Register Response: " + response.toString());
                hideDialog();

                try {
                    JSONObject jobj = new JSONObject(response);
                    boolean error = jobj.getBoolean("error");

                    if (!error) {
                        //user successfully stored in mySql
                        Toast.makeText(getApplicationContext(), "User is successfully resgistered. Try login now!", Toast
                                .LENGTH_LONG).show();

                        //Launch login activity
                        Intent intent = new Intent(RegisterActivity.this, LoginAcitivity.class);
                        startActivity(intent);
                        finish();
                    } else {
                        //Error occured in registration. Get the error message
                        String errorMsg = jobj.getString("error_msg");

                        Toast.makeText(getApplicationContext(), errorMsg, Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "Registration Error: " +error.getMessage());
                Toast.makeText(RegisterActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                hideDialog();
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                //posting params to register url
                Map<String, String> params = new HashMap<String, String>();
                params.put("name" ,name);
                params.put("email", email);
                params.put("password", password);
                return params;
            }
        };

        //Adding request to request queue
        MyApplication.getInstance().addToRequestQueue(strReq, tag_string_req);
    }

    private void showDialog(){
        if(!pDialog.isShowing())
            pDialog.show();

    }

    private v