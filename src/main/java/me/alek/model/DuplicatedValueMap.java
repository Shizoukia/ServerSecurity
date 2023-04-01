package me.alek.model;

import lombok.Getter;
import lombok.Setter;

import java.util.Map.Entry;
import java.util.*;

public class DuplicatedValueMap<K, V extends Comparable<V>> {

    @Getter
    private final Map<K, List<V>> map = new HashMap<>();
    private final List<Map.Entry<K, V>> pulledEntrySet = new ArrayList<>();


    public void put(K k, V v) {
        if (map.containsKey(k)) {
            map.get(k).add(v);
        } else {
            List<V> array = new ArrayList<>();
            array.add(v);
            map.put(k, array);
        }
    }

    public List<V> get(K k) {
        return map.get(k);
    }

    public V get(K k, int i) {
        if (map.get(k).size()-1 < i) {
            return null;
        } else {
            return map.get(k).get(i);
        }
    }

    public Set<Map.Entry<K, List<V>>> entrySet() {
        return map.entrySet();
    }

    public double size() {
        double size = 0;
        for (Map.Entry<K, List<V>> entry : map.entrySet()) {
            size += entry.getValue().size();
        }
        return size;
    }

    public List<Map.Entry<K, V>> getPulledEntries() {
        pulledEntrySet.clear();
        map.forEach((key, value) -> {
            for (V innerEntryValue : value) {
                pulledEntrySet.add(new AbstractMap.SimpleEntry<>(key, innerEntryValue));
            }
        });
        return pulledEntrySet;
    }

}
