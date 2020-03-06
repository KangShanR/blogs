---
title: servlet
date: 2017-09-01 14:23:30
categories: programming
tags: [programming,java,servlet]
keywords: servlet
---

> Servlet 是 javaWe b中处理 Http 请求的核心技术，众多的处理 web 框架都是对它的封装而来。Servlet 规范源自 Sun 公司，除了 servlet 技术还包括了 filter / listener 。

## Servlet的生命周期

1. `init()`，生命周期中，只执行一次，当服务器装入 servlet 时，便执行此方法完成初始化。
2. `service()`  servle 的核心，负责响应客户的请求。每当客户请求一个 HttpServlet 对象时，这个对象就生成一个请求参数 `ServletRequest` 与一个响应参数 `ServletReaponse` （同样也是对象）传递给自己的 `Service()` 方法，在这其中会默认调用 `HttpServlet` 的 `doPost()` 或 `doGet()` 方法。<!--more-->
3. `destroy()` 方法，仅执行一次，当服务器停止且卸载 Servlet 时执行该方法。这个时候 Servlet 生命周期终结。

## Servlet 执行的步骤

1. 当客户端发起一个 Http 请求， Servlet 接收到这个请求；
   1. tomcat 收到这个请求时是如何将这个 url 转化成到另一个 servlet ？
      1. 将 xml 解析后自己处理的通过映射关系找到了这个 servlet ？
2. Servlet 容器（Tomcat 引擎）收到这个请求生成一个 HttpRequest 对象，并将请求中的参数信息封装在其中；
3. Servlet 容器（Tomcat engine）生成一个 HttpResponse 对象，并将这两个对象传入给 Servlet 的 `service()` 方法作为参数；
4. 在这个 `Service()` 方法中， HttpServlet 调用 HttpRequest 对象的相关方法获取封装的 Http 请求信息；
5. HttpServlet 再调用 HttpResponse 对象的相关方法对这些参数做出处理并生成响应数据；
6. HttpServlet 将响应数据传给 webClient ，完成请求响应；

_**servlet3.0** 已有使用注册注册 servlet 的功能，添加注解 `@WebServlet(name = "register", urlPatterns = "/register")` 就完成了 servlet 注册到 tomcat 容器的功能_

问题：

1. 服务器上的服务是如何响应请求的？
   1. 这件事情是谁来做的呢？Tomcat engine
2. HTTP 协议规定了有请求必有响应？

## ServletContext

- servlet 直接调用 `getServletContext()` 即可获取。
- servlet 上下文，一个 web 应用启动后只有一个，通过把通用的数据存放在其中，在 web.xml 使用 `<config-param>` 标签配置数据。
- 用以存放数据时，可以在 `<load-on-startup>0</load-on-startup>` （此标签用以标记在应用启动时初始化的 servlet ，value 越小越先初始化，最小值为 0）的 servlet 中进行数据初始化。
- `getRealname(String path)` 可通过工程中的文件路径获取在服务器中的绝对路径。绝对路径是获取下载服务器资源时必用的参数。

## Response

- 通过 Response

### download

> 通过 servlet 下载文件

- 将运行的工程中的资源 copy 用 InputStream 读取，再将 InputStream 中的 bytes 写入 response 中的 ServletOutputStream 即可。
- 如果要指定浏览器不解析下载的资源且指定下载文件名，使用 `setHeader("Content-Disposition", "attachment;filename=")+filename`;
- 要对浏览器传来的中文文件名进行转码：`filename = new String(filename.getBytes("ISO-8859-1"), "utf-8")`；
- 对中文文件名进行转码成不同浏览器需要的格式：
  - 先获取请求客户端的 User-Agent 用判断浏览器的类型；
  - 根据不同浏览器用不同的方式解码：

```java
 // 对不同浏览器给以不同的中文编码
if (ua.contains("MSIE")) {
   // IE浏览器
   originFilename = URLEncoder.encode(originFilename, "utf-8");
   originFilename = originFilename.replace("+", " ");
} else if (ua.contains("Firefox")) {
   // 火狐浏览器
   BASE64Encoder base64Encoder = new BASE64Encoder();
   originFilename = "=?utf-8?B?"
            + base64Encoder.encode(originFilename.getBytes("utf-8")) + "?=";
} else {
   // 其它浏览器
   originFilename = URLEncoder.encode(originFilename, "utf-8");
}
System.out.println("encoded filename:" + originFilename);
```

