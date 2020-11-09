package com.gmail.burinigor7.map;

import java.util.*;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Function;

public class Hashmap<K,V> implements Map<K,V> {

    private static final int DEFAULT_INITIAL_CAPACITY = 16;

    private static final float DEFAULT_LOAD_FACTOR = 0.75f;

    private static class Node<K, V> {
        final int hash;
        final K key;
        V value;
        Node<K, V> next;

        public Node(K key, V value) {
            this.key = key;
            this.value = value;
            this.hash = hash(key);
        }

        @Override
        public String toString() {
            return key + "=" + value;
        }
    }

    private static int hash(Object key) {
        if(key == null) return 0;
        return key.hashCode();
    }

    private Node<K, V>[] table;

    private float loadFactor = DEFAULT_LOAD_FACTOR;

    private int size;

    public Hashmap() {
        table = (Node<K, V>[]) new Node[DEFAULT_INITIAL_CAPACITY];
    }

    public Hashmap(int initialCapacity) {
        int n = 1;
        while (n < initialCapacity) n*=2;
        table = (Node<K, V>[]) new Node[n];
    }

    public Hashmap(float loadFactor) {
        table = (Node<K, V>[]) new Node[DEFAULT_INITIAL_CAPACITY];
        this.loadFactor = loadFactor;
    }

    public Hashmap(int initialCapacity, float loadFactor) {
        int n = 1;
        while (n < initialCapacity) n*=2;
        table = (Node<K, V>[]) new Node[n];
        this.loadFactor = loadFactor;
    }

    @Override
    public V getOrDefault(Object key, V defaultValue) {
        Node<K, V> node = getNode(key);
        return node != null ? node.value : defaultValue;
    }

    @Override
    public void forEach(BiConsumer<? super K, ? super V> action) {

    }

    @Override
    public void replaceAll(BiFunction<? super K, ? super V, ? extends V> function) {

    }

    @Override
    public V putIfAbsent(K key, V value) {
        return null;
    }

    @Override
    public boolean remove(Object key, Object value) {
        Node<K, V> node = getNode(key);
        if(node!= null && node.value.equals(value)) {
            remove(key);
            return true;
        }
        return false;
    }

    @Override
    public boolean replace(K key, V oldValue, V newValue) {
        Node<K,V> node = getNode(key);
        if(node != null && oldValue.equals(node.value)) {
            node.value = newValue;
            return true;
        }
        return false;
    }

    @Override
    public V replace(K key, V value) {
        Node<K,V> node = getNode(key);
        if(node != null) {
            V prevValue = node.value;
            node.value = value;
            return prevValue;
        }
        return null;
    }

    @Override
    public V computeIfAbsent(K key, Function<? super K, ? extends V> mappingFunction) {
        return null;
    }

    @Override
    public V computeIfPresent(K key, BiFunction<? super K, ? super V, ? extends V> remappingFunction) {
        return null;
    }

    @Override
    public V compute(K key, BiFunction<? super K, ? super V, ? extends V> remappingFunction) {
        return null;
    }

    @Override
    public V merge(K key, V value, BiFunction<? super V, ? super V, ? extends V> remappingFunction) {
        return null;
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    @Override
    public boolean containsKey(Object key) {
        return getNode(key) != null;
    }

    @Override
    public boolean containsValue(Object value) {
        for(Node<K, V> node : table) {
            while (node != null) {
                if(node.value.equals(value))
                    return true;
                node = node.next;
            }
        }
        return false;
    }

    @Override
    public V get(Object key) {
        Node<K, V> node = getNode(key);
        return node == null ? null : node.value;
    }

    @Override
    public V put(K key, V value) {
        V prev = null;
        int idx = index(key);
        Node<K, V> node = table[idx];
        if(node == null) return firstForBucket(key, value);
        else {
            while(true) {
                if(node.key.equals(key)) {
                    prev = node.value;
                    node.value = value;
                    break;
                }
                if(node.next == null) {
                    if((float)++size / table.length > loadFactor) rehash();
                    put(new Node<>(key, value));
                    ++size;
                    break;
                }
                node = node.next;
            }
        }
        return prev;
    }

    @Override
    public V remove(Object key) {
        Node<K, V> target = getNode(key);
        if (target != null) {
            Node<K, V> prev = null;
            Node<K, V> current = table[index(key)];
            while (true) {
                if(current.key.equals(target.key)) {
                    V value = current.value;
                    if(prev != null) prev.next = current.next;
                    else table[index(key)] = null;
                    --size;
                    return value;
                }
                if(current.next == null) return null;
                Node<K, V> tmp = current;
                prev = current;
                current = tmp.next;
            }
        }
        return null;
    }

    @Override
    public void putAll(Map<? extends K, ? extends V> m) {

    }

    @Override
    public void clear() {
        int capacity = table.length;
        table = (Node<K, V>[])new Node[capacity];
    }

    @Override
    public Set<K> keySet() {
        return null;
    }

    @Override
    public Collection<V> values() {
        return null;
    }

    @Override
    public Set<Entry<K, V>> entrySet() {
        return null;
    }

    private int index(Object key) {
        return hash(key) % table.length;
    }

    private V firstForBucket(K key, V value) {
        if((float)++size / table.length > loadFactor) rehash();
        put(new Node<>(key, value));
        return null;
    }

    private void rehash() {
        Node<K,V>[] oldTable = table;
        table = (Node<K,V>[]) new Node[oldTable.length * 2];
        for(Node<K,V> node : oldTable) {
            if(node != null) put(node);
        }
    }

    private void put(Node<K, V> node) {
        int idx = index(node.key);
        if(table[idx] == null) table[idx] = node;
        else while (true) {
            if(table[idx].next == null) {
                table[idx].next = node;
                break;
            }
        }
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        for(Node<K, V> node : table) {
            if(node != null) {
                Node<K, V> n = node;
                while (true) {
                    builder.append(n).append(" --> ");
                    if(n.next == null) {
                        builder.delete(builder.length() - 5, builder.length());
                        builder.append('\n');
                        break;
                    }
                    n = n.next;
                }
            }

        }
        return builder.toString();
    }

    private Node<K, V> getNode(Object key) {
        Node<K, V> node = table[index(key)];
        if(node == null) return null;
        while(true) {
            if(node.key.equals(key)) {
                return node;
            }
            if(node.next == null) {
                return null;
            }
            node = node.next;
        }
    }
}