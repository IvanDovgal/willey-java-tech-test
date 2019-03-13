package wiley.test;

import wiley.test.impl.lfu.LfuCache;
import wiley.test.impl.lru.LruCache;

public class CacheBuilder<K, V> {

    public static abstract class StrategyBuilder<K, V> {
        private StrategyBuilder() {

        }

        protected int maxSize;
        public abstract Cache<K, V> build();
        public StrategyBuilder<K, V> setMaxSize(int maxSize) {
            this.maxSize = maxSize;
            return this;
        }
    }

    public static class LruStategyBuilder<K, V> extends StrategyBuilder<K, V> {

        private LruStategyBuilder() {

        }

        @Override
        public Cache<K, V> build() {
            return new LruCache<>(maxSize);
        }

    }

    public static class LfuStategyBuilder<K, V> extends StrategyBuilder<K, V> {

        private LfuStategyBuilder() {

        }

        @Override
        public Cache<K, V> build() {
            return new LfuCache<>(maxSize);
        }

    }

    public StrategyBuilder<K, V> lru() {
        return new LruStategyBuilder<>();
    }

    public StrategyBuilder<K, V> lfu() {
        return new LfuStategyBuilder<>();
    }

}
