---
date: 2020-06-11 16:21:38
categories: java
tags: [lock,concurrent]
keywords: lock
---

# Lock in Java

java 并发编程中的 lock.

[reference](java.util.concurrent.locks.Lock)

## features

- Lock 的限制访问共有资源，但某些 Lock 也可以让多个线程同时访问共享资源，如：{@link ReadWriteLock}。
- Lock 提供了 synchronized 方法、语句同样的功能，但要更为灵活。synchronized 提供访问每一个对象隐式关联的监听锁，但其强制锁的获取与释放都使用阻塞结构方式：
    - 多个锁被获取后，其释放的顺序必须与获取的顺序相反；
    - 所有锁在释放时必须与其获取的语义范围一致。
- synchronized 范围机制（scoping mechanism）使获取监听锁编程方式更为容易，也有益于避免包含锁的编程错误。但在某些情况下需要使用更为灵活的锁，比如在并发遍历数据结构时需要交替进行或链式锁定：
    1. 在获取节点 A 的锁
    2. 获取节点 B 的锁；
    3. 释放节点 A 的锁再获取节点 C 的锁；
    4. 释放节点 B 的锁再获取节点 D 的锁，依次类推。
    5. 通过允许一个锁在不同的范围内获取与释放，与多个锁以任意顺序获取与释放实现以上功能。
- 灵活性带来了额外的工作，缺少阻塞结构也就移除了在 synchronized 语句或方法中自动释放锁的功能。

```java
Lock l = ...;
l.lock();
try {
  // access the resource protected by this lock
} finally {
  l.unlock();
}
 ```
