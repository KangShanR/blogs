---
title: spring框架的学习
date: 2017-08-15 12:14:38
tags: framework
categories: 
description: 
---
>spring配置beans的底层原理就在于通过封装好的解析xml类，将xml文件中配置好的bean实例出一个对象来，再通过配置实现bean之间的相互引用，而实现将要用到的bean（实用类）实例化并使用；

## Core模块 ##
- bean标签：
	- id属性指定这个实例的唯一标识；
	- class属性，用来指定这个实例的类定义；
	- property子元素，指定这个对象的属性，比如：user对象中有属性name,那么这个这个user的bean对象就应该有一个子元素标签property，同时如果这个属性是另外一个本地的bean，name属性指向这个属性：`<property name="advice" id="beanId">`直接使用id属性来引用到其他的bean的id就行；

## AOP模块 ##
>Aspect Oriented Programming,面向切面编程

- 切面Aspect:可以理解为模块，比如，读写数据库、权限检查、异常情况记录；
- Advice,增强：拦截器实现增强接口Advisor，不同的拦截器实现不同的增强接口，比如：方法前拦截器
			MethodBeforeInterceptor implements MethodBeforeAdvice{
				//方法前拦截器
				//调用对象的方法前将执行该方法。参数分别为被调用的方法、参数、对象
				public void before(Method method, Object[] args, Object instance) throws Throwable{
					System.out.println("即将要执行的方法：“+method.getName());
					//如果是Service
					if(instance instanceof WaiterServiceImpl)
						String name = ((AopServiceImpl) instance).geName();
							if(name == null)//检查是否为空
								throw new NullPointerException("name属性不能为null");
					}
					method.invoke(instance,args);
				}
			}
- 拦截器，interceptor，也是pointcut的核心:
	- spring拦截器的配置实现，通过增加配置：
				<bean id="aopMethodBeforeInterceptor" class="org.springframework.aop.supoort.NameMatchMethodPointcutAdvisor">
					<property name="advice">
						<bean class="com.snail.aopdemo.advice.MethodBeforeInterceptor"/>
					</property>
					<property name="mappedName" value="withAop"></property>
				</bean>
	- 从上面的代码可以看出：
		- spring实现拦截器都是通过增强器Advisor，而这个增强器是一个代理，将参数（自定义的Advice）作为参数传入其中；
		- 同时mappedName属性用来指定拦截的方法，这个方法并不是增加类中的方法，而是到时要执行到的所有的匹配方法名字段的方法；
		- 同时：spring支持由正则表达式配置切入点：
					<property name="patterns">      <!-- 正则表达式配置切入点-->
						<list>
							<value>.*get.*</value> <!--包含get字段的方法就被拦截>
							<value>.*absquatulate</value>  <!--包含absquatutulat字段的方法被拦截>
						</list>
					</property>
## ORM模块 ##
>Object RelativeDatabase Mapping,对象关系型数据库映射
- 简介：
	- 此模块对Hibernate/JDO/TopLink、iBatis等ORM框架提供支持；
	- Spring提供在DAO层提供HibernateDaoSupport类与JDBCTemplate类；
	- 在Spring里，Hibernate与SessionFactory等只是Spring一个特殊的Bean，由Spring负责实例化与销毁；所以也就不需要与Hibernate的API打交道，不需要开启关闭Hibernate的Session、Transaction，Spring自动维护这些对象；

#### 实体类 ####
这儿用User类举例：
- 使用注解来让User中属性与数据库中表的列相关联；
- Entity类的注解：
	- @Entity，表明这个类为实体类；
	- @Table（name="users"),指明此实体类与数据库users表相关联；
	- 属性的注解：
		- @Id，主键注解，表明这属性为数据库表中的主键
		- @GeneratedValue（strategy=GenerationType.IDENTITY),指明该主键生成策略为自增
		- @Temporal（value = TemporalType.Date),表明列属性为Date
