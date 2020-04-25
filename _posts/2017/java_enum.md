---
title: java枚举
date: 2017-07-29 12:04:38
categories: programming
tags: [java,programming]
description:
---

# 1. java枚举

<!-- TOC -->

- [1. java枚举](#1-java%e6%9e%9a%e4%b8%be)
	- [1.1. 简介](#11-%e7%ae%80%e4%bb%8b)
	- [1.2. 枚举特性](#12-%e6%9e%9a%e4%b8%be%e7%89%b9%e6%80%a7)
	- [1.3. 位运算在枚举中的运用](#13-%e4%bd%8d%e8%bf%90%e7%ae%97%e5%9c%a8%e6%9e%9a%e4%b8%be%e4%b8%ad%e7%9a%84%e8%bf%90%e7%94%a8)
		- [1.3.1. 做法](#131-%e5%81%9a%e6%b3%95)
		- [1.3.2. 优点](#132-%e4%bc%98%e7%82%b9)

<!-- /TOC -->

> java 枚举 的使用

<!--more-->

## 1.1. 简介

- 枚举enum是在jdk1.5后引入的，是java高级特性之一；
- 枚举的定义关键字：enum
    - `public enum season{}`，其与普通类一样拥有属性、方法、构造函数
    - 枚举是特殊的类，其成员全都是固定下来的；

## 1.2. 枚举特性

- 枚举的 **构造函数默认是私有的** ，可以自定义枚举私有的构造函数
- 所有枚举的公共基类为 abstract Enum<E extends Enum<E>>，我们自定义的 enum 则都继承自它；
- 枚举中的实例就是我们常用到的定义为公共静态常量，它们之间用 `,` 分开；
- 枚举每个实例都有索引，从 **0** 开始；
- 枚举的常用方法:
    - Enum<E> valueOf(String name):通过 name 得到枚举的实例；
    - Enum<E>[] values(); 返回该枚举的实例数组；
    - Enum<E>[] static valueOf(Class<E extends Enum> enumType, String name);根据指定枚举名返回这个枚举类里所有的实例
    - int ordinal();返回该枚举常量在声明中的位置；
    - int compareTo(E o);与指定对象比较，如果相同就返回0；
    - String name();返回该枚举实例的声明名；

**Note:**

- 在枚举单例模式中，利用的就是枚举的构造函数是私有的，其 **实例就是本身的一个公开静态常量实例来实现** ，而其他的方法定义在这个枚举中；

## 1.3. 位运算在枚举中的运用

查看 com.fasterxml.jackson.core.JsonGenerator 源码所得。

### 1.3.1. 做法

- 定义枚举作为某组件属性之一（masks），枚举中定义一个 int 掩码 `mask` ，枚举实例其掩码 `mask = 1 << original`；
- 对于组件添加枚举属性时使用位运算或： `masks |= mask`，为其累加属性；
- 在判定是否存在枚举属性时使用：`masks & mask == mask`；

### 1.3.2. 优点

- 位运算相对于其他比较判断所需要资源更少，运算更快。
