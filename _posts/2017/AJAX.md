---
title: AJAX
date: 2017-08-25 13:04:38
categories: programming
tags: [java,leadingEnd,programming]
keywords:
---

> AJAX，Asynchronous Javascript And Xml，异步的Javascript和xml；
> 其最大优点是不用重新加载整个网页实现与服务器数据交换并更新部分网页内容；
> 不是新的编程语言也不需要浏览器插件，只是基于 Javascript 和 xml 的新的方法；

## 传统方法使用 ajax

传统方法指使用基础的 js 调用 ajax 。
case:

```js
$(function () {
    //使用传统 ajax 异步请求
    $("#username").focusout(function () {
        console.log("username change");
        // new 一个 ajax 请求对象
        var xhr = new XMLHttpRequest();
        //请求响应后的回调函数
        xhr.onreadystatechange = function () {
            console.log("username request");
            if (xhr.status == 200 && xhr.readyState == 4 && xhr.responseText=="false") {
                console.log("wrong name");
                $("#usernameLint").html("用户不存在！");
                $("#usernameLint").css("color","red");
            }
        }
        //ajax 请求
        xhr.open("get", "${pageContext.request.contextPath}/usernameCheck?username=" +
            $("#username").val(), true);
        //发关请求
        xhr.send();
    })
})
```

## 使用 jquery 调用 ajax

case:

```js
$(function () {
    console.log("init")
    $("#vrfCodeImg").click(function(){
        $(this).attr("src", "${pageContext.request.contextPath}/verification_code_img?"+ new Date().getTime())
    });

    $("#username").keyup(function () {
        console.log("username keyup");
        $.ajax({
            url:"${pageContext.request.contextPath}/usernameCheck",
            type: "GET",
            data:{"username":this.val()},
            success:function (data) {
                console.log("success" + data);

            }
        })
    })
});
```

- ajax 请求与页面无关，所以其并不能获取到响应时 jsp 对象的内容，只能对 writer 的内容接收，不能使用诸如 `requestScope.res` 的内容进行获取响应数据；
- jsp 被翻译后的内容在页面上请求前 ajax 当然可以获取到，只是响应后的数据与 jsp 无关了，所以不能使用与 jsp 页面相关的数据（request/pageContext. etc）。
