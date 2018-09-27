---
layout: "post"
title: "Mybatis Mapper 映射文件编写"
date: "2018-09-26 17:03"
---

## Qustions

- 当多个表之间关联查询时，使用查询时 id 会出现多个表之间的冗余，这个时间如果子对象只都取父表的 id ，那么会出现一个问题：当子对象为空时，这时取出的对象会产生这样一个只有父表 id 数据的对象，而实际上这个对象是不应该出现的。

## 待解决问题

- [x] 在写 mapper `<if test="id != null">` 的条件语句时，加上 and 条件（ `<if id != null AND id > 0`）时启动时就会报错
  - 将 `AND` 改成 小写即可
- [x] 使用分页数据 `limit n,m` 同时使用 `ORDER BY columnName DESC/ASC` 需要先排序再进行分页
