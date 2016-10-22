package com.snapdeal.scm.utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author chitransh
 */
public class Trie<T, V> {

    private Node<T, V> root;
    private int noOfKeys;

    private static class Node<T, V> {
        private V value;
        private Map<T, Node<T, V>> next = new HashMap<>();

        public V getValue() {
            return value;
        }

        public void setValue(V value) {
            this.value = value;
        }

        public Map<T, Node<T, V>> getNext() {
            return next;
        }

        public void setNext(Map<T, Node<T, V>> next) {
            this.next = next;
        }
    }

    public boolean contains(T[] keys) {
        return get(keys) != null;
    }

    public int size() {
        return noOfKeys;
    }

    public boolean isEmpty() {
        return size() == 0;
    }

    public void put(V value, T... keys) {
        keys = getNullSafeKeys(keys);
        if (value == null) {
            delete(keys);
        } else {
            this.root = put(this.root, keys, value, 0);
        }
    }

    private Node<T, V> put(Node<T, V> node, T[] keys, V value, int index) {
        if (node == null) {
            node = new Node<T, V>();
        }
        if (index == keys.length) {
            if (node.getValue() == null) {
                noOfKeys++;
            }
            node.setValue(value);
            return node;
        }
        T key = keys[index];
        node.next.put(key, put(node.next.get(key), keys, value, index + 1));
        return node;
    }

    @SuppressWarnings("unchecked")
    private T[] getNullSafeKeys(T[] keys) {
        List<T> nullSafeKeys = new ArrayList<>(keys.length);
        boolean containsNull = false;
        for (T key : keys) {
            if (key == null) {
                containsNull = true;
            } else {
                nullSafeKeys.add(key);
            }
        }
        return !containsNull ? keys : nullSafeKeys.toArray((T[]) new Object[nullSafeKeys.size()]);
    }

    public V get(T... keys) {
        keys = getNullSafeKeys(keys);
        Node<T, V> node = get(this.root, keys, 0);
        if (node == null) {
            return null;
        }
        return node.getValue();
    }

    private Node<T, V> get(Node<T, V> node, T[] keys, int index) {
        if (node == null) {
            return null;
        }
        if (index == keys.length) {
            return node;
        }
        T key = keys[index];
        return get(node.getNext().get(key), keys, index + 1);
    }

    public V getClosest(T... keys) {
        keys = getNullSafeKeys(keys);
        Node<T, V> closest = getClosest(this.root, keys, 0);
        if (closest == null) {
            return null;
        }
        return closest.getValue();
    }

    private Node<T, V> getClosest(Node<T, V> node, T[] keys, int index) {
        if (node == null) {
            return null;
        }
        if (index == keys.length) {
            return node;
        }
        T key = keys[index];
        if (node.getNext().get(key) == null) {
            return node;
        }
        return getClosest(node.getNext().get(key), keys, index + 1);
    }

    private void delete(T[] keys) {
        this.root = delete(this.root, keys, 0);
    }

    private Node<T, V> delete(Node<T, V> node, T[] keys, int index) {
        if (node == null) return null;
        if (index == keys.length) {
            if (node.getValue() != null) {
                noOfKeys--;
            }
            node.setValue(null);
        } else {
            T key = keys[index];
            node.next.put(key, delete(node.next.get(key), keys, index + 1));
        }

        if (node.getValue() != null) {
            return node;
        }
        ;
        for (T existingKey : node.next.keySet())
            if (node.next.get(existingKey) != null)
                return node;
        return null;
    }

    /*
    public static void main(String[] args) {

        Trie<String, Integer> trie = new Trie<>();
        trie.put(10, "Forward", "GoJavas", "Bihar", "Patna");
        trie.put(11, "Forward", "GoJavas", "Bihar", "Madhepura");
        trie.put(12, "Forward", "GoJavas", "Bihar", null);
        trie.put(13, "Forward", "GoJavas", "Haryana", "Gurgaon");
        trie.put(14, "Forward", "GoJavas", "Haryana", null);
        trie.put(15, "Forward", "GoJavas", null, null);
        trie.put(18, "Forward", null, null, null);

        System.out.println("Testing for closest matches ------------------------------");
        System.out.println(trie.getClosest("Forward", "GoJavas", "Bihar", "Patna")); // should be 10
        System.out.println(trie.getClosest("Forward", "GoJavas", "Bihar", "Madhepura")); // should be 11
        System.out.println(trie.getClosest("Forward", "GoJavas", "Bihar", "Muzaffarpur")); // should be 12
        System.out.println(trie.getClosest("Forward", "GoJavas", "Bihar", "Bhagalpur")); // should be 12
        System.out.println(trie.getClosest("Forward", "GoJavas", "Haryana", "Gurgaon")); // should be 13
        System.out.println(trie.getClosest("Forward", "GoJavas", "Haryana", "Panipat")); // should be 14
        System.out.println(trie.getClosest("Forward", "GoJavas", "Maharashtra", "Mumbai")); // should be 15
        System.out.println(trie.getClosest("Forward", "Delhivery", "Maharashtra", "Mumbai")); // should be 18
        System.out.println(trie.getClosest("Backward", "Delhivery", "Maharashtra", "Mumbai")); // should be null

        System.out.println("Testing for exact matches ------------------------------");
        System.out.println(trie.get("Forward", "GoJavas", "Bihar", "Patna")); // should be 10
        System.out.println(trie.get("Forward", "GoJavas", "Bihar", "Madhepura")); // should be 11
        System.out.println(trie.get("Forward", "GoJavas", "Bihar", "Muzaffarpur")); // should be null
        System.out.println(trie.get("Forward", "GoJavas", "Bihar", "Bhagalpur")); // should be null
        System.out.println(trie.get("Forward", "GoJavas", "Haryana", "Gurgaon")); // should be 13
        System.out.println(trie.get("Forward", "GoJavas", "Haryana", "Panipat")); // should be null
        System.out.println(trie.get("Forward", "GoJavas", "Maharashtra", "Mumbai")); // should be null
        System.out.println(trie.get("Forward", "Delhivery", "Maharashtra", "Mumbai")); // should be null
        System.out.println(trie.get("Backward", "Delhivery", "Maharashtra", "Mumbai")); // should be null
    }
    */
}
