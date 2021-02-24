---
layout: "post"
title: Lock Condition
date: 2021-02-05 12:08:38
tags: [Java, Lock] 
categories: Java
description: Lock Condition
---

# Condition

> Java 新式锁所用以判定条件接口<!--more-->

## Translation

> Condition 源码文档翻译

- Condition 区别于 Object monitor 方法（wait, notify, notifyAll）：联合任意 Lock 实现，对于第个 object 提供多个等待区（wait-sets）。Lock 代替 synchronized 方法语句，Condition 代替 Object monitor 方法。
- Condition （也叫条件变量 condition variables、条件队列 condition queues）提供了一种挂起一个线程执行的方式，线程挂起直到另一个线程通过在某状态为 true 时通知为止。因为这种共享的状态信息发生在不同的线程中，所以其必须被保护，一个某种形式的 lock 与 condition 相关联。等待一个 condition 提供的关系属性是其原子地释放其关联的 lock 并挂起当前线程，就像 [Object.wait()](./Thread.md#wait()) 。
- Condition 实例与 Lock 实例对象严格绑定的，要获取指定 Lock 的 Condition 实例对象，调用方法 Lock 实例的 newCondition() 方法。
- 一个 Condition 可以实现与 Object monitor 方法不一样的行为、语义，比如：通知的顺序保证、执行通知时不需要持有 lock 。如果某个 Condition 实现提供了某项指定语义，需要在其文档中加以说明。
- 需要注意的是, Condition 对象与其他普通对象无异，同样可以被用于 synchronized 语句中，同样可以调用其 monitor （wait/notification）方法。使用 Condition 实例作为锁，与 Condition 关联的 Lock 对象与调用 Condition 的 signaling(),waiting() 方法并没有特殊关系。但建议不要用 Condition 实例当作 synchronized 锁对象，以避免歧义。
