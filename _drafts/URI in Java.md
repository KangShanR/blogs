---
date: 2020-08-13 11:14:00
tags: [framework,java,URI]
categories: programming
description: java URI class
---

# URI in Java

URI syntax and components [URI 语法](https://www.ietf.org/rfc/rfc2396.txt)

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

## URL

URL: Uniform Resource Locator.

URL 与 URI 之间相互转换，使用方法： `URL.toURI()` 与 `URI.toURL()` 。

URL 不负责编码与解码，所以其不识别转义后的 URL 与转义前的 RUL。URL 的 HTML 形式的解码可使用 URLEncoder 与 URLDecoder 。

## URI 文档翻译

[original doc](https://www.ietf.org/rfc/rfc2396.txt)

> 文档语法：
> This document uses two conventions to describe and define the syntax for URI.  The first, called the layout form, is a general of the order of components and component separators, as in

>      <first>/<second>;<third>?<fourth>

> The component names are enclosed in angle-brackets and any characters outside angle-brackets are literal separators.  Whitespace should be ignored.  These descriptions are used informally and do not define the syntax requirements.
> The second convention is a BNF-like grammar, used to define the formal URI syntax.  The grammar is that of [RFC822], except that "|" is used to designate alternatives.  Briefly, rules are separated from definitions by an equal "=", indentation is used to continue a rule definition over more than one line, literals are quoted with "", parentheses "(" and ")" are used to group elements, optional elements are enclosed in "[" and "]" brackets, and elements may be preceded with <n>* to designate n or more repetitions of the following element; n defaults to 0.
> Unlike many specifications that use a BNF-like grammar to define the bytes (octets) allowed by a protocol, the URI grammar is defined in terms of characters.  Each literal in the grammar corresponds to the character it represents, rather than to the octet encoding of that character in any particular coded character set.  How a URI is represented in terms of bits and bytes on the wire is dependent upon the character encoding of the protocol used to transport it, or the charset of the document which contains it.

### 保留字符

> reserved    = ";" | "/" | "?" | ":" | "@" | "&" | "=" | "+" |"$" | ","

如果使用了这些保留字符在 URI 中，需要转义。这些保留字符可以出现在 URI 中但不能出现在特定的 URI 组件中。一般来讲，如果 URI 语义改变或保留字符被转义为 US-ASCII 码所替换，字符就将被保留。

### 非保留字符

大小写字母数字与标点符号

> unreserved  = alphanum | mark
> mark        = "-" | "_" | "." | "!" | "~" | "*" | "'" | "(" | ")"

在 URI 中都为非保留字符，非保留字符被转义后仍保留原语义。但最好不要转义除非上下文只接受转义 URI。

### 转义编码

转义后的八进制符是由三个字符组成：`%` 加上两个十六进制字符。eg: `%20` 表示 ASCII 码 32 space。

是否转义取决于转义算法，一般只需要将保留字符转义，而非保留字符可不转义。

### 被排除的 ASCII 字符

- 控制符 US-ASCII coded characters 00-1F, 7F hexadecimal
- 空格 space US-ASCII coded character 20 hexadecimal
- 分隔符 delims "<" | ">" | "#" | "%" | <"> 网关代理用于分隔符 "{" | "}" | "|" | "\" | "^" | "[" | "]" | "`"

## URI 句法部件

URI 语法主要取决于 scheme ，一般来讲，绝对 URI 的写法： `<scheme>:<scheme-specific-part>`。
