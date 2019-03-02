---
layout: "post"
title: "java-abstractClass&interface"
date: "2019-01-22 13:01"
---

# abstract class & interface

> java 中 抽象类与接口

## 两者的异同

同：
1. 都是面向对象中抽象的使用，相对于抽象类，接口是极其抽象的；

异：
1. 抽象类作为一种类，可以有各种成员，具有所有的普通类的特性。而接口只能有 public static final 的属性成员，接口方法都是缺省为 public abstract 修辞。
2. 抽象类作为一种类用于声明一种对象（其子类的共同属性抽象为父类的方法）较为合适，而接口通常用于声明一种行为。
3. 一个普通类只能于继承一个抽象类，却可以实现多个接口。
