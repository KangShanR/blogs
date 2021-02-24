---
title: Spring Boot Features
layout: post
date: 2020-07-23 8:29:00
tags: [java,SpringBoot]
categories: SpringBoot
description: Spring Boot Features
---

# Spring Boot Features

[SpringBoot 的特性](https://docs.spring.io/spring-boot/docs/current-SNAPSHOT/reference/htmlsingle/#boot-features)

## Profiles

Spring Profiles 提供隔离配置的功能，可以做到不同环境生效不同的配置。在 `@Component` `@Configuration` `@ConfigurationProperties` 上添加 `@Profile` 注解即可指定其生效环境。如果 `@ConfigurationProperties` 的配置 bean 是通过 `@EnableConfigurationProperties` 注册而不是通过自动扫描，`@Profile` 需要注解在配置组件 `@Configuration` 类上。

指定 profile 的方式有： 添加配置 `spring.profiles.active=dev,test` 的形式，也可以是通过命令行参数添加 `--spring.profiles.active=pro`。<!--more-->

## SpringBoot Testing

[reference](https://docs.spring.io/spring-boot/docs/current/reference/htmlsingle/#boot-features-testing-spring-boot-applications-testing-with-mock-environment)

使用 SpringBoot Test 相对于 Spring更为方便一些。如果使用 Junit5 只需要一个注解 `@SpringBootTest`

- 可以使用服务端测试，也可以使用客户端测试。

## logging

[reference](https://docs.spring.io/spring-boot/docs/current/reference/htmlsingle/#boot-features-logging)

- 使用配置 logging.file.name 与 logging.file.path 指定日志文件名与路径，如果不指定文件名仅打印到控制台，如果不指定文件路径将写文件在当前路径。

### logging level

- 指定日志级别 `logging.level.<logger-name>=<level>` logger 指日志器，可以是类或包
- 可以在环境变量中添加参数指定日志级别 `LOGGING_LEVEL_ORG_SPRINGFRAMEWORK_WEB=DEBUG` ，但这不指定类的日志器，因为环境变量中会将所有大写转换成小写。使用 SPRING_APPLICATION_JSON 变量（在命令行参数、系统变量、命令行参数中添加 Json）实现。

### Log Groups

- 直接定义多个类或包到同一个组 `logging.level.tomcat=org.apache.catalina,org.apache.coyote` ，再直接指定组的日志级别 `logging.level.tomcat=DEBUG`
- Spring Boot 预定义了日志组 sql 与 web

### Custom Log Configuration

[reference](https://docs.spring.io/spring-boot/docs/current/reference/htmlsingle/#boot-features-logback-extensions)

在系统变量中指定日志系统 `org.springframework.boot.logging.LoggingSystem` ，此值必须是 LoggingSystem 的实现类全限定名。同时此属性只能在系统属性中指定，因为日志开头在 ApplicationContext 初始化完成前就会使用日志系统。

- LoggingSystem 实现有三种：Logback/Log4j2/JavaUtilLogging ，其中 Logback 功能最全。
- 通过配置文件或 Environment 属性 `logging.config` 可以自定义日志系统

## JSON

[reference](https://docs.spring.io/spring-boot/docs/current/reference/htmlsingle/#boot-features-json)

Spring Boot 整合了三种 JSON 映射库：

1. Gson，SpringBoot 自动配置为 bean 并可以通过 spring.gson.* 配置多个属性，更详细地控制可以通过 GsonBuilderCustomizer bean 实现。
2. Jackson，Spring 偏好并默认的。当 Jackson 在 classpath 中时， 一个 ObjectMapper bean 会自动注入，并可自定义多个配置属性。
3. JSON-B

## Developing Web Applications

[reference](https://docs.spring.io/spring-boot/docs/current/reference/htmlsingle/#boot-features-developing-web-applications)

### The Spring Web MVC Framework

#### Custom JSON Serializers and Deserializers

@JsonComponent 注解添加 jackson 序列化反序列化器到容器中。
