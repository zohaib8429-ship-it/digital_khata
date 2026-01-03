package com.intentactivity.digitalkhata;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface KhataDao {
    @Insert
    long insertCustomer(Customer customer);

    @Update
    void updateCustomer(Customer customer);

    @Delete
    void deleteCustomer(Customer customer);

    @Query("SELECT * FROM customers ORDER BY name ASC")
    LiveData<List<Customer>> getAllCustomers();

    @Query("SELECT * FROM customers WHERE name LIKE :query OR phoneNumber LIKE :query")
    LiveData<List<Customer>> searchCustomers(String query);

    @Insert
    void insertTransaction(Transaction transaction);

    @Query("SELECT * FROM transactions WHERE customerId = :customerId ORDER BY date DESC")
    LiveData<List<Transaction>> getTransactionsForCustomer(int customerId);

    @Query("SELECT SUM(amount) FROM transactions WHERE customerId = :customerId AND type = 'GAVE'")
    LiveData<Double> getTotalGaveForCustomer(int customerId);

    @Query("SELECT SUM(amount) FROM transactions WHERE customerId = :customerId AND type = 'GOT'")
    LiveData<Double> getTotalGotForCustomer(int customerId);

    @Query("SELECT SUM(amount) FROM transactions WHERE type = 'GAVE'")
    LiveData<Double> getTotalReceivable();

    @Query("SELECT SUM(amount) FROM transactions WHERE type = 'GOT'")
    LiveData<Double> getTotalPayable();
}
