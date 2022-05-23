package com.example.titanicinventory;

public class User {
    private String name;
    private long id;
    private  String rol;
    private String contrasena;

    public User(String name, long id, String rol, String contrasena){
        this.name = name;
        this.id = id;
        this.rol = rol;
        this.contrasena = contrasena;
    }

    public String getName(){
        return name;
    }
    
    public void setName(String s){
        name = s;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getRol() {
        return rol;
    }

    public void setRol(String rol) {
        this.rol = rol;
    }

    public String getContrasena() {
        return contrasena;
    }

    public void setContrasena(String contrasena) {
        this.contrasena = contrasena;
    }

    @Override
    public String toString() {
        return "User{" +
                "name='" + name + '\'' +
                ", id=" + id +
                ", rol='" + rol + '\'' +
                ", contrasena='" + contrasena + '\'' +
                '}';
    }
}
