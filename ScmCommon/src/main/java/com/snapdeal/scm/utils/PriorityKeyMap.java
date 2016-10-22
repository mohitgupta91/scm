package com.snapdeal.scm.utils;

import java.lang.reflect.Array;
import java.util.*;

/**
 * @author chitransh
 */
public class PriorityKeyMap<K, V> {

    private Map<K, V>[] maps;

    @SafeVarargs
    @SuppressWarnings("unchecked")
    public PriorityKeyMap(Comparator<K>... keyComparatorsInOrder) {
        if (keyComparatorsInOrder == null || keyComparatorsInOrder.length == 0) {
            throw new IllegalArgumentException("Either the list of rule comparators is null or empty");
        }
        this.maps = (Map<K, V>[]) Array.newInstance(Map.class, keyComparatorsInOrder.length);
        for (int i = 0; i < keyComparatorsInOrder.length; i++) {
            maps[i] = new TreeMap<>(keyComparatorsInOrder[i]);
        }
    }

    public boolean containsKey(K key) {
        return get(key) != null;
    }

    public V get(K key) {
        for (Map<K, V> map : this.maps) {
            V value = map.get(key);
            if (value != null) {
                return value;
            }
        }
        return null;
    }

    public V put(K key, V value) {
        for (Map<K, V> map : this.maps) {
            map.put(key, value);
        }
        return value;
    }

    /*
    public static void main(String[] args) {

        *//*
        Courier Origin City	Y	Y	N   N
        Courier Origin State	Y	Y	N
        Courier Group	        Y	N	N
        Courier Type	        Y	Y	Y
        *//*

        Comparator<ShippedNotConMetrics> firstPreference = (first, second) -> {
            int compareTo = first.getOriginCity().compareTo(second.getOriginCity());
            if (compareTo != 0) {
                return compareTo;
            }
            compareTo = first.getOriginState().compareTo(second.getOriginState());
            if (compareTo != 0) {
                return compareTo;
            }
            compareTo = first.getCourierGroup().compareTo(second.getCourierGroup());
            if (compareTo != 0) {
                return compareTo;
            }
            compareTo = first.getCourierType().compareTo(second.getCourierType());
            if (compareTo != 0) {
                return compareTo;
            }
            return 0;
        };

        Comparator<ShippedNotConMetrics> secondPreference = (first, second) -> {
            int compareTo = first.getOriginCity().compareTo(second.getOriginCity());
            if (compareTo != 0) {
                return compareTo;
            }
            compareTo = first.getOriginState().compareTo(second.getOriginState());
            if (compareTo != 0) {
                return compareTo;
            }
            compareTo = first.getCourierType().compareTo(second.getCourierType());
            if (compareTo != 0) {
                return compareTo;
            }
            return 0;
        };

        Comparator<ShippedNotConMetrics> thirdPreference = (first, second) ->
                first.getCourierType().compareTo(second.getCourierType());

        PriorityKeyMap<ShippedNotConMetrics, Integer> priorityKeyMap = new PriorityKeyMap<>(firstPreference, secondPreference, thirdPreference);
        priorityKeyMap.put(new ShippedNotConMetrics(null, "Forward", "Bihar", "Patna", "typeOne", null, null, null), 10);

        System.out.println(priorityKeyMap.get(new ShippedNotConMetrics(null, "Forward", "Bihar", "Patna", "typeOne", null, null, null)));
        System.out.println(priorityKeyMap.get(new ShippedNotConMetrics(null, "Backward", "Bihar", "Patna", "typeOne", null, null, null)));
        System.out.println(priorityKeyMap.get(new ShippedNotConMetrics(null, "Backward", "Haryana", "Gurugram", "typeOne", null, null, null)));
        System.out.println(priorityKeyMap.get(new ShippedNotConMetrics(null, "Backward", "Haryana", "Gurugram", "typeTwo", null, null, null)));
    }
    */
}
