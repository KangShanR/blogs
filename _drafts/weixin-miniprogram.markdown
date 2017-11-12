---
title: "weixin-miniprogram"
categories: programming
date: "2017-11-09 10:52"
tag: miniprogram
---

# 微信小程序开发

## 使用微信自带的框架语言

### 基本的语法

使用模板有两种方式：
- 使用 `<template>` 标签
- 使用 `<include>` 标签引入外部

## 事件

以下三类事件都是冒泡事件，所谓冒泡事件就是在子层级视图引发的事件也会同时引发出父层级的事件

### 点击事件 tap

### 长点击事件 langtap

### 触摸事件

#### touchstart 点击开始事件

#### touchend 点击结束事件

#### touchcancel 点击中断事件

##### touchend 与 touchcancel 的区别

touchend 只是单纯地结束了一次点击，而 touchcancel 是点击发生后并未结束出现了来电话这类高权限事件打断这次点击，这时才出现 touchcancel 事件

#### touchmove

### 其他事件

#### submit

#### input

## 事件绑定

### bind 绑定

使用 bind 绑定了冒泡事件，就会同时触发父层级的事件；

### catch 绑定

使用 catch 绑定的事件，则不会触发父层级的事件；
