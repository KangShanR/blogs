---
title: markdownPad使用攻略
date: 2015-04-29 12:54:38
tags: tips
categories: 
description: 
---

>说明：这是一篇在使用markdown记录笔记过程中自己总结一些使用小技巧；

<!--more-->

首先来一个：

#### 官方的使用说明 ####

- 包括了常用的快捷键使用：
- Welcome to GitHub Pages

You can use the [editor on GitHub](https://github.com/KangShanR/Study-Notes/edit/master/index.md) to maintain and preview the content for your website in Markdown files.

Whenever you commit to this repository, GitHub Pages will run [Jekyll](https://jekyllrb.com/) to rebuild the pages in your site, from the content in your Markdown files.

### Markdown

Markdown is a lightweight and easy-to-use syntax for styling your writing. It includes conventions for

```markdown
Syntax highlighted code block

# Header 1
## Header 2
### Header 3

- Bulleted
- List

1. Numbered
2. List

**Bold** and _Italic_ and `Code` text

[Link](url) and ![Image](img src)
```

**Shortcut:**
- **Bold** (`Ctrl+B`) and *Italic* (`Ctrl+I`)
- Quotes (`Ctrl+Q`)
- Code blocks (`Ctrl+K`)
- Headings 1, 2, 3 (`Ctrl+1`, `Ctrl+2`, `Ctrl+3`)
- Lists (`Ctrl+U` and `Ctrl+Shift+O`)
- Timestamp(`Ctrl+T`)
- Horizontal Rule(`Ctrl+R`)


For more details see [GitHub Flavored Markdown](https://guides.github.com/features/mastering-markdown/).

### Jekyll Themes

Your Pages site will use the layout and styles from the Jekyll theme you have selected in your [repository settings](https://github.com/KangShanR/Study-Notes/settings). The name of this theme is saved in the Jekyll `_config.yml` configuration file.

### Support or Contact

Having trouble with Pages? Check out our [documentation](https://help.github.com/categories/github-pages-basics/) or [contact support](https://github.com/contact) and we’ll help you sort it out.

### 新得到的小点子 ###

诸多功能：[为知笔记中提供的一些功能](http://www.wiz.cn/feature-markdown.html "诸多功能在后面")，其中包括了表格的实现等没怎么用到但很实现的功能；
- 其他在使用过程中摸索出来的小点有：
	1. 段落：一级段落使用一个“>”符号，以此类推，二级就用">>"，三级就用“>>>”，可以有n多级；得注意的是，这种格式经过hexo生成后在网站上显示为水平居中，字体放大一号，同时左边并没有段落的竖线；
	2. 插入链接：`[百度](http://www.baidu.com "百度一下")`示范一下：[百度](http://www.baidu.com "百度一下")，可以看到`[]`中放的是显示的字符串，`()`里放的是url与提示；注意，各个标识符都是小写状态，快捷键ctrl+l；
	3. 想要插入代码:
		1. 在< pre >标签中插入代码:<pre>这儿就是插入pre标签后的效果</pre>
		2. 在每一行代码前插入三个table符，记住是每一行代码都要在前面加上三个table符；
		3. 快捷键`ctrl+k`,这种方式适合短小的单行代码，因为一旦换行，就打破了代码块；

> 浏览公司md文档时看到的些之前没有学到的markdown编辑技巧；

## 各个标记的 md 的格式

- 插入本地**相对路径**的图片：
    - ![加载不出来时的alt文字说明](/img/ex-gf.png)
    - ![testImg](http://github.com/KangShanR/blogs/blob/master/pictures/baahuballi/17655319.png?raw=true)
    - **Answer:插入本地图片**，再生成html文件一样是引用同样的url，所以用远程的url或者本地的相对url路径可以加载出图片。如果用本地的相对url，就得将本地的图片一起部署到资源库中。
        - 但使用hexo生成html时，会将不同的日期的博客生生成一个文件夹，文件夹中再放入一个index.html文件，访问时按路径来访问这个index.html。所以，在写博客markdown时，就将图片路径全写成项目的绝对路径；
- 插入表格，
	- 方式一：
    <table>
      <tr>
        <th></th>
        <th>单程</th>
        <th>往返</th>
        <th>套班</th>
      </tr>
      <tr>
        <td>市内</td>
        <td>100元/天</td>
        <td>200元/天</td>
        <td>50元/趟</td>
      </tr>
      <tr>
        <td>省际</td>
        <td colspan="3">200元/天</td>
      </tr>
    </table>
	- 方式二：
	- 单程|返程|套班
--|---|--
  100元/套|200元/套|50元/套


- **注意**：第三行中第二列加入了属性`colspan="3"`，此属性值决定了这一列要占用三列；
- 这样的表格就比之前使用的方式可订制性更强；
- 插入**勾选项**
    - [ ]选项一
    - [ ]选项二
    - [ ]选项三
- 插入链接的格式：[显示字符](url地址)

![ef](http://kangshan.oschina.io/img/ex-gf.png)

## Atom 编辑 markdown 的技巧
> 在使用 Atom 来编写 markdown 时，安装相关的 package(markdown-writer)，更改其`keymap`，实现快捷操作；
> Atom 中使用的 font 与 markdownPad2 设置一样：`Commic Sans MS`，默认的为`Default`

### 新鲜命令
> 之前使用其他文本编辑器（诸如：markdownpad2、wiznote）没有使用到的技巧

**新鲜命令**

| 命令 | 热键     |作用|
| :------------- | :------------- | :------------- |
| join-line|ctrl-j|将后一行尾追加到选中行中|
| delete-line  | ctrl-d  | 将光标所在行整行删除  |
| duplicate-line|ctrl-shift-d|将光标所在行或选中行复制粘贴在下行|
| move-line-up | alt-up arrow  |将选中行或光标所在行向上行移动|
| move-line-down | alt-down arrow  |将选中行或光标所在行向下行移动|
| **multi-line-select** | ctrl-alt-up/down arrow  |选择定位多行同一行进行编辑|

### 不常使用热键的命令
> 可能会用到但是不常使用热键的命令

**命令列表：**

- horizontal rule：使用三个或三个以上的连字符号即可实现`---`
- `markdown-writer: correct order list numbers`：此命令可以将所有有误的列表编号校正
- `markdown-writer: insert footnote`，插入脚注说明[^label2]
- `markdown-writer: insert footnote``，插入脚[^003]
- ~~This is a examle of strikethrough~~，shortcut：ctrl-h

[^004]: 这是一个插入脚注的示例，标签为 003

[^label2]: crazy footnote: R U OK ?

[^001]: crazy footnote!
