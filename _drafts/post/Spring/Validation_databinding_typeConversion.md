---
title: Spring Validating, Data Binding and Type Conversion
date: 2020-04-15 12:14:38
tags: [validating,data binding,type conversion,Java,Spring]
categories: Spring
description: spring 中的数据验证、绑定与类型转换
---

# 1. Validating & Data Binding and Type Conversion

<!-- TOC -->

- [1. Validating & Data Binding and Type Conversion](#1-validating--data-binding-and-type-conversion)
  - [1.1. Validation on Spring's Validator Interface](#11-validation-on-springs-validator-interface)
    - [1.1.1. Configuring a Bean Validation Provider](#111-configuring-a-bean-validation-provider)
    - [1.1.2. Resolving Codes to Error Messages](#112-resolving-codes-to-error-messages)
  - [1.2. Bean Manipulation and the `BeanWrapper`](#12-bean-manipulation-and-the-beanwrapper)
    - [1.2.1. Built-in `PropertyEditor` Implements](#121-built-in-propertyeditor-implements)
      - [1.2.1.1. Spring 内置的 `PropertyEditor` 实现](#1211-spring-%e5%86%85%e7%bd%ae%e7%9a%84-propertyeditor-%e5%ae%9e%e7%8e%b0)
      - [1.2.1.2. 注册自定义 `PropertyEditor`](#1212-%e6%b3%a8%e5%86%8c%e8%87%aa%e5%ae%9a%e4%b9%89-propertyeditor)
        - [1.2.1.2.1. 使用 `PropertyEditorRegistrar`](#12121-%e4%bd%bf%e7%94%a8-propertyeditorregistrar)
  - [1.3. Spring Type Conversion](#13-spring-type-conversion)
    - [1.3.1. Converter SPI](#131-converter-spi)
    - [1.3.2. ConverterFactory](#132-converterfactory)
    - [1.3.3. GenericConverter](#133-genericconverter)
    - [1.3.4. ConditionalGenericConverter](#134-conditionalgenericconverter)
    - [1.3.5. Formatting](#135-formatting)
    - [1.3.6. The `ConversionService` API](#136-the-conversionservice-api)
    - [1.3.7. 配置 `ConversionService`](#137-%e9%85%8d%e7%bd%ae-conversionservice)
  - [1.4. Spring Field Formatting](#14-spring-field-formatting)
    - [1.4.1. 注解驱动 Formatting](#141-%e6%b3%a8%e8%a7%a3%e9%a9%b1%e5%8a%a8-formatting)
    - [1.4.2. 拓展 Formatting 注册](#142-%e6%8b%93%e5%b1%95-formatting-%e6%b3%a8%e5%86%8c)
  - [1.5. Spring MVC 中配置序列化与反序列化的 Converter](#15-spring-mvc-%e4%b8%ad%e9%85%8d%e7%bd%ae%e5%ba%8f%e5%88%97%e5%8c%96%e4%b8%8e%e5%8f%8d%e5%ba%8f%e5%88%97%e5%8c%96%e7%9a%84-converter)
    - [1.5.1. 序列化时间类型数据](#151-%e5%ba%8f%e5%88%97%e5%8c%96%e6%97%b6%e9%97%b4%e7%b1%bb%e5%9e%8b%e6%95%b0%e6%8d%ae)

<!-- /TOC -->

[spring reference](https://docs.spring.io/spring/docs/current/spring-framework-reference/core.html#validation)

Spring 中的 数据验证、数据绑定、类型转换。<!--more-->

## 1.1. Validation on Spring's Validator Interface

- 实现接口 Validator ，定义验证各个 POJO 的代码。
- 结合 `ValidationUtils 使用

### 1.1.1. Configuring a Bean Validation Provider

[reference](https://docs.spring.io/spring/docs/current/spring-framework-reference/core.html#validation-beanvalidation-overview)

- `LocalValidatorFactoryBean` 继承了 ValidatorFactory 和 Validator 两个接口，将其注册为一个 Bean。
- 在需要使用验证的 Bean 中注入 Validator 即可。
- 使用 `@Constraint` 自定义约束，再使用 `ConstraintValidator` 实现约束行为。
- Spring 驱动方法验证
    - 整合方法验证直接将 `MethodValidationPostProcessor` 注册到容器中

        ```java
        @Bean
        public MethodValidationPostProcessor validationPostProcessor() {
            return new MethodValidationPostProcessor;
        }
        ```

    - 目标类必须使用 Spring 的 `@Validated` 注解，同时目标类依赖 AOP 代理，否则不能正常工作。

### 1.1.2. Resolving Codes to Error Messages

- 如果需要使用 `MessageSource` 输出错误信息，可以使用在拒绝字段时提供的 error code。
- `MessageCodesResolver` 决定 `Error` 注册哪个 error code 。
- 默认解析器 `DefaultMessageCodesResolver` 不仅注册 reject 时提供的 message 与 code，也包括传递的字段名。

## 1.2. Bean Manipulation and the `BeanWrapper`

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

### 1.2.1. Built-in `PropertyEditor` Implements

[reference](https://docs.spring.io/spring/docs/current/spring-framework-reference/core.html#validation)

- Spring 使用 `PropertyEditor` 的概念来完成 `Object` 与 `String` 之间的转换。
- 常用使用场景：
    - 给 bean 设置 properties。在 xml 给 bean 配置了 class property ，使用 `ClassEditor` 将 String 转成 Class 对象。
    - 在 Spring MVC 中使用 `PropertyEditor` 解析 HTTP 请求参数（可手动绑定 `CommandController` 所有子类）。

#### 1.2.1.1. Spring 内置的 `PropertyEditor` 实现

[reference](https://docs.spring.io/spring/docs/current/spring-framework-reference/core.html#validation)

其中有默认注册为 `BeanWrapperImpl` ，Spring 将自动使用这些组件实现 String 与 各个 type 的转换。

#### 1.2.1.2. 注册自定义 `PropertyEditor`

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

##### 1.2.1.2.1. 使用 `PropertyEditorRegistrar`

- 在不同场景需要使用同一系列的 editor 时更适合此注册器。
- 与接口 `CustomEditorConfigurer` 连用，将 PropertyEditorRegistrar 实例注册到 CustomerEditorConfigurer 中，editor 可轻松地共享到 `DataBinder` 与 Spring MVC Controller。同时，这样操作在每次调用 PropertyEditor 时都创建一个新的实例，而避免了同步。

## 1.3. Spring Type Conversion

[reference](https://docs.spring.io/spring/docs/current/spring-framework-reference/core.html#beans-beans-conversion-customeditor-registration)

### 1.3.1. Converter SPI

- 函数式接口， `Converter<S, T>`

### 1.3.2. ConverterFactory

- 使用 `ConverterFactory<S, R>` 可以实现提供将一个类型转换成多个类型的 不同的转换器。

### 1.3.3. GenericConverter

相对于 Converter 提供了更复杂灵活的转换功能。针对转换多个目标 类型。

### 1.3.4. ConditionalGenericConverter

- 联合了 `GenericConverter` 和 `ConditionalConverter` 两个接口而成，可以指定目标字段进行转换。
- 可以给 GenericConverter 加上条件，指定允许哪些转换，哪些不允许；

### 1.3.5. Formatting

### 1.3.6. The `ConversionService` API

[reference](https://docs.spring.io/spring/docs/current/spring-framework-reference/core.html#core-convert-ConversionService-API)

- 大部分 `ConversionService` 实现了 `ConverterRegistry` ，这就提供了注册 Converter 的 SPI。内部实现将 conversion 工作委托给注册的 converter 。
- `GenericConversionService` 覆盖了大部分 converter 使用场景
- `ConversionServiceFactory` 提供工厂创建常用 `ConversionService` 。

### 1.3.7. 配置 `ConversionService`

- 使用默认的 `ConversionServiceFactoryBean` 为容器默认转换器服务，其提供了基础数据转换器（详见 `{@link DefaultConversionService # addDefaultConverters()}`）。
- 添加自定义转换器

```xml
<bean id="conversionService"
        class="org.springframework.context.support.ConversionServiceFactoryBean">
    <property name="converters">
        <set>
            <bean class="example.MyCustomConverter"/>
        </set>
    </property>
</bean>
```

- 使用默认转换复合类型数据（集合转集合）[reference](https://docs.spring.io/spring/docs/current/spring-framework-reference/core.html#core-convert-ConversionService-API)

```java
DefaultConversionService cs = new DefaultConversionService();

List<Integer> input = ...
cs.convert(input,
    TypeDescriptor.forObject(input), // List<Integer> type descriptor
    TypeDescriptor.collection(List.class, TypeDescriptor.valueOf(String.class)));
```

- 使用 `DefaultConversionService` 默认添加各个转换器到 Spring 容器中
- 添加自定义 Converter 到其中，使用时直接调用 convert 方法，其在添加时是将各个 converter 放入一个指定 map 中，在使用时再 get 到相应的目标 converter 再再使用（这里可以看出来，再复杂的系统设计，最后都回归到了数据结构中）。

_规律：一个工具有多个功能时就可以集成到一个 service 组件中：Converter -> ConverterService。再将一个组件注册到 IoC 容器中，在容器中任何位置自动装配上此组件即可使用。_

## 1.4. Spring Field Formatting

[reference](https://docs.spring.io/spring/docs/current/spring-framework-reference/core.html#format)

formatting 是 converting 的一个子集。在客户端环境（web 应用或桌面应用），需要将 `String` 与各个类型互转，同时也需要将 String 本地化。这个格式化的过程在 core.convert.Converter SPI 并没有直接解决。Spring 3 中引入 Formatting SPI 提供了可选的 `PropertyEditor`实现来完成格式化。

- `Formatter` 接口实现了 Printer 与 Parser 两个接口。

### 1.4.1. 注解驱动 Formatting

- `AnnotationFormatterFactory<? extend Annotation>` 实现此接口，使用注解指定类中字段格式化。常用注解所在包： `org.springframework.format.annotation`

### 1.4.2. 拓展 Formatting 注册

1. 使用 FormatterRegistry SPI，其中可以提供多种类型的 formatter 注册包括注解指定字段、直接指定字段类型，最终 formatter 根据其 parser 与 printer 转换成两个 converter 注册到 conversionService 中

## 1.5. Spring MVC 中配置序列化与反序列化的 Converter

使用 JSR310 的序列化工具。

### 1.5.1. 序列化时间类型数据

默认情况下其添加的 jdk8 与 LocalDateTimeSerialize 等序列化工具使用的 formatter 常并不是我们想的结果，这时需要配置自己想要的 formatter。

- jdk time 包 DateTimeFormatter 有具体的构造方法。可直接使用其 Builder 类 ： `DateTimeFormatterBuilder` 。
    - `ResolveStyle` 指定 DateTime 解析模式：STRICT 严格按照日期来，超出则无效；SMART 智能模式，比如天超过当月最大天就到最大的；LENENT 宽容模式，超出边界也将被转换，比如：月份 15
    - 指定 pattern ，在 builder 中 addPattern(String pattern) ，方法注释有对详细注释，其中有调用达到指定 pattern 字符等效方法说明。
    - builder.configure() 中最后将所有的 serializer 与 deserializer 都添加进 new SimpleModule 中，再将 simpleModule 注册到 objectMapper 中（所有的配置数据都会注册入 objectMapper），最后 build 方法即将此 objectMapper 返回供 `AbstractJackson2HttpMessageConverter` 构造（for Spring MVC）使用。
