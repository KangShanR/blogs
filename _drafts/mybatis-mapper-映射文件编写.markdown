---
layout: "post"
title: "Mybatis Mapper 映射文件编写"
date: "2018-09-26 17:03"
---

## Qustions

- 当多个表之间关联查询时，使用查询时 id 会出现多个表之间的冗余，这个时间如果子对象只都取父表的 id ，那么会出现一个问题：当子对象为空时，这时取出的对象会产生这样一个只有父表 id 数据的对象，而实际上这个对象是不应该出现的。

## 待解决问题

- [x] 在写 mapper `<if test="id != null">` 的条件语句时，加上 and 条件（ `<if id != null AND id > 0`）时启动时就会报错
  - 将 `AND` 改成小写 `and` 即可
- [x] 使用分页数据 `limit n,m` 同时使用 `ORDER BY columnName DESC/ASC` 需要先排序再进行分页
- [x] baseResultMap 继承有两层时，中间层的字段不能被继承，只 WithBlobs 层的 加密字段不能被读取到，这个时候不得不写一次加密的字段映射栏
  - [x] 解决办法：继承多层并没有错误，错误在于，继承时没有依次继承， 第三层（最外层） 的 ResulMap 应该继承 DOMapper 的 ResulMapWithBlobs 这个 ResulMap ，而不是 BaseResultMap
  - 当继承发生，接收结果的对象也继承了 自动生成的 DO 对象时，如果需要对其中的字段进行重写（比如：坐标这种字段，自动生成的对象是个 Object ,明显不能用来接收 mysql 的 point 数据，所以就得重写一个坐标对象 Coord 放在继承的对象外面，这个时候我们一般就写成相同的字段名）。发生这种情况，就会担心写的查询语句映射到对象时会发生错误，经过 test 与思考，没有发生错误，因为在写 <resultMap> 时，就使用 `javaType` 指定了对象。
  - 这个问题的根源在于：没有弄懂 Mapper 中各个 <ResultMap> 继承是可以随意继承的，而继承的格式按照 Mapper 的写法就行。而在每个 Mapper 中引入的 mgm 生成的 baseResultMap 只是为了少写 其路径名，也就是说可以不写这个 baseResultMap 需要继承时直接在需要的 ResultMap 中直接 extends="" 。而这时的继承就写继承的全路径就是，而不再是继承本 Mapper 中的（只需要写一个 id 就行）
  - 总算搞懂了这个 ResulMap 映射关系了！
- [ ] 写 查询 语句时，如果把 resultType 写成了 `ResultMap` 将会引起难以查找的错误：
  - 这个时候启动服务时调任何接口都会抛出异常，但不能定位到这个 mapper 来，只会说：不能解析到
`resultMap can't be java.lang.Integer` 。
    - 实际上，我们的查询方法中，根据整个 mapper 定义：resultMap 是将映射到 已经写的 resultMap 中的（诸如：BaseResultMap 。甚至我在想，这个一样可以写个全路径的 resultMap ,直接利用外面已经写好的)
  - `resultType` 是指将结果直接映射成一个 java 类型，比如 使用 COUNT() 函数查询出一个数量来，这个时候会返回一个 Integer 就好，所以， `resultType="java.lang.Integer"`
