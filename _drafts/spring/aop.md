---
layout: "post"
title: "aop"
date: "2018-11-26 10:50"
---
<!-- TOC -->

- [1. aop 面向切面](#1-aop-%e9%9d%a2%e5%90%91%e5%88%87%e9%9d%a2)
  - [AOP Concepts](#aop-concepts)
  - [1.1. aop 实现分为两类](#11-aop-%e5%ae%9e%e7%8e%b0%e5%88%86%e4%b8%ba%e4%b8%a4%e7%b1%bb)
  - [1.2. spring aop](#12-spring-aop)

# 1. aop 面向切面

> aop aspect oriented programming 。面向切面编程，常用于具有横切性质的系统级服务，如：事务管理、安全检查、缓存、对象池管理。
> **需要说明的是** aop 是一种编程思想，并不仅限于 java 更不仅限于 java spring 。但这儿主要针对 java spring aop 进行讨论。纵向重复代码在横向上抽取。

aspcetj 是基于 java 语言的 aop 框架，提供了强大的 aop 功能，其他众多的 aop 框架都借鉴了其思想。包括两个部分：

- 定义如何表达/定义 aop 语法规范。用于解决 java 中的交叉关注点问题。
- 工具部分：编译/调试。

## AOP Concepts

Aspect Oriented Programming 基本概念

1. Aspect: 多个类间的模块化的事物。企业应用中的事务管理就是很好的跨类切面的例子。在 Spring AOP 中通过常规类（schema 途径）或注解 `@AspectsJ` 实现切面。
2. Join Point: 程序执行中的某一点，如一个方法执行或异常处理。在 Spring AOP 中，一个 Joint Point 通常代表一个方法执行。
3. Advice: 切面在入点（Joint Point）所采用的动作。Advice 类型有："Aroud" "Before" "After" 。大多框架（包括 Spring AOP）将 Advice 建模成拦截器，并为 Join Point 维护一个拦截器链。
4. Pointcut： 匹配 Join Point 的判断。Advice 关联一个 Pointcut 表达式，并在每一个匹配上 Pointcut 的切点处执行（eg：执行有某个特定名字的方法）。匹配上 Pointcut 表达式的 Join Point 的概念是 AOP 的核心，Spring 默认使用使用 AspectJ pointcut 表达式语言。
5. Introduction: 引入外部方法或字段到一个类。Spring AOP 可以引入新的接口与相应的实现到被增强的类。在 AspectJ 社区中，introduction 通常被当作一个内部类的定义。
6. Target Object:被一个或多个增加的对象。也被当作被增强的对象。Spring AOP 通过运行时代理实现，所以 Target Object 也是一个代理对象。
7. AOP Proxy：AOP 框架实现 aspect 规约（增加方法执行等等）而创建的对象。在 Spring AOP 中，AOP proxy 通常为 JDK 动态代理或 CGlib 代理。
8. Weaving: 将 aspect 与其他应用的类型或对象连接以创建增加类。此动作可在编译期（通过 AspectJ Compoler）、加载期、运行时进行。Spring AOP 与大多 Java AOP 框架一样都在编译期执行 weaving。

## 1.1. aop 实现分为两类

- 静态 AOP 实现：在编译阶段就对程序进行修改，即实现对目标类的增强，生成静态的 AOP 代理类，以 aspcetj 为代表。具有良好的性能，但需要特殊的编译器。
- 动态 AOP 实现：AOP 框架在运行阶段动态生成 AOP 代理，以实现对目标对象的增加，如： spring AOP 。纯java 实现，无需特殊编译器，性能相对略差。

**基本概念**：

- Aspect 切面：用于组织多个 advice ， advice 就放在 aspect 中定义；
- Joinpoint 连接点：程序执行过程中明确的点，如：方法的调用/异常的拋出。 在 Spring AOP 中，连接点总是 方法调用。
- Advice 增强：AOP 框架支持在特定的切入点执行的增加处理。类型有：Before Around After
- Pointcut 切入点：中以插入增强处理的连接点。当连接点满足指定要求时，该连接点将被添加增加处理，该连接点也就说变成了切点。

## 1.2. spring aop

- Spring AOP 代理由 IoC 容器负责生成、管理，其依赖关系也由 IoC 窗口负责管理。
- 在 Spring 使用 Aspectj 支持需要添加三个库：
    - aspcetjweaver.jar
    - aspectjrt.jar
    - aopalliance.jar
- 配置文件加如下配置：

  ```xml
  <!--启动@AspectJ支持-->
  <aop:aspectj-autoproxy/>

  <!--指定自动搜索Bean组件、自动搜索切面类-->
  <context:component-scan base-package="edu.shu.sprint.service">
      <context:include-filter type="annotation" expression="org.aspectj.lang.annotation.Aspect"/>
  </context:component-scan>
  ```
