---
title: servlet
date: 2017-09-01 14:23:30
categories: programming
tags: [programming,java,servlet]
keywords:

---

# Servlet

> Servlet 是 javaWe b中处理 Http 请求的核心技术，众多的处理 web 框架都是对它的封装而来。

## Servlet的生命周期

1. `init()`，生命周期中，只执行一次，当服务器装入 servlet 时，便执行此方法完成初始化。
2. `service()`  servlet的核心，负责响应客户的请求。每当客户请求一个 HttpServlet 对象时，这个对象就生成一个请求参数 `ServletRequest` 与一个响应参数 `ServletReaponse` （同样也是对象）传递给自己的 `Service()` 方法，在这其中会默认调用 `HttpServlet` 的 `doPost()` 或 `doGet()` 方法。<!--more-->
3. `destroy()` 方法，仅执行一次，当服务器停止且卸载Servlet时执行该方法。这个时候Servlet生命周期终结。

## Servlet 执行的步骤

1. 当客户端发起一个 Http 请求， Servlet 接收到这个请求；
2. Servlet 容器收到这个请求生成一个 HttpRequest 对象，并将请求中的参数信息封装在其中；
3. Servlet 容器生成一个 HttpResponse 对象，并将这两个对象传入给 Servle 的 `service()` 方法作为参数；
4. 在这个 `Service()` 方法中， HttpServlet 调用 HttpRequest 对象的相关方法获取封装的 Http 请求信息；
5. HttpServlet 再调用 HttpResponse 对象的相关方法对这些参数做出处理并生成响应数据；
6. HttpServlet 将响应数据传给 webClient ，完成请求响应；
