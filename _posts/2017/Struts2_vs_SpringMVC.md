---
title: Struts2与SpringMVC的对比
date: 2017-08-31 02:04:38
categories: programming
tags: [programming,framework,struts2,springmvc]
---

# Struts2和SpringMVC的对比

> 同样是MVC架构的主流框架，两者在实现MVC的很多地方都有所不同。
> SpringMVC作为后起之秀，在某些方面更易用。比如，其通过中央分发器实现对控制器适配器、映射器以及视图解析器的解耦合更粒度更细。

## Struts2与SpringMVC的区别

> 两者之间的具体区别在这儿列出：

1. 在实现拦截上：
	1. Struts2的拦截属于**类级别的拦截**，拦截器中的属性也为所有方法共享，也就是一个类对应一个request；
	2. SpringMVC的拦截属于**方法级别的拦截**，一个方法对应一个request；
	3. Struts2的拦截机制是自己的，而SpringMVC的拦截是通过AOP实现的，这也就让Struts2的配置要繁杂得多；
3. 上面的原因也就导致了：
	1. SpringMVC的拦截作为方法级别的拦截独享了 request ， response 中封装的数据，而在 Struts2 中，请求响应的 Action 中的属性进行了共享，同时每次请求都会产生一个 Action 实例，一个 Action 就对应一个 Request 上下文；
2. 从入口上来讲：SpringMVC 的入口是 Servlet ，而 Struts2 的入口是 Filter；
3. AJAX的配套使用上来讲：
	1. SpringMVC 集成了 AJAX，使用起来只需 `@ResponseBody` 注解即可实现，而 Struts2 要实现 AJAX 需要额外安装插件或写代码集成进去；
2. 和Spring的衔接上讲：
	1. SpringMVC 可以实现无缝地衔接 Spring ，Struts2 与 Spring 的衔接需要配置得更繁杂，这让 SpringMVC 在项目的管理和安全上更好；
2. 从设计思想上讲：
	1. Struts2 更符合 OOP，而 SpringMVC 是在 Servlet 上扩展，和 Spring 都利用了强大的 AOP 和 DI；
2. SpringMVC 可以说是零配置，开发效率性能比 Struts2 高；
