---
title: JDBC的理解
date: 2016-07-24 12:14:32
tags: [java,数据库,programming]
categories: programming
keywords: jdbc,持久层,数据库,连接,状态,会话
description: 
---

> 全称：java database connectivity,专用于java数据库连接，其中封装了基本的连接数据库的API,数据库连接的高级框架（诸如：Mybatis/Hibernate)的使用都基于JDBC的原理，也就是理解了JDBC对于我们更理解各种持久层数据连接层的框架很有帮助；

<!--more-->

## 一个简单的JDBC实现

```java
/**
 * 获取数据库连接的方法
 * @return
 */
public static Connection getConnection(){
    Connection con = null;
    try{
        //获取连接mysql数据库的驱动
        Class.forName("com.mysql.jdbc.Driver");
        //设置连接数据库的url
        String url = "jdbc:mysql://localhost:3306/agileone1?useUnicode=true&characterEncoding=UTF-8";
        //连接数据库的用户名
        String userName = "root";
        //连接数据库的密码
        String userPass="";
        //从而获取到数据库连接connection
        con = DriverManager.getConnection(url, userName, userPass);
    }catch (Exception e){
        e.printStackTrace();
    }
    return con;
}
```

- 上述代码块就通过java.sql包里封装好的各个类与类反射获取到了连接指定数据库的链接；
- 接下来就可以利用这个连接来进行crud类操作

```java
    /**
    * 插入数据进入数据库的方法
    * @param username
    * @param password
    */
static void insertData(String username, String password){
    Connection con = getConnection();
    String sql = "INSERT INTO Userdata (username, userPass) VALUES('"+username+"', '"+password+"')";
    try {
        // 从连接创建状态
        Statement state = con.createStatement();
        // 执行更新语句，返回影响行数
        int re = state.executeUpdate(sql);
        System.out.println(re);
    } catch (SQLException e) {
        e.printStackTrace();
    }
}
    /**
    * 模糊查询的方法
    * @param name
    */
static void query(String name){
    Connection con = getConnection();
    //sql语句
    String sql = "SELECT * FROM userdata WHERE username = '" +name+"'";
    try{
        Statement state = con.createStatement();
        //执行查询语句，返回批量结果集
        ResultSet re = state.executeQuery(sql);
        //对结果集进行迭代输出
        while(re.next()){
            System.out.println("id:" + re.getInt("id"));
            System.out.println("username:" + re.getString("username"));
            System.out.println("id:" + re.getString("userPass"));
        }
    }catch(SQLException e){
        e.printStackTrace();
    }
}
/**当sql语句执行完成，需要对上面建立的各个连接与状态进行关闭：
* 关闭连接与状态的方法
*/
static void close(Connection con){
    if(con !=null){
        try {
            con.close();
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}
```

- 其中针对不同的方法与连接状态，关闭的对象也不一样，除了Connection还有Statement,Prestatement,resultSet，各个对象都是调用其Close方法；
- 通常情况下，调用对象的Close()方法放在finally语句块中，这样就可以执行完查询语句方法后，交接将各个对象关闭；

### Note

- *以上的代码基本实现了JDBC的整个流程，从创建连接到执行查询语句，再到关闭连接、状态、结果集*
- *JDBC中封装的连接数据库的API包：java.sql是连接sql的基础包，使用JDBC就得引入这个jar包：`mysql-connector-java-5.1.38-bin.jar`*
- 在使用 maven 管理包时，在没有加入 mysql 驱动包时连接本地的 mysql 库依然能连接上也能查询到数据。前面项目把 `Class.forName("com.mysql.jdbc.Driver")` 这一行代码注释后一样能连上数据库使用。
  - 目前原因猜测：当 `mysql-connector-java` 包被 build 进项目后，程序运行时自动将其加载好了。不用再使用代码加载驱动了！

## c3p0

> 在项目中使用 c3p0 来整合连接数据库的工具。来源：jdbc 是 java 中连接数据库的工具，连接时因为 Connection 的开闭是非常耗费资源的，所以常会构造一个连接池来降低 Connection 的开闭操作频率。c3p0 便整合了连接池常用 jdbc 操作的 jar 包。[c3p0 source](https://www.mchange.com/projects/c3p0/)

### c3p0 的实现

如何实现 Connection 的管理的？使用 Connection 后是否需要再进行相应的操作？

#### 问题

> Feb 24 2020, 昨天晚上使用 c3p0 时出现的问题：`No suitable Driver!` 一直找不到原因，重新导了几遍 mysql-connector-java 包。睡前还想包哪里导得有问题，今天起来没去上班新起一个项目，使用 maven 导 c3p0 进来还是有同样的问题。打开 c3p0 的官方文档看到工厂方法创建 Databases ，尝试过程中发现 url 中与别人内置的相比少了个 `:` 。加上这个 `:` 在协议字段 `mysql` 之后，万事大吉！

**总结：**

- 此之前已经知道 url 中那一段协议，但手写 url 时没写进去，后面检查时也没有对此进行检查。
- 这种很暴力的错误很有可能就是在这种低级的地方导致。找不到驱动最直接的想法就是包没导好，想不到的是找包是根据协议来找的。

## 关于

1. 在 java sql 代码中写 %

在 java 中写 sql 时想要把模糊点位符 `%` 直接写进 sql （用以模糊条件）。如果写成 `select * from user where username like %?%;` 执行时会出现语法错误，解析此条 sql 时会对 `%` 错误解析。正确的写法是: `select * from user where username like \"%\"?\"%\";`。将 `%` 使用双引号包起来同时要对双引号进行转义：`\"%\"`。

## apache DbUtils

> apache common 下的对 jdbc 常用方法的封装，可以的直写 sql 语句执行并对其结果进行封装。[preference](http://commons.apache.org/proper/commons-dbutils/examples.html)

引入一段使用过的代码：

```java
/**
 * connection 使用的是 c3p0 封装的连接池
 * QueryRunner 是 DbUtils 核心的类
 * query 方法查询签名： T query(Connection conn, String sql, ResultSetHandler rsh, Object... params);
 */
@Test
public void queryTest () throws PropertyVetoException, SQLException {
    QueryRunner queryRunner = new QueryRunner();
    String sql = "select * from `user` where `username` like \"%\"?\"%\";";
    queryRunner.query(C3p0Util.getConnection(), sql, rshd, "i");
}

@Test
public void updateTest() throws PropertyVetoException, SQLException {
    String sql = "update `user` set `username` = 'ant man', `gender` = 'male' where id = ?";
    new QueryRunner().update(C3p0Util.getConnection(), sql,  8);
}

@After
public void close() {
    System.out.println("after");
    // close the databases
}

/**
 * RusultSetHandler 对结果进行处理
 * 接口只有一个方式，隐式函数式接口，直接使用 lambda 表达式
 */
private static ResultSetHandler rshd = rs -> {
    List<UserDTO> users = JdbcUtil.convertRes2User(rs);
    users.stream().forEach(System.out::println);
    return null;
};
```

- 使用此 util 的好处在于其对 sql 的封装与参数设置、结果处理的封装，使用时 sql 点位符 `?` 可以在方法参数中直接添加，结果可以直接使用 lambda 表达式进行封装成 javabean 。
- DbUtils 还提供了更高阶的方法，异步执行：AsyncQueryRunner 。异步并行执行多个 sql 相对于串行同步执行多个 sql 可节省响应时间。
- 对结果进行转换的操作可以直接使用 ResultSetHandler 的实现类 BeanListHandler ，其处理的核心在于 RowProcess
