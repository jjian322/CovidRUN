package com.example.covidrun.statistics;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.covidrun.R;

import java.util.ArrayList;

public class csvAdapter extends RecyclerView.Adapter<csvAdapter.CaseViewHolder> {

    private ArrayList<csvAdapterItem> caseList;

    public static class CaseViewHolder extends RecyclerView.ViewHolder{
        public ImageView mImageView;
        public TextView mTextView1;
        public TextView mTextView2;

        public CaseViewHolder(@NonNull View itemView){
            super(itemView);
            mImageView = itemView.findViewById(R.id.imageView);
            mTextView1 = itemView.findViewById(R.id.txt1);
            mTextView2 = itemView.findViewById(R.id.txt2);
        }
    }

    public csvAdapter(ArrayList<csvAdapterItem> itemList){
        caseList = itemList;
    }

    @NonNull
    @Override
    public CaseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.case_item,parent,false);
        CaseViewHolder cvh = new CaseViewHolder(v);
        return cvh;
    }

    @Override
    public void onBindViewHolder(@NonNull CaseViewHolder holder, int position) {
        csvAdapterItem currentItem = caseList.get(position);
        holder.mImageView.setImageResource(currentItem.getimageResource());
        holder.mTextView1.setText(currentItem.gettxt1());
        holder.mTextView2.setText(""+currentItem.gettxt2());
        if(position < -1){
            holder.mTextView1.setTextColor(Color.BLACK);
            holder.mTextView2.setTextColor(Color.BLACK);
        }
    }

    @Override
    public int getItemCount() {
        return caseList.size();
    }
}
