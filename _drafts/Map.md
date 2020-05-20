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
    - HashTable，是Dictionary的子类，与HashMap类似；

## HashMap

- HashMap 中，其 hash 算法是在引用 Object 的 hash 方法后再对进行了额外的位运算： 

```java
static final int hash(Object key) {
    int h;
    return (key == null) ? 0 : (h = key.hashCode()) ^ (h >>> 16);
}
```

- 如此做法，
