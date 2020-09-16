---
date: 2020-09-15 17:50:17
tags: [java,class loader]
categories: programming
---

# ClassLoader

- ClassLoader 用于加载文件系统中 class 文件。
- 每个 Class 对象都可以通过 getClassLoader() 方法获取到其加载器。
- 数组 class 对象不通过 classLoader 加载，在运行时需要时再加载。数组元素的 classLoader 与 数组的 classLoader 是同一个，如果数组元素是基本类型，此数组没有 classLoader 。
- ClassLoader 使用委托模式查找 classes 和 resources 。当请求找资源时，ClassLoader 实例在查找资源前会先委托其 父加载器查找。虚拟机内置的 ClassLoader 叫 "bootstrap class loader"， bootstrap class loader 没有父加载器，但常扮演其他加载器的父加载器。
- ClassLoader 用于加载除 class 外的文件资源，eg: .properties
