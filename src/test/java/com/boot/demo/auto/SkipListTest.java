package com.boot.demo.auto;

import com.boot.demo.auto.skiplist.SkipList;
import org.junit.Assert;
import org.junit.Test;

/**
 * SkipListTest
 *
 * @author zhucj
 * @since 20230824
 */
public class SkipListTest {

    @Test
    public void testSkipList() {
        SkipList<Long> list = new SkipList<>();
        list.put(1, 100L);
        list.put(2, 100L);
        list.put(3, 100L);

        Long v1 = list.get(1);
        Assert.assertEquals(100L, v1.longValue());
    }
}