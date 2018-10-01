---
layout: "post"
title: "mysql-point"
date: "2018-10-01 18:50"
---

# mysql 中 point 数据的使用

插入语句:
```
insert into
ev_site(`position`,`status`,`site_name`)
value
(
	POINTFROMTEXT('POINT(104.067923463 30.6799428454)'),
	"NORMAL",'KKD'
);
```
