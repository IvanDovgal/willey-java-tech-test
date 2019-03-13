package wiley.test.impl;

import wiley.test.Cache;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;

public abstract class CacheImpl<K, V> implements Cache<K, V> {

    protected final long maxSize;
    protected final Map<K, V> cacheData = new HashMap<>();

    public CacheImpl(long maxSize) {
        this.maxSize = maxSize;
    }

    @Override
    public boolean contains(K key) {
        return cacheData.containsKey(key);
    }

    @Override
    public void invalidate(K key) {
        performInvalidate(key);
        cacheData.remove(key);
    }


    public Optional<V> get(K key, boolean performGet) {
        if(performGet)
            performGet(key);
        if (contains(key))
            return Optional.of(cacheData.get(key));
        return Optional.empty();
    }

    @Override
    public Optional<V> get(K key) {
        return get(key, true);
    }

    protected abstract Collection<K> discardKeys(long count);

    protected abstract void performCleanUp();

    protected abstract void performInvalidate(K key);

    protected abstract void performGet(K key);

    protected abstract void performPut(K key);

    @Override
    public V get(K key, Function<K, V> loader) {
        Optional<V> cachedValue = get(key, false);
        if(cachedValue.isPresent()) {
            performGet(key);
            return cachedValue.get();
        } else {
            V value = loader.apply(key);
            put(key, value);
            return value;
        }
    }

    private void put(K key, V value, boolean performPut) {
        long over = Math.max(cacheData.size() - maxSize + 1, 0);
        if (over > 0)
            discardKeys(over)
                    .forEach(cacheData::remove);
        if(performPut)
            performPut(key);
        cacheData.put(key, value);
    }

    @Override
    public void put(K key, V value) {
       put(key, value, true);
    }

    @Override
    public void cleanUp() {
        performCleanUp();
        cacheData.clear();
    }

    @Override
    public long size() {
        return cacheData.size();
    }

}
