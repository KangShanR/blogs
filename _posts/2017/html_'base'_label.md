---
title: html
date: 2015-03-25 15:39:42
tags: html5
categories: programming
description: 
---

> hyper type markup language

<!--more-->

简介：用于规定页面上所有的链接的默认URL和默认目标：
`<base href="http://……“ target="_blank">`
- base标签放在 `<head>` 标签之中;
- 最好把base标签排在head标签中第一个元素，这样其它的元素就可以使用base元素的信息；
- 一个文档中最多能使用一个base元素；
- 如果使用了base标签，至少要具备href属性或者target属性其中一个；
- 其中的元素：
	- href：规定页面中相对链接的基准URL，其值就是一个URL;
	- target:规定页面中所有的超链接和表单在何处打开，但该属性会被每个单独的链接中的target属性所覆盖，其值包括：
		- _blank，打开新的页面
		- _ parent，在父页面打开
		- _self，在当前页面打开
		- _top，在浏览器头页面打开
		- framename

#### base 标签支持的属性

- 支持HTML的全局属性，如：class,id,style等
- 不支持事件属性，如:onClick之类

### 实体引用

> Some characters are reserved in HTML.
If you use the less than (<) or greater than (>) signs in your text, the browser might mix them with tags.
Character entities are used to display reserved characters in HTML.
A character entity looks like this: &entity_name; OR &#entity_number;

html 中常用的实体引用符号：

Result|Description|Entity Name|Entity Number
---|---|---|---
|non-breaking|space|`&nbsp;`|`&#160;`
<|less than|`&lt;`|`&#60;`
>|greater than|`&gt;`|`&#62;`
&|ampersand|`&amp;`|`&#38;`
"|ouble quotation mark|`&quot;`|`&#34;`
'|single quotation mark (apostrophe)|`&apos;`|`&#39;`
¢|cent|`cent;`|`&#162;`
£|pound|`&pound;`|`&#163;`
¥|yen|`&yen;`|`&#165;`
€|euro|`&euro;`|`&#8364;`
©|copyright|`&copy;`|`&#169;`
®|registered|trademark|`&reg;`|`&#174;`