package wiley.test.impl;

import java.util.Objects;

public class PriorityKey<K> {
    private final K key;
    private final int priority;

    public K getKey() {
        return key;
    }

    public int getPriority() {
        return priority;
    }

    public PriorityKey(K key, int priority) {
        this.key = key;
        this.priority = priority;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PriorityKey<?> that = (PriorityKey<?>) o;
        return priority == that.priority &&
                Objects.equals(key, that.key);
    }

    @Override
    public int hashCode() {
        return Objects.hash(key, priority);
    }

    public PriorityKey<K> withPriority(int priority) {
        return new PriorityKey<>(key, priority);
    }
}
