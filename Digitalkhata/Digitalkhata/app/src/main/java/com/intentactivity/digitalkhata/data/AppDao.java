package com.intentactivity.digitalkhata.data;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.intentactivity.digitalkhata.model.Customer;
import com.intentactivity.digitalkhata.model.Transaction;

import java.util.List;

@Dao
public interface AppDao {
    
    // Customer Queries
    @Insert
    void insertCustomer(Customer customer);

    @Update
    void updateCustomer(Customer customer);

    @Delete
    void deleteCustomer(Customer customer);

    @Query("SELECT * FROM customers ORDER BY name ASC")
    LiveData<List<Customer>> getAllCustomers();

    @Query("SELECT * FROM customers WHERE name LIKE :searchQuery OR phoneNumber LIKE :searchQuery")
    LiveData<List<Customer>> searchCustomers(String searchQuery);

    // Transaction Queries
    @Insert
    void insertTransaction(Transaction transaction);

    @Query("SELECT * FROM transactions WHERE customerId = :customerId ORDER BY date DESC")
    LiveData<List<Transaction>> getTransactionsForCustomer(int customerId);
    
    @Query("UPDATE customers SET totalBalance = (SELECT SUM(CASE WHEN type = 'GAVE' THEN amount ELSE -amount END) FROM transactions WHERE customerId = :customerId) WHERE id = :customerId")
    void updateCustomerBalance(int customerId);
}