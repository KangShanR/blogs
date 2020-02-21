---
layout: "post"
title: "javascript"
date: "2017-11-13 14:53"
---

# javascript 的方法总结

## javascript 中function 定义函数

使用 function 定义函数的方法有两种方式：

```js
// 调用这个方法不会引起报错
// 函数声明
alert(sum1(100,200));
function sum1(arg1,arg2){
    return arg1+arg2;
}

// 函数表达式
alert(sum2(100,200));
var sum2 = function(arg1,arg2){
  return arg1 + arg2;
}
```

后一种的调用函数表达式就会报错。原因在于，上一种方法调用时函数已经被加载到整个函数库顶部，而下一种方式使用 alert 方法调用 sum2 函数时，并没有定义好这个函数，就会报错。

### 无论你怎么去定义你的函数，JS 解释器都会把它翻译成一个Function 对象

### js 中的匿名函数

第二个方式的定义该当就是将匿名函数指向引用 sum2 。而我们常见到的匿名函数的使用 JQuery 的方法定义使用匿名函数： `(function(x,y){return x+y})(1,2)` 这儿使用了小括号将匿名函数包裹起来，这样在执行这段匿名函数时就会将这个匿名函数返回为一个 Function 对象（我们执行这个匿名函数时实质上是在调用这个 Function 对象），所以我们调用这个匿名函数按照调用普通函数的理解就是在这个 Function 对象后加上参数，所以就有了在这个匿名函数后加上小括号，再跟上参数列表。

## 闭包特性

就是应用局部变量进行拓展；
