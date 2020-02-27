---
title: servlet
date: 2017-09-01 14:23:30
categories: programming
tags: [programming,java,servlet]
keywords:

---

> Servlet 是 javaWe b中处理 Http 请求的核心技术，众多的处理 web 框架都是对它的封装而来。Servlet 规范源自 Sun 公司，除了 servlet 技术还包括了 filter / listener 。

## Servlet的生命周期

1. `init()`，生命周期中，只执行一次，当服务器装入 servlet 时，便执行此方法完成初始化。
2. `service()`  servle 的核心，负责响应客户的请求。每当客户请求一个 HttpServlet 对象时，这个对象就生成一个请求参数 `ServletRequest` 与一个响应参数 `ServletReaponse` （同样也是对象）传递给自己的 `Service()` 方法，在这其中会默认调用 `HttpServlet` 的 `doPost()` 或 `doGet()` 方法。<!--more-->
3. `destroy()` 方法，仅执行一次，当服务器停止且卸载 Servlet 时执行该方法。这个时候 Servlet 生命周期终结。

## Servlet 执行的步骤

1. 当客户端发起一个 Http 请求， Servlet 接收到这个请求；
   1. tomcat 收到这个请求时是如何将这个 url 转化成到另一个 servlet ？
      1. 将 xml 解析后自己处理的通过映射关系找到了这个 servlet ？
2. Servlet 容器（Tomcat 引擎）收到这个请求生成一个 HttpRequest 对象，并将请求中的参数信息封装在其中；
3. Servlet 容器（Tomcat engine）生成一个 HttpResponse 对象，并将这两个对象传入给 Servlet 的 `service()` 方法作为参数；
4. 在这个 `Service()` 方法中， HttpServlet 调用 HttpRequest 对象的相关方法获取封装的 Http 请求信息；
5. HttpServlet 再调用 HttpResponse 对象的相关方法对这些参数做出处理并生成响应数据；
6. HttpServlet 将响应数据传给 webClient ，完成请求响应；

问题：

1. 服务器上的服务是如何响应请求的？
   1. 这件事情是谁来做的呢？Tomcat engine
2. HTTP 协议规定了有请求必有响应？

## ServletContext

- servlet 上下文，一个 web 应用启动后只有一个，通过把通用的数据存放在其中，在 web.xml 使用 `<config-param>` 标签配置数据。
- 可在 servlet 直接调用 `getServletContext()` 获取。
- 用以存放数据时，可以在 `<load-on-startup>0</load-on-startup>` （此标签用以标记在应用启动时初始化的 servlet ，value 越小越先初始化，最小值为 0）的 servlet 中进行数据初始化。
