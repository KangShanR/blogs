---
title: Hibernate
date: 2017-05-23 02:04:38
tags: [framework,java,programming]
categories: programming
description: 关于Hibernate框架的基本使用与理解
---
- Hibernate是一种ORM（Object Relative-Database Mapping)框架，用于与各种数据库、SQL语句打交道，是数据持久化的一咱解决方案；
- ORM，是在Java对象与关系数据库之间建立某种映射，以实现直接存取Java对象，一般这个对象就叫做POJO（Plain Ordinary Java Object）；
- ORM框架与MVC框架不同，使用范围也截然不同，类似功能的框架还有SUN的JDO、Apache的Mybatis与OpenJPA等；
- 降低了开发认知负担，最大优势在于级联，最大的劣势也在于级联，降低了执行效率，经过了多层封装，资源消耗变得很高；
- 相较于Hibernate,Mybatis在只是简单地封装了JDBC，代码开发更多，执行效率更高，资源消耗更低；