#### 同样使用 download 发送验证码图片给客户端

> [reference](http://www.programmersought.com/article/5287123976/)，案例写在 JDBCDemo 项目中。

1. 使用 BufferdImage 生成图片；
2. 将图片写入 response.ServletOutputStream 中；

## Request

> Tomcat engine 封装的请求

- 获取客户机的信息： `getRemoteAddr()` `getRemoteUrl()` `getRemoteUri`, etc.
- 防盗链： `getHeader("Referer")` 获取请求来源。

### redirect & forward

> 重定向与转发

- redirect 重定向，其本质是将 statusCode 改成 302 ，再加上 Location 设置成一个新的 url，响应给客户端，客户端拿到后会根据这 StatusCode 重新请求新的 url（`senRedirect(String url)`， url 使用相对的， tomcat container 会转换成绝对的）。整个流程有发生过两次客户端的请求，可以看到浏览器在完成请求后其 url 变成了后一个请求的 url。
  - 对于 container 转换 url 规则：
    - 如果 url **没有 `/` ** 开头， container 将视 url 作与当前 servlet 相对关联
    - 如果 url 以 `/` 开头， container 将视 url 为与 container 根相对关联
    - 如果 url 以 `//` 开头， container 将视 url 为一个网络相对路径
- forward 是对一次请求中，前一个请求被应用内部转发到另一个 servlet ，Tomcat 会将整个过程完成并响应给客户端。浏览器接收最后的响应。前一次处理可以对 request 进行改动再交给后一次处理。
- 如果要将错误提示回显到原页面，只能使用 forward(jsp) 。如使用 redirect 将重新发起一次请求到 jsp ，新的 request 没有上一个 request 中保存的 attributes 将会丢失。

### 中文乱码的问题

- 当请求数据中，如果中文在请求体里（请求方式为 post），只需要对 request 设置解码字符集为 **utf-8** 即可
- 当中文在请求 url 中（method = get），需要对请求参数先进行 `iso8859-1` 编码，再使用 `utf-8` 字符集解码： `value = new String(value.getBytes("iso-8859-1"), "utf-8");`

原因：使用 get 方式请求，数据放在 url 中， http 协议不支持含中文的 utf8 字符集，所以对 url 数据统一使用 iso88591 编码。

## Cookie

服务端设置响应给客户端后，客户端再请求时会根据 Cookie 的属性进行带 Cookie 访问。

- 设置 cookie 过期时间： `cookie.setMaxAge(int seconds);`
- 删除 Cookie : `cookie.setMaxAge(0);`
- 设置 cookie 路径： `cookie.setPath(String path);` 如果不设置，其路径为当前资源

## Session

session 技术是基于cookie 的，其本质是服务器为客户端创建一块内存，为其编号，将编号用 cookie 形式响应给客户端，客户端再取此 id 去取 session 数据。

## jsp

> 概念：Java Server Pages；
> jsp技术使用java语言作为脚本语言，用于提供一个接口用于服务器响应HTTP请求的程序；
> jsp是一个特殊的 servlet ,它最终被执行成一个java的class文件并置入内存，当响应开始后会利用输出流生成一个Html页面传送给请求方；

- 其本质是一个 servlet ，被 Tomcat container 翻译成一个 servlet ，最后执行的是一个 java 代码将结果写入 outputStream 。被 http 协议传输给浏览器。
- 所有 jsp/jspx 文件的请求都是被 容器中全局配置 `web.xml` 中指定由 JspServlet 处理。

### Jsp的工作原理

1. 当请求方请求一个jsp页面时，jsp先被服务器中的**Servlet引擎转换成一个java源文件**，这个过程会对jsp页面中的**语法进行检查**，如果页面语法没有错则转换成功，这时Jsp引擎再利用 **javac** 将java源文件**编译成class文件加载到内存中**；
2. 其次再创建一个**Servlet的对象**，并执行该对你的`jspInit()`方法，与Servlet生命周期中一致这个`jspInit()`方法在其生成周期中只被执行一次；
3. **创建并启动一个线程**，线程来调用对象的`jspService()`方法（如果有多个请求同时请求一个jsp，则jsp引擎**会创建多个线程来请求**）；
4. Servlet容器将 **请求方的参数封装到HttpServletRequest对象** 中并再生成一个 HttpServletResponse 对象，将这两个对象作为参数传给 `jspService()` 方法；
5. 与servlet一样，jspServlet会调用Request对象中相应的方法将封装在其中的参数取出来，再**调用Response中相关的方法进行业务逻辑处理**，生成响应数据；
6. `jspService()` 方法将执行完的结果返回给请求方；

### Jsp指令

1. **page：**
    1. `<%@ page%>`作用于整个jsp页面，包括静态的包含文件；
    2. 一般放在jsp页面顶部，但不管放在哪个地方其作用范围都是整个jsp；
    3. 该指令不能作用于动态包含文件
    4. 可以在一个页面中用上多个，但是其中的属性只能用一次，不过`import`属性像java中的`import`一样，可以多次使用；
2. **taglib:**
    1. 表明此页面是使用的什么标签，也指定标签的前缀（比如，struts的标签，jstl 标签）；
3. **include**
    1. 包含指令，将会在jsp编译时插入一个包含文本或代码的文件；
    2. 这个包含的文件可以是html文件也可以是jsp文件、文本文件、java代码。但在这些包含文件中不能使用 `<html><body>` ，这些标签将会影响jsp文件中的标签的解析；
    3. 引入外部 jsp 文件包括动态引入与静态引入。
       1. 格式分别为：静态 `<%@ include file="">` ；动态 `<jsp:include file=""/>`
       2. 动态的执行过程：两个 jsp 先都编译成 _jsp.class 文件，在请求时外部 jsp 执行到引入标签就根据 uri 找到被包含的 jsp class 进行执行，最终把两个 class 执行的结果写在一直。
       3. 静态包含执行过程：在解析 jsp 成 class 时就将被包含 jsp 置入主 jsp 成为同一个 jsp，再被编译成 class 文件。
       4. 两种包含方式不同之处决定了：静态包含不让在两个 jsp context 域间传递数据，而动态包含可以。

### jsp 内置 9 大对象

1. request 就是 HttpRequest
2. response HttpResponse
3. session Session
4. application ServletContext
5. config ，ServletConfig servlet 的配置
6. out JspWriter 将页面写入 outputStream
7. page 被翻译后的 servlet
8. pageContext jsp 页面容器
9. exception 内置的一个 Throwable 与 errorPage 标签连胜：在发生异常时在新的页面 getMessage()

### EL 表达式

- expression language
- 在 jsp 中使用的表达式，其最重要的作用就是读取各域中的数据。
- 格式：`${[scope.]<key>}`
- 如果不指定域，那么将从小的域开始往大的域内查找，直到找到为止。依次为： pageContext > request > session > application(servletContext)。其底层就是使用了 jsp 的 `pageContext.getAttribute(String name);`
- 在 jsp 中使用 EL expression 获取项目绝对路径：`${pageContext.request.contextPath}`，使用 pageContext 获取到 jsp 万能内置对象以获取一系列的对象（request/response/session ，注意：此处获取的不是 requestScope 域对象， scope 域对象只能获取到 request 对象的域，其中只有 attributes ）。
- `${!empty (value)}` 用以判空，其中 空包括 `""`；

### jstl

- jsp standard tag language;
- 与 el 获取数据连用，组合出页面逻辑与数据；
- 需要导入 jstl 核心库 `<%@ taglib uri="名字，带着 http 协议头" prefix="c"%>`

#### if

使用格式：`<c:if test="boolean">`

#### forEach

使用格式：`<c:forEach items=listName var=itemname` 这里需要给 item 取一个变量名，在标签中就使用此变量名直接使用。
