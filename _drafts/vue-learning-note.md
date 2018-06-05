---
layout: "post"
title: "vue-learning-note"
date: "2017-10-25 18:12"
---
# vue 学习
> 在新公司接触并即将使用到前端vue框架，笔记记录在此；

## installation
> 学习 vue 前的准备，安装运行环境

### node.js
> node 的安装可以将npm工具都集成在系统中，使用 npm 安装vue更方便；

## vue 的核心基础点
> vue 的核心点，使用最多也最简单

### 组件化应用构建

## 要学习 vue 过程中复习到的点
> 很多的前端知识都忘记了，现在不得不再复习一次

### 关于使用外链
> html 中外链引用的点

在一个 html 文件中，使用外链实现将外部 js 或 css 文件引入到本 html 中：
1. 外部 css 引入格式：在 `<head>` 标签中写入 `<link rel="stylesheet" type="text/css" href="view/css/vue-test.css">`；
2. 如果不引入外部的 css 文件，要在内部定义 css ，在 `<head></head>` 标签中写标签 `<style></style>`，而将所有的样式都写入标签 `<style></sytle>` 中；
3. 外部 js 引入格式：在 `<head>` 标签中写入 `<script src="https://unpkg.com/vue"></script>`；
4. `<script>` 标签可以写在 `<head>` 标签中，也可以写在 `<body>` 中，而在 `vue` 引入页面中，所有的 `vue` 对象都写在 `<body>` 中的 `<script>` 中，且这个 `<script>` 写在其他引用 `vue` 对象之后，不然引用无效；
