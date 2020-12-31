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
- 直接在映射器方法参数中添加 RowBounds　即添加了分页参数。

#### resultMap

在使用查询语句中，id 元素在嵌套结果映射中扮演着非常重要的角色。你应该总是指定一个或多个可以唯一标识结果的属性。 虽然，即使不指定这个属性，MyBatis 仍然可以工作，但是会产生严重的性能问题。

- resultMap 可以作为其他 resultMap 中的子元素 `<association>` 的子属性，而实现在多处重用 resultMap 作为子元素。
    - `association` 中 `column` 与 `select` 联用成子查询，在关联字段查询数据。select 会根据 column 字段作为查询参数去查找数据。
- `columnPrefix` 列名前缀，可以实现在一个 resultMap 中重用另一个 resultMap 作为两个不同的 association 。比如，一个领域模型中，博客拥有一个原作者，一个协同作者。此时同一个作者的字段完全一致，但在一个博客查询中有两个不同的字段来标识。此查询中将两个不同的作者的 column 查询出来并命名不同的前缀，如：`a.author_name as author_name,c.author_name as co_author_name` ，这时在 resultMap 中映射为：`<association property="author" resultMap="authorResult" /> <association property="coAuthor" resultMap="authorResult" columnPrefix="co_"`
- `notNullColumn` 指定非空列。一个结果集映射中，至少要一个非空的字段能被映射才会被创建对象，指定了这个属性后，所指定的列必须非空才会创建出一个对象来。

#### 自动映射

[autoMapping](https://mybatis.org/mybatis-3/zh/sqlmap-xml.html#Result_Maps)

自动映射将实现，查询出的 column （或其别名）与 bean 属性名匹配时自动映射（忽略两者之间大小写差异），不用手动书写映射关系。数据库字段命名规则一般为大写并使用下划线连接， bean 属性命名一般以小驼峰规则，这两者之间可以实现映射自动转换，添加此配置 `mapUnderscoreToCamelCase = true`。使用此自动转换时，在数据库命名与 java 命名都规范的情况下，在 mapper 中可以不用写 resultMap 给 select 语句使用而直接使用 resultType 即可。而就算写 resultMap 也可以不用写各个字段的别名，直接使用其数据库字段名，将自动映射成驼峰形式的变量。

## Mybatis Mapper 映射文件编写

> date: "2018-09-26 17:03"

### Questions

当多个表之间关联查询时，使用查询时 id 会出现多个表之间的冗余，这个时间如果子对象只都取父表的 id ，那么会出现一个问题：当子对象为空时，这时取出的对象会产生这样一个只有父表 id 数据的对象，而实际上这个对象是不应该出现的。

### 待解决问题

- [x] 在写 mapper `<if test="id != null">` 的条件语句时，加上 and 条件（ `<if id != null AND id > 0`）时启动时就会报错
    - 将 `AND` 改成小写 `and` 即可
- [x] 使用分页数据 `limit n,m` 同时使用 `ORDER BY columnName DESC/ASC` 需要先排序再进行分页
- [x] baseResultMap 继承有两层时，中间层的字段不能被继承，只 WithBlobs 层的 加密字段不能被读取到，这个时候不得不写一次加密的字段映射栏
    - [x] 解决办法：继承多层并没有错误，错误在于，继承时没有依次继承， 第三层（最外层） 的 ResulMap 应该继承 DOMapper 的 ResulMapWithBlobs 这个 ResulMap ，而不是 BaseResultMap
    - 当继承发生，接收结果的对象也继承了 自动生成的 DO 对象时，如果需要对其中的字段进行重写（比如：坐标这种字段，自动生成的对象是个 Object ,明显不能用来接收 mysql 的 point 数据，所以就得重写一个坐标对象 Coord 放在继承的对象外面，这个时候我们一般就写成相同的字段名）。发生这种情况，就会担心写的查询语句映射到对象时会发生错误，经过 test 与思考，没有发生错误，因为在写 <resultMap> 时，就使用 `javaType` 指定了对象。
    - 这个问题的根源在于：没有弄懂 Mapper 中各个 <ResultMap> 继承是可以随意继承的，而继承的格式按照 Mapper 的写法就行。而在每个 Mapper 中引入的 mgm 生成的 baseResultMap 只是为了少写 其路径名，也就是说可以不写这个 baseResultMap 需要继承时直接在需要的 ResultMap 中直接 extends="" 。而这时的继承就写继承的全路径就是，而不再是继承本 Mapper 中的（只需要写一个 id 就行）
    - 总算搞懂了这个 ResulMap 映射关系了！
- [ ] 写 查询 语句时，如果把 resultType 写成了 `ResultMap` 将会引起难以查找的错误：
    - 这个时候启动服务时调任何接口都会抛出异常，但不能定位到这个 mapper 来，只会说：不能解析到
`resultMap can't be java.lang.Integer` 。
        - 实际上，我们的查询方法中，根据整个 mapper 定义：resultMap 是将映射到 已经写的 resultMap 中的（诸如：BaseResultMap 。甚至我在想，这个一样可以写个全路径的 resultMap ,直接利用外面已经写好的)
    - `resultType` 是指将结果直接映射成一个 java 类型，比如 使用 COUNT() 函数查询出一个数量来，这个时候会返回一个 Integer 就好，所以， `resultType="java.lang.Integer"`

### mapper 查询语句要点

> mapper 查询时，使用 Map 作为参数进行条件映射有诸多方便之处：
> 可以加上很多的 `<where>` 语句配上 `<if test="list != null and list.size > 0 ">` 可以在 Service 层去配自己想要的查询条件，结合使用方便很多。
> 而且，如上示例，在 Map 参数中，使用列表作为参数，同样可以在查询语句中进行列表（是否为空、列表长度）判定。
> **需要被淘汰的 sql 写法，更好的 sql 写法是使用 [Java API](https://mybatis.org/mybatis-3/zh/java-api.html  )**

 **具体使用中，可以参照下面查询语句：**

```xml
<select id="selectSitesByParam" resultMap="ExtendBaseResultMap"
    parameterType="map">

    SELECT
        <include refid="site_column"/>,
        <include refid="site_tag_column"/>
    FROM ev_site AS s
    LEFT JOIN ev_site_tag AS t ON t.`site_id` = s.id
    <where>
        <if test="companyIds != null and companyIds.size &gt; 0">
            and s.`branch_company_id` IN
            <foreach collection="companyIds" index="index" item="item" open="(" separator="," close=")">
                #{item}
            </foreach>
        </if>
        <if test="siteName != null">
            and s.`site_name` LIKE CONCAT('%',#{siteName},'%')
        </if>
        <if test="orAddr != null">
            OR s.`addr` LIKE CONCAT('%',#{orAddr},'%')
        </if>
        <if test="andAddr != null">
            AND s.`addr` LIKE CONCAT('%',#{andAddr},'%')
        </if>
        <if test="statusList != null">
            and s.`status` IN
            <foreach collection="statusList" item="item" index="index"
                     open="(" separator="," close=")">
                #{item}
            </foreach>
        </if>
        <if test="siteTypeList != null">
            and s.`site_type` IN
            <foreach collection="siteTypeList" item="item" index="index"
                     open="(" separator="," close=")">
                #{item}
            </foreach>
        </if>
    </where>
    ORDER BY s.id DESC
    <if test="startNo != null and endNo != null">
        LIMIT #{startNo},#{endNo}
    </if>
</select>
```
