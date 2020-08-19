---
layout: "post"
date: "2018-09-28 13:26"
---

# CompletableFuture

Java 异步编程的神器，利用执行器 Executor 去执行多个任务时内置多个整合异步执行结果的方法，且全面支持 Java8 的函数式接口。

然而完全不能用于异步，只能在当前线程外开启异步而不能异步于当前线程执行任务。也就是说执行再多的异步任务，当前线程还是得等到 CompletableFuture 聚合出结果来才能执行下一步。 要在当前线程执行任务，使用线程池 ThreadPoolExecutor 。
