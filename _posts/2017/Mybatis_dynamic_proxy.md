---
title: Mybatis中的动态代理
date: 2017-08-29 13:04:38
categories: programming
tags: [java,framework,programming]
keywords: 
---

# Mybatis中动态代理的运用 #
> 在Mybatis框架中，实现sql语句的与接口的映射用到的就是动态代理。下面简要地分析下动态代理模式的实现在这其中的运用。

<!--more-->

## Mapper动态代理的原理 ##


- 在Mybatis中实现Mapper动态代理需要的东西有：
	- Mapper接口，这个接口里写好了各个执行语句的方法；
	- 以这个接口路径名为命名空间的Mapper映射文件，条件：
		- 这个映射文件中的各个映射statement语句的id与接口的方法名一致；
		- parameterType与接口的方法的参数类型一致；
		- resultType与接口的返回类型一致；
- 而在Mybatis的配置文件中，各个Mapper映射文件配置在其中。当我们在外部调用这个Mapper接口的方法时，动态代理就开始工作：
	- 明显的，接口的方法对应的statement语句，代理就会将这个相对应的statement执行语句在被代理的方法中实现。
	- 而Mybatis框架实现这个代理过程就是通过建立对应的session，在session对象中执行statement中的sql语句，同时Mybatis也会把事务提交并关闭session这些操作一并给完成；