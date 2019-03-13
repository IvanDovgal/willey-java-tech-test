package wiley.test;

import org.junit.jupiter.api.Test;
import wiley.test.impl.lru.LruCache;

import java.util.function.Function;

import static org.junit.jupiter.api.Assertions.*;

class LruCacheTest {

    @Test
    void testLruStrategy() {
        LruCache<String, String> cache = new LruCache<>(3);
        cache.get("1", key -> "1");
        cache.get("2", key -> "2");
        cache.get("3", key -> "3");
        cache.get("3");
        cache.get("3");
        cache.get("3");
        cache.get("2");
        cache.get("1");
        cache.get("1");
        // order -- 3, 2, 1
        cache.get("4", key -> "4");
        cache.get("5", key -> "5");
        cache.get("6", key -> "6");
        assertTrue(cache.get("1").isPresent());
        assertTrue(cache.get("6").isPresent());
        assertTrue(cache.get("2").isPresent());
        assertFalse(cache.get("5").isPresent());
        assertFalse(cache.get("3").isPresent());
    }

    @Test
    void testMaxSizeRestriction() {
        LruCache<String, String> cache = new LruCache<>(3);
        cache.get("1", key -> "1");
        cache.get("2", key -> "2");
        cache.get("3", key -> "3");
        cache.get("4", key -> "4");
        assertEquals(cache.size(), 3);
    }


    @Test
    void testCleanUp() {
        LruCache<String, String> cache = new LruCache<>(3);
        cache.get("1", key -> "1");
        cache.get("2", key -> "2");
        cache.get("3", key -> "3");
        assertEquals(cache.size(), 3);
        cache.cleanUp();
        assertEquals(cache.size(), 0);
        assertFalse(cache.get("1").isPresent());
    }


    @Test
    void testInvalidate() {
        LruCache<String, String> cache = new LruCache<>(3);
        cache.get("1", key -> "1");
        cache.get("2", key -> "2");
        cache.get("3", key -> "3");
        assertTrue(cache.get("1").isPresent());
        cache.invalidate("1");
        assertFalse(cache.get("1").isPresent());
    }


    @Test
    void testLoader() {
        LruCache<String, String> cache = new LruCache<>(3);
        boolean[] callLoader = { false, false, false };
        cache.get("1", key -> {
            callLoader[0] = true;
            return "1";
        });
        cache.get("2", key -> {
            callLoader[1] = true;
            return "2";
        });
        cache.get("1", key -> {
            callLoader[2] = true;
            return "1";
        });
        assertArrayEquals(callLoader, new boolean[]{ true, true, false });
    }

}