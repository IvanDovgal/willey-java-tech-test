package wiley.test.impl.lru;

import wiley.test.impl.CacheImpl;
import wiley.test.impl.PriorityKey;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class LruCache<K, V> extends CacheImpl<K, V> {

    private final PriorityQueue<PriorityKey<K>> lru;
    private int currentTime = Integer.MAX_VALUE;

    public LruCache(long maxSize) {
        super(maxSize);
        lru = new PriorityQueue<>((int) maxSize, Comparator.comparingInt(PriorityKey::getPriority));
    }

    @Override
    protected Collection<K> discardKeys(long count) {
        return Stream.generate(lru::poll)
                .limit(count)
                .map(PriorityKey::getKey)
                .collect(Collectors.toList());
    }

    @Override
    protected void performCleanUp() {
        currentTime = 0;
        lru.clear();
    }

    @Override
    protected void performInvalidate(K key) {
        currentTime--;
        lru.remove(key);
    }

    @Override
    protected void performGet(K key) {
        currentTime--;
        if (lru.contains(key)) {
            lru.add(new PriorityKey<>(key, currentTime));
        }
    }

    @Override
    protected void performPut(K key) {
        currentTime--;
        if(!lru.contains(key)) {
            lru.add(new PriorityKey<>(key, currentTime));
        }
    }
}
