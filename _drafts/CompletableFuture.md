---
layout: "draft"
date: "2018-09-28 13:26"
tag: [java, concurrent]
---

# Java Concurrent

> Java 多线程

## CompletableFuture

Java 异步编程的神器，利用执行器 Executor 去执行多个任务时内置多个整合异步执行结果的方法，且全面支持 Java8 的函数式接口。

然而完全不能用于异步，只能在当前线程外开启异步而不能异步于当前线程执行任务。也就是说执行再多的异步任务，当前线程还是得等到 CompletableFuture 聚合出结果来才能执行下一步。 要在当前线程执行任务，使用线程池 ThreadPoolExecutor 。

## ThreadPoolExecutor

> 使用线程池实现多线程编程

工厂类 Executors 提供了便捷的构造线程池的方法，

1. Executors.newCachedThreadPool() 无限线程数量，自动回收线程
2. newFixedThreadPool() 固定线程数量线程池
3. newSingleThreadExecutor() 单线程执行器

如果需要更详细地控制微调线程池，就需要使用类中其他参数。

### Core and Maximum Pool Size

ThreadPoolExecutor 会根据 corePoolSize 与 maximumPoolSize 自动调整线程池 pool size 。

当一个新的任务通过 execute(Runnable r) 方法被提交时，如果正在运行的线程数量低于 corePoolSize，这时会创建一个新的线程用以执行任务，不管此时其他已初始化好的线程是否是空闲状态。如果正运行的线程数量超过 corePoolSize 但少于 maximumPoolSize ，只有当队列是满的时才会创建新的线程执行任务。设置 corePoolSize == maximumPoolSize 就是设置了一个 fixed size Thread Pool 。如果设置 maximumPoolSize 为无限大（ Integer.MAX_VALUE ），就让线程池可并发执行任意数量的任务。一般来说， core Pool size 与 maximum pool size 在构造时设置好，但也可以使用 setCorePoolSize() 与 setMaximumPoolSize() 方法动态设置。