- DAO层接口，不同的Entity对应不同的DAO接口：
	- UserDao接口，就定义对User表的操作：
			public interface UserDao{
				public void saveUser(User user);
				public List<User> findUsers();
				public int getCount ();
				public User findUserByName(String name);
			}
	- UserDao接口的实现类，UserDaoImpl：
		- 这个实现类要继承HibernateDaoSupport类，这就意味着从父类继承了Hibernate与HibernateTimplate对象，该对象就对实体对象进行各类操作；
				public class UserDaoImpl extends HibernateDaoSupport implements UserDao{
					//实现接口中的方法
					public void saveUser(User user){
						this.getHibernateTemplate().persist(user);//使用父类方法get到Template,并调用其persist方法将user存入
					}
				}
					public int getCount(){    //查询记录条数
						Number num = (Number)this.getSession(true).createQuery("select count (*) from User).uniqueResult();
						return n.intValue();
					}
					public List<User> findUsers(){  //查询所有的user
						return this.getHibernateTemplate().find("select * from User");
					}
					public User findUserByName(String name){  //根据用户名查询用户
						List<User> users = this.getHibernateTemplate().find{"select * from User u where u.name=?", name);  //使用Template
						if(users.size() > 0)
							return users.get(0);返回第一条
						return null;
					}
				}
	- ***从上述代码例子中可以看出：***
		- 查询(Read)、创建(Create)都由父类HibernateDaoSupport提供的getHibernateTemplate方法获取到实例并执行实例的方法来实现，其中查询使用实例方法find(String sql, Stirng 拼接String），而save则用持久化方法persist(Object user);
		- 当要涉及到数据库计算时，则用getSession获取到与数据库的会话对象，让会话对象执行sql统计语句；
		- 同时，所有sql语言的操作对象都指向实体类，而没有对数据库中的表进行组织sql语句；
	- **个人理解：**spring通过封装Hibernate在框架中，让DAO接口的实现类继承HibernateDaoSupport尖，就将Hibernate对象创建出来，而直接操作这个对象的方法来获取session/Hibernate对象直接与数据库交互，而就节省了操作JDBC的代码；

#### 配置集成Hibernate ####
- 在spring的bean.xml文件中配置集成hibernate到目前这一步只需要***配置三个bean***：
	- 数据源datasource，这是一切的基础，所有的操作最终都会落到对数据库的操作上；
			<bean id="datasource" class="org.apache.commonsdbcp.BasicDataSource">
				<property name="driverClassName" class="org.gjt.mm.mysql.Driver"></property>
				<property name="url" value="jdbc\:mysql\://localhost\:3306/ssh"></property>
				<property name="username" value="root"></property>
				<propery name="password" value=""></property>
			</bean>
		*- 数据源的配置就是用依赖包中的一个封装好的类（这儿就是BasicDataSource）的对象，并将这个对象的属性值配置好，这些属性就包括了数据库的驱动、连接的url、连接数据库的用户名、密码。可以想象的是，这个处理数据库的对象封装了所有的连接数据库的方法，使用密码与用户名，交给驱动对象，这个驱动对象就按照提供的url连接到主机上的数据库，并登录到数据库；*
	- 有了数据源，就可以将关联连接库的数据源配置到sessionFactory中去：
			<bean id="sessionFactory" class="org.springframework.orm.hibernate4.annotation.AnnotationSessionFactoryBean" destroy-method="destroy">
				<property name="dataSource">
					<ref bean="dataSource"/>
				<property/>
				<property name="annotatedClasses">
					<list>
						<value>com.woniuxy.orm.class_qulified_name</value>
					</list>
				<property/>
				<property name="hibernateProperties">
					<pros>
						<pro key="hibernate.dialect">
							org.hibernate.dialect.MySQLDialect
						</pro>
						<pro key="hibernate.show_sql">true</pro>
						<pro key="hibernate.format_sql">true</pro>
						<pro key="hibernate.hbm2ddl.auto">create</pro>
					</pros>
				</property>
			</bean>
		- *配置会话工厂（因为我们的实体类使用的注解映射，所以就使用注解会话工厂AnnotationSessionFatoryBean)其中包括了：数据源、实体类还有hibernate,而hibernate的配置就包括了方言、输出sql语句、格式化sql语句、创建表结构*
	- 有了会话工厂，距离操作数据库就只有让Dao配置为bean，而操作代码就getBean来获取到这个Dao的实例，让这个实例来操作数据库：
			<bean id="userDao" class="daoImpl_qualified_name">
				<property name="sessionfactory" ref="sessionFactory" />
			</bean>
		- *在Dao的bean中，将前面配置好的sessionFactory装配到其中*
	- 以上情况是实体类User是使用注解来配置的，这种情况下，使用的sessionFactory是AnnotationSessionFactoryBean,但当实体类是使用xml文件来配置时，使用的配置bean应该是LocalSessionFactoryBean。同时，sessionFactory中配置实体的方式也变成：
			<property name="mappingDirectoryLocations">
				<list>
					<value>classpath:entiteis_配置文件的路径</value>
				</list>
			</property>

#### Hibernate的事务管理 ####
- **分层的做法：应用层调用Service层，Service层对数据进行检查（是否重复之类），然后Service层（注入一个Dao属性）调用Dao层，Dao层调用Hibernate实现数据的操作。原则上不允许跨层访问，业务层次分明。**
- 事务管理transaction，对应的层为Service层；

#### spring的bean.xml配置文件的理解 ####
- 所有的操作都基于对数据库的crud，所以所有的配置都围绕着操作数据库；
- 所以，第一个bean的是数据源：dataSource
	- 其中的属性就包括：
		- 连接数据库的驱动：driverClassName
		- 数据库连接url:url
		- 数据库连接用户名：username
		- 数据库连接密码：password
	- 通常情况下，我们把数据源信息都单独分离在jdbc.properties文件中，并在要用到的配置文件中将其配置为上下文`<context:property-placeholder> location="classpath:jdbc.properties/>`，之后就可以在数据源dataSource中配置其中的属性：`<property name="driverClass" value="${driver}"></property>`
