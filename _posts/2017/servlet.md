---
title: servlet
date: 2017-09-01 14:23:30
categories: programming
tags: [programming,java,servlet]
keywords: 

---

# Servlet #

> Servlet是javaWeb中处理Http请求的核心技术，众多的处理web框架都是对它的封装而来。

<!--more-->

## Servlet的生命周期 ##

1. `init()`，生命周期中，只执行一次，当服务器装入servlet时，便执行此方法完成初始化。
2. `service()`，servlet的核心，负责响应客户的请求。每当客户请求一个HttpServlet对象时，这个对象就生成一个请求参数`ServletRequest`与一个响应参数`ServletReaponse`（同样也是对象）传递给自己的`Service()`方法，在这其中会默认调用`HttpServlet`的`doPost()`或`doGet()`方法。<!--more-->
3. `destroy()`方法，仅执行一次，当服务器停止且卸载Servlet时执行该方法。这个时候Servlet生命周期终结。

## Servlet执行的步骤 ##


1. 当客户端发起一个Http请求，Servlet接收到这个请求；
2. Servlet容器收到这个请求生成一个HttpRequest对象，并将请求中的参数信息封装在其中；
3. Servlet容器生成一个HttpResponse对象，并将这两个对象传入给Servlet的`service()`方法作为参数；
4. 在这个`Service()`方法中，HttpServlet调用HttpRequest对象的相关方法获取封装的Http请求信息；
5. HttpServlet再调用HttpResponse对象的相关方法对这些参数做出处理并生成响应数据；
6. HttpServlet将响应数据传给webClient，完成请求响应； 