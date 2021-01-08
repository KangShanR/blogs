---
tag: [mysql, InnoDB, index]
categories: programming
description: The On-disk structures of Index in the InnoDB
date: "2021-1-7 10:4:00"
---

# InnoDB Indexes On-disk structures

## Clustered and Secondary Indexes

> [reference](https://dev.mysql.com/doc/refman/5.7/en/innodb-index-types.html)
>
> InnoDB 中每张都有聚簇索引用以存储行数据。一般来讲，聚簇索引等于主键。为获取更好的查询修改性能， 需要了解使用聚簇索引优化每张表的常用查询与 DML 操作。

- 当为表定义一个主键 PRIMARY KEY，InnoDB 会使用主键作为聚簇索引。为创建的每张表定义主键，如果没有逻辑上唯一并非空的列（或多个列），创建一个自增列，其值是自动填充的。
- 如果没有定义主键，Mysql会设置第一个唯一索引列（其key非空），InnoDB 以此列为聚簇索引。
- 如果表未设置主键也没有合适的唯一索引，InnoDB 将在内部生成一个名为 GEN_CLUST_INDEX 的聚簇索引，此索引在一个包含原生 ID 值的组合列上。表中各列使用这个 ID 值排序，这个原生 ID 占 6-byte，新行插入时自增，因此这些行都按物理插入顺序排列。

### How to Clustered Index Speed Up Queries

因为聚簇索引查询直接访问所有源数据页，所以通过聚簇索引访问行更快速。如果表够大，相对于将原数据与索引记录分在不同页存储的存储结构，聚簇索引结构会节省磁盘 I/O 操作。

### How Secondary Indexes Relate to the Clustered Index

除聚簇索引外的索引都叫二级索引。在 InnoDB 中，一个二级索引包含行的主键，类似二级索引的列。InnoDB 使用主键值去查询聚簇索引中的行。

如果主键过长，二级索引将占用更多空间，所以尽量使用短的主键。

## The Physical Structure of an InnoDB Index


