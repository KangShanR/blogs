IOException parsing XML document from ServletContext resource
[/WEB-INF/springmvc-servlet.xml]; nested exception is java.io.FileNotFoundException:
Could not open ServletContext resource [/WEB-INF/springmvc-servlet.xml]

/WEB-INF/springmvc-servlet.xml--->spring mvc的默认的配置文件的格式
    servletName-servlet.xml

1.文档声明
 <beans xmlns="http://www.springframework.org/schema/beans"
	xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xmlns:mvc="http://www.springframework.org/schema/mvc"
	xmlns:context="http://www.springframework.org/schema/context"
	xsi:schemaLocation="http://www.springframework.org/schema/beans
		http://www.springframework.org/schema/beans/spring-beans-4.1.xsd
		http://www.springframework.org/schema/context
		http://www.springframework.org/schema/context/spring-context-4.1.xsd
		http://www.springframework.org/schema/mvc
		http://www.springframework.org/schema/mvc/spring-mvc-4.1.xsd">

	servlet配置中的load-on-startup节点说明
	1.如果设置为正数，则servlet容器启动之后就实例化并初始化该servlet，其数值表示初始化的顺序
	2.如果值为0，则servlet容器立即初始化该servlet
	3.如果为负数，则只有在需要使用到该servlet的时候才会初始化该servlet
2.springmvc中方法支持的参数类型
  1.request、response、session
  2.简单数据类型
  3.model、modelandview
  4.普通的pojo对象
  5.集合类型数据、数组
  6.@RequestParam注解，用于将指定的参数名的数据注入给方法的参数
3.返回值类型
	String：
		逻辑视图名，相对于视图解析器
		redirect：重定向，语法为："redirect:url地址(包含后缀)"
		forward：页面转发，语法为："forward:url地址(包含后缀)"
	modelandview
	void
		使用request和response实现页面跳转
		使用注入modelandview实现页面跳转
	简单的pojo对象、集合类型的的数据
4.数据回显
  1.将数据放入Model中-->数据-->request域
  2.使用注解 @ModelAttribute--->数据放入到model中，自动将数据放入到model中
5.输入校验:保证数据的合法性
  1.映入hibernate-volidator校验的框架-->基于注解的方式处理数据校验的
     1.length:长度
     2.email:邮箱
     3.notempty:非空
  2.需要校验的实体类的属性上添加相应的注解
     @Length(min=6)//长度必须大于6
	private String password;
  3.数据校验器配置springmvc中以及校验失败之后的信息
     !-- 注解驱动： -->
	<mvc:annotation-driven validator="validator"/>

	<!-- 数据校验器 -->
	<bean id="validator" class="org.springframework.validation.beanvalidation.LocalValidatorFactoryBean">
		<!-- 校验器 -->
		<property name="providerClass" value="org.hibernate.validator.HibernateValidator"/>
		<!-- 自己的错误信息提示 -->
		<property name="validationMessageSource">
			<!-- 内部bean的配置方式 -->
			<bean class="org.springframework.context.support.ReloadableResourceBundleMessageSource">
				<!-- 指定文件的名称 -->
				<property name="basenames">
					<list>
						<value>classpath:validateMessage</value>
					</list>
				</property>
			</bean>
		</property>
	</bean>
    4.在需要使用validator校验的方法上，对相应的参数前面使用@validated，可以通过在方法参数上添加一个数据类型为BindingResult参数来获取校验的结果
    5、界面通过spring提供的hasBindErrors标签判断参数绑定是否失败，通过errors.allErrors获取参数绑定后的错误信息
			<!-- name:绑定参数的参数名 -->
			<spring:hasBindErrors name="user">
				<c:forEach items="${errors.allErrors }" var="error">
					${error.defaultMessage }
				</c:forEach>
			</spring:hasBindErrors>
6.restful风格
  1.serlvet的匹配必须/
  2.url地址采用占位符的方法进行参数的设置--->delete/5
  3. @PathVariable用于取得地址中的数据
7.json数据交换
  1.MappingJackson2JsonView-->进行json数据转换的view
  2.引入jackson的依赖：annotations、core、databind
  3.springmvc提供以下注解进行处理
    1.@RespondBody：将返回的数据转换成json数据类型
    2.@RequestBody：将json字符转换成普通的java对象
5. RequestMapping注解说明
  1.value：请求的url地址
  2.method：标识该方法只能处理请求的方式
  3.produces：生成的数据类型(格式)
  4.consumes：接受的数据类型(格式)
8. 返回xml数据
  1.MappingJackson2XmlView-->处理xml数据的交互
  2.引入dataformat、annotations
  3.springmvc提供以下注解进行处理
    1.@RespondBody：将返回的数据转换成xml数据类型
    2.@RequestBody：将xml字符转换成普通的java对象
9.拦截器
   1.实现HandlerInterceptor接口
      1.preHandle：执行handler之前执行的方法--->返回值为boolean：true-->继续执行
      2.postHandle：handler执行完成之后调用的方法
      3.afterCompletion：放回到视图之前调用的方法，对数据进行统一的修改
   2.spring mvc的配置文件中配置拦截器
      1.指定某种路径
		<mvc:mapping path="/userController/*"/>
      2.全路径
		<mvc:mapping path="/**"/>
   3.说明
       1.mvc:interceptors节点中的bean用于注册全局的拦截器，针对所有的url地址
       2.mvc:interceptors节点中的mvc:interceptor用户注册指定路径的拦截器
10.文件上传--->在springmvc提供的接口(多部件解析器)
   1.引入依赖：commons-io、commons-fileupload
   2.在springmvc的配置文件中配置多部件解析器(该bean的id必须为multipartResolver)
      <bean id="multipartResolver" class="org.springframework.web.multipart.commons.CommonsMultipartResolver">
		<!-- 文件上传的大小:单位 byte -->
		<property name="maxUploadSize" value="10240000"/>
		<!-- 默认的编码 -->
		<property name="defaultEncoding" value="UTF-8"/>
	</bean>
   3.文件上传的方法参数中声明一个数据类型为MultipartFile的参数，用于接收上的文件，参数名和name保持一致
