package com.intentactivity.digitalkhata;

import android.app.Application;

import androidx.lifecycle.LiveData;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class KhataRepository {
    private final KhataDao khataDao;
    private final LiveData<List<Customer>> allCustomers;
    private final ExecutorService executorService;

    public KhataRepository(Application application) {
        AppDatabase database = AppDatabase.getInstance(application);
        khataDao = database.khataDao();
        allCustomers = khataDao.getAllCustomers();
        executorService = Executors.newFixedThreadPool(2);
    }

    public void insertCustomer(Customer customer) {
        executorService.execute(() -> khataDao.insertCustomer(customer));
    }

    public void updateCustomer(Customer customer) {
        executorService.execute(() -> khataDao.updateCustomer(customer));
    }

    public void deleteCustomer(Customer customer) {
        executorService.execute(() -> khataDao.deleteCustomer(customer));
    }

    public LiveData<List<Customer>> getAllCustomers() {
        return allCustomers;
    }

    public LiveData<List<Customer>> searchCustomers(String query) {
        return khataDao.searchCustomers("%" + query + "%");
    }

    public void insertTransaction(Transaction transaction) {
        executorService.execute(() -> khataDao.insertTransaction(transaction));
    }

    public LiveData<List<Transaction>> getTransactionsForCustomer(int customerId) {
        return khataDao.getTransactionsForCustomer(customerId);
    }

    public LiveData<Double> getTotalGaveForCustomer(int customerId) {
        return khataDao.getTotalGaveForCustomer(customerId);
    }

    public LiveData<Double> getTotalGotForCustomer(int customerId) {
        return khataDao.getTotalGotForCustomer(customerId);
    }

    public LiveData<Double> getTotalReceivable() {
        return khataDao.getTotalReceivable();
    }

    public LiveData<Double> getTotalPayable() {
        return khataDao.getTotalPayable();
    }
}
