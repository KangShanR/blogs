---
layout: "post"
date: "2019-03-22 18:53"
---

# eventbus

> 轻松实现事件的发布订阅

## 三步实现 eventbus

> [reference](http://greenrobot.org/eventbus/documentation/how-to-get-started/)

1. 定义好自己想要的事件；

```java
@Getter
@Setter
@AllArgumentConstructor
public class MyEvent{
    private String myEventMessage;
}
```

2. 定义好订阅者 Eventbus 3 后 在需要订阅的方法上加上 `@Subscribe` 即可实现

```java
@Subscribe(threadMode = ThreadMode.MAIN)
public onEventPosted(MyEvent event){
    log.info(event.getMyEventMessage());
}
```

  1. subscriber 需要注册与注销到 eventbus 中才能收到事件发布

3. 事件发布：

```java
Eventbus.getDefault().post(new MyEvent("eventMessage"));
```

## ThreadMode

> 订阅者线程模式，确定订阅者收到事件的线程模式

1. ThreadMode.POSTING
    1. `this is default ThreadMode`;
    2. `Subscribers will be called in the same thread posting the event.`
    3. 发布线程 相同线程的 订阅者会收到事件(完全避免了线程切换，最小开销)
    4. 事件同步发布，而订阅者会异步调用在事件发布后（不适用于项目 umeng 消息推送）
    5. 此种模式下的事件处理需要即时反馈以避免阻塞事件发布线程
2. ThreadMode.MAIN
    1. 与 POSTING 模式类似
3. ThreadMode.MAIN_ORDERED
4. ThreadMode.BACKGROUND
5. ThreadMode.ASYNC
