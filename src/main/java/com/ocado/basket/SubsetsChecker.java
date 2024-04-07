package com.ocado.basket;

import java.util.List;

public class SubsetsChecker implements Runnable {

    private final String subset;
    private final List<String> items;
    private final List<String> result;
    private final List<DeliveryType> types;

    protected SubsetsChecker(String subset, List<String> items, List<String> result, List<DeliveryType> types){
        this.subset = subset;
        this.items = items;
        this.result = result;
        this.types = types;
    }

    public void run(){
        for(String item : items){
            if (!checkItem(item)){
                return;
            }
        }
        synchronized (this) {
            result.add(subset);
        }
    }

    private boolean checkItem(String item){
        for(int i=0; i<subset.length();i++){
            if(types.get(Character.getNumericValue(subset.charAt(i))).contains(item)){
                return true;
            }
        }
        return false;
    }
}
