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
- 可以为任意 java 模型设置别名，而在使用时就可以直接使用别名而不用写见冗长全限定名了。`alias`
- 定义 sql 代码片段时，可以使用 `${alia1}.id,${alia2}.name` 占位符，在 `<include>` 引用时确定点位符值：`<include refid=''><property name=alia1 value = user/></include>`

#### resultMap

在使用查询语句中，id 元素在嵌套结果映射中扮演着非常重要的角色。你应该总是指定一个或多个可以唯一标识结果的属性。 虽然，即使不指定这个属性，MyBatis 仍然可以工作，但是会产生严重的性能问题。

- resultMap 可以作为其他 resultMap 中的子元素 `<association>` 的子属性，而实现在多处重用 resultMap 作为子元素。
    - `association` 中 `column` 与 `select` 联用成子查询，在关联字段查询数据。select 会根据 column 字段作为查询参数去查找数据。
- `columnPrefix` 列名前缀，可以实现在一个 resultMap 中重用另一个 resultMap 作为两个不同的 association 。比如，一个领域模型中，博客拥有一个原作者，一个协同作者。此时同一个作者的字段完全一致，但在一个博客查询中有两个不同的字段来标识。此查询中将两个不同的作者的 column 查询出来并命名不同的前缀，如：`a.author_name as author_name,c.author_name as co_author_name` ，这时在 resultMap 中映射为：`<association property="author" resultMap="authorResult" /> <association property="coAuthor" resultMap="authorResult" columnPrefix="co_"`
- `notNullColumn` 指定非空列。一个结果集映射中，至少要一个非空的字段能被映射才会被创建对象，指定了这个属性后，所指定的列必须非空才会创建出一个对象来。

#### 自动映射

[autoMapping](https://mybatis.org/mybatis-3/zh/sqlmap-xml.html#Result_Maps)

自动映射将实现，查询出的 column （或其别名）与 bean 属性名匹配时自动映射（忽略两者之间大小写差异），不用手动书写映射关系。数据库字段命名规则一般为大写并使用下划线连接， bean 属性命名一般以小驼峰规则，这两者之间可以实现映射自动转换，添加此配置 `mapUnderscoreToCamelCase = true`。使用此自动转换时，在数据库命名与 java 命名都规范的情况下，在 mapper 中可以不用写 resultMap 给 select 语句使用而直接使用 resultType 即可。而就算写 resultMap 也可以不用写各个字段的别名，直接使用其数据库字段名，将自动映射成驼峰形式的变量。
