---
title: java枚举
date: 2017-07-29 12:04:38
categories: programming
tags: [java,programming]
description:
---
# java枚举

> java枚举的使用

<!--more-->

## 简介：
- 枚举enum是在jdk1.5后引入的，是java高级特性之一；
- 枚举的定义关键字：enum
	- `public enum season{}`，其与普通类一样拥有属性、方法、构造函数
	- 枚举是特殊的类，其成员全都是固定下来的；


## 枚举特性：

- 枚举的**构造函数默认是私有的**，可以自定义枚举私有的构造函数
- 所有枚举的公共基类为abstract Enum<E extends Enum<E>>，我们自定义的enum则都继承自它；
- 枚举中的实例就是我们常用到的定义为公共静态常量，它们之间用`；`分开；
- 枚举每个实例都有索引，从0开始；
- 枚举的常用方法:
	- Enum<E> valueOf(String name):通过name得到枚举的实例；
	- Enum<E>[] static valueOf(Class<E extends Enum> enumType, String name);根据指定枚举名返回这个枚举类里所有的实例
	- int ordinal();返回该枚举常量在声明中的位置；
	- int compareTo(E o);与指定对象比较，如果相同就返回0；
	- String name();返回该枚举实例的声明名；


----------
**Note:**
- 在枚举单例模式中，利用的就是枚举的构造函数是私有的，其**实例就是本身的一个公开静态常量实例来实现**，而其他的方法定义在这个枚举中；
