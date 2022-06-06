package com.titanicos.TitanicInventory.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Document("sales")

public class Sales {

    @Id
    private String id_sale;
    private String id_vendedor;
    private List<Products> products;
    private Date timestamp;
    private int quantity;
    private boolean active;

    //Constructor
    public Sales(String id_sale,String id_vendedor, int quantity) {
        this.id_sale = id_sale;
        this.id_vendedor = id_vendedor;
        this.products = new ArrayList<>();
        this.timestamp = new Date();
        this.quantity = quantity;
        this.active = true;
    }

    //getters and setters
    public String getId_sale() {
        return id_sale;
    }

    public void setId_sale(String id_sale) {
        this.id_sale = id_sale;
    }

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

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
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
    //to  string
    @Override
    public String toString() {
        return "Sales{" +
                "id_sale='" + id_sale + '\'' +
                "id_vendedor='" + id_vendedor + '\'' +
                ", timestamp=" + timestamp +
                ", quantity=" + quantity +
                ", active=" + active +
                '}';
    }
}