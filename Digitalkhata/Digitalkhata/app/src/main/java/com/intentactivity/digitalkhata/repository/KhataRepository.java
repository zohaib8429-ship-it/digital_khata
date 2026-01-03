package com.intentactivity.digitalkhata.repository;

import android.app.Application;

import androidx.lifecycle.LiveData;

import com.intentactivity.digitalkhata.data.AppDao;
import com.intentactivity.digitalkhata.data.AppDatabase;
import com.intentactivity.digitalkhata.model.Customer;
import com.intentactivity.digitalkhata.model.Transaction;

import java.util.List;

public class KhataRepository {
    private AppDao appDao;
    private LiveData<List<Customer>> allCustomers;

    public KhataRepository(Application application) {
        AppDatabase db = AppDatabase.getDatabase(application);
        appDao = db.appDao();
        allCustomers = appDao.getAllCustomers();
    }

    public LiveData<List<Customer>> getAllCustomers() {
        return allCustomers;
    }

    public LiveData<List<Customer>> searchCustomers(String query) {
        return appDao.searchCustomers("%" + query + "%");
    }

    public void insertCustomer(Customer customer) {
        AppDatabase.databaseWriteExecutor.execute(() -> appDao.insertCustomer(customer));
    }

    public void updateCustomer(Customer customer) {
        AppDatabase.databaseWriteExecutor.execute(() -> appDao.updateCustomer(customer));
    }

    public void deleteCustomer(Customer customer) {
        AppDatabase.databaseWriteExecutor.execute(() -> appDao.deleteCustomer(customer));
    }

    public LiveData<List<Transaction>> getTransactionsForCustomer(int customerId) {
        return appDao.getTransactionsForCustomer(customerId);
    }

    public void insertTransaction(Transaction transaction) {
        AppDatabase.databaseWriteExecutor.execute(() -> {
            appDao.insertTransaction(transaction);
            appDao.updateCustomerBalance(transaction.getCustomerId());
        });
    }
}