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

### 逻辑层 App Service

整个小程序中的逻辑代码都是在 javascript 的基础作了些修改，因此有些差异点需要注意：
- 框架并未在浏览器上运行，因此 js 中有些对象不能使用，诸如：document/window
- 开发人员写的所有代码都会打包成一份 js ，其生命周期是：从小程序启动到销毁。

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
  在需要使用上面模块的地方使用 require(path) 将公共代码引入（require 目前不支持绝对路径）：
  ```
  var common = require('common.js')
  ```

  **note**： `exports` 是指向 module 的一个引用，随便更改会导致指向混乱，所以推荐使用 module.exports 。

### wepy 框架的使用

wepy 是一个更常规化的框架，使用起来更与常规的框架更接近，使用平滑度更好。

[参考文档][b8d48afe]

#### 安装 wepy

详见 [参考文档][b8d48afe] ：
- 安装 node 后直接使用 node 命令，安装 wepy 到用户 node 包中（使用 `wepy -v` 查看版本号以验证是否安装成功）；
- 在相关的项目目录中，使用 `wepy new projcetName` 来新建 wepy 项目， wepy 会自动生成相关项目目录结构，生成之后就可以使用微信开发工具打开此目录进行开发。

  [b8d48afe]: https://segmentfault.com/a/1190000007580866 "wepy 框架参考文档"

#### wepy 的配置

在生成的目录结构中可以找到整个项目的配置文件： `wepy.config.js`，整个项目的配置信息就在其中。可以将项目页面文件的后缀改为更为常用的 `.vue`，从而更方便 IDE 识别。

#### .wpy 文件的结构

.wpy 文件综合了原生的小程序三个 page 文件，其中各个标签对应不同的 page 文件。

- <template> 对应着页面标记文件 wxml
- <script>  对应着 js 脚本文件
- <style> 对应着样式文件 .wxss
