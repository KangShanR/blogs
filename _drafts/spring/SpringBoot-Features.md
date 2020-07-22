---
date: 2020-07-22 19:45:00
tags: [java,SpringBoot]
categories: programming
description: Spring Boot Features
---

# Spring Boot Features

[SpringBoot Features](https://docs.spring.io/spring-boot/docs/current-SNAPSHOT/reference/htmlsingle/#boot-features)

## Application Availability

[系统可用性](https://docs.spring.io/spring-boot/docs/current-SNAPSHOT/reference/htmlsingle/#boot-features-application-availability)

需要在程序启动阶段添加任务可以通过将 bean 实现接口 `ApplicationRunner` 或 `CommandLineRunner`，而不是使用 Spring 组件生命周期回调 `@PostConstruct`。以上两个接口分别可获取到程序启动参数和命令行参数。
