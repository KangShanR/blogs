---
title: Mybatis中的缓存
date: 2017-08-29 13:04:38
categories: programming
tags: [java,framework,programming]
keywords: 
---

# Mybatis中的缓存 #

> 在Mybatis查询中通过配置文件Mapper.xml中的建立的Session执行sql语句查询出的结果，一般会存放在Mybatis缓存中，在被清除之前再次执行相同的查询时，Mybatis只用直接调用缓存中的结果，而不用再次执行一次查询语句以提高效率。

<!--more-->

**分类：**

- Mybatis的缓存分为两种：
	- **一级缓存**：其作用域是同一个Sqlsession，这也就意味着当这个SqlSession被提交(Commited)后，这个缓存也不复存在。Mybatis默认是开启一级缓存的；
	- **二级缓存**：Mybatis默认不开启二级缓存，要开启二级缓存需要在setting全局参数中设置。
		- 作用域是以namespace来划分的，同一个命名空间的mapper查询数据存放在同一个区域，mapper动态代理中不同的mapper也就意味着不同的namespace，因此也可以说二级缓存区域是根据mapper划分的；
		- 同样，一个Sqlsession在执行insert、delete、update等操作commit提交后，会清空缓存区域；
		- 二级缓存需要将查询结果映射的pojo对象实现`java.io.Serializable`接口，实现序列化的反序列化操作。如果存在父类，其都要实现这个接口；
