package wiley.test;

import java.util.Optional;
import java.util.function.Function;

public interface Cache<K, V> {
    boolean contains(K key);
    void invalidate(K key);
    V get(K key, Function<K, V> loader);
    Optional<V> get(K key);
    void put(K key, V value);
    void cleanUp();
    long size();

    static <K, V> CacheBuilder<K, V> builder() {
        return new CacheBuilder<>();
    }

}
