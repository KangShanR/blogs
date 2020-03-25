---
title: Hibernate
date: 2017-08-23 02:04:38
tags: [framework,java,programming]
categories: programming
description: 关于Hibernate框架的基本使用与理解
---

> 简介：Hibernate是一种ORM（Object Relative-Database Mapping)框架，用于与各种数据库、SQL语句打交道，是数据持久化的一种解决方案；在 java 对象（在这儿就为 POJO）与关系数据库之间建立某种映射，实现直接存取 Java 对象。
> 关于ORM：
> 数据持久化核心技术之一，在 JDBC 开发中，大量重复性高的 sql 语句在 DAO 层中，而 ORM 就实现了根据 POJO 属性来拼装 SQL 语句，读取时，用 SQL 语句将各种属性从数据库中读取出来，再拼装为 POJO 对象返回给业务层，这样来实现数据表与 POJO、数据表中列与 POJO 属性之间的映射关系；ORM 框架就实现通过映射关系自动生成 sql 语句的强大强大功能；
> ORM 框架与 MVC 框架不同，使用范围也截然不同，类似功能的框架还有 SUN 的 JDO、Apache 的 Mybatis 与 OpenJPA 等；

<!--more-->

## Hibernate 的原理

- 从最基础的JDBC说起：
    - 在 JDBC 编程中，我们在在 DAO 层编写各种 sql 语句来实现数据库的 crud，而 Hibernate 就充当了 DAO 层，根据 POJO(Plain Old Java Object, is a Java object that doesn't extend or implement some specialized classes and interfaces respectively required by the EJB framework. All normal Java objects are POJO.) 与实体类的映射自动生成 sql 语句；
    - JDBC 中 DAO 中的 sql 语句必须由程序员事先写好，而在 Hibernate 中 sql 语句是动态生成，如果实体类发生变化：在 JDBC 中，与之相应的 sql 语句都得重新写一遍，但在 Hibernate 中只需要更新与这个实体类相关的配置类即可；

## Hibernate 的配置

> Hibernate的配置就分两种，**xml配置**与**注解配置**，两者略有不同，但最终都是把实体属性与数据库表之间的映射给联系好；

### hibernate mapping file

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
   2. `<generator>` 指定自动生成主键值。`class="native"` 是让 hibernate 根据数据库性能选择 unique/sequence/hilo 算法之一来创建主键；
5. `<property>` 用以指定 java class 对象与表字段之间映射，其中 `type` 属性还是用以指定 hibernate 映射类型。[reference](https://www.tutorialspoint.com/hibernate/hibernate_mapping_types.htm)

### 使用注解进行配置

对 POJO 注解达到 hbm.xml 的效果，绝对如 xml 的灵活度达不到。

1. `@Entity` 注解于 POJO 之上，POJO 必须要有一个无参默认构建器，同时其访问修辞符的级别要在 `protectd` 之上（包括）；
2. `@Table` 在注解上指定表数据，有多个属性；
3. `@Id` `@GaneratedValue` 在 POJO 字段上，可以根据表结构决定使用多个或单个 id 。hibernate 默认有 id 生成策略，如果需要指定则使用 `@GeneratedValue`;
4. `@Column` 用以指定 POJO 字段与表字段映射关系。

### Hibernate 的版本配置

> 锁的配置就是为了防止线程上的不安全，Hibernate 为了处理这种当一个线程在更改数据时，另一个线程也参与进来而出现线程不安全；
> Hibernate 的锁机制有两种，乐观锁与悲观锁，悲观锁是只让一个线程来操作数据，而乐观锁是加上一个版本号，现 web 开发中一般都用乐观锁；

- 注解配置版本号：
    - 在实体类中，配置一个乐观锁需要在实体类中专门开一个数值类型的属性并加上注解 `@Version`；
- xml配置版本号：
    - 在 `<id>` 之前，`<property>` 之后，配置上 `<version>` 标签；
    - 比注解配置更灵活的地方在于，在这个标签中，`<version>` 标签中可以指定属性为时间类型值 `<version name="version" type="timestamp" column="version"></version>`；

## hibernate CRUD

### batch processing

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

## Hibernate与Mybatis的对比

> 或者说为什么有了 Mybatis 还会存在着 Hibernate？

- 降低了开发认知负担，最大优势在于**级联**，最大的劣势也在于级联，**降低了执行效率**，经过了**多层封装**，**资源消耗变得很高**；
- 相较于 Hibernate，Mybatis 在只是**简单地封装了JDBC**，**代码开发更多**，**执行效率更高**，**资源消耗更低**；
