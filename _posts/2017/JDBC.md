---
title: JDBC的理解
date: 2016-07-24 12:14:32
tags: [java,数据库,programming]
categories: programming
keywords: jdbc,持久层,数据库,连接,状态,会话
description: 
---

# JDBC

> 全称：fava database connectivity,专用于java数据库连接，其中封装了基本的连接数据库的API,数据库连接的高级框架（诸如：Mybatis/Hibernate)的使用都基于JDBC的原理，也就是理解了JDBC对于我们更理解各种持久层数据连接层的框架很有帮助；

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
//			获取连接mysql数据库的驱动
		Class.forName("com.mysql.jdbc.Driver");
//			设置连接数据库的url
		String url = "jdbc:mysql://localhost:3306/agileone1?useUnicode=true&characterEncoding=UTF-8";
//			连接数据库的用户名
		String userName = "root";
//连接数据库的密码
		String userPass="";
//			从而获取到数据库连接connection
		con = DriverManager.getConnection(url, userName, userPass);
	}catch (Exception e){
		e.printStackTrace();
	}
	return con;
}
```

- 上述代码块就通过java.sql包里封装好的各个类与类反射获取到了连接指定数据库的链接；
	- 接下来就可以利用这个连接来进行crud类操作
			- /**
			 * 插入数据进入数据库的方法
			 * @param username
			 * @param password
			 */
			static void insertData(String username, String password){
				Connection con = getConnection();
				String sql = "INSERT INTO Userdata (username, userPass) VALUES('"+username+"', '"+password+"')";
				try {
			//			从连接创建状态
					Statement state = con.createStatement();
			//			执行更新语句，返回影响行数
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
			//		sql语句
				String sql = "SELECT * FROM userdata WHERE username = '" +name+"'";
				try{
					Statement state = con.createStatement();
			//			执行查询语句，返回批量结果集
					ResultSet re = state.executeQuery(sql);
			//			对结果集进行迭代输出
					while(re.next()){
						System.out.println("id:" + re.getInt("id"));
						System.out.println("username:" + re.getString("username"));
						System.out.println("id:" + re.getString("userPass"));
					}
				}catch(SQLException e){
					e.printStackTrace();
				}
			}
	- 当sql语句执行完成，需要对上面建立的各个连接与状态进行关闭：
			/**
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
		- 其中针对不同的方法与连接状态，关闭的对象也不一样，除了Connection还有Statement,Prestatement,resultSet，各个对象都是调用其Close方法；
		- 通常情况下，调用对象的Close()方法放在finally语句块中，这样就可以执行完查询语句方法后，交接将各个对象关闭；

#### Note: ####


- *以上的代码基本实现了JDBC的整个流程，从创建连接到执行查询语句，再到关闭连接、状态、结果集*
- *JDBC中封装的连接数据库的API包：java.sql是连接sql的基础包，使用JDBC就得引入这个jar包：`mysql-connector-java-5.1.38-bin.jar`*