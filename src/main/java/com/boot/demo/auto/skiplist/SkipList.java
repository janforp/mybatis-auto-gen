package com.boot.demo.auto.skiplist;

import java.util.Random;

/**
 * 跳跃表
 *
 * @author zhucj
 * @see <a href="https://blog.csdn.net/lujianhao_ios/article/details/108372005>跳跃表</a>
 * @since 20230824
 */
public class SkipList<T> {

    // 节点数量
    private int n;

    // 节点最大层数
    private int h;

    // 第一个节点
    private final SkipListEntry<T> head;

    // 最后一个节点
    private final SkipListEntry<T> tail;

    /**
     * Random 类的实例对象 r 用来决定新添加的节点是否能够向更高一层的链表攀升。
     */
    private final Random r;

    public SkipList() {
        // 创建 head 节点
        this.head = new SkipListEntry<>(Integer.MIN_VALUE, null);
        // 创建 tail 节点
        this.tail = new SkipListEntry<>(Integer.MAX_VALUE, null);

        // 将 head 节点的右指针指向 tail 节点
        this.head.right = tail;
        // 将 tail 节点的左指针指向 head 节点
        this.tail.left = head;

        this.h = 0;
        this.n = 0;
        this.r = new Random();
    }

    public T get(Integer key) {
        SkipListEntry<T> p = findEntry(key);
        if (p.key.equals(key)) {
            return p.value;
        } else {
            return null;
        }
    }

    public T put(Integer key, T value) {

        SkipListEntry<T> p;
        SkipListEntry<T> q;
        int i = 0;

        // 查找适合插入的位子
        p = findEntry(key);

        // 如果跳跃表中存在含有key值的节点，则进行value的修改操作即可完成
        if (p.key.equals(key)) {
            T oldValue = p.value;
            p.value = value;
            return oldValue;
        }

        // 如果跳跃表中不存在含有key值的节点，则进行新增操作
        q = new SkipListEntry<>(key, value);
        q.left = p;
        q.right = p.right;
        p.right.left = q;
        p.right = q;

        // 再使用随机数决定是否要向更高level攀升
        while (r.nextDouble() < 0.5) {
            // 如果新元素的级别已经达到跳跃表的最大高度，则新建空白层
            if (i >= h) {
                //  TODO addEmptyLevel();
            }
            // 从p向左扫描含有高层节点的节点
            while (p.up == null) {
                p = p.left;
            }
            p = p.up;

            // 新增和q指针指向的节点含有相同key值的节点对象
            // 这里需要注意的是除底层节点之外的节点对象是不需要value值的
            SkipListEntry<T> z = new SkipListEntry<>(key, null);

            z.left = p;
            z.right = p.right;
            p.right.left = z;
            p.right = z;

            z.down = q;
            q.up = z;

            q = z;
            i = i + 1;
        }

        n = n + 1;

        // 返回null，没有旧节点的value值
        return null;
    }

    public T remove(Integer key) {
        SkipListEntry<T> p;
        SkipListEntry<T> q;

        p = findEntry(key);

        if (!p.key.equals(key)) {
            return null;
        }

        T oldValue = p.value;
        while (p != null) {
            q = p.up;
            p.left.right = p.right;
            p.right.left = p.left;
            p = q;
        }
        return oldValue;
    }

    /**
     * 注意以下几点：
     *
     * 如果传入的 key 值在跳跃表中存在，则 findEntry 返回该对象的底层节点；
     * 如果传入的 key 值在跳跃表中不存在，则 findEntry 返回跳跃表中 key 值小于 key，并且 key 值相差最小的底层节点；
     *
     * @param key 要查询的key
     * @return 要查询的节点
     */
    private SkipListEntry<T> findEntry(Integer key) {
        // 从head头节点开始查找
        SkipListEntry<T> p = head;

        while (true) {
            // 从左向右查找，直到右节点的key值大于要查找的key值
            while (p.right.key/* 右节点的key */ <= key) {
                p = p.right;
            }

            // 如果有更低层的节点，则向低层移动
            if (p.down != null) {
                p = p.down;
            } else {
                break;
            }
        }

        // 返回p，！注意这里p的key值是小于等于传入key的值的（p.key <= key）
        return p;
    }

    private static class SkipListEntry<T> {

        // data
        private final Integer key;

        private T value;

        // links
        private SkipListEntry<T> up;

        private SkipListEntry<T> down;

        private SkipListEntry<T> left;

        private SkipListEntry<T> right;

        // constructor
        public SkipListEntry(Integer key, T value) {
            this.key = key;
            this.value = value;
        }
    }
}