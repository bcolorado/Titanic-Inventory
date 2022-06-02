package com.titanicos.TitanicInventory.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import java.nio.charset.Charset;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import java.util.Arrays;

@Document("products")

public class Products {
    public Products(String id_name, float precio, int cantidad) {
        this.id_name = id_name;
        this.precio = precio;
        this.cantidad=cantidad;
    }

    public String getId_name() {
        return id_name;
    }

    public void setId_name(String id_name) {
        this.id_name = id_name;
    }

    public float getPrecio() {
        return precio;
    }

    public void setPrecio(float precio) {
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

    private float precio;
    private int cantidad;


}
