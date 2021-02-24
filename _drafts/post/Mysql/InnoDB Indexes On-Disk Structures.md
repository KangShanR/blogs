---
title: InnoDB Indexes On-Disk Structures
layout: post
tag: [mysql, InnoDB, index]
categories: Mysql
description: The On-disk structures of Index in the InnoDB
date: "2021-1-7 10:4:00"
---

# InnoDB Indexes On-Disk Structures

## Clustered and Secondary Indexes

> [reference](https://dev.mysql.com/doc/refman/5.7/en/innodb-index-types.html)
>
> InnoDB 中每张都有聚簇索引用以存储行数据。一般来讲，聚簇索引等于主键。为获取更好的查询修改性能， 需要了解使用聚簇索引优化每张表的常用查询与 DML 操作。<!--more-->

- 当为表定义一个主键 PRIMARY KEY，InnoDB 会使用主键作为聚簇索引。为创建的每张表定义主键，如果没有逻辑上唯一并非空的列（或多个列），创建一个自增列，其值是自动填充的。
- 如果没有定义主键，Mysql会设置第一个唯一索引列（其key非空），InnoDB 以此列为聚簇索引。
- 如果表未设置主键也没有合适的唯一索引，InnoDB 将在内部生成一个名为 GEN_CLUST_INDEX 的聚簇索引，此索引在一个包含原生 ID 值的组合列上。表中各列使用这个 ID 值排序，这个原生 ID 占 6-byte，新行插入时自增，因此这些行都按物理插入顺序排列。

### How to Clustered Index Speed Up Queries

因为聚簇索引查询直接访问所有源数据页，所以通过聚簇索引访问行更快速。如果表够大，相对于将原数据与索引记录分在不同页存储的存储结构，聚簇索引结构会节省磁盘 I/O 操作。

### How Secondary Indexes Relate to the Clustered Index

除聚簇索引外的索引都叫二级索引。在 InnoDB 中，一个二级索引包含行的主键，类似二级索引的列。InnoDB 使用主键值去查询聚簇索引中的行。

如果主键过长，二级索引将占用更多空间，所以尽量使用短的主键。

## The Physical Structure of an InnoDB Index

> [reference](https://dev.mysql.com/doc/refman/5.7/en/innodb-physical-structure.html)

> 除空间索引使用 R-tree 数据结构索引多维度数据外，InnoDB 都使用 B-tree 数据结构。两种类型索引记录都存储在树的叶子页，叶子页默认大小是 16KB 。

- 当 InnoDB 插入新记录到聚簇索引中时， InnoDB 会尝试为后来的插入或更新保留 1/16 的页空间。如果 InnoDB 插入新记录是顺序的，索引记录页是到 15/16 就当作已满，如果新记录是无序插入的，索引记录面到 1/2~15/16 就满。
- InnoDB 在创建与重建 B-tree 索引执行批量加载。这是一种创建顺序索引的方法。顺序索引创建每个 B-tree 页的空间占比的配置项是 `innodb_fill_factor` （设置为 100 时，聚簇索引页保留 1/16 的剩余空间），剩下的空间留给索引增长。顺序索引创建不支持空间索引的创建。
- 如果 InnoDB 的空间占用率低于了 `MERGE_THRESHOLD` (可以设置，也应用于空间索引，默认 50%),InnoDB 会尝试重构 B-tree 索引以释放这个页。
- InnoDB 的索引页大小可以通过配置项 `innodb_page_size`　设置，配置优先于 MYSQL 实例的初始化，一旦设置需要重新初始化实例才能修改。配置支持的值包括：64KB,32KB,16KB(default),8KB,4KB。
- 一个 MYSQL 实例使用了一个特定的 page_size 后，不能使用另一个使用不同 page_size 的实例的数据文件与日志文件。（数据文件与日志文件也就是说可以共用）

## Sorted Index Builds

> [reference](https://dev.mysql.com/doc/refman/5.7/en/sorted-index-builds.html)

> InnoDB 在创建或重建索引时用批量加载而不是一次只插入一条索引记录。这种方法也叫顺序索引创建，顺序索引创建不支持空间索引。

- 索引的创建有三个阶段，第一阶段，扫描聚簇索引，一个索引项生成后被加入到排序的缓存中，当序列缓存变满时，索引项被排序并写入到一个临时文件中。这个过程也叫做“run”。第二阶段，一个或多个“run”写到临时文件时，临时文件中的所有索引项执行合并排序。第三阶段，排好序的索引项插入到 B-tree。
- 在引进顺序索引（sorted index）构建之前，InnoDB 使用 insert API 一次只插入一个索引记录。这种方法是打开一个 B-tree 索引指针找到插入位置，然后用积极策略（optimistic）插入索引项到 B-tree 页。如果因为页已满而插入失败，将会执行消极插入策略（pessimistic），消极策略将打开一个 B-tree 指针，为找到索引项的插入空间分离合并 B-tree 节点。这种从上至下（top-down）的构建索引方式的缺点是查找一个插入位置的成本且需要固定的分离与合并节点。
- 顺序索引构建使用自下到上（bottom-up）的方式构建索引。这种方式中， B-tree 的每一级都持有一个最右叶子页（right-most leaf page）的引用，最右叶子页在需要的 B-tree 深度都有，索引项插入的顺序由这些叶子页的排列顺序决定。一旦一个叶子页满了，节点指点追加在其父页上，兄弟叶子页安排于后面的插入。这个过程持续到所有项被插入，插入可能上升到根节点。当兄弟页被插入好，前叶子页的引用就释放，并且新的叶子页变成最右叶子页和新的插入位置。