- 第二个配置的bean：sessionFactory
	- 这儿就把上一次配置好的dataSource数据源装配到sessionFactory的属性中：
			<!-- 配置本地会话工厂bean -->
			<bean id="sessionFactory" class="org.springframework.orm.hibernate5.LocalSessionFactoryBean">
				<!-- 配置数据源 -->
				<property name="dataSource" ref="dataSource"/>
				<!-- 指定hibernate配置文件-->
				<property name="configLocations" value="classpath:hibernate.cfg.xml"/>
				<!-- 指定hibernate映射文件-->
				<property name="mappingDirectoryLocations">
					<list>
						<value>classpath:mappings/*.xml</value>
					</list>
				</property>
			</bean>
		- Note:*这儿配置spring的sessionFactory属性就会把Hibernatek r sessionFactory属性覆盖；*
		- configLocations属性：*将指定路径的配置文件都加载进去，相应的LocalSessionFactoryBean中的的此属性的setter方法的参数为可变参数:*
				public void setConfigLocations(Resource... configLocations) {
					this.configLocations = configLocations;
				}
	- 而在上述代码中，hibernate的配置文件也直接引入到sessionFatory中来，而在外部的hibernate配置文件：
		<hibernate-configuration>
		    <session-factory>
		    	<!-- 配置方言 -->
				<property name="hibernate.dialect">org.hibernate.dialect.MySQL5InnoDBDialect</property>
				<!-- session上下文控制权:交给session来控制 -->
				<property name="hibernate.current_session_context_class">org.springframework.orm.hibernate5.SpringSessionContext</property>
				<!-- 格式化sql语句 -->
				<property name="format_sql">true</property>
				<!-- 显示sql语句 -->
				<property name="show_sql">true</property>
				<!-- 表的生成策略 -->
				<property name="hbm2ddl.auto">update</property>
		    </session-factory>
		</hibernate-configuration>
		- 其中配置了部分的Hibernate属性，同时也可以配置c3p0的属性在其中；
- 第三个配置：事务管理器transactionManager
	- 将前面配置好的sessionFactory装配到这个bean中，作为sessionFactory属性值；
- 第四个配置：tx:advice，配置事务的传播特性，指定具备事务的方法名；
		<!-- 事务配置增强 -->
		<tx:advice id="txAdvice" transaction-manager="txMng">
	 		<tx:attributes>
	 			<tx:method name="save*" isolation="DEFAULT" propagation="REQUIRED"/>
	 			<tx:method name="update*" isolation="DEFAULT" propagation="REQUIRED"/>
	 			<tx:method name="delete*" isolation="DEFAULT" propagation="REQUIRED"/>
	 			<tx:method name="batch*" isolation="DEFAULT" propagation="REQUIRED"/>
	 			
	 			<tx:method name="get*" isolation="DEFAULT" propagation="REQUIRED" read-only="true"/>
	 			<tx:method name="load*" isolation="DEFAULT" propagation="REQUIRED" read-only="true"/>
	 			<tx:method name="find*" isolation="DEFAULT" propagation="REQUIRED" read-only="true"/>
	 			
	 			<!-- 统配 -->
	 			<tx:method name="*" isolation="DEFAULT" propagation="REQUIRED" read-only="true"/>
	 		</tx:attributes>
 		</tx:advice>
	- 最后一行统配就指定了所有的方法都配置上事务，同时isolation指此事务的隔离级别，propagation指事务的传播属性，read-only指是否为只读；
- 第五个配置aop:config，配置事务的切入点，以及被管理的对象
		<aop:config>
			<aop:pointcut id="interceptorPointCuts" expression="execution(* com.woniuxy.sshdemo.service.impl.*(..))"/>
			<aop:advisor advice-ref="txAdvice" pointcut-ref="interceptorPointCuts"/>
		</aop:config>
	- 先将切面的切点配置进来，也就是各个service的执行对象。再将这此切点配置到advisor中
- 其它的配置：
	- dao的执行类，配置一个id加上sessionFactory;
	- service的执行类,配置上dao这个属性的对象；
- **使用注解来实现事务的配置**
	- 这时bean.xml：
		- 数据源不变
		- sessionFactory不变；
		- 事务管理器trasactionManager依然不变；
		- 变的是：添加一个事务注解驱动`tx:annotation-driven trasaction-manager="transactionManager"/>`，添加这个驱动配置后，对产生事务的类添加注解`@Transactional`，标记这个类为事务类，对其中的事务方法添加注解`@Transactional(isolation=Isolation.DEFAULT,propagation=Propagation.REQUIRED)`注解（标明了这个方法的隔离水平与传播水平）。这样的注解就取代了上面例子中tx:advice与aop:config两个配置节点的功能；