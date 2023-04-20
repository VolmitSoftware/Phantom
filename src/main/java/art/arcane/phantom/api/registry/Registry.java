package art.arcane.phantom.api.registry;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class Registry<K, V> {
    private final Map<K, V> lookup;
    private final Map<V, K> inverseLookup;

    public Registry() {
        lookup = new ConcurrentHashMap<>();
        inverseLookup = new ConcurrentHashMap<>();
    }

    public void register(K k, V v) {
        lookup.put(k, v);
        inverseLookup.put(v, k);
    }

    public int size() {
        return lookup.size();
    }

    public Set<K> keys() {
        return new HashSet<>(lookup.keySet());
    }

    public Set<V> values() {
        return new HashSet<>(lookup.values());
    }

    public V get(K k) {
        return lookup.get(k);
    }

    public K keyFor(V v) {
        return inverseLookup.get(v);
    }
}
