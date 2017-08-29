## mybatis框架的使用与理解 
#### Mybatis与JDBC ####
**概述：**
1. MyBatis是一个半自动的面向sql的orm框架；
2. JDBC全称：java database connectivity，是java封装在在java.sql包里的API，其设计目的在于连接数据库，通过java语言实现sql查询语言在数据库中的操作；
3. Mybatis就是对JDBC的封装，使其操作数据库更为灵活，更多的数据与实体之间的映射交给配置文件来实现；
4. **JDBC**作为java连接数据库的底层框架，
	1. **实现数据库连接功能的步骤：**
		1. 加载驱动
		2. 获取数据库连接
		3. 准备语句（获取状态对象）
		4. 执行语句
		5. 处理结果集
		6. 关闭数据库（释放资源）
		7. 异常处理
	1. 可以看到在整个流程中的**JDBC功能实现的缺陷**：
		1. jdbc的硬编码不方便维护，需要不停地连接、释放数据库资源；
		2. 参数绑定在硬编码中，查询条件不定，修改语句也不方便；
	3. 所以，就有了MyBatis存在的必要；

----------
### MyBatis的实现 ###
**在一个工程中要使用MyBatis框架：**
1. 将MyBatis的核心包、依赖包引入。如果是Maven项目，直接在pom.xml中引入依赖：
		<dependencies>
		<!-- 实现连接数据库的包 -->
	  	<dependency>
	  		<groupId>mysql</groupId>
	  		<artifactId>mysql-connector-java</artifactId>
	  		<version>5.1.37</version>
	  	</dependency>
		<!-- 日志记录的包 -->
	  	<dependency>
	  		<groupId>log4j</groupId>
	  		<artifactId>log4j</artifactId>
	  		<version>1.2.17</version>
	  	</dependency>
	  	<dependency>
	  		<groupId>org.slf4j</groupId>
	  		<artifactId>slf4j-log4j12</artifactId>
	  		<version>1.7.21</version>
	  	</dependency>
		<!-- 实现测试包 -->
	  	<dependency>
	  		<groupId>junit</groupId>
	  		<artifactId>junit</artifactId>
	  		<version>4.12</version>
	  		<scope>test</scope>
	  	</dependency>
	  	<!-- mybatis核心包 -->
		<dependency>
		    	<groupId>org.mybatis</groupId>
		    	<artifactId>mybatis</artifactId>
		    	<version>3.4.2</version>
		</dependency>
	 	 </dependencies>
1. 引入相关的jar包后，就可以对mybatis进行配置：
	1. 
  