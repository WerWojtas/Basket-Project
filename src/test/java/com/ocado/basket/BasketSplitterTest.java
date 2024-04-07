package com.ocado.basket;

import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

public class BasketSplitterTest {

    @Test
    public void getSubsetsTest(){
        BasketSplitter splitter = new BasketSplitter("src/main/resources/config.json");
        List<String>[] subsets = splitter.getSubsets();
        List<String> result = Arrays.asList("0", "1", "2", "3", "4", "5", "6", "7", "8", "9");
        assertEquals(subsets[0], result);
        List<String> result_2 = Arrays.asList("01", "02", "03", "04", "05", "06", "07", "08", "09", "12", "13", "14", "15", "16", "17", "18", "19", "23", "24", "25", "26", "27", "28", "29", "34", "35", "36", "37", "38", "39", "45", "46", "47", "48", "49", "56", "57", "58", "59", "67", "68", "69", "78", "79", "89");
        assertEquals(subsets[1], result_2);
        assertEquals(subsets[2].size(), 120);
        assertEquals(subsets[7].get(11),"01235679");
        int sum = 0;
        for (int i = 0; i < 10; i++) {
            sum += subsets[i].size();
        }
        assertEquals(sum, 1023);
    }

    @Test
    public void generateDeliveryTypesTest(){
        BasketSplitter splitter = new BasketSplitter("src/main/resources/config.json");
        List<String> items = Arrays.asList("Cocoa Butter", "Tart - Raisin And Pecan", "Table Cloth 54x72 White", "Flower - Daisies");
        List<DeliveryType> deliveryTypes = splitter.generateDeliveryTypes(items);
        deliveryTypes.sort(Comparator.comparingInt(DeliveryType::getSize).reversed());
        assertEquals(deliveryTypes.size(), 8);
        assertEquals(deliveryTypes.get(0).getName(), "Courier");
        assertEquals(deliveryTypes.get(0).getSize(), 4);
        assertEquals(deliveryTypes.get(1).getName(), "Mailbox delivery");
        assertEquals(deliveryTypes.get(1).getSize(), 3);
        assertTrue(deliveryTypes.get(1).returnAsList().contains("Cocoa Butter"));
        assertTrue(deliveryTypes.get(1).returnAsList().contains("Tart - Raisin And Pecan"));
        assertTrue(deliveryTypes.get(1).returnAsList().contains("Table Cloth 54x72 White"));
        List<String> items2 = Arrays.asList("Cocoa Butter", "Tart - Raisin And Pecan", "Table Cloth 54x72 White");
        List<DeliveryType> deliveryTypes2 = splitter.generateDeliveryTypes(items2);
        assertEquals(deliveryTypes2.size(), 7);
        assertFalse(deliveryTypes2.contains("Pick-up point"));
    }

    @Test
    public void splitTest(){
        BasketSplitter splitter = new BasketSplitter("src/main/resources/config.json");
        List<String> items = Arrays.asList("Cocoa Butter", "Tart - Raisin And Pecan", "Table Cloth 54x72 White", "Flower - Daisies", "Fond - Chocolate", "Cookies - Englishbay Wht");
        Map<String,List<String>> result = splitter.split(items);
        assertEquals(result.size(), 2);
        assertTrue(result.containsKey("Courier"));
        assertEquals(result.get("Courier").size(), 5);
        for (String key : result.keySet()){
            if (!key.equals("Courier")){
                System.out.println(key);
                assertEquals(result.get(key).size(), 1);
            }
        }
        List<String> items2 = Arrays.asList("Cookies Oatmeal Raisin","English Muffin","Sole - Dover, Whole, Fresh","Sugar - Cubes");
        Map<String,List<String>> result2 = splitter.split(items2);
        assertEquals(result2.size(), 3);
        List<String> items3 = Arrays.asList("Fond - Chocolate", "Chocolate - Unsweetened", "Nut - Almond, Blanched, Whole", "Haggis", "Mushroom - Porcini Frozen", "Cake - Miini Cheesecake Cherry", "Sauce - Mint", "Longan", "Bag Clear 10 Lb", "Nantucket - Pomegranate Pear", "Puree - Strawberry", "Numi - Assorted Teas", "Apples - Spartan", "Garlic - Peeled", "Cabbage - Nappa", "Bagel - Whole White Sesame", "Tea - Apple Green Tea");
        Map<String,List<String>> result3 = splitter.split(items3);
        assertEquals(result3.size(), 3);
        assertTrue(result3.keySet().contains("Courier"));
        assertTrue(result3.keySet().contains("Same day delivery"));
        assertTrue(result3.keySet().contains("Express Collection"));
        List<String> items4 = Arrays.asList("Ecolab - Medallion", "Pasta - Fusili Tri - Coloured","Puree - Guava","Cookies Oatmeal Raisin",  "Pork Salted Bellies","Dc Hikiage Hira Huba");
        Map<String,List<String>> result4 = splitter.split(items4);
        assertEquals(result4.size(), 2);
        assertTrue(result4.keySet().contains("Parcel locker"));
        assertTrue(result4.keySet().contains("Next day shipping"));
        List<String> items5 = Arrays.asList("Ecolab - Medallion", "Pasta - Fusili Tri - Coloured","Puree - Strawberry","Cookies Oatmeal Raisin",  "Pork Salted Bellies","Dc Hikiage Hira Huba","Chickhen - Chicken Phyllo");
        Map<String,List<String>> result5 = splitter.split(items5);
        assertEquals(result5.size(), 3);
        assertTrue(result5.keySet().contains("Mailbox delivery"));
    }
}
