---
layout: "post"
title: "work-list"
date: "2017-10-25 10:25"
---
# 工作记录
> 记录最近一周的工作要点，方便后期核查与对比




## 2017-10-25
> 今日周三

### 要点
#### Atom 安装packages 出现的问题

- Atom安装插件markdown-preview-plus，可以实现预览本地图片，但目前还没有实现预览窗口与编辑窗口的同步滚动；
- preview-plus等packages可以在packages-management中设置其属性，包括其快捷键的绑定；
- - 插入**勾选项**
    - [ ]选项一
    - [ ]选项二
    - [ ]选项三

- 使用Atom-markdown-writer实现多行编辑：`ctrl+alt+up/down`选择多行，再直接进行编辑；
- [ ]选项一
- [ ]`选项二`
- [ ]选项三![test](/blogs/pictures/graduate/graduate(1).jpg)
  - [ ] 像上一项这种，图片名中包含一个字符`()`，怎么写在路径中呢？
    - [ ] 一样的使用，上面没有出现结果仅仅是因为这张图片的命名中有` `（空格）字符出现。
  - [ ] 按照项目路径写一个图片的路径来如上；
- [ ] 修改Atomr的`shortcut`快捷键：修改`keysmap`CSON文件，将快捷键与操作映射为键值对，修改之后重启Atom才能生效；
- [ ] 当使用`markdown-preview-plus`这个md预览包时可以预览到图片，但不能预览到`勾选项`；而使用 Atom 自带的核心包`markdown-preview`，正好相反，可以预览到`勾选项`不能预览到图片；

## 2017-10-26
> 周四

### 要点
#### markdown-writer 的热键修改
1. 修改`markdown-writer`的快捷键，需要将其命令写成全小写，不然无效。如：`markdown-writer:toggle-blockquote`，通常找命令时看到查找出来的命令的使用的大驼峰，我们习惯写成首字母大写，结果保存无效；
