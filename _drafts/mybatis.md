---
layout: "post"
title: "mybatis"
date: "2018-12-04 10:24"
---

# mybatis

- 当单独使用 mybatis 没有和 spring 框架整合时，mybatis 的配置文件 mybatis.xml 需要与其他属性文件放在同一个文件夹里，因为在 mybatis.xml 中，不能使用 `<context:property-placeholder location="classpath:path-value.properties" />`。在 mybatis.xml 中引入属性文件是：
  ```
  <configuration>
    <properties resource="jdbc.properties" />

    <!-- other configuration... -->
    
  </configuration>
  ```
