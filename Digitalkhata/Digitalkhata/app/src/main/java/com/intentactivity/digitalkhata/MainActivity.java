package com.intentactivity.digitalkhata;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.intentactivity.digitalkhata.adapter.CustomerAdapter;
import com.intentactivity.digitalkhata.model.Customer;
import com.intentactivity.digitalkhata.viewmodel.KhataViewModel;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    private KhataViewModel viewModel;
    private CustomerAdapter adapter;
    private TextView tvTotalGet, tvTotalGive;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tvTotalGet = findViewById(R.id.tvTotalGet);
        tvTotalGive = findViewById(R.id.tvTotalGive);

        RecyclerView recyclerView = findViewById(R.id.rvCustomers);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new CustomerAdapter();
        recyclerView.setAdapter(adapter);

        viewModel = new ViewModelProvider(this).get(KhataViewModel.class);
        viewModel.getAllCustomers().observe(this, customers -> {
            adapter.submitList(customers);
            calculateTotals(customers);
        });

        FloatingActionButton fab = findViewById(R.id.fabAddCustomer);
        fab.setOnClickListener(v -> showAddCustomerDialog());

        EditText etSearch = findViewById(R.id.etSearch);
        etSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                viewModel.searchCustomers(s.toString()).observe(MainActivity.this, customers -> {
                    adapter.submitList(customers);
                });
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });

        adapter.setOnItemClickListener(customer -> {
            Intent intent = new Intent(MainActivity.this, CustomerDetailActivity.class);
            intent.putExtra("CUSTOMER_ID", customer.getId());
            intent.putExtra("CUSTOMER_NAME", customer.getName());
            intent.putExtra("CUSTOMER_PHONE", customer.getPhoneNumber());
            startActivity(intent);
        });

        // Long click for Edit/Delete
        adapter.setOnItemLongClickListener(this::showOptionsDialog);
    }

    private void calculateTotals(List<Customer> customers) {
        double totalGet = 0;
        double totalGive = 0;

        for (Customer customer : customers) {
            double balance = customer.getTotalBalance();
            if (balance > 0) {
                totalGet += balance;
            } else if (balance < 0) {
                totalGive += Math.abs(balance);
            }
        }

        tvTotalGet.setText("Rs " + totalGet);
        tvTotalGive.setText("Rs " + totalGive);
    }

    private void showOptionsDialog(Customer customer) {
        String[] options = {"Edit", "Delete"};
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(customer.getName())
                .setItems(options, (dialog, which) -> {
                    if (which == 0) {
                        showEditCustomerDialog(customer);
                    } else {
                        showDeleteConfirmation(customer);
                    }
                })
                .show();
    }

    private void showEditCustomerDialog(Customer customer) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View view = LayoutInflater.from(this).inflate(R.layout.dialog_add_customer, null);
        
        EditText etName = view.findViewById(R.id.etDialogName);
        EditText etPhone = view.findViewById(R.id.etDialogPhone);
        EditText etAddress = view.findViewById(R.id.etDialogAddress);

        etName.setText(customer.getName());
        etPhone.setText(customer.getPhoneNumber());
        etAddress.setText(customer.getAddress());

        builder.setView(view)
                .setTitle("Edit Customer")
                .setPositiveButton("Update", (dialog, which) -> {
                    customer.setName(etName.getText().toString());
                    customer.setPhoneNumber(etPhone.getText().toString());
                    customer.setAddress(etAddress.getText().toString());
                    
                    if (!customer.getName().isEmpty()) {
                        viewModel.updateCustomer(customer);
                        Toast.makeText(this, "Customer Updated", Toast.LENGTH_SHORT).show();
                    }
                })
                .setNegativeButton("Cancel", null)
                .show();
    }

    private void showDeleteConfirmation(Customer customer) {
        new AlertDialog.Builder(this)
                .setTitle("Delete Customer")
                .setMessage("Are you sure you want to delete " + customer.getName() + "? All transactions will be removed.")
                .setPositiveButton("Delete", (dialog, which) -> {
                    viewModel.deleteCustomer(customer);
                    Toast.makeText(this, "Customer Deleted", Toast.LENGTH_SHORT).show();
                })
                .setNegativeButton("Cancel", null)
                .show();
    }

    private void showAddCustomerDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View view = LayoutInflater.from(this).inflate(R.layout.dialog_add_customer, null);
        
        EditText etName = view.findViewById(R.id.etDialogName);
        EditText etPhone = view.findViewById(R.id.etDialogPhone);
        EditText etAddress = view.findViewById(R.id.etDialogAddress);

        builder.setView(view)
                .setTitle("Add New Customer")
                .setPositiveButton("Add", (dialog, which) -> {
                    String name = etName.getText().toString();
                    String phone = etPhone.getText().toString();
                    String address = etAddress.getText().toString();
                    
                    if (!name.isEmpty()) {
                        viewModel.insertCustomer(new Customer(name, phone, address));
                        Toast.makeText(this, "Customer Added", Toast.LENGTH_SHORT).show();
                    }
                })
                .setNegativeButton("Cancel", null)
                .show();
    }
}