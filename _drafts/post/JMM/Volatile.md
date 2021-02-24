---
layout: "post"
title: Volatile
date: 2020-04-21 12:08:38
tags: [java,volatile]
categories: Java
description: java keyword `volatile`
---

# 1. Java Volatile

java 中关键字 volatile 的使用 [reference](https://mp.weixin.qq.com/s/AE0oeKiCU_aetfkW9Qk0cg)<!--more-->

- volatile 的做法：
    - 使用内存屏障让前后的指令重排（为提升效率，而在编译运行期对指令进行重排）都不干扰所修辞的变量；
    - 单个线程里对共享变量的更新强制刷新到主内存中，而使其他线程中本地内存中的共享变量失效。
    - 不保证原子性，
        - 保证原子性：使用 `synchronize`
        - 使用 `lock`
        - 前两种太重，可使用 Atomic 包，其使用 CAS 循环实现原子操作。

## 1.1. Volatile in Double Check Singleton

双检锁单例中的共享变量需要加上 `volatile` 才能真正做到正确，否则在多线程情况下出现问题。

单例模式

```java
private Singleton() {}

private static Singleton ins;

public static Singleton getInstance() {
    if (ins == null) {
        synchronized (Singleton.class) {
            if (ins == null) {
                ins = new Singleton();
            }
        }
    }
    return ins;
}
```

- 当 `int = new Instance()` 这一行代码并非原子性，其指定分为 3 个 :
    - a. memory = allocate() 分配内存；
    - b. ctorInstance(memory) 初始化对象；
    - c. ins=memory 设置 ins 指向刚分配的地址。
- a -> b -> c 三条指令可能会被重排 a -> c -> b 的顺序。在双检锁的案例中，当线程1走到 c 时，其他线程走到第一层检查 ins 是否为空时，会得到 false 的结果，因为此时 ins 已指向了一个内存地址，而线程1 此时还未为其初始化，而出现 bug 。
