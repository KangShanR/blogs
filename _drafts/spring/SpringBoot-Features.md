---
date: 2020-07-23 8:29:00
tags: [java,SpringBoot]
categories: programming
description: Spring Boot Features
---

# Spring Boot Features

[SpringBoot 的特性](https://docs.spring.io/spring-boot/docs/current-SNAPSHOT/reference/htmlsingle/#boot-features)

## Profiles

Spring Profiles 提供隔离配置的功能，可以做到不同环境生效不同的配置。在 `@Component` `@Configuration` `@ConfigurationProperties` 上添加 `@Profile` 注解即可指定其生效环境。如果 `@ConfigurationProperties` 的配置 bean 是通过 `@EnableConfigurationProperties` 注册而不是通过自动扫描，`@Profile` 需要注解在配置组件 `@Configuration` 类上。

指定 profile 的方式有： 添加配置 `spring.profiles.active=dev,test` 的形式，也可以是通过命令行参数添加 `--spring.profiles.active=pro`。
