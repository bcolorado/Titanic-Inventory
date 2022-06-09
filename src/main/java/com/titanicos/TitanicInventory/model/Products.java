package com.titanicos.TitanicInventory.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document("products")

public class Products {
    public Products(String id_name, int precio, int cantidad) {
        this.id_name = id_name;
        this.precio = precio;
        this.cantidad=cantidad;
        this.active=true;
    }

    public int getSubtotal() {
        return Subtotal;
    }
    public void setSubtotal(int subtotal){this.Subtotal=subtotal;}
    public String getId_name() {
        return id_name;
    }

    public void setId_name(String id_name) {
        this.id_name = id_name;
    }

    public int getPrecio() {
        return precio;
    }

    public void setPrecio(int precio) {
        this.precio = precio;
    }

    public int getCantidad() {
        return cantidad;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }

    @Override
    public String toString() {
        return "Products{" +
                "id_name='" + id_name + '\'' +
                ", precio=" + precio +
                ", cantidad=" + cantidad +
                '}';
    }

    @Id
    private String id_name;
    private int Subtotal;
    private int precio;
    private int cantidad;

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    private boolean active;


}
