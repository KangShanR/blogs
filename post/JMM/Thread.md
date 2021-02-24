---
layout: "post"
title: Java Thread & Java Object
date: 2020-12-28 12:01:00
tags: [java,programming]
categories: [Java]
description: Java Thread & Java Object
---

> Java 线程与对象的基本方法

## .1. Thread

> java thread<!--more-->

### .1.1. join

- join(long timeout) 方法将循环调用 wait(long timeout) 方法，直到该线程(thisThread)死亡。也就是说，当前线程（执行 thisThread.join() 方法的线程)）会加入到该线程的等待区以获取该线程 monitor 。一般来讲，很少有与当前线程一起竞争该线程的锁，也就是会出现循环地获取到 thisThread 的锁，直到 timeout。
- 当 timeout = 0 时，将一直等待 thisThread ，直到 thisThread 死亡。
- 当 thisTread 死亡时，将调用其 notifyAll() 方法，将所有等待区的线程唤醒。
- *整个过程就像，当前线程加入到 thisThread 的生命周期里一样，所以此方法命名为 join 是有一定道理的。*

### .1.2. interrupt

> public void interrupt();

JDK doc:

- 中断线程方法，Java 规范并不保证会立即中断响应，可能会是在处理完特定任务到达某个中断点再中断。对于被中断的线程来说，会被立即设置中断状态。
- 除非线程是中断自己，否则 checkAccess() 方法会被调用以检查是否允许访问（如果中断自己则检查始终会通过），这可能会导致抛出 SecurityException
- 如果线程正被 Object 的 wait 系列方法、Thread 的 join,sleep 系列方法所阻塞，其中断状态会被 interrupt 清除并收到一个 InterruptedException 。
- 如果线程正在 InterruptibleChannel 上进行 I/O 操作而被阻塞，此 channel 将被关闭，同时线程被设置为中断状态并收到 ClosedByInterruptException　。
- 如果线程正阻塞在 java.nio.channels.Selector 线程将被设置中断状态，并立即从 selection 操作中返回，可能会返回一个非零值，与调用 selector.wakeup 类似。
- 除前面说到的三种情形之外，线程都会被设置中断状态。
- 中断线程无实时响应效果。

### .1.3. tips

- [当前线程并不能捕获到其他线程的异常](https://stackoverflow.com/questions/6546193/how-to-catch-an-exception-from-a-thread)。如果需要对异步线程异常控制，使用 Future 或 Thread.UncaughtExceptionHandler；

## .2. Object

> 与线程生命周期相关的锁方法

### .2.1. wait()

> 据 Object 文档翻译

- 调用此方法的前提是线程先对该对象的 monitor 加锁，notify 方法也是一样。
- 调用此方法的线程会先释放该对象的 monitor ，再进入到对象 monitor wait-sets。
- wait(long timeout) 方法让当前线程进入等待，直到有其他的线程调用对象的 notify()/notifyAll() 方法或者指定的时间流逝。
- 当前线程必须拥有对象的监视器 monitor 。
- 此该当让当前线程进入到对象的等待区，并放弃针对此对象的所有同步声明。线程 scheduling purpose 被废弃并进入到到冬眠阶段直到以下四个事件发生为止：
    - 其他线程调用对象的 notify() 方法，并且本线程刚好被选中苏醒。
    - 其他线程调用对象的 notifyAll() 方
    - 其他线程调用本线程的 {@link Thread#interrupt()} 方法打断本线程等待阻塞。
    - 指定的等待超时时间 timeout 已到，或多或少。如果 timeout 为 0 ，那么超时设置将不生效，只能让其他线程来 notify 。
- wait() 方法只会让线程进入到指定对象的等待区，在等待该对象时也可以持有其他对象的同步锁。
- wait() 方法只能被持有对象的 monitor 线程调用。

### .2.2. notify()

> Object 的 notify() 方法，此方法的文档描述了获取对象 monitor 的场景。

- 方法用于唤醒一个等待在该对象 monitor 的线程。
- 在等待区的线程都有可能被唤醒，具体由实现决定。一个线程进入对象的等待区调用对象的 wait() 方法即可。
- 被唤醒的线程需要等待当前有锁的线程释放了当前对象的锁后才能直接运行，同时需要与其他参与到该对象同步竞争的活性线程一起竞争。eg: 被唤醒的线程更喜欢在成为下一个锁定该对象的线程没有可靠的优势与劣势。（？？？）
- notify() 方法只能被拥有对象 monitor 的线程调用。一个线程要拥有对象的 monitor 有以下三种方式：
    - 正执行对象的一个实例同步方法
    - 执行对象的同步语句块
    - 对于 class 对象的monitor，执行该 class 的静态同步方法
- 同一时间只能有一个线程持有对象的 monitor 。

### .2.3. clone()

> 此方法与同步无关，但早期留意过一起翻译了。

- clone() 方法创建当前对象的复制对象。具体的含义取决于对象的实现。一般来说，对于一个对象 x 需要以下含义：
    - x.clone() != x
    - x.clone().getClass() == x.getClass() 非必需
    - x.clone().equals(x) 非必需
- 按惯例：返回的对象需要通过调用 super.clone() 实现，如果一个类及其所有父类（除 Object 外）遵循了这个规则，`x.clone().getClass() == x.getClass()` 就会成立。
- 按惯例，对象调用 clone() 方法返回的对象必须与对象保持独立。为实现这种独立，需要在调用 super.clone() 后对某些字段进行修改（Object.clone() 方法只是浅拷贝）。也就是说对一个可变对象的 copy 由两部分组成：对象内部**深度结构**的 clone 与用这些被 copy 的引用替换内部对象的引用。
- 如果对象未实现 Cloneable 接口，将抛出 CloneNotSupportedException 。所有的数组都认为其实现了 Cloneable 接口。一个数组类型 T[] 在调用 clone() 方法后返回 T[] ，T 可以是任何引用或基本类型。
    - 否则 clone() 将返回该 class 的一个新实例，并且该实例使用原对象相应的字段内容初始化所有的字段。也就是说该方法只是**浅拷贝**。
- Object 本身并未实现 Cloneable 接口，所有直接调用 Object 对象的 clone() 方法将 throw 出运行时异常。
