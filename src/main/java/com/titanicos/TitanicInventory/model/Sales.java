package com.titanicos.TitanicInventory.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

@Document("sales")

public class Sales {

    @Id
    private String id_sale;
    private Date timestamp;
    private int quantity;
    private boolean active;

    //Constructor
    public Sales(String id_sale, Date timestamp, int quantity) {
        this.id_sale = id_sale;
        this.timestamp = timestamp;
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

    //to  string
    @Override
    public String toString() {
        return "Sales{" +
                "id_sale='" + id_sale + '\'' +
                ", timestamp=" + timestamp +
                ", quantity=" + quantity +
                ", active=" + active +
                '}';
    }
}
