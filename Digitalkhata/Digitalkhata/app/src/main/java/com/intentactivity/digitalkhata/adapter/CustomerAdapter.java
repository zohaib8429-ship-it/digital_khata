package com.intentactivity.digitalkhata.adapter;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.intentactivity.digitalkhata.R;
import com.intentactivity.digitalkhata.model.Customer;

public class CustomerAdapter extends ListAdapter<Customer, CustomerAdapter.CustomerViewHolder> {

    private OnItemClickListener listener;
    private OnItemLongClickListener longClickListener;

    public CustomerAdapter() {
        super(DIFF_CALLBACK);
    }

    private static final DiffUtil.ItemCallback<Customer> DIFF_CALLBACK = new DiffUtil.ItemCallback<Customer>() {
        @Override
        public boolean areItemsTheSame(@NonNull Customer oldItem, @NonNull Customer newItem) {
            return oldItem.getId() == newItem.getId();
        }

        @Override
        public boolean areContentsTheSame(@NonNull Customer oldItem, @NonNull Customer newItem) {
            return oldItem.getName().equals(newItem.getName()) &&
                    oldItem.getPhoneNumber().equals(newItem.getPhoneNumber()) &&
                    oldItem.getTotalBalance() == newItem.getTotalBalance();
        }
    };

    @NonNull
    @Override
    public CustomerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.customer_item, parent, false);
        return new CustomerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CustomerViewHolder holder, int position) {
        Customer current = getItem(position);
        holder.tvName.setText(current.getName());
        holder.tvPhone.setText(current.getPhoneNumber());
        
        double balance = current.getTotalBalance();
        holder.tvBalance.setText("Rs. " + Math.abs(balance));
        
        if (balance > 0) {
            holder.tvBalance.setTextColor(Color.parseColor("#E53935")); // Red (You will get)
        } else if (balance < 0) {
            holder.tvBalance.setTextColor(Color.parseColor("#43A047")); // Green (You will give)
        } else {
            holder.tvBalance.setTextColor(Color.BLACK);
        }
    }

    class CustomerViewHolder extends RecyclerView.ViewHolder {
        private TextView tvName, tvPhone, tvBalance;

        public CustomerViewHolder(@NonNull View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.tvCustomerName);
            tvPhone = itemView.findViewById(R.id.tvCustomerPhone);
            tvBalance = itemView.findViewById(R.id.tvBalance);

            itemView.setOnClickListener(v -> {
                int position = getAdapterPosition();
                if (listener != null && position != RecyclerView.NO_POSITION) {
                    listener.onItemClick(getItem(position));
                }
            });

            itemView.setOnLongClickListener(v -> {
                int position = getAdapterPosition();
                if (longClickListener != null && position != RecyclerView.NO_POSITION) {
                    longClickListener.onItemLongClick(getItem(position));
                    return true;
                }
                return false;
            });
        }
    }

    public interface OnItemClickListener {
        void onItemClick(Customer customer);
    }

    public interface OnItemLongClickListener {
        void onItemLongClick(Customer customer);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    public void setOnItemLongClickListener(OnItemLongClickListener listener) {
        this.longClickListener = listener;
    }
}