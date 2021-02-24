---
title: Spring Test
layout: post
date: 2020-04-21 12:08:38
tags: [Java,Spring,test]
categories: Spring
description: spring test 的应用
---

# 1. Spring Test

[reference](https://docs.spring.io/spring/docs/current/spring-framework-reference/testing.html#testing-introduction)

- [1. Spring Test](#1-spring-test)
  - [1.1. 指定测试上下文 Spring TestContext Framework](#11-指定测试上下文-spring-testcontext-framework)
    - [1.1.1. Context Management](#111-context-management)
      - [1.1.1.1. Context Configuration with Context Initializers](#1111-context-configuration-with-context-initializers)
      - [1.1.1.2. Context Configuration Inheritance](#1112-context-configuration-inheritance)
      - [1.1.1.3. **Context Configuration with Environment Profiles**](#1113-context-configuration-with-environment-profiles)
      - [1.1.1.4. Context Configuration with Test Property Source](#1114-context-configuration-with-test-property-source)
      - [1.1.1.5. Declaring Test Property Source](#1115-declaring-test-property-source)
      - [1.1.1.6. Default Properties File Detection](#1116-default-properties-file-detection)
      - [1.1.1.7. Precedence](#1117-precedence)
      - [1.1.1.8. Inheriting and Overriding Test Property Source](#1118-inheriting-and-overriding-test-property-source)
      - [1.1.1.9. Context Configuration with Dynamic Property Sources](#1119-context-configuration-with-dynamic-property-sources)
      - [1.1.1.10. Loading a WebApplicationContext](#11110-loading-a-webapplicationcontext)
      - [Context Cache](#context-cache)

<!-- /TOC -->

使用 `@TestPropertySource` 指定配置文件。

- `Environment` `PropertyResolver` 配置信息解析基础接口。[reference](https://docs.spring.io/spring/docs/current/spring-framework-reference/testing.html#testcontext-ctx-management-property-sources)<!--more-->

## 1.1. 指定测试上下文 Spring TestContext Framework

### 1.1.1. Context Management

[reference](https://docs.spring.io/spring/docs/current/spring-framework-reference/testing.html#testcontext-ctx-management-javaconfig)

> @SpringJunitWebConfig 注解包含了 @ContextConfiguration 与 @ExtentionWith(SpringExtention.class), @WebAppConfiguration ，使用此注解不用再添加 @ContextConfiguration 与 @ExtentionWith(SpringExtention.class)

- 通过注解 `@ContextConfiguration` 引入测试上下文，可以指定多个配置文件为上下文无数据：xml 配置文件(location/value 默认属性)、Groovy scripts 配置文件(location/value 默认属性)、java based configuration 文件
- xml 与 groovy script 配置属性为 location ，省略此属性会自动检测与 Test 文件同名的配置文件。
- 指定基于 java 注解的配置文件时，使用属性 `class`属性，只要是 SpringBean java 配置文件都可以指定，包括没有使用 Spring 注解但因为其唯一的构造器而被当作 Spring bean 的 java 文件。也包括提供 Bean 方法的类。同样如果此属性缺失，SpringTestContext 框架会自动检测默认的配置文件。`AnnotationConfigContextLoader` 与 `AnnotationWebConfigContextLoader` 会自动检测测试类内嵌的静态配置类。内嵌配置类名可以任意定，同样一个测试类中可以有任意多个内嵌配置类。

Mixing XML,Groovy Scripts, and Component Classes**

> 混合多类型资源的上下文

当需要混合三各类型的配置组件到上下文中时，第三方框架 SpringBoot 支持同时指定。但基于历史原因，Spring 标准部署并不支持。大体上 Spring 框架中 Spring-test 模块 `SmartContextLoader` 实现对每个测试上下文只支持单一资源类型，但并意味着只能使用一种资源类型。例外是 `GenericGroovyXmlContextLoader` 与 `GenericGroovyXmlWebContextLoader` 支持同时加载 **XML 配置文件与 Groovy scripts** 。此外，第三方框架 `@ContextConfiguration` 支持同时指定 `location` 与 `classes` 属性。并且就算在标准的测试框架中，也可以使用选择一种资源类型作为切入口，在切入口中添加其他类型的资源到其中来实现加载多种类型资源到上下文。

#### 1.1.1.1. Context Configuration with Context Initializers

在 `@ContextConfiguration` 注解中添加  **initializers** 属性给上下文指定**初始化器**，初始化器实现 `ApplicationContextInitializer` ，其作用在于初始化配置的上下文 ConfigurableApplicationContext （注册 Property Source ，激活 Profile 。ContextLoader 与 FrameworkServlet 分别有定义 `contextInitializersClasses` context-param init-param）。每个初始化器都需要支持其实现的 Ordered 接口或 `@Order` 注解用以保持其初始化动作的秩序。

也可以在 `@ContextConfiguration` 中不指定配置文件(locations,values, classes 属性)只添加初始化器 initializers ，在初始化器中指定配置上下文数据。

#### 1.1.1.2. Context Configuration Inheritance

上下文配置层级

`@ContextConfiguration` 注解属性 `inheritLocations` `inheritInitializers` 用以指定是否保留上下文配置的继承关系，默认为 true 。这意味着默认情况下，测试类间的上下文数据（resource locations,component classes, context initializers）都是自上而下继承的。如有重复，子类覆盖父类。

#### 1.1.1.3. **[Context Configuration with Environment Profiles](https://docs.spring.io/spring/docs/current/spring-framework-reference/testing.html#testcontext-ctx-management-inheritance)**

Spring 框架有一流的环境配置概念支持，测试中使用注解 `@ActiveProfiles` 以支持为测试加载 ApplicationContext 时激活的 Profiles 。@ActiveProfiles 支付 `SmartContextLoader` 但不支持老版的 `ContextLoader` SPI 实现。

- profile 在没有指定的情况下是 `default` ，可以使用此缺省的 profile 。
- @ActiveProfiles 激活注解同样支持上下层级继承，断开继承关系使用属性 `inheritProfiles` 属性。
- 如果需要在运行时决定激活 profile ，可以自定义 `ActiveProfileResolver` ，并使用 `resolver` 属性注册到 @ActiveProfiles 中。

#### 1.1.1.4. [Context Configuration with Test Property Source](https://docs.spring.io/spring/docs/current/spring-framework-reference/testing.html#testcontext-ctx-management-inheritance)

与 @Configuration 配置上的 @PropertySource 类似，测试上下文的 @TestPropertySource 注解同样引入配置数据到上下文。

- 同样在 SmartContextLoader 上支持，而老版 ContextLoader 不支持。

#### 1.1.1.5. Declaring Test Property Source

@TestPropertySource 中的 locations, value 属性用以指定配置文件。三种方式：

1. 简平式 "fig/fig.properties" 指定包相对路径与当前测试文件同一个包
2. 以 slash `/` 开头指定绝对路径
3. 以资源协议开头的路径 "classpath:,file:, http:" 使用指定协议定位资源

- 可在 @TestPropertySource 中指定 properties 属性指定 key-value 属性配置，其拥有最高优先权。key-value 语法有三种形式：

1. key=value
2. key:value
3. key value

- 从 Spring Framework 5.2 开始，同一个测试类中可以指定多个 @TestPropertySource 后者覆盖前者。
- 还可以使用派生自 @TestPropertySource 组合多个注解添加配置数据，但原生的 @TestPropertySource 优先权更高会覆盖派生注解。

#### 1.1.1.6. Default Properties File Detection

当 @TestPropertySource 注解没有指定属性数据位置，当会按 clathpath: 协议自动检测与测试类同名的属性文件。

#### 1.1.1.7. Precedence

使用 @TestPropertySource 注解添加的配置值优先级超过系统环境变量、java 系统属性、@PropertySource 注解等方式添加的配置值，其中 properties 属性添加的 inlined 属性高于 location 引入的外部配置文件。但 @DynamicPropertySource 注解引入的配置值有更高的优先级。

#### 1.1.1.8. Inheriting and Overriding Test Property Source

@TestPropertySource 注解同样有属性 inheritLocations, inheritProperties 用以指定是否自上继承配置数据，默认为 true 。

#### 1.1.1.9. [Context Configuration with Dynamic Property Sources](https://docs.spring.io/spring/docs/current/spring-framework-reference/testing.html#testcontext-ctx-management-inheritance)

- @DynamicPropertySource 注解需要用在一个静态方法上，方法参数为是一个 DynamicPropertyRegistry;
- 需要指定外部容器在测试类上 @TestContainer
- @DynamicPropertySource 有比 @TestPropertySource 更高的优先级

#### 1.1.1.10. [Loading a WebApplicationContext](https://docs.spring.io/spring/docs/current/spring-framework-reference/testing.html#testcontext-ctx-management-web)

直接注入 Mock, Context 使用

#### [Context Cache](https://docs.spring.io/spring/docs/current/spring-framework-reference/testing.html#testcontext-ctx-management-web)

上下文缓存，使用了缓存，后续测试会更快。

使用上下文缓存只在于测试类使用了相同的上下文，且在同一次编译启动中才能使用，如果测试类使用了不同的上下文（@ContextConfiguration initializers, classes, location）profiles, properties ，或者测试之间重启了 Context 都不能使用缓存。
