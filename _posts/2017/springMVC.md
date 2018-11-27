---
title: springMVC的理解与认识
date: 2016-08-20 13:29:17
tags: [java,framework]
categories: programming

---

# SpringMVC

> SpringMVC 同样是一种 MVC 架构，它与传统 MVC 框架的不同之处在于使用了中央调度器，用中央调度器 DispatcherServlet 来分发所有的请求与响应，中央调度器的存在就大大降低了其他组件之间的耦合度。这种分发请求与响应的实现得益于 spring 的装配。

<!--more-->

- **中央处理器的配置**：在web.xml中配置springmvc的servlet

```
<servlet>
  	<servlet-name>springmvc</servlet-name>
  	<servlet-class>org.springframework.web.servlet.DispatcherServlet</servlet-class>
  	<init-param>
  		<param-name>contextConfigLocation</param-name>
  		<param-value>classpath:springmvc.xml</param-value>
  	</init-param>
  	<load-on-startup>0</load-on-startup>
  </servlet>
  <servlet-mapping>
  	<servlet-name>springmvc</servlet-name>
  	<url-pattern>*.do</url-pattern>
  </servlet-mapping>
```
- 中央处理器DispatcherServlet在web.xml中被配置成一个servlet,并通过初始化上下文配置参数springmvc.xml的路径与设置其启动时机与该servlet初始化时机；
	1. 其中 load-on-startup 这个属性来指定这个中央处理器被初始化的时机：
		1. 当为负数时，只有第一次使用时才会初始化，这也就带来一个问题，第一次访问时就会慢一些；
		2. 当为非负数时，中央处理器会在servlet容器启动时初始化，而这个数值就是初始化的顺序；

## 核心对象：

1. **中央分发控制器**（在springMVC框架中，它是核心的核心，所有的分发都由它处理，所以也叫 **中央处理器** ），处理请求并给出响应；
2. **处理器映射器** HandlerMapping ：设置 handler 处理器与 url 资源的映射
	1. 使用 BeanNameUrlHandlerMapping 这个类时，就会将 handler 的 name 属性值作为 url 映射，访问这个处理器就填写其 name 属性值:
			<bean id="login" name="loginController.do" class="com.woniuxy.springdemo.controller.LoginController"/>
		如上，就可能过name属性值，设置	`<a href="loginController.do>登录</a>`
	2.  SimpleUrlHandlerMapping 这个类型的映射要求映射 url 与 controller 的配置 id 相对应，并在这个节点内将 controller 的 id 与相关的 key 对应起来；
	```
	<bean class="org.springframework.web.servlet.handler.SimpleUrlHandlerMapping">
		<property name="mappings">
			<props>
			<!--配置指定的url与bean的id映射,可添加多个-->
				<prop key="/login.do">login</prop>
				<prop key="/login2.do">login2</prop>
			</props>
		</property>
	</bean>
	<bean id="login" name="loginController.do" class="com.woniuxy.springdemo.controller.LoginController"/>
	<bean id="login2" name="loginController2.do" class="com.woniuxy.springdemo.controller.LoginController2"/>
  ```
	3.  使用注解实现处理器与 url 的映射
			<!-- 注解映射器 -->
			<bean class="org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping"/>
		1.  这个配置节点就决定了处理器与其中的方法可以被注解@RequestMapping（"url_name"）映射并指定url
2.  **处理器适配器** HandlerAdapter，用于规定处理器的编写规则
	3.  使用接口来配置适配器：
		1.  当指定为 SimpleControllerHandlerAdapter 时，它就规定了要想成为处理器，就要实现 Controller 这个接口；
		2.  HttpRequestHandlerAdapter 这个适配器要求所有的 Handler 都必须实现 HttpRequestAdapter 接口；
	1. 使用注解实现配置适配器：
  ```
			<!-- 注解适配器 -->
			<bean class="org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerAdapter"/>
  ```
		1. 这个配置节点就决定了，有@controller注解的类就是处理器
2. **视图解析器** 用来解析处理器处理后的逻辑视图，比如：加上前缀后缀，指定到特定的视图。
  ```
		<!-- 配置视图解析器 -->
		<bean class="org.springframework.web.servlet.view.InternalResourceViewResolver">
			<!-- 前缀 -->
			<property name="prefix" value="/WEB-INF/views/"/>
			<!-- 后缀 -->
			<property name="suffix" value=".jsp"/>
		</bean>
  ```
  - 如果 springMVC 没有配置视图解析器，如果 接口返回的 字符串（如： "hello"）给的是相对路径（‘jsp’），那么 spring 会把当前路径给配上去（如果 当前的 controller 的 uri 是 “/v1/say” ，那么这时返回的视图 uri 就是 "/v1/say/hello"），这个时候返回的视图就会在 当前的 controller 中去找 hello 方法接口，产生问题。如果返回的字符串是绝对路径（如： "/WEB-INF/jsp/hello.jsp"），那么spring 就会在服务器的此绝对路径里去找这个 jsp 文件并返回给客户端。
