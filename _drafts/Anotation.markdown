---
title: 项目中注解的使用
date: "2017-11-17 11:44"
categories: programming
tag: [java,programming]
---

# 注解的使用

## 注解 @Autowired 的使用

单词： autowired 的意思就是自动装配。而在 java 编程中使用此注解就是将标注过注解的都自动装配到 spring 容器中。

据此理解的话，在 编码中：
- 如果是属性被此注解标注，则此属性就将这个 bean 注入到容器中，不用写此属性的 getter() 与 setter() 方法，在 bean.xml 配置中也不用写此属性的 <property> 标签了；
- 如果方法或构造函数被 @Autowired 注解，则此方法参数中的 bean 就会自动被查找装入到这个方法中；

## 注解 @ResponseBody

此注解标明这个方法返回不经过视图解析器处理，直接将处理信息写成字节流返回给浏览器，成为 json 对象写到浏览器页面中。

## @RequestBody

- 此注解将请求的 body 部分数据使用 converter 解析并将相应的数据绑定到要返回的对象上；
- 使用 converter 解析的结果绑定到 controller 中的方法的参数上；

## @RequiresPermissions

shiro 框架中的权限验证注解，用于验证是否拥有某权限。
