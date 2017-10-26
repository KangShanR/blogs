---
title: java集合框架之HashMap
date: 2017-04-13 15:02:43
categories: programming
tags: [programming,java]
description: java集合框架中的HashMap
---
### Map
- map接口：是一种将键对象和值对象进行关联的容器，而且值对象可以是另一个Map，这样类推下去可以形成多级映射；
- Map中键对象不允许重复，且键的唯一性很重要；
- 不建议将多个键映射到同一个值对象；
- Map接口下有的实现集合类有：
	- HashMap，用到了哈希码的算法，以便快速查找一个键；
	- TreeMap，其键按序存放
	- HashTable，是Dictionary的子类，与HashMap类似；