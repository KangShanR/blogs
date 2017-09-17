---
title: spring bean中属性覆盖器
date: 2017-09-14 02:04:38
tags: [framework,java,programming]
categories: programming

---

# javabean配置中的属性覆盖器 #

> 我们常常使用property属性文件来配置某些固定的属性值（比如，jdbc数据源、c3p0连接池），这样当多个配置文件使用到这些属性时也更方便我们统一修改；
> <!--more-->
> 在配置文件中使用属性文件的流程是：
1. 先用一个属性配置器将这个属性文件(eg:jdbc.properties)读入；
2. 后面在配置数据源这些属性时直接使用${jdbc.url}字符串来代替；

## 举jdbc属性配置例说明 ##

- 假定我们jdbc属性文件如下：
		#driver
		driver=org.gjt.mm.mysql.Driver
		#username
		username=root
		#password
		password=
		#database URL
		url=jdbc\:mysql\://localhost\:3306/library
- 先将属性文件读入到我们属性配置器中：
		<bean id="PropertyConfigurer" class="org.springframework.beans.factory.config.Property-PlaceholderConfigurer">
			<property name="location" value="classpath:jcbc.properties"/>
		</bean>
	- 也可以直接使用上下文标签：
			<context:property-placeholder location="classpath:jdbc.properties"/>
- 读入之后就可以使用占位符直接使用了：
		<!-- 配置数据源 -->
		<bean id="dataSource" class="com.mchange.v2.c3p0.ComboPooledDataSource" destroy-method="close">
			<property name="driverClass" value="${driver}"></property>
			<property name="jdbcUrl" value="${url}"></property>
			<property name="user" value="${user}"></property>
			<property name="password" value="${password}"></property>
		</bean>