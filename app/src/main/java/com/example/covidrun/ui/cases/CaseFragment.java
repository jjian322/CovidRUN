package com.example.covidrun.ui.cases;

import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.covidrun.R;
import com.example.covidrun.statistics.csvAdapter;
import com.example.covidrun.statistics.csvAdapterItem;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;

public class CaseFragment extends Fragment {
    RecyclerView recyclerViewCase;
    csvAdapter caseAdapter;
    Button State;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate((R.layout.fragment_case), container, false);
        recyclerViewCase = view.findViewById(R.id.case_recycler);
        State = view.findViewById(R.id.case_state);
        loadCaseFromSource();

        State.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), StateCase.class);
                startActivity(intent);
            }
        });

        return view;
    }
    private void loadCaseFromSource(){
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        try {
            URL path = new URL("https://raw.githubusercontent.com/MoH-Malaysia/covid19-public/main/epidemic/cases_malaysia.csv");

            BufferedReader reader = new BufferedReader(new InputStreamReader(path.openStream()));
            String next, line = reader.readLine();

            for (boolean first = true, last = (line == null); !last; first = false, line = next) {
                last = ((next = reader.readLine()) == null);

                String[] RowData = line.split(",");

                String Date = (RowData[0]);
                String cases_New = (RowData[1]);
                String cases_Import = (RowData[2]);
                String cases_Recovered = (RowData[3]);
                String cases_Active = (RowData[4]);

                if(last) {
                    ArrayList<csvAdapterItem> csvItem = new ArrayList<>();
                    csvItem.add(new csvAdapterItem(R.drawable.calander, "Date", Date));
                    csvItem.add(new csvAdapterItem(R.drawable.cases_today, "New Cases", cases_New));
                    csvItem.add(new csvAdapterItem(R.drawable.import_case, "Cases Import", cases_Import));
                    csvItem.add(new csvAdapterItem(R.drawable.total_recoveries, "Cases Recovered", cases_Recovered));
                    csvItem.add(new csvAdapterItem(R.drawable.active_cases, "Active cases", cases_Active));


                    caseAdapter = new csvAdapter(csvItem);
                    LinearLayoutManager caseLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
                    recyclerViewCase.setLayoutManager(caseLayoutManager);
                    recyclerViewCase.setAdapter(caseAdapter);
                }
            }
        } catch (IOException io){
            io.printStackTrace();
        }
    }
}