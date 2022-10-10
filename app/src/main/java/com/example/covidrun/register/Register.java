package com.example.covidrun.register;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import com.example.covidrun.login.Login;
import com.example.covidrun.R;
import com.example.covidrun.sql.DBHandler;

public class Register extends AppCompatActivity {
    private EditText edtName, edtPassword, edtPhonenum, edtEmail;
    private TextView RegisteredUser;
    private Button registerUser;
    private DBHandler dbHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register_user);

        edtName = findViewById(R.id.fullname);
        edtPassword = findViewById(R.id.password);
        edtPhonenum = findViewById(R.id.phoneNum);
        edtEmail = findViewById(R.id.email);
        registerUser = findViewById(R.id.register_bt);

        dbHandler = new DBHandler(Register.this);

        registerUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String userName = edtName.getText().toString();
                String Password = edtPassword.getText().toString();
                String PhoneNumber = edtPhonenum.getText().toString();
                String Email = edtEmail.getText().toString();

                if (TextUtils.isEmpty(userName)) {
                    Toast.makeText(Register.this, "Please enter Name.", Toast.LENGTH_SHORT).show();
                } else if (TextUtils.isEmpty(Password)) {
                    Toast.makeText(Register.this, "Please enter Password.", Toast.LENGTH_SHORT).show();
                } else if (TextUtils.isEmpty(PhoneNumber)) {
                    Toast.makeText(Register.this, "Please enter Phone Number.", Toast.LENGTH_SHORT).show();
                } else if (TextUtils.isEmpty(Email)) {
                    Toast.makeText(Register.this, "Please enter Email.", Toast.LENGTH_SHORT).show();
                } else {
                    dbHandler.addNewUser(userName, Password, PhoneNumber, Email);
                    Toast.makeText(Register.this, "Account has been created.", Toast.LENGTH_SHORT).show();
                    Intent i = new Intent(Register.this, Login.class);
                    startActivity(i);
                }
            }
        });

        RegisteredUser = findViewById(R.id.Registered_User);
        RegisteredUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(Register.this, Login.class);
                startActivity(i);
            }
        });
    }
}
