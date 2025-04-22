package com.example.clothingshop;

import java.util.Date;
import java.util.List;

public class Order {
    private String id;
    private String userId;
    private List<ShoppingItem> items;
    private double total;
    private Date date;

    // Konstruktor, getterek és setterek
    public Order() {}

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getUserId() { return userId; }
    public void setUserId(String userId) { this.userId = userId; }
    public List<ShoppingItem> getItems() { return items; }
    public void setItems(List<ShoppingItem> items) { this.items = items; }
    public double getTotal() { return total; }
    public void setTotal(double total) { this.total = total; }
    public Date getDate() { return date; }
    public void setDate(Date date) { this.date = date; }
}