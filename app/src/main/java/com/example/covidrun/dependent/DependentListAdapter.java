package com.example.covidrun.dependent;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.covidrun.model.DependentModel;
import com.example.covidrun.R;

import java.util.List;

public class DependentListAdapter extends ArrayAdapter<DependentModel> {


    public DependentListAdapter(@NonNull Context context, List<DependentModel> Dependent){
        super(context, R.layout.listview_dependents_row, Dependent);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent){
        final DependentModel dependent = getItem(position);
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.listview_dependents_row, parent, false);
        }


        final TextView textLabel = (TextView) convertView.findViewById(R.id.dependent_Name);
        final TextView detailedTextLabel = (TextView) convertView.findViewById(R.id.dependent_Phone);
        textLabel.setText(dependent.getDependentname());
        detailedTextLabel.setText(dependent.getDependentphone());
        return convertView;
    }
}
