---
title: Hibernate
date: 2017-08-23 02:04:38
tags: [framework,java,programming]
categories: programming
description: 关于Hibernate框架的基本使用与理解
---
<!-- TOC -->

- [1. Hibernate](#1-hibernate)
  - [1.1. Hibernate 的原理](#11-hibernate-%e7%9a%84%e5%8e%9f%e7%90%86)
  - [1.2. Hibernate 的配置](#12-hibernate-%e7%9a%84%e9%85%8d%e7%bd%ae)
    - [1.2.1. hibernate mapping file](#121-hibernate-mapping-file)
    - [1.2.2. 使用注解进行配置](#122-%e4%bd%bf%e7%94%a8%e6%b3%a8%e8%a7%a3%e8%bf%9b%e8%a1%8c%e9%85%8d%e7%bd%ae)
    - [1.2.3. Hibernate 的版本配置](#123-hibernate-%e7%9a%84%e7%89%88%e6%9c%ac%e9%85%8d%e7%bd%ae)
  - [1.3. hibernate CRUD](#13-hibernate-crud)
    - [1.3.1. creatia 条件查询](#131-creatia-%e6%9d%a1%e4%bb%b6%e6%9f%a5%e8%af%a2)
      - [1.3.1.1. 添加逻辑约束](#1311-%e6%b7%bb%e5%8a%a0%e9%80%bb%e8%be%91%e7%ba%a6%e6%9d%9f)
      - [1.3.1.2. 添加分页](#1312-%e6%b7%bb%e5%8a%a0%e5%88%86%e9%a1%b5)
      - [1.3.1.3. 聚合](#1313-%e8%81%9a%e5%90%88)
    - [1.3.2. 原生 sql](#132-%e5%8e%9f%e7%94%9f-sql)
      - [1.3.2.1. 添加 Entity](#1321-%e6%b7%bb%e5%8a%a0-entity)
      - [1.3.2.2. 添加参数](#1322-%e6%b7%bb%e5%8a%a0%e5%8f%82%e6%95%b0)
    - [1.3.3. batch processing](#133-batch-processing)
  - [1.4. 对象状态](#14-%e5%af%b9%e8%b1%a1%e7%8a%b6%e6%80%81)
  - [1.5. 缓存](#15-%e7%bc%93%e5%ad%98)
    - [1.5.1. 二级缓存并发策略](#151-%e4%ba%8c%e7%ba%a7%e7%bc%93%e5%ad%98%e5%b9%b6%e5%8f%91%e7%ad%96%e7%95%a5)
    - [1.5.2. cache provider](#152-cache-provider)
    - [1.5.3. 查询级别缓存](#153-%e6%9f%a5%e8%af%a2%e7%ba%a7%e5%88%ab%e7%bc%93%e5%ad%98)
  - [1.6. 级联](#16-%e7%ba%a7%e8%81%94)
    - [1.6.1. 配置](#161-%e9%85%8d%e7%bd%ae)
  - [1.7. Hibernate与Mybatis的对比](#17-hibernate%e4%b8%8emybatis%e7%9a%84%e5%af%b9%e6%af%94)
  - [1.8. 问题](#18-%e9%97%ae%e9%a2%98)

<!-- /TOC -->

# 1. Hibernate

> 简介：Hibernate是一种ORM（Object Relative-Database Mapping)框架，用于与各种数据库、SQL语句打交道，是数据持久化的一种解决方案；在 java 对象（在这儿就为 POJO）与关系数据库之间建立某种映射，实现直接存取 Java 对象。
> 关于ORM：
> 数据持久化核心技术之一，在 JDBC 开发中，大量重复性高的 sql 语句在 DAO 层中，而 ORM 就实现了根据 POJO 属性来拼装 SQL 语句，读取时，用 SQL 语句将各种属性从数据库中读取出来，再拼装为 POJO 对象返回给业务层，这样来实现数据表与 POJO、数据表中列与 POJO 属性之间的映射关系；ORM 框架就实现通过映射关系自动生成 sql 语句的强大强大功能；
> ORM 框架与 MVC 框架不同，使用范围也截然不同，类似功能的框架还有 SUN 的 JDO、Apache 的 Mybatis 与 OpenJPA 等；

<!--more-->

## 1.1. Hibernate 的原理

- 从最基础的JDBC说起：
    - 在 JDBC 编程中，我们在在 DAO 层编写各种 sql 语句来实现数据库的 crud，而 Hibernate 就充当了 DAO 层，根据 POJO(Plain Old Java Object, is a Java object that doesn't extend or implement some specialized classes and interfaces respectively required by the EJB framework. All normal Java objects are POJO.) 与实体类的映射自动生成 sql 语句；
    - JDBC 中 DAO 中的 sql 语句必须由程序员事先写好，而在 Hibernate 中 sql 语句是动态生成，如果实体类发生变化：在 JDBC 中，与之相应的 sql 语句都得重新写一遍，但在 Hibernate 中只需要更新与这个实体类相关的配置类即可；

## 1.2. Hibernate 的配置

> Hibernate的配置就分两种，**xml配置**与**注解配置**，两者略有不同，但最终都是把实体属性与数据库表之间的映射给联系好；

### 1.2.1. hibernate mapping file

hibernate 的映射文件用于引导 hibernate 在 pojo 与数据库表数据之间的对应关系。
eg:

```xml
<?xml version="1.0" encoding="UTF-8"?>

<!DOCTYPE hibernate-mapping PUBLIC
        "-//Hibernate/Hibernate Mapping DTD//EN"
        "http://www.hibernate.org/dtd/hibernate-mapping-3.0.dtd">
<hibernate-mapping>
    <class name="kang.treadstone.service.dto.UserDTO" table="user">
        <meta attribute = "class-description">
            This class contains the employee detail.
        </meta>
        <id column="id" name="id" type="java.lang.Integer">
            <generator class="native"></generator>
        </id>
        <property name="username" column="username" type="java.lang.String"/>
        <property name="password" column="password" type="java.lang.String"/>
        <property name="gender" column="gender" type="java.lang.String" />
    </class>
</hibernate-mapping>
```

1. 映射配置文件的根标签 `<hibernate-mapping>`；
2. `<class>` 用以指定 java 类对象与数据库表关系，其中 `name` 元素指定 java 类， `table` 属性指定表名；
3. `<meta>` 为可选标签，用以指定对 class 的说明；
4. `<id>` 标签用以指定 java class 中的 ID 到数据库表中主键；
   1. `name` 指定 class id 属性名， `column` 指定表中主键名；
   2. `<generator>` 指定自动生成主键值。`class="native"` 是让 hibernate 根据数据库性能选择 unique/sequence/hilo 算法之一来创建主键（共有 7 种主键生成策略）；
      1. identity，主键自增 mysql 自实现的
      2. sequence， oracle 的主键生成策略
      3. increment，由 hibernate 维护的主键自增策略
      4. hilo， hibernate 用高低位算法实现，与 identity 一样
      5. native，由上面三种之一，hibernate 根据库类型选择
      6. uuid，主键必须为 varchar 类型
      7. assigned，业务中自实现，不让 hibernate 决定
5. `<property>` 用以指定 java class 对象与表字段之间映射，其中 `type` 属性还是用以指定 hibernate 映射类型。[reference](https://www.tutorialspoint.com/hibernate/hibernate_mapping_types.htm)

### 1.2.2. 使用注解进行配置

对 POJO 注解达到 hbm.xml 的效果，绝对如 xml 的灵活度达不到。

1. `@Entity` 注解于 POJO 之上，POJO 必须要有一个无参默认构建器，同时其访问修辞符的级别要在 `protectd` 之上（包括）；
2. `@Table` 在注解上指定表数据，有多个属性；
3. `@Id` `@GaneratedValue` 在 POJO 字段上，可以根据表结构决定使用多个或单个 id 。hibernate 默认有 id 生成策略，如果需要指定则使用 `@GeneratedValue`;
4. `@Column` 用以指定 POJO 字段与表字段映射关系。

### 1.2.3. Hibernate 的版本配置

> 锁的配置就是为了防止线程上的不安全，Hibernate 为了处理这种当一个线程在更改数据时，另一个线程也参与进来而出现线程不安全；
> Hibernate 的锁机制有两种，乐观锁与悲观锁，悲观锁是只让一个线程来操作数据，而乐观锁是加上一个版本号，现 web 开发中一般都用乐观锁；

- 注解配置版本号：
    - 在实体类中，配置一个乐观锁需要在实体类中专门开一个数值类型的属性并加上注解 `@Version`；
- xml配置版本号：
    - 在 `<id>` 之前，`<property>` 之后，配置上 `<version>` 标签；
    - 比注解配置更灵活的地方在于，在这个标签中，`<version>` 标签中可以指定属性为时间类型值 `<version name="version" type="timestamp" column="version"></version>`；

## 1.3. hibernate CRUD

hql, hibernate query language。类似 sql ,hibernate 推荐使用。大小写不敏感。

1. 查询全部： `FROM pojo` POJO 需要全限定名
2. 可使用别名代替 pojo `FROM com.xxx.class AS C` `AS` 可省略
3. WHERE 条件：`FROM pojo p WHERE p.id = 1`
4. ORDER 语句 `SELECT pojo P WHERE P.id > 10 ORDER BY P.id DESC, P.name DESC`
5. GROUP 语句 `SELECT SUM(P.count), P.type FROM pojo P WHERE P.id > 0 GROUP BY P.type`
6. 使用命名参数语法在命名参数前加上 `:`

   ```java
   String hql = "FROM Employee E WHERE E.id = :employee_id";
   Query query = session.createQuery(hql);
   query.setParameter("employee_id",10);
   List results = query.list();
   ```

7. 插入语句，此处插入值放在 old_employee 对象中，通过 `SELECT` 取出。这也是 hibernate 中 orm 思想的体现，完全面向对象映射。包括前面 `FROM POJO` 语句，直接从 POJO 对象中取出数据用于查询语句，让 hibernate mapping 去根据 POJO 获取表的数据用以查询。

   ```java
   String hql = "INSERT INTO Employee(firstName, lastName, salary) SELECT firstName, lastName, salary FROM old_employee";
   Query query = session.createQuery(hql);
   int result = query.executeUpdate();
   System.out.println("Rows affected: " + result);
   ```

8. 更新语句

   ```java
   String hql = "DELETE FROM Employee WHERE id = :employee_id";
   Query query = session.createQuery(hql);
   query.setParameter("employee_id", 10);
   int result = query.executeUpdate();
   System.out.println("Rows affected: " + result);
   ```

9. 删除语句

   ```java
   String hql = "DELETE FROM Employee WHERE id = :employee_id";
   Query query = session.createQuery(hql);
   query.setParameter("employee_id", 10);
   int result = query.executeUpdate();
   System.out.println("Rows affected: " + result);
   ```

### 1.3.1. creatia 条件查询

使用 hibernate 内置的将查询语句对象化的条件查询。

eg:

```java
Criteria cr = session.createCriteria(Employee.class);
cr.add(Restrictions.eq("salary", 2000));
List results = cr.list();
```

#### 1.3.1.1. 添加逻辑约束

```java
Criteria cr = session.createCriteria(Employee.class);
Criterion salary = Restrictions.gt("salary", 2000);
Criterion name = Restrictions.ilike("firstNname","zara%");
// To get records matching with OR conditions
LogicalExpression orExp = Restrictions.or(salary, name);
cr.add( orExp );
// To get records matching with AND conditions
LogicalExpression andExp = Restrictions.and(salary, name);
cr.add( andExp );
List results = cr.list();
```

#### 1.3.1.2. 添加分页

eg:

```java
Criteria cr = session.createCriteria(Employee.class);
cr.setFirstResult(1);
cr.setMaxResults(10);
List results = cr.list();
```

```java
Criteria cr = session.createCriteria(Employee.class);
// To get records having salary more than 2000
cr.add(Restrictions.gt("salary", 2000));
// To sort records in descening order
cr.addOrder(Order.desc("salary"));
// To sort records in ascending order
cr.addOrder(Order.asc("salary"));
List results = cr.list();
```

#### 1.3.1.3. 聚合

eg:

```java
Criteria cr = session.createCriteria(Employee.class);
// To get total row count.
cr.setProjection(Projections.rowCount());
// To get average of a property.
cr.setProjection(Projections.avg("salary"));
// To get distinct count of a property.
cr.setProjection(Projections.countDistinct("firstName"));
// To get maximum of a property.
cr.setProjection(Projections.max("salary"));
// To get minimum of a property.
cr.setProjection(Projections.min("salary"));
// To get sum of a property.
cr.setProjection(Projections.sum("salary"));
```

### 1.3.2. 原生 sql

hibernate 同样可以直接使用原生 sql 查询，可用于写存储过程。

eg:

```java
String sql = "SELECT first_name, salary FROM EMPLOYEE";
SQLQuery query = session.createSQLQuery(sql);
query.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
List results = query.list();
```

*以上代码在新版本中未实现效果，报出错误。*

#### 1.3.2.1. 添加 Entity

```java
String sql = "SELECT * FROM EMPLOYEE";
SQLQuery query = session.createSQLQuery(sql);
query.addEntity(Employee.class);
List results = query.list();
```

*query.addEntity() 参数可用 entity 的全限定名。*

#### 1.3.2.2. 添加参数

```java
String sql = "SELECT * FROM EMPLOYEE WHERE id = :employee_id";
SQLQuery query = session.createSQLQuery(sql);
query.addEntity(Employee.class);
query.setParameter("employee_id", 10);
List results = query.list();
```

### 1.3.3. batch processing

hibernate 批量操作数据[reference](https://www.tutorialspoint.com/hibernate/hibernate_batch_processing.htm)

1. 如果不进行批量操作进行 session 清理，在数据进行到 50000 行时会出现内存溢出；
2. 批量操作需要增加配置 `<property name="hibernate.jdbc.batch_size">50</property>`;
3. 同时在操作时也要根据这个批量数量配置来对数据进行分割操作：

   ```java
   public void addEmployees( ){
      Session session = factory.openSession();
      Transaction tx = null;
      Integer employeeID = null;
      try {
         tx = session.beginTransaction();
         for ( int i=0; i<100000; i++ ) {
            String fname = "First Name " + i;
            String lname = "Last Name " + i;
            Integer salary = i;
            Employee employee = new Employee(fname, lname, salary);
            session.save(employee);
         	if( i % 50 == 0 ) {
               session.flush();
               session.clear();
            }
         }
         tx.commit();
      } catch (HibernateException e) {
         if (tx!=null) tx.rollback();
         e.printStackTrace();
      } finally {
         session.close();
      }
      return ;
   }
   ```

4. 不很明白，既然都添加配置了为什么不在 session 代码中封装分割批量操作。
5. 各个配置在恰当的位置，可以直接写此代码进行编译再对数据库进行数据插入操作。

## 1.4. 对象状态

在 hibernate 中，对象会因 hibernate 操作而发生状态流转。其中状态有：

1. 瞬时状态，当一个对象被 new 出而未与 session 发生关联，未被收录入缓存之中为 **瞬时状态**；
2. 持久化状态，当瞬时状态的对象与 session 发生关联（session.save()）后，或游离状态的对象被 session.update() 操作后进入持久化状态（就是普通对象持久化的过程）；
3. 游离|托管状态，当持久化状态的对象被 sesion.close() 执行后，进入到游离状态，在瞬时状态对象被设置 id 后也可进入游离状态。

## 1.5. 缓存

[reference](https://www.tutorialspoint.com/hibernate/hibernate_caching.htm)

1. 一级缓存是 session 默认且不可更改的
2. 能作为的是二级缓存，二级缓存是跨 session 的
3. 所有对象进入二级缓存前都会先尝试能否在一级缓存区是否能够完成
4. session.get() 执行时取的是就是一级缓存之中的对象，如果一级缓存中没有此对象，就会执行 sql 查询到此条数据， mapping 后并将其保存在快照与缓存中，同一个持久化再执行时会对比持久化的对象（缓存中对象）与快照数据是否一致，如果一致将不再连接数据库。
5. 关闭 session 时，session 中所有的缓存对象都会丢失，要么被持久化入库要么更新入库。
6. 二级缓存不一定会提升应用响应速度，可能会适得其反，所以使用前根据应用数据大小与机器性能与测试数据来决定二级缓存的配置。

### 1.5.1. 二级缓存并发策略

指定二级缓存有两步：

1. 选择并发策略
2. 根据并发策略配置相应的 cache provider。

并发策略分四种

1. Transactional 事务型，并发事务中严格控制旧数据出现，大部分读取数据，极少更新数据的情况。
2. Read-write 读写型，与 Tansactional 类似。
3. Nonstrict-read-write 非严格读写型，该策略不保证库与缓存间数据一致性，用于很少更改的数据，容忍少量旧数据的情况。
4. Ready-only 只读型。用于只读型的数据，操作中不会被更改。

### 1.5.2. cache provider

根据并发策略选择 cache provider，hibernate 全局只允许配置一个缓存器。可供选择的有：

1. EHCache 兼容 事务型并发策略
2. OSCache 兼容 事务型
3. SwarmCache 兼容读写型事务型
4. JBossCache 兼容非严格读写型与读写型

### 1.5.3. 查询级别缓存

使用二级缓存整合，用于高频使用相同参数的查询的场景。

使用：

1. 在 hibernate 配置中激活查询缓存： `<property name="hibernate.cache.use_query_cache">true</property>`
2. 查询中设置缓存，可为其指定区域：

```java
Session session = SessionFactory.openSession();
Query query = session.createQuery("FROM EMPLOYEE");
query.setCacheable(true);
query.setCacheRegion("employee");
List users = query.list();
SessionFactory.closeSession();
```

## 1.6. 级联

当对象与表中数据存在多对多、一对多时，为了简化操作，可以在 hibernate 中设置关系对象级联。

级联类型：

1. update-and-save 保存更新时使用
2. delete 删除时使用，慎用
3. all 前两种都用，慎用

### 1.6.1. 配置

```xml
<set name="" class="" cascate="">
</set>
```

## 1.7. Hibernate与Mybatis的对比

> 或者说为什么有了 Mybatis 还会存在着 Hibernate？

- 降低了开发认知负担，最大优势在于**级联**，最大的劣势也在于级联，**降低了执行效率**，经过了**多层封装**，**资源消耗变得很高**；
- 相较于 Hibernate，Mybatis 在只是**简单地封装了JDBC**，**代码开发更多**，**执行效率更高**，**资源消耗更低**；

## 1.8. 问题

1. 使用注解进行 mapping 时，刚开始没有加入到 hibernate configuration 中去，后来使用 `<mapping package="kang.treadstone.service.dto"/>` 将包引入还是不行，添加 `<mapping class="kang.treadstone.service.dto.ProductDTO"/>` 解决。为什么直接引入包不行？
