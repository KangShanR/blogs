---
title: Java Jar
date: 2020-10-20 11:02:00
categories: java
tags: [java, jar, package, jvm]
description: java jar
---

# Java jar

java archive

- 使用 jdk 自带工具 javac 编译 java 文件成 class 字节码，再用打包命令 `java jar cfmv jar-name manifest-file class-files`

## installed extensions

添加外部的 jar 用作扩展。

- 可在命令行中添加 classpath 的参数，指定 classpath 引用外部 jar 包，也可以在 manifest 文件中指定多个 classpath 。
- 可以直接在 jre 中添加扩展。path：jre/lib/ext
- 若有多个 jre，可以在用户目录中指定让多个 jre 使用同一个 jar 包：usr/java/packages/lib/ext

## `java` command line

> `java` 命令行在启动一个 java 进程时至关重要。其中关系到设置 jvm 的各项参数，项目启动的配置参数等等。

- `-` 标准选项 standard option
- `-X` 非标准选项 non-standard option
    - `-Xmn<size>` 设置堆中最大／初始新生代大小。max nursery，如果需要设置初始大小与最大新生代大小，使用 `-XX:NewSize` 和  `-XX:MaxNewSize`。新生代设置过小会导致 GC 频率过高，设置过大，会导致 full GC 时间过长。Oracle 推荐新生代大小在堆的1/4 ~ 1/2 。
    - `-Xms<size>` 设置初始堆大小。单位为 byte ，大小需要为 1024 的倍数且超过 1m。如果没有设置此项，jvm 会计算新生代与老年代的初始大小之和为此项配置的值。
    - `-Xmx<size>` 要求与 Xms 一致，等于 `-XX:MaxHeapSize`，用以设置堆的内存最大分配置额。
- `-XX` 高级选项 advanced option
- `-D<name>=<value>` 设置系统属性

## JVM

[Oracle reference](https://docs.oracle.com/javase/8/docs/technotes/guides/vm/gctuning/toc.html)
