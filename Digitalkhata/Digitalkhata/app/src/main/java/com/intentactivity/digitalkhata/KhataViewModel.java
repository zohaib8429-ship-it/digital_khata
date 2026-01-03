package com.intentactivity.digitalkhata;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

public class KhataViewModel extends AndroidViewModel {
    private final KhataRepository repository;
    private final LiveData<List<Customer>> allCustomers;

    public KhataViewModel(@NonNull Application application) {
        super(application);
        repository = new KhataRepository(application);
        allCustomers = repository.getAllCustomers();
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

    public LiveData<List<Customer>> getAllCustomers() {
        return allCustomers;
    }

    public LiveData<List<Customer>> searchCustomers(String query) {
        return repository.searchCustomers(query);
    }

    public void insertTransaction(Transaction transaction) {
        repository.insertTransaction(transaction);
    }

    public LiveData<List<Transaction>> getTransactionsForCustomer(int customerId) {
        return repository.getTransactionsForCustomer(customerId);
    }

    public LiveData<Double> getTotalGaveForCustomer(int customerId) {
        return repository.getTotalGaveForCustomer(customerId);
    }

    public LiveData<Double> getTotalGotForCustomer(int customerId) {
        return repository.getTotalGotForCustomer(customerId);
    }

    public LiveData<Double> getTotalReceivable() {
        return repository.getTotalReceivable();
    }

    public LiveData<Double> getTotalPayable() {
        return repository.getTotalPayable();
    }
}
