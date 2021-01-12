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

### Queuing 任务队列

> 用于放置提交的不能当前执行的任务。BlockingQueue

队列与 pool size 关系：

- 如果正在运行的线程数量低于 corePoolSize ，执行器将添加一个线程而非放入队列。
- 如果运行的线程数量超过 corePoolSize，执行器将排队入队列而非添加线程。
- 当请求不能放置入队列，如果运行线程数量未超过 maximumPoolSize 将新添加一个线程，否则将拒绝请求。

### 三种排列策略

1. 直接移交 direct handoff 。SynchronousQueue ，直接将任务提交给执行器不放置入队列。
2. 无限队列 LinkedBlockingQueue
3. 有限队列 ArrayBlockingQueue

### 任务拒绝

当运行线程数量与队列容量都有限且已饱和，此时执行器将拒绝提交的任务。RejectedExecutionHandler#rejectExecution(Runnable, ThreadPoolExecutor).预定义的四种拒绝策略：

1. 默认的 ThreadPoolExecutor.AbortPolicy ，直接抛出运行时异常 RejectedExecutionException。
2. ThreadPoolExecutor.CallerRunsPolicy，让提交任务的线程自己运行任务。可以降低高峰时请求任务执行数量。
3. ThreadPoolExecutor.DiscardPolicy，直接丢弃提交的任务。
4. ThreadPoolExecutor.DiscardOldestPolicy，丢弃队列中最老的任务。

可自定义任务拒绝策略。实现 RejectedExecutionHandler .

### Hook Methods

在每个线程任务执行前后添加钩子用以执行自定义。如果钩子方法抛出异常，内部线程执行可能执行失败并被中断。

1. beforeExecute(Thread, Runnable)
2. afterExecute(Runnable, Throwable)

### 队列维护 Queue Maintenance

getQueue() 方法允许访问队列，但只推荐用以监控与 debug。当大量任务被取消，可使用 remove() purge() 方法回收队列空间。

### 线程池析构

如果线程池不再被引用且没有线程在其中，将自动关闭 shutdown 。如果需要保证不被引用的线程池未被用户调用 shutdown() 方法被回收，需要将无用的线程死亡。方法是设置恰当的生命时长 keep-alive time ，低于0 的 corePoolSize 或 allowCoreThreadTimeOut(true)。
