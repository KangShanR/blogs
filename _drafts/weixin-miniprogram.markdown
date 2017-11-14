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


## 函数的使用

### 回调函数的使用

在快递这个小应用中，没有做到使用回调函数。也在考虑是不是没有 api 的原因

## 页面的生命周期

当使用 wx.redirectTo() 与 wx.navigateTo() 这两个API 进行页面的切换时，他们的不同之处在于：
- redirectTo 是将当前页面重定向为一个新的页面，直接替换成了新的页面，原页面会被卸载（其 onUnload 函数会被执行）；
- navigateTo 则只是将新页面加载并置为当前页面（其 onLoad() 函数会被执行），返回会返回到这个页面；同时原页面会被隐藏（其 onHide() 函数会被执行）。

### 页面间数据的传递

可以使用组件 <navigator> 在其 url 属性中设置跳转值与相关的参数。在新页面的 onLoad(options) 函数中使用方法 this.setData({globleData:options.msg}) 将数据传给全局变量。其它地方要使用这个变量时再调用全局变量了。
