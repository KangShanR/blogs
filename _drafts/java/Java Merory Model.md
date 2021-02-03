# Java Memory Model

[reference](http://www.cs.umd.edu/~pugh/java/memoryModel/)

## JSR 133 (Java Memory Model) FAQ

[reference](http://www.cs.umd.edu/~pugh/java/memoryModel/jsr-133-faq.html)

### How does synchronization do?

- Synchronization 同步存在多方面含义。最为人所知的是恒定排他性：一旦某个线程获取到 monitor ，monitor 上的同步意味着一个线程进入了 monitor 所保护的同步块，在此线程退出此同步块前没有其他线程能够再进入。
- 同步同时保证一个线程在进入同步块前与中的写对于其他在相同 monitor 的线程保持可预测的可见性。当退出同步块，会释放 monitor ，这会将缓存中的数据 flush 到主存中，让此线程的写对其他线程可见。在进入同步块前，我们需要获取 monitor ，这带来的内存效应是将处理器 processor 的缓存置为无效，这样变量就必须从主存中重载。这样我们看到的效果就是前一次释放的所有写都可见。

### How does a final fields work under the new JMM?

- 对象的常量字段都在其构造器内赋值。
- 一旦构造完成，即使没有添加 synchronization，常量字段的数据可以被其他所有线程可见。此外，常量字段所引用的对象或数组的可见值将被更新到与常量字段一样保持最新。
- 一个对象正确的构造意味着：在构造期间对象的引用没有“逃逸”。**换句话说：不要将被构造对象的引用置于任何其他线程可见的位置，不要将其指向静态字段，不要将其注册为其他任何对象的监听器，等等。这些操作应该在对象构造完成之后进行，而不是在构造期间进行。**
- 正确构造同样保证了引用常量字段所指向的对象或数组值在构造后依然是最新的值，所以可以让常量的指针指向对象或数组，而不用担心其他线程看到正确的引用看不到引用的值。但在这里最新的值仅仅是指构造完成后，不是所有的阶段。
- 对于一个不可变对象（所有字段都是常量）被一个线程构造完成后，如果其他所有线程想要正确可见，仍然需要使用 synchronization 。并没有其他途径可以保证，这就要求程序获取常量字段代码对于并发的管理理解深入而细致，
- JMM 对于使用 JNI 修改常量字段的行为并没有定义。

### What does volatile do?

- volatile 字段用于线程间状态交流。对于 volatile 字段的读取都会获取到其他程的最后一次写。volatile 被设计用于不接受因缓存（cache）或指令重排（reordering）导致的 stale 值的字段。
- volatile 维持半同步，用于标识字段以让在 processor 缓存中，被一个线程修改后立即被 flush 到主存中，编译器与运行时被禁止将 volatile 字段值置于 processor 寄存器中，从而保证其他线程对其修改可见。
- 指令重排限制：
    - 老版 JMM 不允许对 volatile 字段进行 reordering ，但可以对非 volatile 字段进行重排。这让 volatile 字段成了一个线程间信号条件的方式。
    - 新版 JMM 除会限制字段不能被指令重排（reordering），同时要求 volatile 字段周围的字段都不能轻易被 reordering 。
    - volatile 字段在修改与释放 monitor 内存效果一致（将 processor 缓存数据 flush 到主存中，从而其他线程可见），在读取与获取 monitor 内存效果一致（将本地处理器缓存中数据置为无效，变量值不得不从主存中读取）。

**Important Note:**

- 多线程访问 volatile 变量都是为了合适地设置 happens-before 关系。并不是线程A在写 volatile field f 时所有可见在线程Ｂ访问 volatile field g 后都可见。释放与获取锁需要匹配到相同的 volatile 字段才能保证语义正确。

### Does the new JMM fix the "double-checked locking" problem?

双检锁问题

- 单例模式中很多人喜欢使用双检锁模式，认为其可以降低线程阻塞概率。

```java
//double-checked locking, Don't do like this!
private static Ins ins = null;
public Ins getIns(){
    if(ins == null){
        synchronized(this){
            if(ins == null){
                ins = new Ins();
            }
        }
    }
    return ins;
}
```

- 上段代码的问题在于：在 synchronized 代码块中， ins 的初始化与赋值指令可能会被编译器或缓存重排，从而导致 ins 在某一时期内依然是个半初始化的对象，在这期间 synchronized 块外部其他读取 ins 的线程就会读取到这个半初始化的 ins 对象，就会产生使用未初始化完成的 ins 错误。
- 在老版JMM 中添加 volatile 关键字到 ins 字段前并不能解决问题，在 JVM1.5 新版 JMM 的 volatile 可以解决问题。在使用 volatile 修辞后的 ins 并不会出现指令重排，也构成了内部线程初始化与外部线程读取的 happens-before 关系（一旦 ins 开始初始化，其他线程必须对其结果可见，也就是需要在其初始化完成前才读取到）。
