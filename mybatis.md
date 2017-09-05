---
title: Mybatis框架的应用与理解
date: 2017-08-31 13:12:34
categories: programming
tags: [java,programming]
description: Mybatis框架的基本实现与应用
---
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
	1. mybatis.xml文件中，配置mybatis的标签是：
			<?xml version="1.0" encoding="UTF-8" ?>  
			<!DOCTYPE configuration PUBLIC "-//mybatis.org//DTD Config 3.0//EN"
				"http://mybatis.org/dtd/mybatis-3-config.dtd">
			
			<configuration>
			
				<!-- 引入配置文件 -->
				<properties resource="jdbc.properties" />
			
				<!--自定义别名 -->
				<typeAliases>
					<!--定义单个别名 ,不区别大小写 -->
					<!-- <typeAlias type="com.woniuxy.mybatis.entity.User" alias="user"/> -->
					<!-- 批量定义别名，该包内所有的类的类名直接作为别名，不区分大小写 -->
					<package name="com.woniuxy.mybatis.entity" />
				</typeAliases>
			
				<!-- jdbc的配置信息 -->
				<environments default="development">
					<environment id="development">
						<!-- 配置事务管理器 -->
						<transactionManager type="JDBC" />
						<!-- 数据库连接池 -->
						<dataSource type="POOLED">
							<property name="driver" value="${jdbc.driver}" />
							<property name="url" value="${jdbc.url}" />
							<property name="username" value="${jdbc.username}" />
							<property name="password" value="${jdbc.password}" />
						</dataSource>
					</environment>
				</environments>
			
				<!-- 映射资源 -->
				<mappers>
					<mapper resource="mappings/Users.xml" />
					<mapper resource="mappings/UserMapper.xml" />
				</mappers>
				
			</configuration>
		- 其根标签是：`<configuration>`
		- 解析上述各标签：
			- properties:属性文件的引入，将java属性文件引入到该配置文件中，如上所述：引入了jdbc.properties文件，就可以在配置数据源属性时，通过点位符&｛｝来获取到装配进来的属性文件中的属性值；
			- typeAliases:别名的设定，可以指定单个类的别名，也可以使用子标签`package` 指定整个包的别名为类的simpleName;
			- environments配置中，除了配置了JDBC数据库连接信息(数据源类型：POOLED)外,还得设定事务管理器类型：JDBC；
			- mappers:映射各个实体类或映射类配置文件到此配置文件中去；
	- mappers中的配置文件：
		- Users.xml文件是指定了这个user类与数据库中操作的方法，包括这些方法的参数与返回值类型；
				<?xml version="1.0" encoding="UTF-8" ?>
				<!DOCTYPE mapper
					PUBLIC "-//mybatis.org//DTD Mapper 3.0//EN"
					"http://mybatis.org/dtd/mybatis-3-mapper.dtd">
					
				<mapper namespace="test2">
				
					<!-- 根据id获取用户信息 -->
					<select id="findUserById" parameterType="int" resultType="User">
						SELECT * FROM t_user WHERE ID = #{id}
					</select>
					
					<!-- 根据用户名获取用户列表 -->
					<select id="findUserByUsername" parameterType="string" resultType="User">
						SELECT * FROM t_user WHERE user_name like '%${value}%'
					</select>
					
					<!-- 添加用户数据 -->
					<insert id="saveUserInfo" parameterType="User">
						<!-- 使用selectkey返回插入数据的id -->
						<selectKey keyColumn="id" keyProperty="id" resultType="int" order="AFTER">
							SELECT LAST_INSERT_ID()
						</selectKey>
						<!-- 如果主键使用uuid，则可以通过下面方法把id转出 -->
						<!-- <selectKey keyProperty="id" resultType="string" order="BEFORE">
							SELECT UUID()
						</selectKey> -->
						INSERT INTO t_user(user_name,cnname,sex,mobile,email,note) VALUES 
						(#{user_name},#{cnname},#{sex},#{mobile},#{email},#{note})
					</insert>
					
					<!-- 删除数据 -->
					<delete id="deleteUserById" parameterType="int">
						DELETE from t_user where id=#{value}
					</delete>
					
					<!-- 更新数据 -->
					<update id="updateUserInfo" parameterType="User">
						UPDATE t_user SET
						user_name=#{user_name},cnname=#{cnname},sex=#{sex},mobile=#{mobile},email=#{email},note=#{note} 
						WHERE id=#{id}
					</update>
					
				</mapper>

			- `#{}`与`${}`的区别：
				- `#{}`
					- 表示占位符号，相当于实现了JDBC中的paparedStatement占位符的作用，也就实现了防止sql注入；
					- 同时实现了java类型与jdbc类型转换，可以接收简单类型值与pojo属性值；
					- 如果传输单个简单类型值，｛｝中可以是value或其它名称；
				- `${}`
					- 表示sql字符串拼接，也就是将｛｝中的内容拼接在sql中且不进行jdbc类型转换；
					- 可以接收简单类型值或pojo值，如果是简单类型值｛｝中只能是'value';
			- 文档头可以看出这是mabatis3映射的定义文档类型，所以其根标签为`mapper`，其中各子标签或属性：
				- namespace属性，指定其命名空间，这样在其它地方要调用此配置文件中的各语句时，就需要通过这个命名空间来访问到此文档中定义id的各个语句，通过session对象的方法来调用时，这些语句就作为参数执行方法：`session.selectOne("findUserById()",user)；
				- session的来源：
						/**
						 * 测试前的预备工作
						 */
						@Before
						public void before(){
							System.out.println(Before.class);
					
							try {
								// 读入mybatis配置文件到输入流中
								is = Resources.getResourceAsStream("mybatis.xml");
								factoryBuilder = new SqlSessionFactoryBuilder();
								//使用输入流创建sqlSessionFactory
								factory = factoryBuilder.build(is);
								//打开sqlSession
								session = factory.openSession();
							} catch (IOException e) {
								e.printStackTrace();
							}
						}
					- 上述代码是利用junit包实现了测试功能，@Before注解标这个方法在@Test方法之前运行，从该方法就可以看出myBaitis的运行流程：
						- 将mybatis.xml配置文件读放流中，再使用sessionFactoryBuilder对该流进行创建工厂（抽象工厂模式）；
						- 调用工厂的openSession()方法打开会话，获得sesison实例；
						- 操作数据库最后一步就是调用session对象的方法：`List<User> users = session.selectList("test.findUserByUsername", "k");`
							- 这儿的test就是之前指定的namespace，这儿指定的id的语句执行，并将另外的参数传入，形成完整的sql语句；
					- 这个流程是最初级的，而我们现实中常常不使用这样的形式，而使用接口来将所有**方法的调用都交由接口来实现：**
						- 在获取到session时，就通过**session.getMapper(Class<T> mapper)方法来获取到相关的mapper接口的对象**，而在使用过程中，就直接调用这个接口对象的方法（方法执行时就调用相应的语句执行），这个方法与语句之间映射就由指定了nameSpace的xml配置文件来实现，这种方法实现原理叫Mapper动态代理；
- **从上面的例子就可以看出Mybatis解决了的jdbc的问题：**
	- 数据库的链接的创建与释放者造成系统资源的浪费，而使用数据库连接池就可以此问题，在Mybatis的配置中设置；
	- sql语句写在java代码中造成的不易维护，而Mybatis就将这些sql语句配置在相应的配置文件中，这样就与java代码实现了分离；
	- 使用映射好的java对象来作为sql语句的statemente的参数，而不用像在jdbc的执行方法中定义输入参数的类型；
	- 对于结果集的解析在JDBC中需要一层层遍历，并将各个属性封装到entity中再装入集合中。Mybatis将这些封装好，直接将结果映射到java对象中；
- 同时，上面例子并没有在持久层操作，也就是DAO层并没有进行开发，直接使用在TEST中进行调用抽象工厂的构造方法来构造工厂与sqlSession，再用sqlSession对象的方法，sqlSession的方法中调用配置好的sql语句与参数；
- 如果要实现持久层的开发不过是把上面构造SqlSessionFactory/SqlSession的方法封装进Dao类中实现；

----------
### 使用Mapper代理开发 
#### 概述：
- **动态代理开发**，就是让生成Mapper对象的这个过程交给SqlSession对象的getMapper（Class<T> mapper)方法，这时的SqlSession可以理解为工厂，也就是通过了工厂来构造获取Mapper接口的对象；
- Mapper对象就是Mapper接口的实例，这个接口与写好相关的mapper.xml配置文件相关联，Mybatis就用这个接口作为代理与被代理的mapper的公共接口，这个对象的方法执行时就与相关联的Mapper.xml配置文件里的sql语句来执行sql方法；
- Mapper接口与Mapper.xml配置文件相关联的规则（此规则由Mybatis规定，符合此规则便形成关联）：
	- Mapper.xml文件中的namespce与mapper接口的类路径相同；
	- Mapper接口方法名和Mapper.xml中定义的每个statement的id相同；
	- 接口方法的输入参数类型和配置文件中的sql的parameterType的类型相同；
	- 接口方法的返回数据类型与配置文件中的resultType类型相同的；
- 实现代理开发就意味着之前的session方法执行都交给这个Mapper对象，所有的操作方法都由这个接口定义，调用这个方法就只用传入定义的参数类型，这也就**实现了由Mapper对象的方法来代理SqlSession对象中的操作数据库的方法**(*这儿涉及到代理[设计模式](http://kangshan.oschina.io/2016/06/13/DesignPattern/ "设计模式")的理解*)，相对于直接使用SqlSession的各种执行方法更为简便；
	- **动态代理开发中的小点：**
		- 动态代理在使用时，Mybatis会生成一个动态代理对象，至于这个动态代理对象会调用SqlSession的的selectOne()方法还是selectList()方法根据定义的mapper接口方法的返回值决定，如果返回List则调用selectList()方法，如果返回单个对象就调用selectOne()方法；
		- 使用mapper代理开发就不用写mapper接口实现类，输入参数也可以直接使用map对象或pojo对象，保证了dao的通用性；