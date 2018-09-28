---
layout: "post"
title: "Function interface -- Comparator()"
date: "2018-09-28 13:26"
---

# 函数式接口 Comparator<T>

> 此接口是一个比较器，用于对两个参数进行比较。关键方法 `int compare(t1 ,t2)`

- 关键方法返回一个 int 整数，分别用 正负零 表示 大小等。
- 实现这个接口时，参数 t1 与 t2 的位置也决定了结果，同样的方法体，将参数调个位置，其结果也就相反。
