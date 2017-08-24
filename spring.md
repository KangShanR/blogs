---
title: spring框架的学习
date: 2017-08-15 12:14:38
tags:
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
			MethodBeforeInterceptor implements MethodBeforeAdvice｛
				//方法前拦截器
				//调用对象的方法前将执行该方法。参数分别为被调用的方法、参数、对象
				public void before(Method method, Object[] args, Object instance) throws Throwable{
					System.out.println("即将要执行的方法：“+method.getName());
					//如果是Service
					if(instance instanceof WaiterServiceImpl)
						String name = ((AopServiceImpl) instance).geName();
							if （name == null)//检查是否为空
								throw new NullPointerException("name属性不能为null");
					}
					method.invoke(instance,args);
				｝
			｝
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
	- 从上述代码例子中可以看出：
		- 查询R、创建C都由父类HibernateDaoSupport提供的getHibernateTemplate方法获取到实例并执行实例的方法来实现，其中查询使用实例方法find(String sql, Stirng 拼接String），而save则用持久化方法persist(Object user);
		- 当要涉及到数据库计算时，则用getSession获取到与数据库的会话对象，让会话对象执行sql统计语句；
	- 