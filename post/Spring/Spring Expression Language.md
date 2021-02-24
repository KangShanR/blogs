---
layout: post
title: SpEL
date: 2020-09-23 17:15:00
tags: [java,Spring,SpEL]
categories: [Spring]
description: Spring Expression Language
---

[Spring 表达式](https://docs.spring.io/spring/docs/5.2.9.RELEASE/spring-framework-reference/core.html#expressions-beandef)

## .1. SpEL Reference

[reference](https://docs.spring.io/spring/docs/5.2.9.RELEASE/spring-framework-reference/core.html#expressions-operator-safe-navigation)

### .1.1. Bean Reference

[bean reference](https://docs.spring.io/spring/docs/5.2.9.RELEASE/spring-framework-reference/core.html#expressions-ref-variables)

- 需要 evaluation Context 中配置上 BeanResolver 。
- 引用 bean 时 `@something`
- 引用 Factory Bean `&something`<!--more-->

### .1.2. Ternary Operator(if-then-else)

[三元运算符](https://docs.spring.io/spring/docs/5.2.9.RELEASE/spring-framework-reference/core.html#expressions-ref-variables)

`"false ? 'trueExp' : 'falseExp'"`

### .1.3. Elvis Operator

[Elvis' hairstyle Operator](https://docs.spring.io/spring/docs/5.2.9.RELEASE/spring-framework-reference/core.html#expressions-operator-elvis)

猫王运算符 `?:` 等价于 `notNull ? value:default`

### .1.4. Safe Navigation Operator

表达式中使用安全导引 `Members?.location` 如果是所取的数据为 null 将返回 null 而不是直接 NullPointerException 。

### .1.5. Collection Selection

集合 List/Map 选择器，对于一个 List 选择器作用于其每一个元素，而 Map 作用于其 Map.Entry 。

- 表达式中对 list 数据进行筛选 `locations.?[city == 'Serbian']` 选择 city 为 Serbian 的数据。
- `locations.?[value > 10]` 选择 map 中 value > 10 的数据。
