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
  
- 此外，可使用 tryLock() ，此方法获取锁可以被打断，使用方法 lockInterruptibly() 打断获取锁的流程。也可以使用 tryLock(long, TimeUnit) 方法实现超时限制获取锁。
- Lock 可以实现与隐式监听锁很不一样的行为与语义，诸如顺序保证、非重入使用、死锁检测，如果 Lock 要实现这此语义，需要文档中特别说明。
- 需要注意的是 Lock 实例与其他对象实例一样可以作为 synchronized 语句的锁对象，使用 Lock 实例作为 synchronized 语句的锁目标与 Lock 实例本身的 lock() 方法调用没有任何特别的关系。除在 Lock 本身内部实现外，建议尽量避免如此使用。

### 内存同步

- 所有 Lock 实现必须强制像内置的监听锁样实现内存同步语义，[Java SE 文档描述](https://docs.oracle.com/javase/specs/jls/se7/html/jls-17.html#jls-17.4)
- 成功的 lock 与 unlock 执行必须与锁与解锁动作语义一样实现内存同步效果。

### 实现注意事项

- 锁获取有三种不同的形式，可中断、不可中断、超时设置（interruptible/non-interruptible/timed），其性能特质、顺序保证也各不相同。此外，中断一个正在获取锁的特性可能不会实现。
- 因此一个 Lock 的实现中对于三种形式的锁获取并不需要保证都有明确定义相同的语义与规范，也可以不需要在锁获取进行时中断的属性。

## TODO list

- [ ] [ReentrantLock](java.util.concurrent.locks.ReentrantLock)
- [ ] [ReadWriteLock](java.util.concurrent.locks.ReadWriteLock)
- [ ] [ReentrantReadWriteLock](java.util.concurrent.locks.ReentrantReadWriteLock)
- [ ] [JSE 8 specifications](https://docs.oracle.com/javase/specs/index.html)
