---
date: "2017-11-17 11:44"
categories: programming
tag: [java,programming]
---

# 注解的使用

## 注解 @Autowired 的使用

单词： autowired 的意思就是自动装配。而在 java 编程中使用此注解就是将标注过注解的都自动装配到 spring 容器中。

据此理解的话，在编码中：

- 如果是属性被此注解标注，则此属性就将这个 bean 注入到容器中，不用写此属性的 getter() 与 setter() 方法，在 bean.xml 配置中也不用写此属性的 <property> 标签了；
- 如果方法或构造函数被 @Autowired 注解，则此方法参数中的 bean 就会自动被查找装入到这个方法中；

## 注解 @ResponseBody

此注解标明这个方法返回不经过视图解析器处理，直接将处理信息写成字节流返回给浏览器，成为 json 对象写到浏览器页面中。

## @RequestBody

- 此注解将请求的 body 部分数据使用 converter 解析并将相应的数据绑定到要返回的对象上；
- 使用 converter 解析的结果绑定到 controller 中的方法的参数上；
- 使用json 解析时，使用的属性访问器，也就是说要对应请求 json 数据 key 与属性访问器对应，而不是与请求 vo 的 property 的名对应；

## @RequiresPermissions

shiro 框架中的权限验证注解，用于验证是否拥有某权限。

## @PathVariable

用于将方法参数绑定到请求路径中去

```java
@Controller  
@RequestMapping("/owners/{ownerId}")  
public class RelativePathUriTemplateController {  

  @RequestMapping("/pets/{petId}")  
  public void findPet(@PathVariable String ownerId, @PathVariable String petId, Model model) {
    // implementation omitted
  }  
}  
```

### @RequestHeader @CookieValue

```java
@RequestMapping("/displayHeaderInfo.do")  
public void displayHeaderInfo(@RequestHeader("Accept-Encoding") String encoding,  
                              @RequestHeader("Keep-Alive") long keepAlive)  {  
}  
```

上面的代码就参数 encoding 与 keepAlive 分别绑定到了请求的 Header 中去。

```java
@RequestMapping("/displayHeaderInfo.do")  
public void displayHeaderInfo(@CookieValue("JSESSIONID") String cookie)  {  
}
```

而这一段代码就将参数 cookie 绑定到 JSESSIONID 上。
_note:关于这儿的请求是将请求的 header/cookie 中的值绑定到请求方法参数中还是将请求时的参数绑定到 header/cookie 中是一个未搞清的问题。查找上说是将 header/cookie 值绑定到请求方法参数中，但如果是这样就没必要设置这个参数了，直接获取这些值在方法中调用就是，而后者却更有必要，调用方法时参数就直接当作 header/cookie 值去请求了看来也更合理_

### @Repository

> 对应 dao 层的数据

## 注解的定义与使用

> 注解是 JAVA5.0 之后的高级特性。可以使用自定义注解来使用。

### 四个元注解

注解的注解，用于标注该注解的基本属性。

- @Documented 注解是否包含在 JavaDoc 中
- @Retention 什么时候使用该注解，定义该注解的生命周期
  - RetentionPolicy.SOURCE
  - RetentionPolicy.CLASS
  - RetentionPolicy.RUNTIME
- @Target 定义该注解使用的地方
  - ElementType.TYPE 类、接口、枚举等
  - ElementType.FIELD 字段属性
  - ElementType.METHOD 方法
  - ElementType.PARAMETER 方法参数
  - ElementType.CONSTRUCTOR 构造函数
  - ElementType.LOCAL_VARIABLE 本地变量
  - ElementType.PACKAGE 包
- @Inherited 是否允许子类继承该注解

### 注解的定义

注解定义中的属性只能是 String、Enum、及基本数据类型

```java
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@interface Todo {
  public enum Priority {LOW, MEDIUM, HIGH}
  public enum Status {STARTED, NOT_STARTED}
  String author() default "Yash";
  Priority priority() default Priority.LOW;
  Status status() default Status.NOT_STARTED;
}
```

如果注解中属性只有一个，那么使用时不需要写属性名，直接写值即可：

```java
@Target(ElementType.TYPE)
@interface Remark{
  String author() default "kfc";
}

@Remark("jfk")
public class Change{
}
```
