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

@Document("users")
public class User {

    public User(String id, byte[] password, byte[] salt, String name, String rol) {
        this.id = id;
        this.password = password;
        this.salt = salt;
        this.name = name;
        this.rol = rol;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public byte[] getPassword() {
        return password;
    }

    public void setPassword(byte[] password) {
        this.password = password;
    }

    public byte[] getSalt() {
        return salt;
    }

    public void setSalt(byte[] salt) {
        this.salt = salt;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRol() {
        return rol;
    }

    public void setRol(String rol) {
        this.rol = rol;
    }

    @Override
    public String toString() {
        return "User{" +
                "id='" + id + '\'' +
                ", password=" + Arrays.toString(password) +
                ", salt=" + Arrays.toString(salt) +
                ", name='" + name + '\'' +
                ", rol='" + rol + '\'' +
                '}';
    }

    public User() {
    }

    @Id
    private String id;

    private byte[] password;
    private byte[] salt;
    private String name;
    private String rol;

}
