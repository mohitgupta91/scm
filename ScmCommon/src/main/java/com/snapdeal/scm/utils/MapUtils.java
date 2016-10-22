package com.snapdeal.scm.utils;

import java.util.*;

/**
 * @author chitransh
 */
public class MapUtils {

    /**
     * @return a map sorted by values; assumes no null values in a map
     */
    public static <K> Map<K, Double> sortMapByValue(Map<K, Double> map, boolean reverseSort) {

        List<Map.Entry<K, Double>>       list = new LinkedList<>(map.entrySet());
        Comparator<Map.Entry<K, Double>> comparator;
        if (reverseSort) {
            comparator = (o1, o2) -> o2.getValue().compareTo(o1.getValue());
        } else {
            comparator = (o1, o2) -> o1.getValue().compareTo(o2.getValue());
        }
        Collections.sort(list, comparator);
        Map<K, Double> resultMap = new LinkedHashMap<>();
        list.forEach(entry -> resultMap.put(entry.getKey(), entry.getValue()));
        return resultMap;
    }

    public static <K> Map<K, Double> sortMapByValueWithTopResults(Map<K, Double> map, int noOfTopResults, K othersKey) {

        Map<K, Double> sortedMap = sortMapByValue(map, true);
        Map<K, Double> resultMap = new LinkedHashMap<>();
        int            count     = 1;

        for (K key : sortedMap.keySet()) {
            if (count < noOfTopResults) {
                resultMap.put(key, sortedMap.get(key));
                count++;
            } else {
                Double defaultValue = resultMap.get(othersKey);
                Double currentValue = sortedMap.get(key);
                if (defaultValue != null) {
                    resultMap.put(othersKey, currentValue + defaultValue);
                } else {
                    resultMap.put(othersKey, currentValue);
                }
            }
        }
        return resultMap;
    }

    public static void main(String[] args) {

        Map<String, Double> unsortedMap = new HashMap<>();
        unsortedMap.put("A", 99.5);
        unsortedMap.put("B", 67.4);
        unsortedMap.put("C", 67.4);
        unsortedMap.put("D", 29.9);
        unsortedMap.put("E", 89.9);
        unsortedMap.put("F", 39.9);
        unsortedMap.put("G", 59.9);
        unsortedMap.put("G", 49.9);
        unsortedMap.put("H", 41.9);
        unsortedMap.put("J", 63.9);

        System.out.println("Unsorted Map: " + unsortedMap);
        Map<String, Double> sortedMap = sortMapByValue(unsortedMap, false);
        System.out.println("Sorted Map in ascending order of values: " + sortedMap);
        Map<String, Double> sortedWithOthersMap = sortMapByValueWithTopResults(unsortedMap, 5, "OTHERS");
        System.out.println("Sorted with others Map in descending orders of their values: " + sortedWithOthersMap);
    }
}