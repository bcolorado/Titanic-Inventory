package com.titanicos.TitanicInventory.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Document("sales")

public class Sales {

    @Id
    private String _id;
    private String id_vendedor;
    private List<Products> products;
    private Date timestamp;
    private int total;
    private boolean active;

    //Constructor
    public Sales(String id_vendedor, int total) {
        //this.id_sale = id_sale;
        this.id_vendedor = id_vendedor;
        this.products = new ArrayList<>();
        this.timestamp = new Date();
        this.total = total;
        this.active = true;
    }

    //getters and setters
    public String get_id(){return _id;}
    public String getId_vendedor() {
        return id_vendedor;
    }

    public void setId_vendedor(String id_vendedor) {
        this.id_vendedor = id_vendedor;
    }
    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }
    public void addProduct(Products product){
        this.products.add(product);
    }
    public List<Products> getProducts(){return products;}
    //to  string
    @Override
    public String toString() {
        return "Sales{" +
                "id_vendedor='" + id_vendedor + '\'' +
                ", timestamp=" + timestamp +
                ", total=" + total +
                ", active=" + active +
                '}';
    }
}
