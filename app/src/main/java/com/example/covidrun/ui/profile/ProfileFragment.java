package com.example.covidrun.ui.profile;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.covidrun.R;
import com.example.covidrun.dependent.DependentAdd;
import com.example.covidrun.dependent.DependentEdit;
import com.example.covidrun.dependent.DependentListAdapter;
import com.example.covidrun.model.DependentModel;
import com.example.covidrun.sql.DBHandler;

import java.util.ArrayList;
import java.util.List;

public class ProfileFragment extends Fragment {

    private DBHandler dbHandler;

    private DependentListAdapter dependentListAdapter = null;
    private final List<DependentModel> dependents = new ArrayList<>();


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate((R.layout.fragment_profile), container, false);

        dbHandler = new DBHandler(getActivity());
        Cursor cursor = dbHandler.getUser();
        cursor.moveToFirst();

        LinearLayout editProfileCard = view.findViewById(R.id.profile_card);
        TextView editDependentCard = view.findViewById(R.id.profile_dependent);

        dependents.addAll(dbHandler.readDependent());
        dependentListAdapter = new DependentListAdapter(getActivity(), dependents);
        ListView DependentListView = ((ListView) view.findViewById(R.id.dependent));
        DependentListView.setAdapter(dependentListAdapter);

        ((TextView) view.findViewById(R.id.profile_Name)).setText(cursor.getString(1));
        ((TextView) view.findViewById(R.id.profile_Phone)).setText(cursor.getString(3));
        ((TextView) view.findViewById(R.id.profile_Email)).setText(cursor.getString(4));

        editProfileCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), ProfileEdit.class);
                startActivity(intent);
            }
        });

        editDependentCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), DependentAdd.class);
                startActivity(intent);
            }
        });

        DependentListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(getActivity(), DependentEdit.class);
                startActivity(intent);
            }
        });
        return view;
    }

}
