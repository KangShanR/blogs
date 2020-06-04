---
title: log4j
date: 2020-04-26 09:36:38
categories: 
tags: log
keywords: log4j
---

# 1. log4j
<!-- TOC -->

- [1. log4j](#1-log4j)
  - [1.1. 单独地给某类记录日志](#11-单独地给某类记录日志)
  - [1.2. 日志输出格式](#12-日志输出格式)
    - [1.2.1. conversion character](#121-conversion-character)

<!-- /TOC -->
[reference](http://logging.apache.org/log4j/1.2/manual.html)
[reference](https://mp.weixin.qq.com/s/vrzUHShgekkvZi1yrmbbxg)

## 1.1. 单独地给某类记录日志

[reference](https://www.jianshu.com/p/ccafda45bcea)

指定 log4j 配置文件直接在资源目录新建一个 properties 再添加其各个配置属性。

## 1.2. 日志输出格式

[reference](http://logging.apache.org/log4j/1.2/apidocs/org/apache/log4j/PatternLayout.html)

格式类： `org.apache.log4j.PatternLayout`

- 各个 pattern 都以 `%` 开头。其后可能跟着格式修改器（format modifier）与转换符号（conversion character）
- 转换符指定数据类型 conversion character，如：类别 category、优先级 priority、日期 date 、线程名 thread name
- format modifier 格式修改器控制字段宽度、缩进、左右对齐之类

### 1.2.1. conversion character

[转换符的使用](http://logging.apache.org/log4j/1.2/apidocs/org/apache/log4j/PatternLayout.html)

|character|Effect|remark|
|:--|:--||
|`c`|用于输出日志事件类别 `{2}` 指定精度，为2将输出其上级名||
|`C`|用于输出调用日志的类的全限定名，同样可指定精度|生成 caller 信息耗费资源，不建议使用|
|`d`|date 指定日期，使用 SimpleDateFormat 格式化，可使用 `ABSOLUTE`, `DATE` `ISO8601` 三种指定其格式||
|`F`|file 指定文件名|生成 location information 耗费资源不建议使用|
|`l`|location 指定输出位置信息|在ide 日志中可直接点击跳转。同样此机制耗费资源，执行缓慢|
|`L`|line 输出位置信息，行数|耗费资源|
|`m`|message 输出应用提供的消息||
|`M`|method 输出方法名|执行缓慢|
|`n`|输出平台所依赖的换行符|取决于应用系统的换行符|
|`p`|priority 输出日志优先级|`%5p` 前数字用来指定分隔距离|
|`r`|输出从创建布局到日志事件创建所花费毫秒数||
|`t`|thread 输出产生日志线程名||
|`%`|输出 `%`||
|`x`|输出线程相关的内嵌的诊断上下文 NDC (nested diagnostic context)||
|`X`|输出 MDC (mapped diagnostic context)||
