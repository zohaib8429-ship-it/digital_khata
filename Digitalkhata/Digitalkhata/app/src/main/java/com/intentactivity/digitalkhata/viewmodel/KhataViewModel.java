package com.intentactivity.digitalkhata.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.intentactivity.digitalkhata.model.Customer;
import com.intentactivity.digitalkhata.model.Transaction;
import com.intentactivity.digitalkhata.repository.KhataRepository;

import java.util.List;

public class KhataViewModel extends AndroidViewModel {
    private KhataRepository repository;
    private LiveData<List<Customer>> allCustomers;

    public KhataViewModel(@NonNull Application application) {
        super(application);
        repository = new KhataRepository(application);
        allCustomers = repository.getAllCustomers();
    }

    public LiveData<List<Customer>> getAllCustomers() {
        return allCustomers;
    }

    public LiveData<List<Customer>> searchCustomers(String query) {
        return repository.searchCustomers(query);
    }

    public void insertCustomer(Customer customer) {
        repository.insertCustomer(customer);
    }

    public void updateCustomer(Customer customer) {
        repository.updateCustomer(customer);
    }

    public void deleteCustomer(Customer customer) {
        repository.deleteCustomer(customer);
    }

    public LiveData<List<Transaction>> getTransactionsForCustomer(int customerId) {
        return repository.getTransactionsForCustomer(customerId);
    }

    public void insertTransaction(Transaction transaction) {
        repository.insertTransaction(transaction);
    }
}