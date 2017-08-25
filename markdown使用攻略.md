---
title: markdownPad使用攻略
date: 2015-04-29 12:54:38
tags:
---
>说明：这是一篇在使用markdown记录笔记过程中自己总结一些使用小技巧；

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

[Link](url) and ![Image](src)
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