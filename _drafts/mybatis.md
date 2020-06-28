---
layout: "post"
date: "2018-12-04 10:24"
---

# mybatis

[reference](https://mybatis.org/mybatis-3/zh/configuration.html)

- 当单独使用 mybatis 没有和 spring 框架整合时，mybatis 的配置文件 mybatis.xml 需要与其他属性文件放在同一个文件夹里，因为在 mybatis.xml 中，不能使用 `<context:property-placeholder location="classpath:path-value.properties" />`。在 mybatis.xml 中引入属性文件是：

  ```xml
  <configuration>
    <properties resource="jdbc.properties" />

    <!-- other configuration... -->

  </configuration>
  ```

## sql 实现

- 动态 sql 实现 [动态 sql 参考](http://www.mybatis.org/mybatis-3/zh/dynamic-sql.html)
    - 其中包括 if/where/choose(when/otherwise)/foreach/trim/set/ 诸多方法
- 使用 java 注解写 sql 语句（如： @Select("select * from t_user where t_user.name like concat(#{username}, '%')")
    - 使用这种方法实现 sql 也可以，但这样实现需要保证 entity 对象与数据库表数据 字段映射有所实现（在其他的 mapper.xml 中已经实现并注册到 mybatis 中）。如果没有映射需要保证表中字段与 entity 字段一致（大多数情况下命名规则会有出入）[参考](http://www.mybatis.org/mybatis-3/zh/getting-started.html)此文档有涉及到使用 注解实现写 sql 语句。
    - 对于简单语句来说，注解使代码显得更加简洁，然而 Java 注解对于稍微复杂的语句就会力不从心并且会显得更加混乱。因此，如果你需要做很复杂的事情，那么最好使用 XML 来映射语句。

## mybatis 的配置

[reference](https://mybatis.org/mybatis-3/zh/configuration.html)

- defaultEnumTypeHandler 指定 Enum 使用的默认 TypeHandler 。（新增于 3.4.5）	一个类型别名或全限定类名。	org.apache.ibatis.type.EnumTypeHandler

### 映射器

[参考](http://www.mybatis.org/mybatis-3/zh/configuration.html#mappers)

- 映射器的配置是配置 sql 语句 xml 文件，使用 `<mapper resource="mappings/user.xml">` 将其配置 mybatis 资源中去，自动将 此 xml 的命名空间的 interface 映射到 mybatis 中去。
- 如果出现有的只用注解在 interface 上将 sql 语句写出来，这种使用 `<mapper class="classpath"` 将其注册到 mybatis 中去。
- 批量的这种可以使用 `<mapper package="packagepath"` 这样就容易造成 interface 与第一种注册方法相重复注册的情况。所以如果想使用包名批量注册要注意是否会引起重复将 interface 注册。
