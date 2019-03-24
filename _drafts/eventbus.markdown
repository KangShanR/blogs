---
layout: "post"
title: "eventbus"
date: "2019-03-22 18:53"
---

# eventbus

> 轻松实现事件的发布订阅

##  三步实现 evnetbus

> [reference](http://greenrobot.org/eventbus/documentation/how-to-get-started/)


1. 定义好自己想要的事件；
```
@Getter
@Setter
@AllArgumentConstructor
public class MyEvent{
    private String myEventMessage;
}
```
2. 定义好订阅者，eventbus 3 后 在需要订阅的方法上加上 `@Subscrible` 即可实现
```
@Subscrible(thredMode = ThreadMode.MAIN)
publice onEventPosted(MyEvent event){
    log.info(event.getMyEventMessage());
}
```
  1. subscriber 需要注册与注销到 eventbus 中才能收到事件发布
3. 事件发布：
```
Eventbus.getDefult().post(new MyEvent("eventMessage"));
```


## ThreadMode

> 订阅者线程模式，确定订阅者收到事件的线程模式

1. ThreadMode.POSTING
2. ThreadMode.MAIN
3. ThreadMode.MAIN_ORDERD
4. ThreadMode.BACKGROUND
5. ThreadMode.ASYNC
