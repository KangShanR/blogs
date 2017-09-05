---
title: hexo+github=your website
date: 2017-01-23 02:04:38
categories: programming
tags: github
description: 使用hexo与github搭建个人网站中的小点子
---
在学习使用hexo过程中的笔记，大部分来自于[hexo官方网站](https://hexo.io/docs/ "hexo Docs")的文档介绍；
还有搭建网站过程中，参考了不少其他站主的文章：[小茗的搭建网站博客](http://www.cnblogs.com/liuxianan/p/build-blog-website-by-hexo-github.html "小茗")
[使用blueLake的教程](http://chaoo.oschina.io/2016/12/29/BlueLake%E5%8D%9A%E5%AE%A2%E4%B8%BB%E9%A2%98%E7%9A%84%E8%AF%A6%E7%BB%86%E9%85%8D%E7%BD%AE.html "BlueLake使用教程")
[生成新浪微博秀；](http://app.weibo.com/tool/weiboshow "新浪微博秀")
- hexo generate
	- 根据已提供的md文件资料生成静态网页，放在public文件夹中；
	- hexo generate --watch
		- 对比查看文件的改动，只有当文件改动被审查过才会执行generate命令生成静态网页；
	- hexo generate --deploy与hexo deploy --generate
		- 两者效果一样都在generate后执行布置命令deploy
- 新建md文档：`hexo new [dir] filename`
	- 使用这个命令生成md文档时，当指定其生成的目录为`_draft`时，<!--more-->hexo会自动将文档的头加上：
		- <pre>---
			title: hexo+github=your website
			date: 2017-01-23 02:04:38
		---</pre>
	- 而当指定目录为`_post`时，其效果会加上标签：
		- <pre>---
			title: hexo+github=your website
			date: 2017-01-23 02:04:38
			tags: 
			---
		</pre>
	- 但是全部的信息包括：
		- <pre>---
			title: hexo+github=your website
			date: 2017-01-23 02:04:38
			categories: 默认分类 #分类，这一点用到的地方不少，*善用分类与标签*
			tags: [tag1,tag2,tag3]#文章标签，可空；使用多标签时，将`[]`符号带上，并且标签之间用`,`分开
			description: 附加一段文章摘要，字数最好在140字以内，会出现在meta的description里面
			---
		</pre>
	- Hexo默认的文件头只有title、date、tags属性，生成的html会缺少Meta信息，不利于搜索引擎收录。建议自行在文件头中添加keywords和description属性。categories属性可自行选择是否添加。标准包含全部meta信息的头应该是：
			
			---
			title: ##文章标题
			date: ##时间，格式为 YYYY-MM-DD HH:mm:ss
			categories: ##分类
			tags: ##标签，多标签格式为 [tag1,tag2,...]
			keywords: ##文章关键词，多关键词格式为 keyword1,keywords2,...
			description: ##文章描述
			---
			正文


- 生成md文档还有另外一个命令：`hexo new page newpage`
	- 与上一个命令不同之处在于：生成的md文件放在了根目录而不会放在source/_post中，而会在source文件夹中生在一个newpage的folder，并在其中生一个index.md，且这个md文档的title会被设置成newpage，也就是生成了这样一个页面，而这个页面并未在网页目录中，但我们就可以好好地设置这个页面用于在其他的文档引用；
- 实现readmore:在任何你想要预览到此的位置加上标签`<!--more-->`，即可实现；
- *使用过程中摸索的小问题：*
	- *每次部署前generate一次就把public中的所有文件先覆盖了，而这样再deploy时就把github远程上资料全部覆盖，这样在github上添加readme都会被覆盖，也就导致了远程机上没有自定义的东西全是生成好的，而且每次一部署都会将先前的重新生成一次再覆盖，感觉造成了许多资源的浪费也很自定义自己想要的网页效果，问题就在于可不可以每次只生成新的资源，再只将新的资源push到master branch上，或者push到其它的branch上，再merge到一起，只要生成新的资源时不会与老资源冲突，也就能达到效果。*
		- 在landscape主题中，据观察也只变化了首页index.html与相应的日期的folder,其它的folder与file都没有改变，所以上述的方法是可行的；
		- 但是，这样操作还有一个问题需要注意，新生成的网页的目录结构都还在，这就需要把新生成的目录结构给拷到之前已存在的目录中，并在这个添加了新网页的目录中push；
		- 实验失败：8/25/2017 7:52:15 AM 
			- 将新的几个md文件放在source文件夹下面的_post中生成新的页面并添加在github的仓库中push上去，网站上并没部署这几个新的页面（仓库中已经有这几个新的页面的html了）；
			- 所以，还是用最暴力的方法更新，每次重新生成整个仓库的资源，将整个仓库资源deploy到github上；
	- 调整整个背景颜色与图片的信息：
		- 进入到相关的主题folder中，再依次进入`source-->css-->style.styl`，打开这个文件，找到`body`在这个级别下就有各种配置参数可以调整，包括了`background: color_value url(图片路径)`
	- 每新建一个hexo目录（也就是说当你在其它托管网站上新建一个项目时）在这个hexo目录中依然要执行上面安装所有的插件到hexo中，比如安部署器deployer：`npm install hexo-deployer-git --save`