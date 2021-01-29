---
date: 2021-01-30 00:34:00
tags: [java,spring,IoC]
categories: programming
description: Spring IoC container extension points
---

# Spring Container Extend Points

[Container Extend Points](https://docs.spring.io/spring-framework/docs/current/reference/html/core.html#beans-factory-extension)

spring 提供两种后处理器：

1. bean 后处理器 对容器中的 bean 进行后处理 加强
    1. bean 后处理器是一种特殊的 bean 无需 id 属性，不对外提供服务对 容器内其他 bean 进行后处理，其他的 bean 创建成功后，对 bean 进行近一步的增强处理。
    2. bean 后处理器必须实现 BeanPostProcessor 接口。该接口有两个方法：
        1. Object postProcessBeforeInitialization(Object bean, String beanName)
        2. Object postProcessAfterInitialization(Object bean, String beanName)
            1. 这两个方法的参数：bean 是即将进行后处理的 bean ， beanName 是这个 bean 在容器中配置的 id
            2. before 方法用于 bean 初始化（调用 afterPropertiesSet; 调用 init-method 指定的方法）之前， after 方法用于 bean 初始化之后。
    3. 如果使用 BeanFactory 作为 spring 容器，必须手动注册 bean 后处理器，必须获取 bean 后处理器实例
2. 容器后处理器 对 ioc 容器进行加强处理，增加其功能
    1. 负责处理容器本身，用于增加容器功能
    2. 容器后处理器必须实现 BeanFactoryPostProcessor 接口，其中方法：
        1. postProcessBeanFactory(ConfigurableListableBeanFactory BeanFactory)
    3. 同样，如果 使用 BeanFactory 作为容器，必须手动调用容器后处理器来处理 BeanFactory 容器。