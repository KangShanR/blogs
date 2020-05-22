---
title: java 集合框架之 Map
date: 2017-04-13 15:02:43
categories: programming
tags: [programming,java, HashMap]
description: java 集合框架中的 Map
---

# Map

- map接口：是一种将键对象和值对象进行关联的容器，而且值对象可以是另一个Map，这样类推下去可以形成多级映射；
- Map中键对象不允许重复，且键的唯一性很重要；
- 不建议将多个键映射到同一个值对象；
- Map接口下有的实现集合类有：
    - HashMap，用到了哈希码的算法，以便快速查找一个键；
    - TreeMap，其键按序存放
    - HashTable，是 Dictionary 的子类，与 HashMap 类似；

## HashMap

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

### HashTable 与 HashMap 不同之处

- hashtable 中直接对 hashcode 按 capacity 取模，而 hashMap 中其 threshold 并不单指总 Entry 数量阈值，在其初始化时指下一次 resize() capacity 的目标值。
- HashTable 严格按时 HashTable 数据结构来定义，使用 Object hashCode() 计算其 hash 值，并按桶数量取模放置各个值入 HashTable 中。而在 HashMap 中，其对 Key 的 hash 值进入了高 16 位与低 16 位的或运算作为最终 hash 值，同时，在放置 Node 时，也并不是直接取模
