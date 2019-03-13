package wiley.test;

import org.junit.jupiter.api.Test;
import wiley.test.impl.lfu.LfuCache;

import java.util.function.Function;

import static org.junit.jupiter.api.Assertions.*;

class LfuCacheTest {

    @Test
    void testLfuStrategy() {
        LfuCache<String, String> cache = new LfuCache<>(3);
        cache.get("1", key -> "1");
        cache.get("2", key -> "2");
        cache.get("3", key -> "3");
        cache.get("3");
        cache.get("3");
        cache.get("3");
        cache.get("2");
        cache.get("1");
        cache.get("1");
        // access to 3 -- 3 times, 2 - 1, 1 - 2
        cache.get("4", key -> "4");
        cache.get("5", key -> "5");
        cache.get("6", key -> "6");
        assertTrue(cache.get("1").isPresent());
        assertTrue(cache.get("6").isPresent());
        assertTrue(cache.get("3").isPresent());
        assertFalse(cache.get("5").isPresent());
        assertFalse(cache.get("2").isPresent());
    }

    @Test
    void testMaxSizeRestriction() {
        LfuCache<String, String> cache = new LfuCache<>(3);
        cache.get("1", key -> "1");
        cache.get("2", key -> "2");
        cache.get("3", key -> "3");
        cache.get("4", key -> "4");
        assertEquals(cache.size(), 3);
    }


    @Test
    void testCleanUp() {
        LfuCache<String, String> cache = new LfuCache<>(3);
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
        LfuCache<String, String> cache = new LfuCache<>(3);
        cache.get("1", key -> "1");
        cache.get("2", key -> "2");
        cache.get("3", key -> "3");
        assertTrue(cache.get("1").isPresent());
        cache.invalidate("1");
        assertFalse(cache.get("1").isPresent());
    }


    @Test
    void testLoader() {
        LfuCache<String, String> cache = new LfuCache<>(3);
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