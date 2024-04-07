package com.ocado.basket;
import java.io.FileReader;
import java.util.*;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;


public class BasketSplitter {

    private final JSONObject json;
    private final List<String>[] subSets;
    private final int deliverySize = 10;
    private final int threadNumber = 4;
    public BasketSplitter(String absolutePathToConfigFile) {
        try (FileReader reader = new FileReader(absolutePathToConfigFile)) {
            json = (JSONObject) new JSONParser().parse(reader);
            subSets = generateSubsets();
        } catch (Exception e) {
            String message = "An error occurred while reading or parsing the config file";
            throw new RuntimeException(message, e);
        }
    }
    public Map<String, List<String>> split(List<String> items) {
        if(items.isEmpty()){
            throw new RuntimeException("Items list is empty");
        }
        List<DeliveryType> deliveryTypeList = generateDeliveryTypes(items);
        deliveryTypeList.sort(Comparator.comparingInt(DeliveryType::getSize).reversed());
        ThreadPoolExecutor threadPool = (ThreadPoolExecutor )Executors.newFixedThreadPool(threadNumber);
        for (int i=0; i<deliveryTypeList.size(); i++){
            List<String> possibleSets = new ArrayList<>();
            for (String subSet : subSets[i]){
                if(checkSizes(items.size(), subSet,deliveryTypeList)){
                    threadPool.submit(new SubsetsChecker(subSet, items, possibleSets, deliveryTypeList));
                }
            }
            while (threadPool.getActiveCount()>0){
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            Optional<String> result = possibleSets.stream()
                    .min(Comparator.comparingInt(s -> s.charAt(0)));
            if(result.isPresent()){
                threadPool.shutdown();
                return getResult(result.get(), deliveryTypeList);
            }
        }
        throw new RuntimeException("No solution found - one of the products contains non existing delivery type");
    }

    private List<String>[] generateSubsets(){
        List<String>[] subSets = new List[this.deliverySize];
        for (int i = 0; i < this.deliverySize; i++) {
            subSets[i] = new ArrayList<>();
            generateSubsetsRecursion(i+1,0, "", subSets[i]);
        }
        return subSets;
    }

    private void generateSubsetsRecursion(int elementNumber, int index, String subset, List<String> subList){
        if(elementNumber == 0){
            subList.add(subset);
            return;
        }
        if(index == this.deliverySize){
            return;
        }
        for (int i = index; i < this.deliverySize; i++){
            String newSubset = subset + (i);
            generateSubsetsRecursion(elementNumber-1, i+1, newSubset, subList);
        }
    }

    public List<DeliveryType> generateDeliveryTypes(List<String> items){
        Map<String,Set<String>> deliveryTypes = new HashMap<>();
        for (String item : items) {
            JSONArray deliveryTypesArray = (JSONArray) json.get(item);
            for (Object deliveryType : deliveryTypesArray) {
                deliveryTypes.computeIfAbsent((String) deliveryType, k -> new HashSet<>()).add(item);
            }
        }
        List<DeliveryType> deliveryTypeList = new ArrayList<>();
        for (Map.Entry<String, Set<String>> entry : deliveryTypes.entrySet()) {
            deliveryTypeList.add(new DeliveryType(entry.getKey(), entry.getValue()));
        }
        return deliveryTypeList;
    }

    private boolean checkSizes(int itemsNum, String subset, List<DeliveryType> deliveryTypeList){
        int sum = 0;
        for(int i=0; i<subset.length();i++){
            if(Character.getNumericValue(subset.charAt(i)) >= deliveryTypeList.size()){
                return false;
            }
            sum+= deliveryTypeList.get(Character.getNumericValue(subset.charAt(i))).getSize();
        }
        return sum >= itemsNum;
    }

    private Map<String,List<String>> getResult(String subset, List<DeliveryType> deliveryTypeList){
        Map<String,List<String>> result = new HashMap<>();
        Set<String> productsCheck = new HashSet<>();
        for(int i=0; i<subset.length();i++){
            int subsetIndex = Character.getNumericValue(subset.charAt(i));
            List<String> products = deliveryTypeList.get(subsetIndex).returnAsList();
            result.put(deliveryTypeList.get(subsetIndex).getName(), new ArrayList<>());
            for (String product : products){
                if(!productsCheck.contains(product)){
                    result.get(deliveryTypeList.get(subsetIndex).getName()).add(product);
                    productsCheck.add(product);
                }
            }
        }
        return result;
    }

    public List<String>[] getSubsets() {
        return subSets;
    }
}
