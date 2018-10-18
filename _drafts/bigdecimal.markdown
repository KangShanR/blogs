---
layout: "post"
title: "Bigdecimal"
date: "2018-10-18 19:52"
---

# Bigdecimal

>  在使用中，从数据库取出来的 Bigdecimal 数据 其 stringCache 为四位小数 比如： 0 就会被记为 0.0000 ，这个时候如果使用 Bigdecimal.ZERO 与 取出来的数据 `0` 进行 equals 方法比较，返回结果就是 false 。如果要实现值的正常比较，就应该使用 Bigdecimal.compare() 方法 根据其返回 int 数据来进行判断比较。
