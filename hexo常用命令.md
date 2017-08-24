---
title: hexo常用命令
tags:
---
在学习使用hexo过程中的笔记，大部分来自于[hexo官方网站](https://hexo.io/docs/ "hexo Docs")的文档介绍；
- hexo generate
	- 根据已提供的md文件资料生成静态网页，放在public文件夹中；
	- hexo generate --watch
		- 对比查看文件的改动，只有当文件改动被审查过才会执行generate命令生成静态网页；
	- hexo generate --deploy与hexo deploy --generate
		- 两者效果一样都在generate后执行布置命令deploy
- *使用过程中的小问题：*
	- *每次部署前generate一次就把public中的所有文件先覆盖了，而这样再deploy时就把github远程上资料全部覆盖，这样在github上添加readme都会被覆盖，也就导致了远程机上没有自定义的东西全是生成好的，而且每次一部署都会将先前的重新生成一次再覆盖，感觉造成了许多资源的浪费也很自定义自己想要的网页效果，问题就在于可不可以每次只生成新的资源，再只将新的资源push到master branch上，或者push到其它的branch上，再merge到一起，只要生成新的资源时不会与老资源冲突，也就能达到效果。*
	- ![]()