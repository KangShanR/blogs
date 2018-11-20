---
layout: "post"
title: "git settings"
date: "2018-10-19 11:06"
---


```
游茜:
同学们注意哈， Git参数的设置( git config --global merge.ff no )， 有个坑。

冬:
what？

游茜:
如果设置了这两个参数， 需要同时再设置这下面2个：

git config --global merge.commit no
git config --global pull.ff yes

游茜:
如果不设置 merge.commit 为 no ， 那么每次 pull 代码的话会自动给你commit， 会导致多一次无用的提交记录

游茜:
还需要同时设置 pull.ff  为yes

游茜:
最后你的 ~/.gitconfig  文件应该是这样， git的行为才能正常。。。

```
