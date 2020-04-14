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
  - [1.3. Container Extension points](#13-container-extension-points)
    - [1.3.1. 自定义 BeanPostProcessor](#131-%e8%87%aa%e5%ae%9a%e4%b9%89-beanpostprocessor)
    - [1.3.2. 自定义 BeanFactoryPostProcessor](#132-%e8%87%aa%e5%ae%9a%e4%b9%89-beanfactorypostprocessor)
    - [1.3.3. 通过 FactoryBean 自定义初始化逻辑](#133-%e9%80%9a%e8%bf%87-factorybean-%e8%87%aa%e5%ae%9a%e4%b9%89%e5%88%9d%e5%a7%8b%e5%8c%96%e9%80%bb%e8%be%91)
  - [1.4. ApplicationContext 额外功能](#14-applicationcontext-%e9%a2%9d%e5%a4%96%e5%8a%9f%e8%83%bd)
    - [1.4.1. 使用 MessageResource 做国际化](#141-%e4%bd%bf%e7%94%a8-messageresource-%e5%81%9a%e5%9b%bd%e9%99%85%e5%8c%96)
    - [1.4.2. 标准事件与自定义事件](#142-%e6%a0%87%e5%87%86%e4%ba%8b%e4%bb%b6%e4%b8%8e%e8%87%aa%e5%ae%9a%e4%b9%89%e4%ba%8b%e4%bb%b6)
      - [1.4.2.1. 内置的事件](#1421-%e5%86%85%e7%bd%ae%e7%9a%84%e4%ba%8b%e4%bb%b6)
      - [1.4.2.2. 监听器实现](#1422-%e7%9b%91%e5%90%ac%e5%99%a8%e5%ae%9e%e7%8e%b0)
    - [1.4.3. Web 应用中实例化 ApplicationContext](#143-web-%e5%ba%94%e7%94%a8%e4%b8%ad%e5%ae%9e%e4%be%8b%e5%8c%96-applicationcontext)
    - [1.4.4. 发布一个 Spring ApplicationContext 为 Java EE RAR 文件](#144-%e5%8f%91%e5%b8%83%e4%b8%80%e4%b8%aa-spring-applicationcontext-%e4%b8%ba-java-ee-rar-%e6%96%87%e4%bb%b6)
  - [1.5. Environment Abstraction](#15-environment-abstraction)
    - [1.5.1. 使用 @Profile 注解 bean](#151-%e4%bd%bf%e7%94%a8-profile-%e6%b3%a8%e8%a7%a3-bean)
    - [1.5.2. 激活项目 Profile](#152-%e6%bf%80%e6%b4%bb%e9%a1%b9%e7%9b%ae-profile)
    - [1.5.3. PropertySource Abstraction](#153-propertysource-abstraction)
    - [1.5.4. 使用 @PropertySource](#154-%e4%bd%bf%e7%94%a8-propertysource)
    - [1.5.5. Placeholder Resolution in Statement](#155-placeholder-resolution-in-statement)

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

## 1.3. Container Extension points

容器扩展[reference](https://docs.spring.io/spring/docs/current/spring-framework-reference/core.html#beans-factory-extension)

### 1.3.1. 自定义 BeanPostProcessor

- 针对 bean 初始化后执行的回调配置，在每个 bean 初始化后执行其中的回调。
- 可以配置多个，可指定其执行的顺序，通过 `Ordered` 接口或注解 `@Order`
- BeanPostProcessor 只与自己所有的上下文环境相关，所在容器之间的处理器互不干扰。
- 如果要操作容器中 BeanDefinition（bean 蓝图），要定义 BeanFactoryPostProcessor
- 代理包装就是使用 BeanPostProcessor
- ApplicationContext 自动检测 BeanPostProcessor 实现并注册到容器中，并在随后的 bean creation 中调用。
- 指定 lazy-init 属性对这两类处理器无效，因为如果其中没有其他 bean 引用 processor 不会初始化。

### 1.3.2. 自定义 BeanFactoryPostProcessor

[BeanFactoryPostProcessor](https://docs.spring.io/spring/docs/current/spring-framework-reference/core.html#beans-factory-extension)

- 可直接访问 ApplicationContext.getBean() 获取 bean 到 processor 中，但这种方法不妥之处在于其获取的 bean 未破坏了容器生命周期管理，会产生潜在的副作用
- BeanFactoryPostProcessor 同样其域范围是容器。
- 作用是操作配置的元数据
- ApplicationContext 自动检测并部署 BeanFactoryPostProcessor 的实现。
- PropertySourcesPlaceholderConfigurer 可以为 bean 从外部文件读取数据配置 bean property，外部文件是 java 标准的 Properties 格式。

  ```xml
  <bean class="org.springframework.context.support.PropertySourcesPlaceholderConfigurer">
    <property name="locations" value="classpath:com/something/jdbc.properties"/>
  </bean>
  <bean id="dataSource" destroy-method="close"
        class="org.apache.commons.dbcp.BasicDataSource">
    <property name="driverClassName" value="${jdbc.driverClassName}"/>
    <property name="url" value="${jdbc.url}"/>
    <property name="username" value="${jdbc.username}"/>
    <property name="password" value="${jdbc.password}"/>
  </bean>
  ```

  外部数据：

  ```java
  jdbc.driverClassName=org.hsqldb.jdbcDriver
  jdbc.url=jdbc:hsqldb:hsql://production:9002
  jdbc.username=sa
  jdbc.password=root
  ```

- PropertyOverrideConfigurer 与 PropertySourcesPlaceholderConfigurer 类似，但可以给配置添加默认值
- 在不同环境使用不同的配置情况下，使用自定义处理器有用，具体使用还不明白。

### 1.3.3. 通过 FactoryBean 自定义初始化逻辑

[reference](https://docs.spring.io/spring/docs/current/spring-framework-reference/core.html#beans-factory-extension)

- IoC 容器初始化逻辑的插口，如果有冗杂的初始化代码需要写，写在实现此接口的 java bean 中，而不是使用 xml 配置。
- 定义一个 bean 实现 FactoryBean 接口，此时 bean 是自己的工厂。
- 从容器中获取 FactoryBean 实例时， 在 bean id 前加上 `&`：`getBean("&beanName")` 即可。

## 1.4. ApplicationContext 额外功能

ApplicationContext 可以理解为 Spring IoC 容器。ApplicationContext 额外的功能[reference](https://docs.spring.io/spring/docs/current/spring-framework-reference/core.html#context-introduction)

### 1.4.1. 使用 MessageResource 做国际化

[reference](https://docs.spring.io/spring/docs/current/spring-framework-reference/core.html#context-introduction)

- ApplicationContext 默认实现都继承了 MessageResource 接口，只要注册了任何一个 MessageContext Bean 在容器中，即可使用其功能。
- 默认的 MessageContext 实现 `org.springframework.context.support.ResourceBundleMessageSource` ，可定义相关 ResourceBundle 用于 message 定制。
- `ReloadableResourceBundleMessageSource` 有更灵活的实现，允许从 Spring 资源中加载任何路径中的文件， `ResourceBundleMessageSource` 只能加载 classpath 中的资源文件。同时也支持热重载资源文件。

### 1.4.2. 标准事件与自定义事件

ApplicationContext 中事件的处理通过 `ApplicationEvent` 类和 `ApplicationListener` 接口完成，当发布一个 event 实现了 `ApplicationListener` 的类将被通知到此事件。这是一个典型的观察者设计模式。[reference](https://docs.spring.io/spring/docs/current/spring-framework-reference/core.html#context-introduction)

从 Spring 4.2 开始，事件处理可使用注解配置。

#### 1.4.2.1. 内置的事件

1. ContextRefreshedEvent 在 ApplicationContext 初始化或刷新时触发，在 context 未 closed，可以多次调用 `refresh()` 刷新
2. ContextStartedEvent 在 `ConfigurableApplicationContext` 接口中调用 `start()` 方法开启一个 `ApplicationContext` 时发布此事件。
3. ContextStoppedEvent 在`ConfigurableApplicationContext` 接口中调用 `stop()` 方法停止一个 context 时发布。
4. ContextClosedEvent 在 `ConfigurableApplicationContext` 接口中调用 `close()` 方法关闭一个应用时发布。
5. RequestHandledEvent 在一个使用 Spring DispatcherServlet 的 web 应用中，一个请求完成后发布此事件。
6. ServletRequestHandledEvent `RequestHandledEvent` 的子类，可以添加指定 Servlet 上下文信息。

#### 1.4.2.2. 监听器实现

[reference](https://docs.spring.io/spring/docs/current/spring-framework-reference/core.html#context-introduction)

- ApplicationContext 将自动注册为一个 `ApplicationEventPulisher`，事件发布器。
- 使用注解注册监听器 `@EventLisener` ，注解在方法之上不用再实现 `ApplicationLisener` 。
- 指定监听事件对象类型 `@EventListener({ContextStartedEvent.class, ContextRefreshedEvent.class})`

### 1.4.3. Web 应用中实例化 ApplicationContext

[reference](https://docs.spring.io/spring/docs/current/spring-framework-reference/core.html#context-create)

### 1.4.4. 发布一个 Spring ApplicationContext 为 Java EE RAR 文件

[reference](https://docs.spring.io/spring/docs/current/spring-framework-reference/core.html#context-create)

## 1.5. Environment Abstraction

Spring IoC 环境抽象。[reference](https://docs.spring.io/spring/docs/current/spring-framework-reference/core.html#beans-environment)

### 1.5.1. 使用 @Profile 注解 bean

使用 `@Profile` 注解实现 bean 的不同环境是否激活。[reference](https://docs.spring.io/spring/docs/current/spring-framework-reference/core.html#beans-environment)

- 如果 注解在配置类上，类中所有未单独使用 `@Profile` 注解的 bean 都会使用此注解。
- value 可以使用 表达式，表达式共有三种布尔运算符：与 `&`, 或 `|` , 非 `!` 。表达式运算符可以连用，但超过两个，两两之间必须使用括号确定运行顺序： `@Profile(value="prd|(test&us-east)")` 。在 xml 配置中只能使用 `!` ，使用 与 `&` 运算在 `<beans profile="production"/>` 中再内嵌一个 `<beans profile="us-central" />`
- value 是数组，多个 value 之间可以使用 `,` 分隔开
- 可以使用 `@Profile` 当元注解自定义 profile 功能注解。
- 当使用 `@Profile` 注解在配置类的 bean defined method 方法上时，可以指定同一个 bean 在不同环境配置中为不同的实例。但在这种情况下，注意同一个方法的重载方法使用不同的 profile 只有第一个的生效。解决方案，使用不同的方法名避免 bean defined method 重载。

### 1.5.2. 激活项目 Profile

激活 spring profile 方式有很多。[reference](https://docs.spring.io/spring/docs/current/spring-framework-reference/core.html#beans-environment)

1. 最直接简单方式：在 ApplicationContext 中获取 Environment 再调用其 `setProfiles()` 方法，之后还得 `refresh()` 一次刷新配置。
2. 通过指定 `spring.profiles.active="dev"` ，指定方式：
   1. 添加 JVM 系统属性：`-D` 命令行；
   2. 系统环境变量
   3. servlet context 参数
   4. JNDI 入口
   5. 测试模块可使用注解 `@ActiveProifles`

### 1.5.3. PropertySource Abstraction

[reference](https://docs.spring.io/spring/docs/current/spring-framework-reference/core.html#beans-property-source-abstraction)

- Spring 的 `Environment` 抽象通过可配置的 Property Source （属性源）提供查询操作。
- Spring 独立应用使用 `StandardEnvironment` 为默认的 Property Source 。 `StandardServletEnvironment` 还提供额外的Servlet config 和 servlet context 参数。
- `StandardServletEnvironment` property source 从高到低优先级：
    - ServletConfig parameters (if applicable — for example, in case of a DispatcherServlet context)
    - ServletContext parameters (web.xml context-param entries)
    - JNDI environment variables (java:comp/env/ entries)
    - JVM system properties (-D command-line arguments)
    - JVM system environment (operating system environment variables)
- 整个查询机制是可配的，可自定义 property source 添加到 environment 中

  ```java
  ConfigurableApplicationContext ctx = new GenericApplicationContext();
  MutablePropertySources sources = ctx.getEnvironment().getPropertySources();
  sources.addFirst(new MyPropertySource());
  ```

### 1.5.4. 使用 @PropertySource

使用此注解为 Configuration 添加外部配置文件。

- `@PropertySource("classpath:/com/${my.placeholder:default/path}/app.properties")` 其中可使用点位符添加已加入的配置作为路径
- 可以添加多个 `@PropertySource` 或自定义同功能（使用此注解当元注解）的注解在同一个 configuration 上，但不推荐，因为直接的注解会覆盖数据注解。

### 1.5.5. Placeholder Resolution in Statement

[reference](https://docs.spring.io/spring/docs/current/spring-framework-reference/core.html#beans-property-source-abstraction)

使用已定义好的配置：

```xml
<beans>
    <import resource="com/bank/service/${customer}-config.xml"/>
</beans>
```
