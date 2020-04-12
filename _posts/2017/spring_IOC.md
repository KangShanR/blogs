---
title: spring反转控制的思想
date: 2017-09-15 12:14:38
tags: [framework,java]
categories: programming

---

# 1. Spring IoC

<!-- TOC -->

- [1. Spring IoC](#1-spring-ioc)
  - [1.1. 反向控制（Inverse of Control)](#11-%e5%8f%8d%e5%90%91%e6%8e%a7%e5%88%b6inverse-of-control)
    - [1.1.1. 举例](#111-%e4%b8%be%e4%be%8b)
  - [1.2. Container Configuration](#12-container-configuration)
    - [1.2.1. java based container configuration](#121-java-based-container-configuration)
      - [1.2.1.1. AnnotationConfigApplicationContext 初始化 IoC 容器](#1211-annotationconfigapplicationcontext-%e5%88%9d%e5%a7%8b%e5%8c%96-ioc-%e5%ae%b9%e5%99%a8)
      - [1.2.1.2. ComponentScan](#1212-componentscan)
      - [1.2.1.3. AnnotationConfigWebApplicationContext](#1213-annotationconfigwebapplicationcontext)
  - [1.3. ApplicationContext 额外功能](#13-applicationcontext-%e9%a2%9d%e5%a4%96%e5%8a%9f%e8%83%bd)
    - [1.3.1. 使用 MessageResource 做国际化](#131-%e4%bd%bf%e7%94%a8-messageresource-%e5%81%9a%e5%9b%bd%e9%99%85%e5%8c%96)
    - [1.3.2. 标准事件与自定义事件](#132-%e6%a0%87%e5%87%86%e4%ba%8b%e4%bb%b6%e4%b8%8e%e8%87%aa%e5%ae%9a%e4%b9%89%e4%ba%8b%e4%bb%b6)
      - [1.3.2.1. 内置的事件](#1321-%e5%86%85%e7%bd%ae%e7%9a%84%e4%ba%8b%e4%bb%b6)
      - [1.3.2.2. 监听器实现](#1322-%e7%9b%91%e5%90%ac%e5%99%a8%e5%ae%9e%e7%8e%b0)

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

## 1.3. ApplicationContext 额外功能

ApplicationContext 可以理解为 Spring IoC 容器。

### 1.3.1. 使用 MessageResource 做国际化

[reference](https://docs.spring.io/spring/docs/current/spring-framework-reference/core.html#context-introduction)

- ApplicationContext 默认实现都继承了 MessageResource 接口，只要注册了任何一个 MessageContext Bean 在容器中，即可使用其功能。
- 默认的 MessageContext 实现 `org.springframework.context.support.ResourceBundleMessageSource` ，可定义相关 ResourceBoundle 用于 message 定制。
- `ReloadableResourceBundleMessageSource` 有更灵活的实现，允许从 Spring 资源中加载任何路径中的文件， `ResourceBoundleMessageSource` 只能加载 classpath 中的资源文件。同时也支持热重载资源文件。

### 1.3.2. 标准事件与自定义事件

ApplicationContext 中事件的处理通过 `ApplicationEvent` 类和 `ApplicationListener` 接口完成，当发布一个 event 实现了 `ApplicationListener` 的类将被通知到此事件。这是一个典型的观察者设计模式。[reference](https://docs.spring.io/spring/docs/current/spring-framework-reference/core.html#context-introduction)

从 Spring 4.2 开始，事件处理可使用注解配置。

#### 1.3.2.1. 内置的事件

1. ContextRefreshedEvent 在 ApplicationContext 初始化或刷新时触发，在 context 未 closed，可以多次调用 `refresh()` 刷新
2. ContextStartedEvent 在 `ConfigurableApplicationContext` 接口中调用 `start()` 方法开启一个 `ApplicationContext` 时发布此事件。
3. ContextStopedEvent 在`ConfigurableApplicationContext` 接口中调用 `stop()` 方法停止一个 context 时发布。
4. ContextClosedEvent 在 `ConfugurableApplicationContext` 接口中调用 `close()` 方法关闭一个应用时发布。
5. RequestHandledEvent 在一个使用 Spring DispatcherServelt 的 web 应用中，一个请求完成后发布此事件。
6. ServletRequesthandledEvent `RequestHandledEvent` 的子类，可以添加指定 Servlet 上下文信息。

#### 1.3.2.2. 监听器实现

[reference](https://docs.spring.io/spring/docs/current/spring-framework-reference/core.html#context-introduction)

- ApplicationContext 将自动注册为一个 `ApplicationEventPulisher`，事件发布器。
- 使用注解注册监听器 `@EventLisener` ，注解在方法之上不用再实现 `ApplicationLisener` 。
- 指定监听事件对象类型 `@EventListener({ContextStartedEvent.class, ContextRefreshedEvent.class})`
- 
