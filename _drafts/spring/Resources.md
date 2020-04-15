---
title: "Resources in Spring"
date: "2020-4-15 14:48"
categories: programming
tags: [Resource]
---

# 1. Resources in Spring
<!-- TOC -->

- [1. Resources in Spring](#1-resources-in-spring)
  - [1.1. 资源分类（内置的 Resource 接口实现）](#11-%e8%b5%84%e6%ba%90%e5%88%86%e7%b1%bb%e5%86%85%e7%bd%ae%e7%9a%84-resource-%e6%8e%a5%e5%8f%a3%e5%ae%9e%e7%8e%b0)
  - [1.2. ResourceLoader](#12-resourceloader)
  - [1.3. Application Contexts and Resource Paths](#13-application-contexts-and-resource-paths)
    - [1.3.1. Constructing a Application Contexts](#131-constructing-a-application-contexts)
      - [1.3.1.1. 创建 ClassPathXmlApplicationContext 实例快捷方式](#1311-%e5%88%9b%e5%bb%ba-classpathxmlapplicationcontext-%e5%ae%9e%e4%be%8b%e5%bf%ab%e6%8d%b7%e6%96%b9%e5%bc%8f)
  - [1.4. 占位符](#14-%e5%8d%a0%e4%bd%8d%e7%ac%a6)

<!-- /TOC -->

[Spring 中的资源](https://docs.spring.io/spring/docs/current/spring-framework-reference/core.html#resources)

## 1.1. 资源分类（内置的 Resource 接口实现）

- UrlResource
- 包装一个 `java.net.URL` 可以访问任何一个能通过 URL 访问的对象。
- 所有的 URL 都有一个相应的 String 标明其路径。不同的前缀表示不同的 URL 类型。
    - `file:` 表示文件系统路径
    - `http:` 表示通过 HTTP 协议访问的资源
    - `ftp:` 通过 ftp 访问的资源
- ClassPathResource
- FileSystemResource
- ServletContextResource
- InputStreamResource
- ByteArrayResource

## 1.2. ResourceLoader

- 通过 String path 获取资源。会根据 path 前缀的不同来获取到不同类型的资源。
    - `classpath:` 从 classpath 中加载
    - `file:` 从文件系统中加载一个 URL
    - `http:` 加载一个远程连接 URL
    - (none): 根据 ApplicationContext 决定
- ApplicationContext 本身有实现此接口，所有当有 bean 实现了 ResourceLoaderAware 接口可以使用 context 作为 loader 。
- 推荐自注册一个 ResourceLoader ，直接使用 context 会将整个 context 耦合到 ResourceLoader 。
- `@AutoWired` 注解可直接注册一个 ResourceLoader

## 1.3. Application Contexts and Resource Paths

### 1.3.1. Constructing a Application Contexts

```java
ApplicationContext ctx = new ClassPathXmlApplicationContext("conf/appContext.xml");
ApplicationContext ctx =new FileSystemXmlApplicationContext("conf/appContext.xml");
```

#### 1.3.1.1. 创建 ClassPathXmlApplicationContext 实例快捷方式

当配置文件与某一个 java 文件在同一个路径中，可以使用 java class 文件的路径信息获取相应的文件信息。

```java
ApplicationContext ctx = new ClassPathXmlApplicationContext(new String[] {"services.xml", "daos.xml"}, MessengerService.class);
```

## 1.4. 占位符

[reference](https://docs.spring.io/spring/docs/current/spring-framework-reference/core.html#resources-implementations)

- 本质上使用 ClassLoader.getResource() 加载资源。
- `classpath:*` 与 Ant-Style patterns 连用
    - 如果目标文件没有在文件系统中，只有在有至少一个根目录时才会稳定地查找到文件（反例： `classpath*:*.xml`）。
    - 当有多个 classloader location 时，使用 `classpath:` 前缀会导致只有一个 class loader getResource() 时会正确查找，解决方案： 使用 `classpath*:` 为前缀
