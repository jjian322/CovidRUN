package com.example.covidrun.dependent;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.example.covidrun.MainActivity;
import com.example.covidrun.ProfileFragmentActivity;
import com.example.covidrun.R;
import com.example.covidrun.sql.DBHandler;

public class DependentEdit extends AppCompatActivity {
    private EditText editName, editPhone;
    private Button updateDependent;
    private DBHandler dbHandler;
    String dependentName, dependentPhone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dependent_edit);
        getSupportActionBar().setTitle("Edit Dependent");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        editName = findViewById(R.id.edit_dependent_name);
        editPhone = findViewById(R.id.edit_dependent_phone);
        updateDependent = findViewById(R.id.update_dependent_button);

        dbHandler = new DBHandler(DependentEdit.this);
        Cursor cursor = dbHandler.getDependent();
        cursor.moveToFirst();

        dependentName = cursor.getString(1);
        dependentPhone = cursor.getString(2);

        editName.setText(dependentName);
        editPhone.setText(dependentPhone);

        updateDependent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (editName.getText().toString().isEmpty()) {
                    Toast.makeText(DependentEdit.this, "Please enter Name.", Toast.LENGTH_SHORT).show();
                } else if (editPhone.getText().toString().isEmpty()) {
                    Toast.makeText(DependentEdit.this, "Please enter Phone Number.", Toast.LENGTH_SHORT).show();
                } else {
                    dbHandler.updateDependent(dependentName, editName.getText().toString(), editPhone.getText().toString());
                    Toast.makeText(DependentEdit.this, "Dependent Updated..", Toast.LENGTH_SHORT).show();

                    Intent intent = new Intent(DependentEdit.this, ProfileFragmentActivity.class);
                    startActivity(intent);
                }
            }
        });
    }
}
