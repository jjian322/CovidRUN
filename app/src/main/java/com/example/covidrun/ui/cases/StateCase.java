package com.example.covidrun.ui.cases;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.View;
import android.widget.Button;

import com.example.covidrun.MainActivity;
import com.example.covidrun.R;
import com.example.covidrun.statistics.StateAdapter;
import com.example.covidrun.statistics.StateAdapterItem;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Locale;

public class StateCase extends AppCompatActivity {
    RecyclerView StateRecyclerView;
    StateAdapter stateAdapter;
    ArrayList<StateAdapterItem> stateItem = new ArrayList<StateAdapterItem>();
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd", Locale.ENGLISH);
    LocalDate now = LocalDate.now();
    LocalDate date;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_state_case);
        getSupportActionBar().setTitle("State Case");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        StateRecyclerView = findViewById(R.id.StateRecyclerView);
        Button back = findViewById(R.id.back_button);

        loadStateDetailsFromSource();

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(StateCase.this, MainActivity.class);
                startActivity(intent);
            }
        });
    }

    private void loadStateDetailsFromSource(){
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        try {
            URL path = new URL("https://raw.githubusercontent.com/MoH-Malaysia/covid19-public/main/epidemic/cases_state.csv");

            BufferedReader reader = new BufferedReader(new InputStreamReader(path.openStream()));
            reader.readLine();
            String line = null;
            int i = 0;

            while((line = reader.readLine()) != null){

                String[] RowData = line.split(",");

                String date_now = (RowData[0]);
                String state = (RowData[1]);
                String cases_New = (RowData[2]);
                String cases_Import = (RowData[3]);
                String cases_Recovered = (RowData[4]);
                String cases_Active = (RowData[5]);

                date = LocalDate.parse(date_now, formatter);
                if(date.equals(now.minusDays(1))) {
                        stateItem.add(new StateAdapterItem(state, "New Cases: " + cases_New, "New Import Case: " + cases_Import,
                                "New Case Recovered: " + cases_Recovered, "Total Case Active: " + cases_Active));
                        stateAdapter = new StateAdapter(stateItem);
                }
                LinearLayoutManager stateLayoutManager = new LinearLayoutManager(StateCase.this, LinearLayoutManager.VERTICAL, false);
                StateRecyclerView.setLayoutManager(stateLayoutManager);
                StateRecyclerView.setAdapter(stateAdapter);
            }
        } catch (IOException io){
            io.printStackTrace();
        }
    }
}