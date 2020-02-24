# jdbc

java database connectivity

## c3p0

> 在项目中使用 c3p0 来整合连接数据库的工具。来源：jdbc 是 java 中连接数据库的工具，连接时因为 Connection 的开闭是非常耗费资源的，所以常会构造一个连接池来降低 Connection 的开闭操作频率。c3p0 便整合了连接池常用 jdbc 操作的 jar 包。[c3p0 source](https://www.mchange.com/projects/c3p0/)

### c3p0 的实现

如何实现 Connection 的管理的？使用 Connection 后是否需要再进行相应的操作？

## 问题

> Feb 24 2020, 昨天晚上使用 c3p0 时出现的问题：`No suitable Driver!` 一直找不到原因，重新导了几遍 mysql-connector-java 包。睡前还想包哪里导得有问题，今天起来没去上班新起一个项目，使用 maven 导 c3p0 进来还是有同样的问题。打开 c3p0 的官方文档看到工厂方法创建 Databases ，尝试过程中发现 url 中与别人内置的相比少了个 `:` 。加上这个 `:` 在协议字段 `mysql` 之后，万事大吉！

### 总结

- 此之前已经知道 url 中那一段协议，但手写 url 时没写进去，后面检查时也没有对此进行检查。
- 这种很暴力的错误很有可能就是在这种低级的地方导致。找不到驱动最直接的想法就是包没导好，想不到的是找包是根据协议来找的。

## 相关问题

在使用 maven 管理包时，在没有加入 mysql 驱动包时连接本地的 mysql 库依然能连接上也能查询到数据。前面项目把 `Class.forName("com.mysql.jdbc.Driver")` 这一行代码注释后一样能连上数据库使用。

目前原因猜测：当 `mysql-connector-java` 包被 build 进项目后，程序运行时自动将其加载好了。不用再使用代码加载驱动了！

## 在 java sql 代码中写 %

在 java 中写 sql 时想要把模糊点位符 `%` 直接写进 sql （用以模糊条件）。

如果写成 `select * from user where username like %?%;` 执行时会出现语法错误，解析此条 sql 时会对 `%` 错误解析。正确的写法是: `select * from user where username like \"%\"?\"%\";`。将 `%` 使用双引号包起来同时要对双引号进行转义：`\"%\"`。
