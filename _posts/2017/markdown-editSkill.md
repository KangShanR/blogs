# markdown笔记 #

> 浏览公司md文档时看到的些之前没有学到的markdown编辑技巧；
> 按时间记录；

<!-- TOC depthFrom:1 depthTo:6 withLinks:1 updateOnSave:1 orderedList:1 -->

1. [markdown笔记 #](#markdown笔记-)
	1. [各个标记的 md 的格式](#各个标记的-md-的格式)
	2. [Atom 编辑 markdown 的技巧](#atom-编辑-markdown-的技巧)
		1. [新鲜命令](#新鲜命令)
		2. [不能使用热键的命令](#不能使用热键的命令)

<!-- /TOC -->

## 各个标记的 md 的格式

- 插入本地**相对路径**的图片：
    - ![加载不出来时的alt文字说明](/img/ex-gf.png)
    - ![testImg](http://github.com/KangShanR/blogs/blob/master/pictures/baahuballi/17655319.png?raw=true)
    - **Answer:插入本地图片**，再生成html文件一样是引用同样的url，所以用远程的url或者本地的相对url路径可以加载出图片。如果用本地的相对url，就得将本地的图片一起部署到资源库中。
        - 但使用hexo生成html时，会将不同的日期的博客生生成一个文件夹，文件夹中再放入一个index.html文件，访问时按路径来访问这个index.html。所以，在写博客markdown时，就将图片路径全写成项目的绝对路径；
- 插入表格：
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

- **注意**：第三行中第二列加入了属性`colspan="3"`，此属性值决定了这一列要占用三列；
- 这样的表格就比之前使用的方式可订制性更强；
- 插入**勾选项**
    - [ ]选项一
    - [ ]选项二
    - [ ]选项三
- 插入链接的格式：[显示字符](url地址)

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
