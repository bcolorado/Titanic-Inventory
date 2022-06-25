package com.titanicos.TitanicInventory.model;

import com.mongodb.lang.Nullable;

public class Consults {
    private String id;
    private String dato;
    public Consults(String id,String dato){
        this.id=id;
        this.dato=dato;
    }
    @Override
    public String toString(){
        return id+ " "+ dato;
    }
}
