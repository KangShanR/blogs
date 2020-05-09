---
layout: "post"
title: "java - Type"
date: "2019-01-29 18:46"
tags: [generic,java]
---

# 1. java Type

<!-- TOC -->

- [1. java Type](#1-java-type)
  - [1.1. java generic](#11-java-generic)
    - [1.1.1. 范型通配符](#111-%e8%8c%83%e5%9e%8b%e9%80%9a%e9%85%8d%e7%ac%a6)
      - [1.1.1.1. PECS 原则](#1111-pecs-%e5%8e%9f%e5%88%99)

<!-- /TOC -->

> java 中使用的范型相关的 Type。在反序列化时，对于范型相关的类需要使用到的 Type 。mc

[参考](https://blog.csdn.net/u011983531/article/details/80295479)

## 1.1. java generic

java 范型 [reference](https://blog.csdn.net/weixin_30662109/article/details/98836666?utm_medium=distribute.pc_relevant.none-task-blog-BlogCommendFromMachineLearnPai2-1.nonecase&depth_1-utm_source=distribute.pc_relevant.none-task-blog-BlogCommendFromMachineLearnPai2-1.nonecase)

- 静态方法要实现范型，要在返回结果前加上范型标识 `<T, R> void generic()`
- 使用无参的范型方法要在使用时确定范型类型在方法前加上范型参数：`builder.<String, Integer>build()`。否则，编译器只会当范型类型为 Object。
- 范型构造器的使用与无参范型构造的实现：通过上一条即可实现。

### 1.1.1. 范型通配符

- 范型中 `super` 与 `extends` 的区别使用、非常规意义
    - `? super` 指定范型类型下界，是所指类型与其父类型直至根类 Object。这种通配读取出数据只能使用 Object 接收，而可以正常地写入数据。
        - 原因：当外部容器被 `? super` 通配指定了下界，也就指定了容器中数据类型的最细粒度，符合最小粒度与粒度更小的数据都可合法地写入。而读取数据时，对于通配的数据类型的存在，只能选择最大粒度的数据类型接收才能保证正确接收。
    - `extends` 则相反，使用此通配，将不能进行 add ，只能为其指定一个现成的上界及上界之下的类型列表，可读。因为通配符 `? extends` 的存在（只要是目标类型及其子类都算合法），写入时编译器不知道此时类型应该按哪一个类型算。而读取时，编译器知道容器中所有通配的类型的上界就是最粗粒度，只按是粗粒度取出数据即可。

#### 1.1.1.1. PECS 原则

在范型中使用通配符时，遵守 PECS 原则：

- 在集合中，如果只读取类型 T 的数据， 不写入，使用 `? extends` 通配：Producer Extends
- 在集合中，如果只写入类型 T 数据，不用读取，使用 `? super` 通配： Comsumer Super。可以读取，不过读出来的只能识别为 Object。
    - 所以一般方法返回结果（要往内写数据用以返回）使用 super 通配，而方法参数（要读取其中的数据）使用 extends 通配
- 如果即要读取又要写入，就不使用通配符

---

- Arrays.asList(T ...) 方法产生的一个 ArrayList 是 Arrays 中定义的一个并未完全实现抽象类 AbstractList 的 List，使用其 add(T t) 方法会抛出异常。
