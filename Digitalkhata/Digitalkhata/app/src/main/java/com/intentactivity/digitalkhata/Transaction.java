package com.intentactivity.digitalkhata;

import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;
import androidx.room.PrimaryKey;

@Entity(tableName = "transactions",
        foreignKeys = @ForeignKey(entity = Customer.class,
                parentColumns = "id",
                childColumns = "customerId",
                onDelete = ForeignKey.CASCADE),
        indices = {@Index("customerId")})
public class Transaction {
    @PrimaryKey(autoGenerate = true)
    private int id;
    private int customerId;
    private double amount;
    private String type; // "GAVE" or "GOT"
    private String description;
    private long date;

    public Transaction(int customerId, double amount, String type, String description, long date) {
        this.customerId = customerId;
        this.amount = amount;
        this.type = type;
        this.description = description;
        this.date = date;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public int getCustomerId() { return customerId; }
    public void setCustomerId(int customerId) { this.customerId = customerId; }
    public double getAmount() { return amount; }
    public void setAmount(double amount) { this.amount = amount; }
    public String getType() { return type; }
    public void setType(String type) { this.type = type; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public long getDate() { return date; }
    public void setDate(long date) { this.date = date; }
}
