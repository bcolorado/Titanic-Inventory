package com.titanicos.TitanicInventory.model;

public class Queries {
    private String id;
    private String dato;
    public Queries(String id, String dato){
        this.id=id;
        this.dato=dato;
    }
    @Override
    public String toString(){
        return id+ " "+ dato;
    }

    public String getDato() {
        return dato;
    }

    public String getId() {
        return id;
    }
}
