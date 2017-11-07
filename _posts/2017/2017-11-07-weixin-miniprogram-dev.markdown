---
layout: "post"
title: "weixin-miniprogram-dev"
date: "2017-11-07 16:00"
---

# 微信小程序开发

开发小程序过程中使用微信编写好的框架知识整理。

## 配置（.json 文件）

- 在整个程序全局配置中，用 app.json 文件配置。而每个单独的页面配置就在每个 page 文件夹中的 .json 文件中，页面的配置会覆盖全局配置信息。[官方文档][e7cda257]
- 页面配置只有 window 相关配置；
- 全局配置中，app.json 中需要配置：
  |属性|	类型|	必填|	描述|
  |:---:|:---:|:---:|:---|
  |pages	|String Array	|是|	设置页面路径|
  |window|	Object|	否|	设置默认页面的窗口表现|
  |tabBar	|Object|	否|	设置底部 |tab| 的表现|
  |networkTimeout|	Object|	否|设置网络超时时间|
  |debug	|Boolean|	否|	设置是否开启| debug 模式|

  [e7cda257]: https://mp.weixin.qq.com/debug/wxadoc/dev/framework/config.html "小程序开发配置说明"

## 框架
### 文件作用域

小程序中，各个页面的脚本使用 javascript(js) ，在 js 的基础上作了些拓展。
- 比如：每个程序定义了 App({}) 方法与 Page({}) 方法，分别用来对应程序与页面。使用 getApp(){} 方法与 getCurrentPage(){} 方法来获取整个程序与页面；
- 不同的页面对应不同的 js 文件，不同的 js 文件中可以定义的变量的作用域只在这个 js 文件内，也就意味着：不同的 js 文件里可以定义同名的变量；
- 定义全局变量：
  - 在 app.js 中的 App({})中定义： `App({globalData: 1})`；
    - 获取时，在本文件 （app.js） ：
      ```
      app.globalData++;//全局变量自增 1
      ```
    - 在其他文件（other.js）：
      ```
      var app = getApp();
      app.globalData++;
      ```

### 模块化

小程序开发中，将 js 脚本公共复用频率高的脚本抽离出来，其它的地方要使用时再通过 export 将其导入使用。
- eg: 抽离的公共 js 文件为 common.js
  ```
  //common.js
  //方法一
  function sayHello(name){
    console.log('Hello' + name);
  }
  //方法二
  function sayGoodbye(name){
    console.log("Goodbye ${name}")
  }

  //使用 module.exports 或 exports 将其暴露出来
  module.exports.sayHello = sayHello
  exports.sayGoodbye = sayGoodbye
  ```
  在需要使用上面模块的地方使用 require(path) 将公共代码引入：
  ```
  var common = require('common.js')
  ```
