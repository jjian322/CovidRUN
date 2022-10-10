package com.example.covidrun.dependent;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.covidrun.MainActivity;
import com.example.covidrun.ProfileFragmentActivity;
import com.example.covidrun.sql.DBHandler;
import com.example.covidrun.R;
import com.example.covidrun.ui.profile.ProfileFragment;

public class DependentAdd extends AppCompatActivity {
    private EditText addName, addPhone;
    private Button addDependent;
    private DBHandler dbHandler;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dependent_add);
        getSupportActionBar().setTitle("Add Dependents");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        addName = findViewById(R.id.add_Dependent_Name);
        addPhone = findViewById(R.id.add_Dependent_Phone);
        addDependent = findViewById(R.id.add_button);

        dbHandler = new DBHandler(this);

        addDependent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String Name = addName.getText().toString();
                String Phone = addPhone.getText().toString();

                if (TextUtils.isEmpty(Name)) {
                    Toast.makeText(DependentAdd.this, "Please enter Name.", Toast.LENGTH_SHORT).show();
                } else if (TextUtils.isEmpty(Phone)) {
                    Toast.makeText(DependentAdd.this, "Please enter Phone Number.", Toast.LENGTH_SHORT).show();
                } else {
                    dbHandler.addNewDependent(Name, Phone);
                    Toast.makeText(DependentAdd.this, "Dependent has been added.", Toast.LENGTH_SHORT).show();
                    Intent i = new Intent(DependentAdd.this, ProfileFragmentActivity.class);
                    startActivity(i);
                }
            }
        });
    }
}
