---
title: springMVC的理解与认识
date: 2016-08-20 13:29:17
tags: [java,framework]
categories: programming

---

# 1. SpringMVC

<!-- TOC -->

- [1. SpringMVC](#1-springmvc)
	- [1.1. 核心对象](#11-%e6%a0%b8%e5%bf%83%e5%af%b9%e8%b1%a1)
		- [1.1.1. springMVC 中的处理器](#111-springmvc-%e4%b8%ad%e7%9a%84%e5%a4%84%e7%90%86%e5%99%a8)
	- [1.2. 使用代码代替 xml 配置文件](#12-%e4%bd%bf%e7%94%a8%e4%bb%a3%e7%a0%81%e4%bb%a3%e6%9b%bf-xml-%e9%85%8d%e7%bd%ae%e6%96%87%e4%bb%b6)
		- [1.2.1. LocalResolver 区域解析器](#121-localresolver-%e5%8c%ba%e5%9f%9f%e8%a7%a3%e6%9e%90%e5%99%a8)
		- [1.2.2. 多部件解析器 MultipartResolver](#122-%e5%a4%9a%e9%83%a8%e4%bb%b6%e8%a7%a3%e6%9e%90%e5%99%a8-multipartresolver)
		- [1.2.3. json 数据交互](#123-json-%e6%95%b0%e6%8d%ae%e4%ba%a4%e4%ba%92)
		- [1.2.4. xml 数据交互](#124-xml-%e6%95%b0%e6%8d%ae%e4%ba%a4%e4%ba%92)
		- [1.2.5. 数据校验](#125-%e6%95%b0%e6%8d%ae%e6%a0%a1%e9%aa%8c)
		- [1.2.6. Restful 架构](#126-restful-%e6%9e%b6%e6%9e%84)
	- [1.3. configuration based on java codes](#13-configuration-based-on-java-codes)
		- [1.3.1. ant style](#131-ant-style)
		- [1.3.2. RequestMapping](#132-requestmapping)
			- [1.3.2.1. 自定义注解](#1321-%e8%87%aa%e5%ae%9a%e4%b9%89%e6%b3%a8%e8%a7%a3)
	- [1.4. Functional Endpoints](#14-functional-endpoints)
	- [1.5. Annotated Controllers](#15-annotated-controllers)
		- [1.5.1. DataBidder](#151-databidder)
		- [1.5.2. Exceptions](#152-exceptions)
			- [1.5.2.1. 异常处理器链 Chain of Exceptions](#1521-%e5%bc%82%e5%b8%b8%e5%a4%84%e7%90%86%e5%99%a8%e9%93%be-chain-of-exceptions)
	- [1.6.](#16)

<!-- /TOC -->

> SpringMVC 同样是一种 MVC 架构，它与传统 MVC 框架的不同之处在于使用了中央调度器，用中央调度器 DispatcherServlet 来分发所有的请求与响应，中央调度器的存在就大大降低了其他组件之间的耦合度。这种分发请求与响应的实现得益于 spring 的装配。

<!--more-->

- **中央处理器的配置**：在web.xml中配置 springmvc 的 servlet

```xml
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

- 中央处理器 DispatcherServlet 在web.xml中被配置成一个 servlet ,并通过初始化上下文配置参数 springmvc.xml 的路径与设置其启动时机与该servlet初始化时机；
	1. 其中 load-on-startup 这个属性来指定这个中央处理器被初始化的时机：
		1. 当为负数时，只有第一次使用时才会初始化，这也就带来一个问题，第一次访问时就会慢一些；
		2. 当为非负数时，中央处理器会在servlet容器启动时初始化，而这个数值就是初始化的顺序；

## 1.1. 核心对象

> 各个核心对象都有默认值，也就是说如果没有手动配置这些， springmvc 会按默认配置进行构建窗口。默认配置文件：spring-webmvc 包中 org.springframework.web.servlet 中的 DispatcherServlet.properties 。

1. **中央分发控制器**（在springMVC框架中，它是核心的核心，所有的分发都由它处理，所以也叫 **中央处理器** ），处理请求并给出响应（下面的三个关键组件就是装配在 springmvc 中的，但中央处理器是装配在 web.xml 中作为一个 servlet 的）

   ```xml
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
		<url-pattern>/</url-pattern>
	</servlet-mapping>
  	```

	1. [替代方案 WebApplicationInitializer](https://docs.spring.io/spring/docs/current/spring-framework-reference/web.html)

2. **处理器映射器** HandlerMapping ：设置 handler 处理器与 url 资源的映射
	1. 使用 BeanNameUrlHandlerMapping 这个类时，就会将 handler 的 name 属性值作为 url 映射，访问这个处理器就填写其 name 属性值:`<bean id="login" name="loginController.do" class="com.kang.springdemo.controller.LoginController"/>` 如上，就可能过name属性值，设置 `<a href="loginController.do>登录</a>`
	2. SimpleUrlHandlerMapping 这个类型的映射要求映射 url 与 controller 的配置 id 相对应，并在这个节点内将 controller 的 id 与相关的 key 对应起来；

		```xml
		<bean class="org.springframework.web.servlet.handler.SimpleUrlHandlerMapping">
			<property name="mappings">
				<props>
				<!--配置指定的url与bean的id映射,可添加多个-->
					<prop key="/login.do">login</prop>
					<prop key="/login2.do">login2</prop>
				</props>
			</property>
		</bean>
		<bean id="login" name="loginController.do" class="com.kang.springdemo.controller.LoginController"/>
		<bean id="login2" name="loginController2.do" class="com.kang.springdemo.controller.LoginController2"/>
  		```

	3. 使用注解实现处理器与 url 的映射 `<bean class="org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping"/>`
		1. 这个配置节点就决定了处理器与其中的方法可以被注解 `@RequestMapping("url_name")` 映射并指定 url
3. **处理器适配器** HandlerAdapter，用于规定处理器的编写规则
	1. 使用接口来配置适配器：
		1. 当指定为 SimpleControllerHandlerAdapter 时，它就规定了要想成为处理器，就要实现 Controller 这个接口；
		2. HttpRequestHandlerAdapter 这个适配器要求所有的 Handler 都必须实现 HttpRequestAdapter 接口；
	2. 使用注解实现配置适配器（这个配置节点就决定了，有 `@Controller` 注解的类就是处理器）：

  		```xml
			<!-- 注解适配器 -->
			<bean class="org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerAdapter"/>

  		```

4. **视图解析器** 用来解析处理器处理后的逻辑视图，比如：加上前缀后缀，指定到特定的视图。

	```xml
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
	1. 在 SpringMVC 中这四个核心的对象就已经可以将整个架构支撑起来了，整个**SpringMVC架构流程**：
		1. 中央分发器收到来自客户端的请求时，先将请求分发给处理器映射器，由控制器映射器决定了请求的**处理器**是谁（按面向对象编程思想，这儿一定是生成了映射的处理器对象，同时也**生成拦截器**之类的组件）；
		2. 同时分发器分发请求给处理器适配器，**适配器对相关的处理器进行适配扩展，并调用处理器对请求进行处理，** 处理结果就包括了逻辑视图与其他的响应结果（比如：存放在ModelAndView中），适配器再将这些处理结果返回给中央分发器；
		3. 中央分发器将结果分发给**视图解析器**，视图解析器对逻辑视图进行解析（比如：加上前缀后缀），视图解析器再解析之后的具体的view返回给中央分发器；
		4. 中央分发器收到view后对其进行**渲染**（将数据结果填充至视图中），再把最终结果响应出去；

- 整个SpringMVC流程如上，我们常常使用时并不会完全按照上面四个核心对象进行配置，相对来说有更便利的方法来配置这四个核心对象:
    - **组件扫描器：**自动扫描 `@Controller` 标记的控制器，这样省去将各个配置器配置在bean中：
		<!-- 扫描器组件，将指定包中的带有特定注解的类全都扫描进容器可用的controller中 -->
		<context:component-scan base-package="com.kang.springdemo.controller,
		                                      com.kang.springdemo.service">
			<!-- 指定注解过滤器 -->
			<context:include-filter type="annotation"
			expression="org.springframework.stereotype.Controller"/>
		</context:component-scan>
    - **注解映射器：**注解式映射器配置可以直接将使用过注解`@RequestMapping`的方法进行映射，而直接在处理器中寻到相关的处理方法； `<bean class="org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping"/>`
    - **注解处理器适配器：**注解式处理器适配器，配置此甜酸器直接对标记`@RequestMapping`的方法进行适配： `<bean class="org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerAdapter"/>`
    - **SpringMVC中的注解驱动配置：**注解驱动综合了前两个注解式的适配器与映射器，因此配置此驱动即可省略适配器与映射器的配置：`<mvc:annotation-driven/>`

- **Tips:**
    - *在spring-webmvc包中web_servlet包中最后有一个配置文件DispatcherServlet.properties，这个文件就规定了springmvc的默认核心对象；*

### 1.1.1. springMVC 中的处理器

在 DispatcherServlet 中， spring 规约的处理器 bean [reference](https://docs.spring.io/spring/docs/current/spring-framework-reference/web.html#mvc-servlet-special-bean-types)

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

5. 使用 controller 处理器时，各类方法 **返回数据类型**：
	1. ModelAndView，此对象中可以添加model数据（addObject（String name,Object object)方法），也可以指定view（通过setViewName(String name)方法，而这个name也就决定了去到哪个jsp视图）
	2. String 字符串：当要在使用映射方法处理之后想到转到另外一个方法中去(不进入视图）则直接返回字符串"response:+方法映射"；
	3. **void**:在controller形参上定义request与response来指定响应结果：
		1. "forward:+方法映射"，转发到相应方法中去，与response不同在于地址栏还是原来的地址，转发并没有执行新的request与response，而是和转发前的请求共用一个request与response，所以转发前的属性在转发后一样可以读取到；
		2. 也可以直接使用`response`重定向到指定页面：`response.sendRedirect(String url);`
		3. 使用`response`指定响应结果：
			1. 响应jason数据：`response.setCharacterEncoding("utf-8");`
			2. `response.getWriter().write(String "jason格式的字符串");`

## 1.2. 使用代码代替 xml 配置文件

参照 spring mvc doc： org.springframework.web.WebApplicationInitializer。

### 1.2.1. LocalResolver 区域解析器

在 springmvc 中配置这个解析器，用于国际化。其中解析器常用的有：

- cookie ，根据 CookieLocaleResolver 来选择区域；
    - 这个区域解析器所采用的Cookie可以通过cookieName和cookieMaxAge属性进行定制。cookieMaxAge属性表示这个Cookie应该持续多少秒，-1表示这个Cookie在浏览器关闭之后就失效。
- SessionLocaleResolver
    - 它通过检验用户会话中预置的属性来解析区域。如果该会话属性不存在，它会根据accept-language HTTP头部确定默认区域。

### 1.2.2. 多部件解析器 MultipartResolver

用于文件上传，需要引入包：commons-fileupload 与 commons-io

### 1.2.3. json 数据交互

> 在前后端分离的项目中，特别是存在为移动端提供的接口都应该使用 json 数据的格式对前端提供接口。

渲染 view 是 mappingJakson2JsonView

- 实际使用的是 `MappingJackson2HttpMessageConverter`
- 添加在 DispatcherServlet 的适配器中的 messageConverters 中：

  ```xml
  <property name="messageConverters">
    <list>
      <bean class="org.springframework.http.converter.xml.MappingJackson2HttpMessageConverter" />
    </list>
  </property>
  ```

需要使用的依赖包：

1. jackson-core
2. jackson-annotations
3. jackson-databind

使用两个注解：

- @RequestBody
    - 用于将请求的字符串使用 converter 转换成 json/xml 等格式并绑定到 controller 参数上去
- @ResponseBody
    - 用于将 controller 返回的结果 使用 converter 转换成 json/xml 格式直接 response 给浏览器

### 1.2.4. xml 数据交互

使用的渲染 view 是 mappingJackson2XmlView

- 实际使用的是 `MappingJackson2XmlHttpMessageConverter`
- 添加在 DispatcherServlet 的适配器中的 messageConverters 中：

  ```xml
  <property name="messageConverters">
    <list>
      <bean class="org.springframework.http.converter.xml.MappingJackson2XmlHttpMessageConverter" />
    </list>
  </property>
  ```

需要的依赖包：

1. jackson-annotations
2. jackson-dataformat-xml

使用：
 同样使用两个标签 `@RequestBody` `@ResponseBody` 并联合使用 `@RequestMapping(produces={}, consumes={})` 来确定请求与返回数据的格式。

### 1.2.5. 数据校验

springmvc 中可以直接使用 Hibernate 的一个校验框架：hibernate-validator。基于注解实现数据的校验。

### 1.2.6. Restful 架构

- 只是一种规范，终极的目标是资源 URI

## 1.3. configuration based on java codes

使用 java 代码配置 mvc [reference](https://docs.spring.io/spring/docs/current/spring-framework-reference/web.html#mvc-config)

### 1.3.1. ant style

关于 spring 中写 ant style 路径规则参照此类 doc: `org.springframework.util.AntPathMatcher`

### 1.3.2. RequestMapping

- 可使用正则表达式来匹配 url 参数
- pattern 匹配比较，越具体的 url 越匹配。
- Consumable/Producible Media Types 请求消费/生产数据类型匹配，`MediaType` 中有基本的类型。可使用 非 `!` 运行符
- Parameters,headers
    - 窄化请求路径：指定参数是否存在 `param` `!param`，指定具体参数类型： `param=myValue`
    - headers 相同窄化：

	```java
	@GetMapping(path = "/pets", headers = "myHeader=myValue")
	public void findPet(@PathVariable String petId) {
		// ...
	}
	```

- HTTP HEAD,OPTIONS 请求，自动转换请求到 GET 上，也可配置多个请求方式在同一个 URL 上

#### 1.3.2.1. 自定义注解

_Spring MVC also supports custom request-mapping attributes with custom request-matching logic. This is a more advanced option that requires subclassing RequestMappingHandlerMapping and overriding the getCustomMethodCondition method, where you can check the custom attribute and return your own RequestCondition._

## 1.4. Functional Endpoints

函数式 mvc 编程，与 jdk8 很好地整合。可以直接使用流式编码将请求与响应数据装配好，诸如：header/body[reference](https://docs.spring.io/spring/docs/current/spring-framework-reference/web.html#webmvc-fn)

## 1.5. Annotated Controllers

Spring MVC 提供的注解式 Controller。[reference](https://docs.spring.io/spring/docs/current/spring-framework-reference/web.html#mvc-controller)

### 1.5.1. DataBidder

使用 `@DataBidder` 给 Controller 添加数据绑定。

### 1.5.2. Exceptions

[reference](https://docs.spring.io/spring/docs/current/spring-framework-reference/web.html#mvc-exceptionhandlers)

spring MVC 中的异常。

Spring MVC中的异常处理器 HandlerExceptionResolver 的实现：

1. SimpleMappingExceptionResolver 可指定异常类名与错误页面的映射，用于浏览器应用
2. DefaultHandlerExceptionResolver 通过 Spring MVC 处理异常，并将异常与状态码映射。`ResponseEntityExceptionHandler` and REST API exceptions
3. ResponseStatusExceptionResolver 通过注解 `@ResponseStatus` 处理异常。根据注解值映射其到生意人 HTTP 状态码
4. ExceptionHandlerExceptionResolver 通过调用 Controller 或 ControllerAdvice 中的 `@ExceptionHandler` 方法处理异常。

#### 1.5.2.1. 异常处理器链 Chain of Exceptions

1. 形成异常处理器链直接 delare 多个异常处理器 bean 即可，指定其 `order` 值，越高的 order 值，执行处理得越晚。
2. 异常处理器返回数据规约：
    1. 使用 ModelAndView 指定错误页面
    2. 如果处理器已经将异常处理，返回一个空的 ModelAndView
    3. 如果处理器未处理，后面的处理器继续，如果最后异常一直未被处理，抛给 Servlet 容器。
        1. 当所有异常处理器未将异常处理，异常传递到 Servlet 容器或指定了一个错误状态码（4xx,5xx），servlet container 可以渲染一个默认的错误 HTML 页面，在 web.xml 中配置（servlet API 不提供 java 形式的方式创建 error page mapping，只能以此种形式创建	）：

		 ```xml
		 <error-page>
			<location>/error</location>

		</error-page>
		```

        2. servlet container 同时会作一个 ERROR 分发到配置的 URL ，于是就交给了 DispatherServlet，如果有 Controller 对此 URL 处理，将映射到此 Contrller 进行处理。剩下的就交给 Controller ，可能指定一个 model ，也可能响应一个 JSON。

3. Spring MVC 自动注册内置的异常处理器处理的异常包括：Spring MVC 异常、`@ResponseStatus` 注解的异常、`@ExceptionHandler` 注解的方法处理。可自定义异常处理器列表替换内置的处理器。

## 1.6.  
