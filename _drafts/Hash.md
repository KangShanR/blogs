---
date: 2017-04-13 15:02:43
categories: programming
tags: [programming,java, HashMap]
description: hash function && hash table
---

# Hash

Hash 算法应用在 Java 集合框架。其中 HashTable 基本实现数据结构的 HashTable 。HashMap 的底层有 HashTable 同时也有红黑树。HashSet 的内部就是一个HashMap 。

## Map

- map接口：是一种将键对象和值对象进行关联的容器，而且值对象可以是另一个Map，这样类推下去可以形成多级映射；
- Map中键对象不允许重复，且键的唯一性很重要；
- 不建议将多个键映射到同一个值对象；
- Map接口下有的实现集合类有：
    - HashMap，用到了哈希码的算法，以便快速查找一个键；
    - TreeMap，其键按序存放
    - HashTable，是 Dictionary 的子类，与 HashMap 类似；

## HashMap

[reference](https://www.jianshu.com/p/c658df4f4c77)

- HashMap 中，其 hash 算法是在引用 Object 的 hash 方法后再对进行了额外的位运算：

```java
/**
* Computes key.hashCode() and spreads (XORs) higher bits of hash
* to lower.  Because the table uses power-of-two masking, sets of
* hashes that vary only in bits above the current mask will
* always collide. (Among known examples are sets of Float keys
* holding consecutive whole numbers in small tables.)  So we
* apply a transform that spreads the impact of higher bits
* downward. There is a tradeoff between speed, utility, and
* quality of bit-spreading. Because many common sets of hashes
* are already reasonably distributed (so don't benefit from
* spreading), and because we use trees to handle large sets of
* collisions in bins, we just XOR some shifted bits in the
* cheapest possible way to reduce systematic lossage, as well as
* to incorporate impact of the highest bits that would otherwise
* never be used in index calculations because of table bounds.
*/
static final int hash(Object key) {
    int h;
    return (key == null) ? 0 : (h = key.hashCode()) ^ (h >>> 16);
}
```

- 如此做法：将高 16 位的 hashCode 与 hashCode 低 16 位进行 XOR (异或) 位运算。这样能保证结果高 16 位为 0 ，而低 16 位还不明白是如何保证 hashCode 不会冲突的。
- HashTable 中直接对 HashCode 按 capacity 取模，而 HashMap 中其 threshold 并不单指总 Entry 数量阈值，在其初始化时指下一次 resize() capacity 的目标值。
- HashTable 严格按时 HashTable 数据结构来定义，使用 Object hashCode() 计算其 hash 值，并按桶数量取模放置各个值入 HashTable 中。而在 HashMap 中，其对 Key 的 hash 值进入了高 16 位与低 16 位的或运算作为最终 hash 值，同时，在放置 Node 时，也并不是直接取模，使用 `hash & (capacity - 1)` 得到 bucket 下标。
    - 之所以能使用此种位运算获取到 bucket 下标，因为其桶数量 capacity 始终是 2 的次冥 （`10000` 的形式），`(capacity - 1)` 就会是 `1111` 的形式，而其与 hash 值进行 `&` 运算就刚好得到 hash 的模。
    - 为了保持其 capacity 为 2 的多次冥的形式，HashMap 中使用方法专门做些事：

    ```java
    /**
     * Returns a power of two size for the given target capacity.
     */
    static final int tableSizeFor(int cap) {
        int n = cap - 1;
        n |= n >>> 1;
        n |= n >>> 2;
        n |= n >>> 4;
        n |= n >>> 8;
        n |= n >>> 16;
        return (n < 0) ? 1 : (n >= MAXIMUM_CAPACITY) ? MAXIMUM_CAPACITY : n + 1;
    }
    ```

## HashTable 与 HashMap 的区别

- 底层基本与 HashMap 一致，初始都是使用 HashTable 算法，使用链表来解决 Hash Collision 。但 HashMap 更高明的算法在于，其中链表长度超过阈值 8 就链表会转为红黑树，这也避免了整个 HashTable 退化成一个 链表。
- HashTable 为同步的，可以保证一定的线程安全。
