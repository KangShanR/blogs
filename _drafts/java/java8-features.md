---
layout: "post"
tag: [java8]
date: "2018-09-26 18:50"
---

# 1. java8 new features
<!-- TOC -->

- [1. java8 new features](#1-java8-new-features)
  - [1.1. stream](#11-stream)
  - [1.2. 对 stream 进行以下操作会将流关闭而不能再进行利用](#12-对-stream-进行以下操作会将流关闭而不能再进行利用)
  - [1.3. method](#13-method)
    - [1.3.1. peek(Consumer())](#131-peekconsumer)
  - [1.4. java8 time](#14-java8-time)
    - [1.4.1. java8 中的各个新增的类](#141-java8-中的各个新增的类)

<!-- /TOC -->

## 1.1. stream

> 列表进行 stream() 方法生成列表相应的流对象。对列表进行操作而不影响列表本身，很方便的一个对列表进行统计、过滤的特性。

## 1.2. 对 stream 进行以下操作会将流关闭而不能再进行利用

- collect(Collector<T, T>) 常常将流进行操作后返回为 List<T> 进行再利用。这个时候就将关闭了，不能进行下一步流操作。
- count() 此方法是统计流中数据数量，这个方法也会将流关闭。

>> 如果在使用 流 的过程中，需要对流进行反复操作而又不得不使用用将其关闭的方法时，我们可以多次使用原列表生成流，这样可以多次操作。

## 1.3. method

> 流对于列表进行操作有诸多函数式接口参数方法，可以对列表进行过滤、筛选、求最值。

### 1.3.1. peek(Consumer())

> 这个方法需要使用将对 流 中的元素进行依次的消费。但如果要表现出来，需要在其后跟上 collect() 方法。

## 1.4. java8 time

java 8 time 包提供了更耐用的日历系统。

- chronology 年代表
- ISO： International Standardization Organization :国际标准化组织
    - IsoChronology 国际标准年代

### 1.4.1. java8 中的各个新增的类

- Instant 瞬时，用以表示从 1970-01-01T00:00:00S 点开始计算的时间线中的某一个瞬间。两个属性
    - seconds 秒，即从 1970 年开始计时到此对象代表的时刻经过的秒数
    - nanos 纳秒，相应的纳秒数
- ZoneId 时区 id，代表各个不同的时区
    - 常用于 Instant 与 LocalDateTime 之间的转换；
    - 有两种类型的 ZoneId:
        - 固定偏移量：从标准时间 UTC(universal time coordinated)/Greenwich 完全解析偏移量而来
        - 地理区域：根据地理位置定义的整个时区，其与 UTC/Greenwich 标准时间偏移量是整小时
