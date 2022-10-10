package com.example.covidrun.ui.profile;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.covidrun.MainActivity;
import com.example.covidrun.ProfileFragmentActivity;
import com.example.covidrun.R;
import com.example.covidrun.register.Register;
import com.example.covidrun.sql.DBHandler;

public class ProfileEdit extends AppCompatActivity {

    private EditText editName, editPhone, editEmail, editPassword;
    private Button updateProfile;
    private DBHandler dbHandler;
    String profileName, profilePhone, profileEmail, profilePassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.profile_edit);
        getSupportActionBar().setTitle("Edit Profile");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        editName = findViewById(R.id.edit_name);
        editEmail = findViewById(R.id.edit_email);
        editPhone = findViewById(R.id.edit_phone);
        editPassword = findViewById(R.id.edit_password);
        updateProfile = findViewById(R.id.update_button);

        dbHandler = new DBHandler(ProfileEdit.this);
        Cursor cursor = dbHandler.getUser();
        cursor.moveToFirst();

        profileName = cursor.getString(1);
        profilePhone = cursor.getString(3);
        profileEmail = cursor.getString(4);
        profilePassword = cursor.getString(2);

        editName.setText(profileName);
        editEmail.setText(profileEmail);
        editPhone.setText(profilePhone);
        editPassword.setText(profilePassword);

        updateProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (editName.getText().toString().isEmpty()) {
                    Toast.makeText(ProfileEdit.this, "Please enter Name.", Toast.LENGTH_SHORT).show();
                } else if (editPassword.getText().toString().isEmpty()) {
                    Toast.makeText(ProfileEdit.this, "Please enter Password.", Toast.LENGTH_SHORT).show();
                } else if (editPhone.getText().toString().isEmpty()) {
                    Toast.makeText(ProfileEdit.this, "Please enter Phone Number.", Toast.LENGTH_SHORT).show();
                } else if (editEmail.getText().toString().isEmpty()) {
                    Toast.makeText(ProfileEdit.this, "Please enter Email.", Toast.LENGTH_SHORT).show();
                } else {
                    dbHandler.updateUser(profileName, editName.getText().toString(), editPassword.getText().toString(),
                            editPhone.getText().toString(), editEmail.getText().toString());
                    Toast.makeText(ProfileEdit.this, "Profile Updated..", Toast.LENGTH_SHORT).show();

                    Intent intent = new Intent(ProfileEdit.this, ProfileFragmentActivity.class);
                    startActivity(intent);
                }
            }
        });
    }
}
