package com.example.covidrun.faq;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.example.covidrun.model.FAQModel;
import com.example.covidrun.R;

import java.util.List;

public class FaqAdapter extends RecyclerView.Adapter<FaqAdapter.FaqViewHolder> {
    List<FAQModel> FaqList;

    public FaqAdapter(List<FAQModel> FaqList) {
        this.FaqList = FaqList;
    }

    @NonNull
    @Override
    public FaqViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_faq, parent, false);
        return new FaqViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FaqViewHolder holder, int position) {
        FAQModel Faq = FaqList.get(position);
        holder.titleView.setText(Faq.getTitle());
        holder.contentView.setText(Faq.getContent());

        boolean isExpanded = FaqList.get(position).isExpanded();
        holder.expandableLayout.setVisibility(isExpanded ? View.VISIBLE : View.GONE);
    }

    @Override
    public int getItemCount() { return FaqList.size(); }

    class FaqViewHolder extends RecyclerView.ViewHolder {
        private static final String TAG = "FaqViewHolder";

        ConstraintLayout expandableLayout;
        TextView titleView, contentView;

        public FaqViewHolder(@NonNull final View itemView){
            super(itemView);

            titleView = itemView.findViewById(R.id.titleTextView);
            contentView = itemView.findViewById(R.id.contentTextView);
            expandableLayout = itemView.findViewById(R.id.expandableLayout);

            titleView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    FAQModel faq = FaqList.get(getAdapterPosition());
                    faq.setExpanded(!faq.isExpanded());
                    notifyItemChanged(getAdapterPosition());

                }
            });
        }
    }

}