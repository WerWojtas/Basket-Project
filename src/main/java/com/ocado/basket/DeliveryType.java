package com.ocado.basket;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class DeliveryType {

    private final Set<String> types;
    private final int size;
    private final String name;

    protected DeliveryType(String name, Set<String> types){
        this.name = name;
        this.types = types;
        this.size = types.size();
    }

    public boolean equals(Object obj){
        if(obj instanceof DeliveryType other){
            return this.name.equals(other.name) && this.types.equals(other.types);
        }
        return false;
    }

    public int hashCode(){
        return name.hashCode() + types.hashCode();
    }

    public int getSize(){
        return size;
    }

    protected String getName(){
        return name;
    }

    protected boolean contains(String item){
        return types.contains(item);
    }

    protected List<String> returnAsList(){
        return new ArrayList<>(types);
    }
}
