---
title: Hibernate
date: 2017-08-23 02:04:38
tags: [framework,java,programming]
categories: programming
description: 关于Hibernate框架的基本使用与理解
---

> 简介：Hibernate是一种ORM（Object Relative-Database Mapping)框架，用于与各种数据库、SQL语句打交道，是数据持久化的一种解决方案；在java对象（在这儿就为POJO）与关系数据库之间建立某种映射，实现直接存取Java对象。
> 关于ORM：
> 数据持久化核心技术之一，在JDBC开发中，大量重复性高的sql语句在DAO层中，而ORM就实现了根据POJO属性来拼装SQL语句，读取时，用SQL语句将各种属性从数据库中读取出来，再拼装为 POJO 对象返回给业务层，这样来实现数据表与POJO、数据表中列与POJO属性之间的映射关系；ORM框架就实现通过映射关系自动生成sql语句的强大强大功能；
> ORM框架与MVC框架不同，使用范围也截然不同，类似功能的框架还有SUN的JDO、Apache的Mybatis与OpenJPA等；

<!--more-->

## Hibernate的原理

- 从最基础的JDBC说起：
  - 在 JDBC 编程中，我们在在 DAO 层编写各种 sql 语句来实现数据库的 crud，而 Hibernate 就充当了 DAO 层，根据 POJO 与实体类的映射自动生成 sql 语句；
  - JDBC 中 DAO 中的 sql 语句必须由程序员事先写好，而在 Hibernate 中 sql 语句是动态生成，如果实体类发生变化：在 JDBC 中，与之相应的 sql 语句都得重新写一遍，但在 Hibernate 中只需要更新与这个实体类相关的配置类即可；

## Hibernate的配置

> Hibernate的配置就分两种，**xml配置**与**注解配置**，两者略有不同，但最终都是把实体属性与数据库表之间的映射给联系好；

### Hibernate的版本配置

> 锁的配置就是为了防止线程上的不安全，Hibernate为了处理这种当一个线程在更改数据时，另一个线程也参与进来而出现线程不安全；
> Hibernate的锁机制有两种，乐观锁与悲观锁，悲观锁是只让一个线程来操作数据，而乐观锁是加上一个版本号，现web开发中一般都用乐观锁；

- 注解配置版本号：
  - 在实体类中，配置一个乐观锁需要在实体类中专门开一个数值类型的属性并加上注解`@Version`；
- xml配置版本号：
  - 在`<id>`之前，`<property>`之后，配置上`<version>`标签；
  - 比注解配置更灵活的地方在于，在这个标签中，`<version>`标签中可以指定属性为时间类型值`<version name="version" type="timestamp" column="version"></version>`；

### Hibernate与Mybatis的对比

> 或者说为什么有了Mybatis还会存在着Hibernate？

- 降低了开发认知负担，最大优势在于**级联**，最大的劣势也在于级联，**降低了执行效率**，经过了**多层封装**，**资源消耗变得很高**；
- 相较于Hibernate,Mybatis在只是**简单地封装了JDBC**，**代码开发更多**，**执行效率更高**，**资源消耗更低**；