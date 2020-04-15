# Valicating & Data Binding and Type Conversion

[spring reference](https://docs.spring.io/spring/docs/current/spring-framework-reference/core.html#validation)

Spring 中的 数据验证、数据绑定、类型转换。

## Validation on Spring's Validator Interface

- 实现接口 Validator ，定义验证各个 POJO 的代码。
- 结合 `ValidationUtils 使用

### Resolving Codes to Error Messages

- 如果需要使用 `MessageSource` 输出错误信息，可以使用在拒绝字段时提供的 error code。
- `MessageCodesResolver` 决定 `Error` 注册哪个 error code 。
- 默认解析器 `DefaultMessageCodesResolver` 不仅注册 reject 时提供的 message 与 code，也包括传递的字段名。

## Bean Manipulation and the `BeanWrapper`

[reference](https://docs.spring.io/spring/docs/current/spring-framework-reference/core.html#validation)

- Spring 定义一个 [bean](https://docs.oracle.com/javase/8/docs/api/java/beans/package-summary.html) 的原则：一个类有默认的构造器，对于其属性要有其 setter 与 getter 。
- beans package 中重要的接口 `BeanWrapper` 提供了访问设置 bean 各种属性的功能。

    ```java
    BeanWrapper company = new BeanWrapperImpl(new Company());
    // setting the company name..
    company.setPropertyValue("name", "Some Company Inc.");
    // ... can also be done like this:
    PropertyValue value = new PropertyValue("name", "Some Company Inc.");
    company.setPropertyValue(value);

    // ok, let's create the director and tie it to the company:
    BeanWrapper jim = new BeanWrapperImpl(new Employee());
    jim.setPropertyValue("name", "Jim Stravinsky");
    company.setPropertyValue("managingDirector", jim.getWrappedInstance());

    // retrieving the salary of the managingDirector through the company
    Float salary = (Float) company.getPropertyValue("managingDirector.salary");
    ```

### Built-in `PropertyEditor` Implements

[reference](https://docs.spring.io/spring/docs/current/spring-framework-reference/core.html#validation)

- Spring 使用 `PropertyEditor` 的概念来完成 `Object` 与 `String` 之间的转换。
- 常用使用场景：
    - 给 bean 设置 properties。在 xml 给 bean 配置了 class property ，使用 `ClassEditor` 将 String 转成 Class 对象。
    - 在 Spring MVC 中使用 `PropertyEditor` 解析 HTTP 请求参数（可手动绑定 `CommandController` 所有子类）。

#### Spring 内置的 `PropertyEditor` 实现

[reference](https://docs.spring.io/spring/docs/current/spring-framework-reference/core.html#validation)