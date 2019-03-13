package wiley.test.impl.lfu;

import wiley.test.impl.CacheImpl;
import wiley.test.impl.PriorityKey;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class LfuCache<K, V> extends CacheImpl<K, V> {

    private final PriorityQueue<PriorityKey<K>> keyFrequencyQueue;
    private final HashMap<K, PriorityKey<K>> keyPriorityMap;

    private void increasePriority(K key) {
        if(keyPriorityMap.containsKey(key)) {
            PriorityKey<K> priorityKey = keyPriorityMap.get(key);
            keyFrequencyQueue.remove(priorityKey);
            priorityKey = priorityKey.withPriority(priorityKey.getPriority() + 1);
            keyFrequencyQueue.add(priorityKey);
            keyPriorityMap.put(key, priorityKey);
        } else {
            PriorityKey<K> priorityKey = new PriorityKey<>(key, 1);
            keyFrequencyQueue.add(priorityKey);
            keyPriorityMap.put(key, priorityKey);
        }
    }

    private K pollKey() {
        PriorityKey<K> priorityKey = keyFrequencyQueue.poll();
        keyPriorityMap.remove(priorityKey.getKey());
        return priorityKey.getKey();
    }

    public LfuCache(long maxSize) {
        super(maxSize);
        keyFrequencyQueue = new PriorityQueue<PriorityKey<K>>((int) maxSize, Comparator.comparingInt(PriorityKey::getPriority));
        keyPriorityMap = new HashMap<K, PriorityKey<K>>((int) maxSize);
    }

    @Override
    protected Collection<K> discardKeys(long count) {
        return Stream
                .generate(this::pollKey)
                .limit(count)
                .collect(Collectors.toList());
    }

    @Override
    protected void performCleanUp() {
        keyFrequencyQueue.clear();
        keyPriorityMap.clear();
    }

    @Override
    protected void performInvalidate(K key) {
        keyFrequencyQueue.remove(keyPriorityMap.remove(key));
    }

    @Override
    protected void performGet(K key) {
        increasePriority(key);
    }

    @Override
    protected void performPut(K key) {
        increasePriority(key);
    }
}
