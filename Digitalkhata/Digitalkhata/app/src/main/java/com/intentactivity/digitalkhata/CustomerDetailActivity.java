package com.intentactivity.digitalkhata;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.intentactivity.digitalkhata.adapter.TransactionAdapter;
import com.intentactivity.digitalkhata.model.Transaction;
import com.intentactivity.digitalkhata.viewmodel.KhataViewModel;

import java.util.List;

public class CustomerDetailActivity extends AppCompatActivity {

    private KhataViewModel viewModel;
    private TransactionAdapter adapter;
    private int customerId;
    private String customerName, customerPhone;
    private TextView tvBalance, tvBalanceLabel;
    private double currentBalance = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_detail);

        customerId = getIntent().getIntExtra("CUSTOMER_ID", -1);
        customerName = getIntent().getStringExtra("CUSTOMER_NAME");
        customerPhone = getIntent().getStringExtra("CUSTOMER_PHONE");

        Toolbar toolbar = findViewById(R.id.toolbarDetail);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle(customerName);
        }
        toolbar.setNavigationOnClickListener(v -> onBackPressed());

        tvBalance = findViewById(R.id.tvDetailBalance);
        tvBalanceLabel = findViewById(R.id.tvBalanceLabel);
        
        RecyclerView recyclerView = findViewById(R.id.rvTransactions);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new TransactionAdapter();
        recyclerView.setAdapter(adapter);

        viewModel = new ViewModelProvider(this).get(KhataViewModel.class);
        viewModel.getTransactionsForCustomer(customerId).observe(this, transactions -> {
            adapter.submitList(transactions);
            calculateBalance(transactions);
        });

        findViewById(R.id.btnGave).setOnClickListener(v -> showTransactionDialog("GAVE"));
        findViewById(R.id.btnGot).setOnClickListener(v -> showTransactionDialog("GOT"));
    }

    private void calculateBalance(List<Transaction> transactions) {
        currentBalance = 0;
        for (Transaction t : transactions) {
            if (t.getType().equals("GAVE")) {
                currentBalance += t.getAmount();
            } else {
                currentBalance -= t.getAmount();
            }
        }

        tvBalance.setText("Rs " + Math.abs(currentBalance));
        
        if (currentBalance > 0) {
            tvBalanceLabel.setText("You Will Get");
            tvBalance.setTextColor(getResources().getColor(R.color.white));
        } else if (currentBalance < 0) {
            tvBalanceLabel.setText("You Will Give");
            tvBalance.setTextColor(getResources().getColor(R.color.white));
        } else {
            tvBalanceLabel.setText("Settled");
            tvBalance.setTextColor(getResources().getColor(R.color.white));
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.customer_detail_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_share) {
            shareLedger();
            return true;
        } else if (id == R.id.action_call) {
            callCustomer();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void shareLedger() {
        String message = "Salaam " + customerName + ",\n" +
                "Aapka Digital Khata balance niche hai:\n\n";
        
        if (currentBalance > 0) {
            message += "Aapne Rs. " + currentBalance + " dene hain.";
        } else if (currentBalance < 0) {
            message += "Humne aapko Rs. " + Math.abs(currentBalance) + " dene hain.";
        } else {
            message += "Aapka hisab barabar hai (Settled).";
        }
        
        message += "\n\nShukriya!";

        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_TEXT, message);
        startActivity(Intent.createChooser(intent, "Share via"));
    }

    private void callCustomer() {
        if (customerPhone != null && !customerPhone.isEmpty()) {
            Intent intent = new Intent(Intent.ACTION_DIAL);
            intent.setData(Uri.parse("tel:" + customerPhone));
            startActivity(intent);
        } else {
            Toast.makeText(this, "Phone number not available", Toast.LENGTH_SHORT).show();
        }
    }

    private void showTransactionDialog(String type) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View view = LayoutInflater.from(this).inflate(R.layout.dialog_add_transaction, null);
        
        EditText etAmount = view.findViewById(R.id.etTransAmount);
        EditText etDesc = view.findViewById(R.id.etTransDesc);

        builder.setView(view)
                .setTitle(type.equals("GAVE") ? "You Gave (Udhaar)" : "You Got (Wapsi)")
                .setPositiveButton("Save", (dialog, which) -> {
                    String amountStr = etAmount.getText().toString();
                    String desc = etDesc.getText().toString();
                    
                    if (!amountStr.isEmpty()) {
                        double amount = Double.parseDouble(amountStr);
                        viewModel.insertTransaction(new Transaction(customerId, amount, type, desc, System.currentTimeMillis()));
                        Toast.makeText(this, "Entry Saved", Toast.LENGTH_SHORT).show();
                    }
                })
                .setNegativeButton("Cancel", null)
                .show();
    }
}