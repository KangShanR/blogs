---
title: spring反转控制的思想
date: 2017-09-15 12:14:38
tags: [framework,java]
categories: programming

---

# 1. Spring的反向控制思想

<!-- TOC -->

- [1. Spring的反向控制思想](#1-spring%e7%9a%84%e5%8f%8d%e5%90%91%e6%8e%a7%e5%88%b6%e6%80%9d%e6%83%b3)
  - [1.1. 反向控制（Inverse of Control)](#11-%e5%8f%8d%e5%90%91%e6%8e%a7%e5%88%b6inverse-of-control)
    - [1.1.1. 举例](#111-%e4%b8%be%e4%be%8b)
  - [1.2. Container Configuration](#12-container-configuration)
    - [1.2.1. java based container configuration](#121-java-based-container-configuration)
      - [1.2.1.1. AnnotationConfigApplicationContext 初始化 IoC 容器](#1211-annotationconfigapplicationcontext-%e5%88%9d%e5%a7%8b%e5%8c%96-ioc-%e5%ae%b9%e5%99%a8)
      - [1.2.1.2. ComponentScan](#1212-componentscan)
      - [1.2.1.3. AnnotationConfigWebApplicationContext](#1213-annotationconfigwebapplicationcontext)

<!-- /TOC -->

> Spring 说到底就是一个轻量级的容器，让它来负责各个实例的生产、管理、维护，而这些实例的参数与依赖关系都交由 spring 的配置文件来设置；

<!--more-->

## 1.1. 反向控制（Inverse of Control)

> 概念：也可以叫依赖注入（Dependency Injection)，是 spring 的核心思想。通俗地理解就是将原本正向流程走的程序让其反向执行。
> 之所以也叫依赖注入，是因为在实现反向控制的过程中是将原本要在后面实例化的属性提前注入到自己的实例中。这样 **把离散的组件在运行时组装到一块** ，实现程序流程在运行时组装，这样就可以很方便地添加功能，比如：拦截器；

### 1.1.1. 举例

- 当我们要通过 DAO 层的 dao 与 Service 层的 service 对象来实现分业务层的访问数据库操作：
    - **正向思维**：是先实例化一个 Service 对象，再在这个 Service 对象中实例一个 Dao 对象来，在 Service 方法中执行这个 Dao 对象的方法来访问数据库。
    - **反向控制**：Service 与 Dao 都抽象出各自的接口，而在 Service 实现类中约定一个 Dao 接口的属性，service 方法中再来调用这个 Dao 的方法；而在 Spring 中，这个属性的实现就叫注入，而这个注入是通过 spring 配置文件中的 bean 实现，在 Service 的 bean 中，有一个 `<property>` 的标签，这个标签中配置相关的 dao 的 bean 。
        - _在这个例子中， Service 与 Dao 都是作为独立的组件出现，在编码阶段，既没有实例化对象出来也没有设置依赖关系，而把它交给Spring，**由 Spring 在运行阶段实例化并组装**。这种颠覆传统编码过程就叫反转控制。_

----------
**Note:**

- 反向控制在 spring 配置中高频地被使用，在实现AOP面向切面编程中高频地使用到反向控制来将各个方法注入到其他的方法之前或之后；

## 1.2. Container Configuration

ioc 容器配置。传统配置方法是使用 xml 配置文件实现。

### 1.2.1. java based container configuration

基于 java 代码配置 ioc 容器，也就是注解配置。

- spring 注解配置的中心就是 `@Bean` 用于标记方法与 `@Configuration` 标记的类。
- `@Bean` 用以标记方法实例化、配置并初始化一个 IoC 容器管理的 object ，类似 `<beans/>` 配置文件中的 `<bean>` 元素。
- `@Bean` 可置于 `@Component` 标记的类中，成为 lite （轻量）Bean 模式，但一般与 `@Configuration` 配置类联用。
    - full @Configuration 与 lite @Bean 模式[reference](https://docs.spring.io/spring/docs/current/spring-framework-reference/core.html#beans-java)
        - lite @Bean 模式指 `@Bean` 不与 `@Configuration` 联用，可以是与 `@Component` 联用，也可以是与普通 java 类联用；
        - full @Configuration 下 bean 间的依赖通过调用其他 bean 方法全都实现，而 lite 模式只能通过参数实现 bean 间依赖。因而， lite 模式 bean 方法只是一个提供 bean 引用的工厂方法，没有运行时语义。这给 lite 模式带来的正面意义是在运行时没有 CGLIB 子类产生，对于类设计而言也没有限制，配置类可为 final 。？？？
        - 通常情况 `@Bean` 都与 `@Configuration` 类联用，因为 full 模式下，跨方法的引用都重定向到 **IoC 容器生命周期**管理。这就阻止了 @Bean 方法意外地被常规 java 调用而引起难以追踪的 bug 。

#### 1.2.1.1. AnnotationConfigApplicationContext 初始化 IoC 容器

使用 AnnotationConfigApplicationContext 初始化 IoC 容器。AnnotaionConfigApplicationContext 在 spring 3.0 中引进。

- 通过 `@Configuration` 配置类注册初始化

	```java
	public static void main(String[] args) {
        ApplicationContext ctx = new AnnotationConfigApplicationContext(AppConfig.class);
        MyService myService = ctx.getBean(MyService.class);
        myService.doStuff();
	}
	```

    - AppConfig 是一个 bean 配置类，集合相应的 bean ，在一个类中。
    - 可以使用无参构造一个 AnnotationConfigApplicationContext 实例，再调用其 `register(config.class)` 方法将配置类注册进去，达到同样的效果。
- 通过各个 bean 的 class 注册

	```java
	public static void main(String[] args) {
        ApplicationContext ctx = new AnnotationConfigApplicationContext(MyServiceImpl.class, Dependency1.class, Dependency2.class);
        MyService myService = ctx.getBean(MyService.class);
        myService.doStuff();
	}
	```

#### 1.2.1.2. ComponentScan

- 使用 `@ComponentScan(basePackage={"com.xxx"})` 注解在 Configuration 上将 component 扫描入 IoC Container。等同于 beans.xml 配置中的 `<context:component-scan base-package="com.xxx" />`。
- 在 AnnotationConfigurationApplicationContext 中可使用 `scan(String package)` 达到同样的效果。
- `@Configuration` 被元注解 `@Component` 所注解，所以，只要被 scan 到，同样会被注册到 IoC 容器中。

#### 1.2.1.3. AnnotationConfigWebApplicationContext

- AnnotationConfigWebApplicationContext 是 AnnotationConfigApplicationContext 的变体，用于初始化 springmvc 容器。
- 可用于注册 Spring Servlet lisener `ContextLoaderListener`、spring MVC DispatherServlet 等等。
- 
