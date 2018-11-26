---
layout: "post"
title: "aop"
date: "2018-11-26 10:50"
---

# aop 面向切面

> aop aspect oriented programming 。面向切面编程，常用于具有横切性质的系统级服务，如：事务管理、安全检查、缓存、对象池管理。
>**需要说明的是** aop 是一种编程思想，并不仅限于 java 更不仅限于 java spring 。但这儿主要针对 java spring aop 进行讨论。


aspcetj 是基于 java 语言的 aop 框架，提供了强大的 aop 功能，其他众多的 aop 框架都借鉴了其思想。包括两个部分：
- 定义如何表达/定义 aop 语法规范。用于解决 java 中的交叉关注点问题。
- 工具部分：编译/调试。


## aop 实现分为两类

- 静态 AOP 实现：在编译阶段就对程序进行修改，即实现对目标类的增强，生成静态的 AOP 代理类，以 aspcetj 为代表。具有良好的性能，但需要特殊的编译器。
- 动态 AOP 实现：AOP 框架在运行阶段动态生成 AOP 代理，以实现对目标对象的增加，如： spring AOP 。纯java 实现，无需特殊编译器，性能相对略差。


**基本概念**

- Aspect 切面：用于组织多个 advice ， advice 就放在 aspect 中定义；
- Joinpoint 连接点：程序执行过程中明确的点，如：方法的调用/异常的拋出。 在 Spring AOP 中，连接点总是 方法调用。
- Advice 增强：AOP 框架支持在特定的切入点执行的增加处理。类型有：Before Around After
- Pointcut 切入点：中以插入增强处理的连接点。当连接点满足指定要求时，该连接点将被添加增加处理，该连接点也就说变成了切点。

## spring aop

- Spring AOP 代理由 IoC 容器负责生成、管理，其依赖关系也由 IoC 窗口负责管理。
- 在 Spring 使用 Aspectj 支持需要添加三个库：
  - aspcetjweaver.jar
  - aspectjrt.jar
  - aopalliance.jar
- 配置文件加如下配置：
  ```
  <!--启动@AspectJ支持-->
  <aop:aspectj-autoproxy/>

  <!--指定自动搜索Bean组件、自动搜索切面类-->
  <context:component-scan base-package="edu.shu.sprint.service">
      <context:include-filter type="annotation" expression="org.aspectj.lang.annotation.Aspect"/>
  </context:component-scan>
  ```
