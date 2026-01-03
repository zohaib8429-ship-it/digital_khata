package com.intentactivity.digitalkhata.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.intentactivity.digitalkhata.R;
import com.intentactivity.digitalkhata.model.Transaction;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class TransactionAdapter extends ListAdapter<Transaction, TransactionAdapter.TransactionViewHolder> {

    public TransactionAdapter() {
        super(DIFF_CALLBACK);
    }

    private static final DiffUtil.ItemCallback<Transaction> DIFF_CALLBACK = new DiffUtil.ItemCallback<Transaction>() {
        @Override
        public boolean areItemsTheSame(@NonNull Transaction oldItem, @NonNull Transaction newItem) {
            return oldItem.getId() == newItem.getId();
        }

        @Override
        public boolean areContentsTheSame(@NonNull Transaction oldItem, @NonNull Transaction newItem) {
            return oldItem.getAmount() == newItem.getAmount() &&
                    oldItem.getType().equals(newItem.getType()) &&
                    oldItem.getDate() == newItem.getDate();
        }
    };

    @NonNull
    @Override
    public TransactionViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.transaction_item, parent, false);
        return new TransactionViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TransactionViewHolder holder, int position) {
        Transaction current = getItem(position);
        
        SimpleDateFormat sdf = new SimpleDateFormat("dd MMM yyyy, hh:mm a", Locale.getDefault());
        holder.tvDate.setText(sdf.format(new Date(current.getDate())));
        holder.tvDesc.setText(current.getDescription());

        if (current.getType().equals("GAVE")) {
            holder.tvGave.setText(String.valueOf(current.getAmount()));
            holder.tvGot.setText("-");
        } else {
            holder.tvGave.setText("-");
            holder.tvGot.setText(String.valueOf(current.getAmount()));
        }
    }

    class TransactionViewHolder extends RecyclerView.ViewHolder {
        private TextView tvDate, tvDesc, tvGave, tvGot;

        public TransactionViewHolder(@NonNull View itemView) {
            super(itemView);
            tvDate = itemView.findViewById(R.id.tvTransDate);
            tvDesc = itemView.findViewById(R.id.tvTransDesc);
            tvGave = itemView.findViewById(R.id.tvGaveAmount);
            tvGot = itemView.findViewById(R.id.tvGotAmount);
        }
    }
}