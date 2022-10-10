package com.example.covidrun.statistics;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.example.covidrun.R;

import java.util.ArrayList;

public class StateAdapter extends RecyclerView.Adapter<StateAdapter.StateViewHolder> {
    private ArrayList<StateAdapterItem> itemList;

    public StateAdapter(ArrayList<StateAdapterItem> itemList){
        this.itemList = itemList;
    }

    @NonNull
    @Override
    public StateViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int position){
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.row_state, viewGroup, false);
        StateAdapter.StateViewHolder svh = new StateAdapter.StateViewHolder(view);
        return svh;
    }

    @Override
    public void onBindViewHolder(StateViewHolder holder, int position){
        StateAdapterItem stateAdapter = itemList.get(position);
        holder.stateTitle.setText(stateAdapter.getTitle());
        holder.text1.setText(stateAdapter.getText1());
        holder.text2.setText(stateAdapter.getText2());
        holder.text3.setText(stateAdapter.getText3());
        holder.text4.setText(stateAdapter.getText4());

        boolean isExpanded = itemList.get(position).isExpanded();
        holder.expandableLayout.setVisibility(isExpanded ? View.VISIBLE : View.GONE);
    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    class StateViewHolder extends RecyclerView.ViewHolder{
        private TextView stateTitle, text1, text2, text3, text4;
        ConstraintLayout expandableLayout;

        StateViewHolder(final View itemView){
            super(itemView);
            stateTitle = itemView.findViewById(R.id.stateTextView);
            text1 = itemView.findViewById(R.id.contentTextView);
            text2 = itemView.findViewById(R.id.contentTextView1);
            text3 = itemView.findViewById(R.id.contentTextView2);
            text4 = itemView.findViewById(R.id.contentTextView3);
            expandableLayout = itemView.findViewById(R.id.expandableLayoutState);

            stateTitle.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    StateAdapterItem state = itemList.get(getAdapterPosition());
                    state.setExpanded(!state.isExpanded());
                    notifyItemChanged(getAdapterPosition());

                }
            });
        }
    }
}
