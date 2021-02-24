---
title: SELECT in Mysql
layout: post
tag: [mysql, InnoDB]
categories: [Mysql]
description: SELECT statements in Mysql
date: "2021-1-15 14:53:00"
---

> Mysql 中查询语句

## .1. Join Clause

[reference](https://dev.mysql.com/doc/refman/5.7/en/join.html)<!--more-->

- 在 MYSQL 中，`JOIN`, `CROSS JOIN` 与 `INNER JOIN` 在语法是等同的，彼此之间可以相互替换。但在标准 sql 中它们有所区别，inner join 使用 on 子句，而 cross join 使用其他的。<!--more-->
- 表引用 table_reference 可使用别名：`tbl_name as alias_name` 或 `tbl_name alias_name`。
- table_subquery 也叫派生表或子查询，这样的一个子查询必须有一个结果集表名：`SELECT * FROM (SELECT 1, 2, 3) AS t1;`
- 单次 join 最多引用 61 张表，这其中包括派生表和外部查询块的 FROM 子句视图。
- `INNER JOIN` 与 `,` 在语义上等价于没有 join 条件，都会在指定表间结果产生笛卡尔积结果。（Cartesian product 每张表的数据互相匹配）。但同时也可以在其后加上 on join条件。
    - 但 `,` 的优先级低于其他 INNER JOIN,CROSS JOIN,LEFT JOIN 等。如果与这些操作符混用，an error of the form Unknown column 'col_name' in 'on clause' may occur。
- 在 ON 的查询条件 search_condition 可以是 where 子句中可以使用的任何条件表达式。但一般来讲，ON 子句用来指定怎么 join ，而 WHERE 用来指定哪些行进入到结果集中。
- 如果使用了 LEFT JOIN ON／USING 且右表没有数据行可匹配，则右表字段都会是 NULL.可以使用这种形式语句查询表中在另外一张表中没有匹配的数据。

    ```sql
    SELECT left_tbl.* FROM left_tbl LEFT JOIN right_tbl ON left_tbl.id = right_tbl.id WHERE right_tbl.id IS NULL;
    ```

- USING 语法：在 LEFT JOIN 语句中，与 `ON` 作用类似，但 USING 不用指定 l.id = r.id，是直接要求两个表字段名一致而直接指定多表共有的字段值一致就 JOIN 成功。eg：`SELECT * FROM left_table LEFT JOIN right_table USING(id);` = `SELECT * FROM left_table LEFT JOIN right_table ON left_table.id = right_table.id;`
- `USING(join_column_list)` 子句将多个列放入参数中，这样要求多表都有这些列名存在。
- `NATURAL [LEFT] JOIN` 等同于使用 `INNER JOIN` 或 `LEFT JOIN` 并使用 `USING` 子句指定两表同名的列在其中。
- `RIGHT JOIN` 与 `left join` 工作原理类似，但为保持代码跨库端口化，推荐使用 `left join`。
- [PARTITION](https://dev.mysql.com/doc/refman/5.7/en/partitioning-selection.html)在写DDL时将表分片，指定范围内为一片，查询时可以直接查询片内数据。（不知道这样是否可以将索引单片内查询而提高查询效率？）
