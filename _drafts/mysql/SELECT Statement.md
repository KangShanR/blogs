---
tag: [mysql, InnoDB]
categories: programming
description: SELECT statements in Mysql
date: "2021-1-15 14:53:00"
---
# SELECT

## Join Clause

[reference](https://dev.mysql.com/doc/refman/5.7/en/join.html)

- `INNER JOIN` 与 `,` 在语义上等价于没有 join 条件，都会在指定表间结果产生笛卡尔积结果。（Cartesian product 每张表的数据互相匹配）
    - 但 `,` 的优先级低于其他 INNER JOIN,CROSS JOIN,LEFT JOIN 等。如果与这些操作符混用，an error of the form Unknown column 'col_name' in 'on clause' may occur。
- 在 ON 的查询条件 search_condition 可以是 where 子句中可以使用的任何条件表达式。但一般来讲，ON 子句用来指定怎么 join ，而 WHERE 用来指定哪些行进入到结果集中。
- 如果使用了 LEFT JOIN ON／USING 且右表没有数据行可匹配，则右表字段都会是 NULL.可以使用这种形式语句查询表中在另外一张表中没有匹配的数据。

    ```sql
    SELECT left_tbl.* FROM left_tbl LEFT JOIN right_tbl ON left_tbl.id = right_tbl.id WHERE right_tbl.id IS NULL;
    ```

- USING 语法：在 LEFT JOIN 语句中，与 `ON` 作用类似，但 USING 不用指定 l.id = r.id，是直接要求两个表字段名一致而直接指定多表共有的字段值一致就 JOIN 成功。eg：`SELECT * FROM left_table LEFT JOIN right_table USING(id);` = `SELECT * FROM left_table LEFT JOIN right_table ON left_table.id = right_table.id;`

