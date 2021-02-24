---
layout: post
title: "Spring IoC"
date: 2017-09-15 12:14:38
tags: [Spring, Java]
categories: Spring
---

# 1. Spring IoC

<!-- TOC -->

- [1. Spring IoC](#1-spring-ioc)
  - [1.1. 反向控制（Inverse of Control)](#11-反向控制inverse-of-control)
    - [1.1.1. 举例](#111-举例)
  - [1.2. Container Configuration](#12-container-configuration)
    - [1.2.1. java based container configuration](#121-java-based-container-configuration)
      - [1.2.1.1. AnnotationConfigApplicationContext 初始化 IoC 容器](#1211-annotationconfigapplicationcontext-初始化-ioc-容器)
      - [1.2.1.2. ComponentScan](#1212-componentscan)
      - [1.2.1.3. AnnotationConfigWebApplicationContext](#1213-annotationconfigwebapplicationcontext)
      - [Fine-tuning Annotation-based Autowiring with Qualifiers](#fine-tuning-annotation-based-autowiring-with-qualifiers)
  - [1.3. Container Extension points](#13-container-extension-points)
    - [1.3.1. 自定义 BeanPostProcessor](#131-自定义-beanpostprocessor)
    - [1.3.2. 自定义 BeanFactoryPostProcessor](#132-自定义-beanfactorypostprocessor)
    - [1.3.3. 通过 FactoryBean 自定义初始化逻辑](#133-通过-factorybean-自定义初始化逻辑)
  - [1.4. ApplicationContext 额外功能](#14-applicationcontext-额外功能)
    - [1.4.1. 使用 MessageResource 做国际化](#141-使用-messageresource-做国际化)
    - [1.4.2. 标准事件与自定义事件](#142-标准事件与自定义事件)
      - [1.4.2.1. 内置的事件](#1421-内置的事件)
      - [1.4.2.2. 监听器实现](#1422-监听器实现)
    - [1.4.3. Web 应用中实例化 ApplicationContext](#143-web-应用中实例化-applicationcontext)
    - [1.4.4. 发布一个 Spring ApplicationContext 为 Java EE RAR 文件](#144-发布一个-spring-applicationcontext-为-java-ee-rar-文件)
  - [1.5. Environment Abstraction](#15-environment-abstraction)
    - [1.5.1. 使用 @Profile 注解 bean](#151-使用-profile-注解-bean)
    - [1.5.2. 激活项目 Profile](#152-激活项目-profile)
    - [1.5.3. PropertySource Abstraction](#153-propertysource-abstraction)
    - [1.5.4. 使用 @PropertySource](#154-使用-propertysource)
    - [1.5.5. Placeholder Resolution in Statement](#155-placeholder-resolution-in-statement)
  - [1.6. BeanFactory](#16-beanfactory)

<!-- /TOC -->

> Spring 说到底就是一个轻量级的容器，让它来负责各个实例的生产、管理、维护，而这些实例的参数与依赖关系都交由 spring 的配置文件来设置；<!--more-->

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
- 可用于注册 Spring Servlet listener `ContextLoaderListener`、spring MVC DispatcherServlet 等等。

#### Fine-tuning Annotation-based Autowiring with Qualifiers

[reference](https://docs.spring.io/spring/docs/current/spring-framework-reference/core.html#beans-autowired-annotation-primary)

微调 bean 注入。使用注解 `@Qualifier` 在bean 定义与bean 注入处添加数据用以鉴别 bean 。

- bean 定义处可以不使用 `@Qualifier` ，注入处会自动使用 beanName 。
- `@AutoWired` 注入的策略是先使用 Type 匹配，匹配到多个时会使用 `@Qualifier` 指定的 value 匹配。
- `@Resource` 注解注入匹配策略只使用其唯一名 unique name，type 定义与其无关。
- 如果 bean 定义为一个集合，数组或 Map，使用 `@Resource` 可直接匹配其 beanName 得到此集合。
- 通过 `@AutoWired` 注入时有多个 bean Type 匹配，此时可以添加 `@Qualifier` 数据获取。
- `@AutoWired` 自注入（截止到 Spring 4.3），在这种场景自注入的 self-bean 优先级最低，不能为 Primary 。
- 在同一个 Configuration 类 @Bean 方法中注入自身是解决自引用的问题更高效。

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

Note:

**Programmatically registering BeanPostProcessor instances**

- ApplicationContext 自动检测注册 BeanPostProcessor 外，还可以通过 ConfigurableBeanFactory.addBeanPostProcess() 手动注册。
- 手动注册将忽略 processor 的 order 属性，直接使用手动注册的顺序作为 process() 的顺序。同时手动注册的优先于 ApplicationContext 自动检测注册的 processor。

**BeanPostProcessor instances and AOP auto-proxying**

- 容器对实现 BeanPostProcessor 的类专门处理。所有的 BeanPostProcessor 的实例与 bean 直接引用都在 ApplicationContext 启动时初始化，作为其启动阶段之一。
- 所有的 BeanPostProcessor 实例都有序注册并都在后来相应的阶段处理容器中的 Bean。
- 因为 Spring AOP 自动代理实现本身就是一个 BeanPostProcessor ，对于 BeanPostProcessor 实例，不管是其本身还是其直接依赖的 bean 都是自动代理的对象，所以不要对其编织切面。这于这类错误添加切面的 bean ，会出现日志信息：`Bean someBean is not eligible for getting processed by all BeanPostProcessor interfaces (for example: not eligible for auto-proxying)`。
- 当在 BeanPostProcessor 使用 `@AutoWired`（自动装配） 或 `@Resource`(此类装配策略可能会退化为自动装配)装配了其他 Bean 。Spring 会在类型匹配查找时访问到不需要的 Bean 。所以需要让这些 Bean 对自动代理或其他类型的 Bean Post-Processing 失效。比如：有一个被 @Resource 注解的依赖，字段或 setter 名不能与一个与 bean 声明的名匹配且没有名字属性被使用，Spring 将访问与其他 type 相匹配的 bean 。

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

## 1.6. BeanFactory

[Spring IoC 容器基础](https://docs.spring.io/spring/docs/current/spring-framework-reference/core.html#beans-beanfactory)

- 整个框架组件都基于 BeanFactory 及 相关的接口（`BeanFactoryAware` `InitializingBean`  `DisposableBean`）整个而成。
- BeanFactory 级别的 API （其默认实现： DefaultListableBeanFactory） 只是单纯的一个工厂，并没对应用相关的组件进行设定，配置格式与注解组件都未配置。
- `ApplicationContext` 是 `BeanFactory` 增强，包含了BeanFactory 所有的功能，其实现（eg: GenericApplicationContext）会按惯例扫描各类 bean 。ApplicationContext 的各类变体都会扩展各类功能，如添加 BeanFactoryPostProcessor BeanPostProcessor。
- `AnnotationConfigApplicationContext` 除了添加了 post-processor 等组件外，还通过注解添加其他组件，如：`@EnableTransactionManagement`。在 Spring 注解配置模式中，post-processor bean 的概念仅仅只是容器内部细节。
