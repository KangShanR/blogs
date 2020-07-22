---
date: 2020-07-22 19:45:00
tags: [java,SpringBoot]
categories: programming
description: Spring Boot SpringApplication
---

# 1. SpringApplication

<!-- TOC -->

- [1. SpringApplication](#1-springapplication)
  - [1.1. Application Availability](#11-application-availability)
    - [1.1.1. Liveness State](#111-liveness-state)
    - [1.1.2. Readiness State](#112-readiness-state)
    - [1.1.3. Managing the Application Availability State](#113-managing-the-application-availability-state)
  - [1.2. Application Events and Listeners](#12-application-events-and-listeners)
  - [1.3. Web Environment](#13-web-environment)
  - [1.4. Accessing Application Arguments](#14-accessing-application-arguments)
  - [Using the ApplicationRunner and CommandLineRunner](#using-the-applicationrunner-and-commandlinerunner)

<!-- /TOC -->
[SpringBoot Features](https://docs.spring.io/spring-boot/docs/current-SNAPSHOT/reference/htmlsingle/#boot-features-spring-application)

## 1.1. Application Availability

[系统可用性](https://docs.spring.io/spring-boot/docs/current-SNAPSHOT/reference/htmlsingle/#boot-features-application-availability)

可以通过注入 `ApplicationAvailability` 接口到 bean 中以获取应用可用性状态。

### 1.1.1. Liveness State

应用活性，用以表明应用内部状态是否能正常工作，或是否能从异常中自动恢复。如果不能（失活），基础平台应该重启应用。一般来说，应用活性不应该基于外部检查，否则外部检查系统（数据库、缓存、Web API）出现异常，将触发大量重启与平台累积的失败。

Spring Boot 应用的内部状态一般取决于 `ApplicationContext`。如果 ApplicationContext 成功启动，Spring Boot 就将认为应用在有效的状态。只要 context  be refreshed ，应用就被认为是有活性的。

### 1.1.2. Readiness State

应用就绪状态，用以表明应用是否对处理流量准备就绪。未进入就绪状态将告诉 platform 平台此时不宜路由流量到应用。典型的场景是在应用启动阶段，当 `CommandLineRunner` 与 `ApplicationRunner` 组件正在被处理时，或者应用忙于其他流量请求时而不能处理当前流量请求时。

需要在程序启动阶段添加任务可以通过将 bean 实现接口 `ApplicationRunner` 或 `CommandLineRunner`，而不是使用 Spring 组件生命周期回调 `@PostConstruct`。以上两个接口分别可获取到程序启动参数和命令行参数。

### 1.1.3. Managing the Application Availability State

1. 获取应用当前可用性状态：注入 `ApplicationAvailability` 接口并调用其方法；
2. 监听应用可用性变化：

```java
@EventListener
public void onStateChange(AvailabilityChangeEvent<ReadinessState> event) {
    switch (event.getState()) {
    case ACCEPTING_TRAFFIC:
        // create file /tmp/healthy
    break;
    case REFUSING_TRAFFIC:
        // remove file /tmp/healthy
    break;
    }
}
```

更新应用可用性状态

```java
private final ApplicationEventPublisher eventPublisher;
//自动 constructor 注入 publisher
public LocalCacheVerifier(ApplicationEventPublisher eventPublisher) {
    this.eventPublisher = eventPublisher;
}

public void checkLocalCache() {
    try {
        //...
    }
    catch (CacheCompletelyBrokenException ex) {
        //发布一个可用性更新事件
        AvailabilityChangeEvent.publish(this.eventPublisher, ex, LivenessState.BROKEN);
    }
}
```

## 1.2. Application Events and Listeners

[应用事件与监听器](https://docs.spring.io/spring-boot/docs/current-SNAPSHOT/reference/htmlsingle/#boot-features-application-availability)

除了常规的应用事件 `ContextRefreshedEvent`， 一个 Spring 应用还可以发送其他事件。其中一些事件在整个应用上下文 `ApplicationContext` 创建前，对于这种事件并不能注册其监听器为一个 `Bean` ，但可以通过 `SpringApplication.addListeners()` 或 `SpringApplicationBuilder.listeners()` 方法注册。如果需要不考虑上下文是否创建自动注册监听器，可以在工程中 `META-INF/spring.factories` 引入监听器，使用 key `org.springframework.context.ApplicationListener=com.myproject.MyListener` 。

应用开始启动后，事件发布顺序如下：

1. `ApplicationStartingEvent` 在应用刚开始启动，除 listeners initializers 已注册外，所有的其他处理工作都还未开启时。
2. `ApplicationEnvironmentPreparedEvent` 事件发布时机：`Environment` 在上下文中已清晰但上下文还未创建时
3. `ApplicationContextInitializedEvent` 在 `ApplicationContext` 已准备且 ApplicationInitializers 已被调用但 bean definitions 还未加载时
4. `ApplicationPreparedEvent` refreshed 开始之前 bean definitions 加载之后
5. `ApplicationStartedEvent`context refreshed 之后，application runners 与 command-line runners 调用之前
6. `AvailabilityChangeEvent` 在 `LivenessState.CORRECT` 可用性表明应用程序已具活性后
7. `ApplicationReadyEvent` 在所有的 application/command-line runner 被调用之后
8. `AvailabilityChangeEvent` 应用已为服务请求准备就绪 `ReadinessState.ACCEPTING_TRAFFIC` 之后
9. `ApplicationFailedEvent` 启动异常之后

以上 9 个事件是与 `SpringApplication` 绑定的事件，除此外，在 `ApplicationPreparedEvent` 与 `ApplicationStartedEvent` 之间还有两个事件：

1. `WebServerInitializedEvent` `WebServer` 准备就绪后。而 `ServletWebServerInitializedEvent` 与 `ReactiveWebServerInitializedEvent` 分别在 servlet 与 reactive variants 就绪后。
2. `ContextRefreshedEvent` `ApplicationContext` 刷新后 when an ApplicationContext is refreshed.

Spring Boot 就是使用以上各个事件来处理各类任务。

_根据以上顺序，倒推 Spring 应用启动的处理有：_

1. 注册 listeners 与 initializers
2. 创建环境配置 `Environment`
3. 创建上下文 ApplicationContext ，调用 ApplicationInitializers
4. 加载 bean definitions
5. 如果是 web 工程应用，加载相关 WebServer: ServletWebServer/ReactiveWebServer
6. 刷新 ApplicationContext
7. 改变应用可用性为活性 `LivenessState.CORRECT`
8. 调用 application runner command-line runner
9. 改变应用服务请求就绪状态为就绪 `ReadinessState.ACCEPTING_TRAFFIC`

Spring Framework 的事件发布机制在子 context 发布事件后，父 context 同样会收到相应的事件发布，所以如果应用使用了 SpringApplication 层级，一个监听器会收到相同类型的 application event。为了区别来自哪里，可以将 Context 注入对比。如果 listener 是个 bean 直接使用 @AutoWired 注入，如果 listener 不是 bean 需要实现 ApplicationContextAware 接口注入。

## 1.3. Web Environment

SpringApplication 根据代码行为来创建正确的 `ApplicationContext` ，决定 `WebApplicationType` 的算法是：

1. 如果存在 Spring MVC ，使用 `AnnotationConfigServletWebApplicationContext`
2. 如果不使用 Spring MVC 使用 Spring WebFlux，则使用 `AnnotationConfigReactiveWebServerApplicationContext`
3. 否则，使用 `AnnotationConfigApplicationContext`

算法决定了，如果一个应用使用了 Spring MVC 也使用了 Spring WebFlux ，将按 Spring MVC 处理。可使用 `setWebApplicationType(WebApplicationType)` 方法直接覆盖算法决定。如果要完全控制 `ApplicationContext` 使用方法 `setApplicationContestClass(...)`。在使用 JUnit 测试时，不需要 web 层，可调方法 `sebWebApplicationType(WebApplicationType.NONE)`

## 1.4. Accessing Application Arguments

[访问应用参数](https://docs.spring.io/spring-boot/docs/current-SNAPSHOT/reference/htmlsingle/#boot-features-web-environment)

在 Spring Boot 中，当需要访问应用参数时，注入一个 `ApplicationArguments` bean，即可直接访问 `String[]` 参数或编译成 option 与 not-option 形式的参数。

```java
@AutoWired
public MyBean(ApplicationArguments args) {
    boolean debug = args.containsOption("debug");
    List<String> files = args.getNonOptionArgs();
    // if run with "--debug logfile.txt" debug=true, files=["logfile.txt"]
}
```

Spring Boot 也注册了一个与 Environment 关联的 `CommandLinePropertySource`，这就意味着可以在程序中使用 `@Value` 注解直接注入各个命令行参数。

## Using the ApplicationRunner and CommandLineRunner

[使用应用、命令行Runner](https://docs.spring.io/spring-boot/docs/current-SNAPSHOT/reference/htmlsingle/#boot-features-web-environment)

如果需要在 SpringApplication 启动后执行某些代码，可以实现 `ApplicationRunner` 或 `CommandLineRunner` 接口，这两个接口都提供一个 `run(ApplicationArguments)` 方法定义，其实现都是在 `SpringApplication.run(...)` 方法完成之前调用。如果有多个 Runner 需要执行，可以在 Runner 上添加 `Order` 接口或 `@Order` 注解用以指定顺序。
