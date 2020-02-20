---
layout: "post"
title: "properties in spring"
date: "2018-12-03 15:05"
---

# properties in springmvc

> [参考文档](https://www.baeldung.com/properties-with-spring)

- 在 xml 文件中引入 properties ： `<context:property-placeholder location="classpath:foo.properties" />`
- 在配置 bean java 文件中可以使用注解将其配置：
  ```
  @Configuation
  @PropertySource("classpath:foo.properties")
  public class PropertiesWithJavaConfig{
    //...
  }
  ```
  - 另外 一个更有用的注解方法：
  ```
  @PropertySource({
    "classpath:persistence-${envTarget:mysql}.properties"})
  ```

  **note**:
  - [参考](http://www.cnblogs.com/jycboy/p/7349139.html)
  - classpath 是指被编译过后的 src 中所有的文件（包括：java、xml、properties）都放在的 WEB-INFO/classes 的文件夹。
  - `**/` 表示 任意目录。`**/mysql*.properties` 就表示任意目录下的以 `mysql` 开关的 properties 文件

- 在 java 配置文件中插入一个 property 值使用标签 `@Value` ： `@Value("${jdbc.url:defaultUrl}")` ， `:` 后面跟的是默认值。
- 在 xml 配置文件中插入一个 property 值

  ```xml
  <bean id="dataSource">
    <property name="url" value="${jdbc.url}" />
  </bean>
