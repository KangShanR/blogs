---
date: 2020-08-13 11:14:00
tags: [framework,java,URI]
categories: programming
description: java URI class
---

# URI in Java

URI syntax and components URI 语法

At the highest level a URI reference (hereinafter simply "URI") in string form has the syntax `[scheme:]scheme-specific-part[#fragment]`

## URI 分类

- opaque 模糊类：不以 slash character `/` 开头，不需要进一步解析。eg：
    - mailto:java-net@java.sun.com
    - ews:comp.lang.java
    - urn:isbn:096139210x
- hierarchical 层级类，要么 scheme-specific-part 以 slash character `/` 开头的绝对 URI，要么是不指定 scheme 的相对 URI 。
    - eg：
        - http://java.sun.com/j2se/1.3/
        - docs/guide/collections/designfaq.html#28
        - ../../../demo/jfc/SwingSet2/src/SwingSet2.java
        - file:///~/calendar
    - 层级类的 URI 需要进一步解析，其解析语法是 `[scheme:][//authority][path][?query][#fragment]`
        - 如果指定了层级类 URI 的权限组件部分，either server-based, or registry-based。server-based 权限解析语法：`[user-info@]host[:port]`。几乎目前所有 URI 都是 server-based ，如果使用此语法解析权限组件失败，将认为此权限组件是 registry-based 。
        - path 组件如果以slash character `/` 开头，则表明其是绝对路径，否则是相对的。*The path of a hierarchical URI that is either absolute or specifies an authority is always absolute.*

All told, then, a URI instance has the following nine components:

Describes the components of a URI:scheme,scheme-specific-part,authority,user-info,host,port,path,query,fragment

|Component|Type|
|--|--|
|scheme|String|
|scheme-specific-part|String|
|authority|String|
|user-info|String|
|host|String|
|port|int|
|path|String|
|query|String|
|fragment|String|
