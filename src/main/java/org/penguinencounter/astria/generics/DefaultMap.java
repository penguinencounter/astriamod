package org.penguinencounter.astria.generics;

import java.util.HashMap;
import java.util.Map;

public class DefaultMap<K, V> extends HashMap<K, V> {
    private final V defaultValue;

    // new constructors...
    public DefaultMap(int initialCapacity, float loadFactor, V defaultValue) {
        super(initialCapacity, loadFactor);
        this.defaultValue = defaultValue;
    }

    public DefaultMap(int initialCapacity, V defaultValue) {
        super(initialCapacity);
        this.defaultValue = defaultValue;
    }

    public DefaultMap(V defaultValue) {
        super();
        this.defaultValue = defaultValue;
    }

    public DefaultMap(Map<? extends K, ? extends V> m, V defaultValue) {
        super(m);
        this.defaultValue = defaultValue;
    }

    @Override
    public V get(Object key) {
        return getOrDefault(key, defaultValue);
    }
}
