---
title: Key Words in Java
date: 2020-10-12 18:52:00
categories: Java
tags: [programming,java, HashMap]
description: key words
---

# java key words

## transient【瞬时】

用以序列化、反序列化时所排除的变量，在进行序列化、反序列化时 transient 修辞的变量将不被序列化、反序列化。

- 与 final 连用，final 修辞字段值会直接被序列化，因此 transient 修辞 final 字段无效
- 与 static 连用，static 字段非对象所拥有，所以 transient 修辞的 static 字段无效。<!--more-->

## volatile

[reference](http://tutorials.jenkov.com/java-concurrency/volatile.html)

- volatile 用于将变量放置于主存而非 CPU 缓存，以避免多核多线程中读取变量产生可见性问题。

### volatile 可见性保证原则

可见性指在线程间变量被读写的正确性， java volatile 原则不仅保证 volatile 变量本身，也保证其相关变量：

- 对于一个线程A写 volatile 变量随后线程 B 读取 volatile 变量的场景中，所有在线程A 写 volatile 变量前的变量对线程 A 都具可见性，同时，在线程 B 读取 volatile 变量后所有的变量对线程B 具可见性。
- 线程A在读取一个 volatile 变量时，所有的变量都对线程 A 保持可见性也可以从主存中生读。

### 指令重排

JVM 与 CPU 为提升性能而允许指令重排。*使用指令重排可以将同一个变量的声明与赋值放在一起而提升性能？*

指令重排带来的问题就是打乱了 volatile 可见性原则。

### volatile happen-before 原则

为保证指令重排不对 volatile 可见性干预，volatile 有 Happens-Before 原则。