- **Small Notes:**
	1. 在SpringMVC中这四个核心的对象就已经可以将整个架构支撑起来了，整个**SpringMVC架构流程**：
		2. 中央分发器收到来自客户端的请求时，先将请求分发给处理器映射器，由控制器映射器决定了请求的**处理器**是谁（按面向对象编程思想，这儿一定是生成了映射的处理器对象，同时也**生成拦截器**之类的组件）；
		3. 同时分发器分发请求给处理器适配器，**适配器对相关的处理器进行适配扩展，并调用处理器对请求进行处理，****处理结果就包括了逻辑视图与其他的响应结果（比如：存放在ModelAndView中），适配器再将这些处理结果返回给中央分发器；
		4. 中央分发器将结果分发给**视图解析器**，视图解析器对逻辑视图进行解析（比如：加上前缀后缀），视图解析器再解析之后的具体的view返回给中央分发器；
		5. 中央分发器收到view后对其进行**渲染**（将数据结果填充至视图中），再把最终结果响应出去；

- 整个SpringMVC流程如上，我们常常使用时并不会完全按照上面四个核心对象进行配置，相对来说有更便利的方法来配置这四个核心对象:
	- **组件扫描器：**自动扫描`@Controller`标记的控制器，这样省去将各个配置器配置在bean中：
		<!-- 扫描器组件，将指定包中的带有特定注解的类全都扫描进容器可用的controller中 -->
		<context:component-scan base-package="com.woniuxy.springdemo.controller,
		com.woniuxy.springdemo.service">
			<!-- 指定注解过滤器 -->
			<context:include-filter type="annotation"
			expression="org.springframework.stereotype.Controller"/>
		</context:component-scan>
	- **注解映射器：**注解式映射器配置可以直接将使用过注解`@RequestMapping`的方法进行映射，而直接在处理器中寻到相关的处理方法；
			<bean class="org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping"/>
	- **注解处理器适配器：**注解式处理器适配器，配置此甜酸器直接对标记`@RequestMapping`的方法进行适配：
				<bean class="org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerAdapter"/>
	- **SpringMVC中的注解驱动配置：**注解驱动综合了前两个注解式的适配器与映射器，因此配置此驱动即可省略适配器与映射器的配置：
			<mvc:annotation-driven/>

- **Tips:**
	- *在spring-webmvc包中web_servlet包中最后有一个配置文件DispatcherServlet.properties，这个文件就规定了springmvc的默认核心对象；*



### springMVC 中的处理器

1. 通过上一步实现来自前端请求必须都通过 web.xml 文件中指定的 servlet 处理，也就是大部分请求都是交给了 springmvc，所有的 springmvc 的配置都在 springmvc.xml 文件中，在这个文件中，我们常常通过注解来实现请求与处理器（处理器中的方法）的映射。
2. springmvc.xml 配置中就指定了适配器映射，使用 `@Controller` 注解就让该类成为处理器
3. `@RequestMapping("url_name")` 则用来指定url路径，可以用来注解类与类的方法，想要请求进入处理方法中就要通过类的url与方法的url;
4. 注解使用时映射的方法中，可以与前端数据相通的**参数**有：
	1. 简单数据类型
	2. pojo，这种情况下，pojo对象的属性名要与请求的参数名保持一致；比如，user.username User.password
	3. session/request/response：用法与之前一致，可以用分发请求也可以重定向；
	4. Model/ModelAndView：
	5. 也可以为String类型的参数，只要参数名与来自前端的请求名一致，容器会自动将其注入到方法参数中；
	6. 注解`@RequestParam`的使用：
		1. 其中有参数：
			1. name:用于配置参数的别名，使用这个属性就可让请求url使用别名来访问到这个方法；
			2. required：boolean 类型，当其值为 true 时，请求必须带有这个参数；

1. 使用 controller 处理器时，各类方法 **返回数据类型**：
	1. ModelAndView，此对象中可以添加model数据（addObject（String name,Object object)方法），也可以指定view（通过setViewName(String name)方法，而这个name也就决定了去到哪个jsp视图）
	2. String 字符串：当要在使用映射方法处理之后想到转到另外一个方法中去(不进入视图）则直接返回字符串"response:+方法映射"；
	3. **void**:在controller形参上定义request与response来指定响应结果：
		1. "forward:+方法映射"，转发到相应方法中去，与response不同在于地址栏还是原来的地址，转发并没有执行新的request与response，而是和转发前的请求共用一个request与response，所以转发前的属性在转发后一样可以读取到；
		2. 也可以直接使用`response`重定向到指定页面：`response.sendRedirect(String url);`
		3. 使用`response`指定响应结果：
			1. 响应jason数据：`response.setCharacterEncoding("utf-8");`
			2. `response.getWriter().write(String "jason格式的字符串");`

## springmvc 的高级应用

### LocalResolver 区域解析器

在 springmvc 中配置这个解析器，用于国际化。其中解析器常用的有：
- cookie ，根据 CookieLocaleResolver 来选择区域；
  - 这个区域解析器所采用的Cookie可以通过cookieName和cookieMaxAge属性进行定制。cookieMaxAge属性表示这个Cookie应该持续多少秒，-1表示这个Cookie在浏览器关闭之后就失效。
- SessionLocaleResolver
  - 它通过检验用户会话中预置的属性来解析区域。如果该会话属性不存在，它会根据accept-language HTTP头部确定默认区域。

### 多部件解析器 MultipartResolver

用于图片上传，需要引入包：commons-fileupload 与 commons-io

### json 数据交互

使用两个注解：
- @RequestBody
  - 用于将请求的字符串使用 converter 转换成 json/xml 等格式并绑定到 controller 参数上去
- @ResponseBody
  - 用于将 controller 返回的结果 使用 converter 转换成 json/xml 格式直接 response 给浏览器

### Restful架构

- 只是一种规范，终极的目标是资源 URI
