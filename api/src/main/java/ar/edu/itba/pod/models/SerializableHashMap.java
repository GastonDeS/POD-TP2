package ar.edu.itba.pod.models;

import ar.edu.itba.pod.interfaces.SerializableMap;

import java.util.HashMap;

public class SerializableHashMap<K, V> extends HashMap<K, V> implements SerializableMap<K, V> {
    public SerializableHashMap() {
        super();
    }
}
