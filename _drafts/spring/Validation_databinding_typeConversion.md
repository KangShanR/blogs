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

其中有默认注册为 `BeanWrapperImpl` ，Spring 将自动使用这些组件实现 String 与 各个 type 的转换。

#### 注册自定义 `PropertyEditor`

[reference](https://docs.spring.io/spring/docs/current/spring-framework-reference/core.html#validation)

两种注册方式：

1. 当有获取到 BeanFactory reference 时，使用 ConfigurableBeanFactory 其 `registerCustomEditor()` 方法将自定义的 PropertyEditor 注册进来（不推荐）。
2. 使用名为 `CustomEditorConfigurer` 的 bean factory post-processor，可使用注册 bean 方式注册，可设置其内置属性。

- 实现一个 PropertyEditor

    ```java
    public class ExoticTypeEditor extends PropertyEditorSupport {

        public void setAsText(String text) {
            setValue(new ExoticType(text.toUpperCase()));
        }
    }
    ```

- 将 editor 注册到配置器中

```xml
<bean class="org.springframework.beans.factory.config.CustomEditorConfigurer">
    <property name="customEditors">
        <map>
            <entry key="example.ExoticType" value="example.ExoticTypeEditor"/>
        </map>
    </property>
</bean>
```

##### 使用 `PropertyEditorRegistrar`

- 在不同场景需要使用同一系列的 editor 时更适合此注册器。
- 与接口 `CustomEditorConfigurer` 连用，将 PropertyEditorRegistrar 实例注册到 CustomerEditorConfigurer 中，editor 可轻松地共享到 `DataBinder` 与 Spring MVC Controller。同时，这样操作在每次调用 PropertyEditor 时都创建一个新的实例，而避免了同步。

## Spring Type Conversion

[reference](https://docs.spring.io/spring/docs/current/spring-framework-reference/core.html#beans-beans-conversion-customeditor-registration)

### Converter SPI

- 函数式接口， `Converter<S, T>`

### ConverterFactory

- 使用 `ConverterFactory<S, R>` 可以实现提供将一个类型转换成多个类型的 不同的转换器。

### GenericConverter

相对于 Converter 提供了更复杂的转换功能。针对转换多个目标 类型。

### ConditionalGenericConverter

联合了 `GenericConverter` 和 `ConditionalConverter` 两个接口而成，可以指定目标字段进行转换。
