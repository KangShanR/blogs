---
layout: "post"
title: "mysql-point"
date: "2018-10-01 18:50"
---

插入语句:

```sql
insert into
ev_site(`position`,`status`,`site_name`)
value
(
    POINTFROMTEXT('POINT(104.067923463 30.6799428454)'),
    "NORMAL",'KKD'
);
```

更新语句：

```sql
UPDATE ev_site
SET
`position`	 = PointFromText('Point(104.067923462 30.6799428455)')
WHERE id in(4,5,6)
```

在 Mapper 中写查询语句，如果需要 进行条件 OR 需要注意：

- OR 会将整个语句进行 OR 一遍，所以注意在该加上 括号 的话一定要加上，不然就会出现其他条件一律不能产生约束。
- 但在 Mapper 中加上 () 时，如果前面的 <if> 语句不成立会将 （） 后的 and 直接暴露出来而让语句产生错误。所以，在 mapper 中如果要实现 or 加上 括号得注意条件我重判断

```xml
<select id="selectSitesByParam" resultMap="ExtendBaseResultMap"
        parameterType="map">

        SELECT
            <include refid="site_column"/>,
            <include refid="site_tag_column"/>
        FROM ev_site AS str
        LEFT JOIN ev_site_tag AS t ON t.`site_id` = str.id
        <where>
            <if test="cityIds != null and cityIds.size &gt; 0">
                AND str.`city_id` IN
                <foreach collection="cityIds" index="index" item="item" open="(" separator="," close=")">
                    #{item}
                </foreach>
            </if>

            <if test="siteName != null and orAddr != null">
                AND (str.`site_name` LIKE CONCAT('%',#{siteName},'%')
                OR str.`addr` LIKE CONCAT('%',#{orAddr},'%'))
            </if>
            <if test="siteName != null and orAddr == null">
                AND str.`site_name` LIKE CONCAT('%',#{siteName},'%')
            </if>
            <if test="andAddr != null">
                AND str.`addr` LIKE CONCAT('%',#{andAddr},'%')
            </if>
            <if test="statusList != null and statusList.size &gt; 0">
                AND str.`status` IN
                <foreach collection="statusList" item="item" index="index" open="(" separator="," close=")">
                    #{item}
                </foreach>
            </if>
        </where>
        ORDER BY str.id DESC
        <if test="startNo != null and endNo != null">
            LIMIT #{startNo},#{endNo}
        </if>
    </select>
```

```sql
SELECT str.id,str.create_time,str.modified_time,str.creater_id,str.branch_company_id,str.leader_id,str.site_name,       str.addr,str.status,str.remark,str.site_type,str.open_start_time,str.open_end_time,str.phone_no_secret, str.city_id,       X(str.`position`) AS longitude,Y(str.`position`) AS latitude      ,                     t.id AS tag_id,t.tag_code               FROM ev_site AS str         LEFT JOIN ev_site_tag AS t ON t.`site_id` = str.id          WHERE  ( str.`site_name` LIKE CONCAT('%',"天",'%')                                           OR str.`addr` LIKE CONCAT('%',"天",'%')  )                                                      AND
str.`status`
IN ("MAINTAINING")          ORDER BY str.id DESC
```

## points

- IFNULL(filed, default_value) 的使用：使用此函数就用来判定当字段值为空时，给一个 默认值 返回
    - ```SELECT IFNULL(SUM(use_kwh),0) from ev_billing_record```

### mysql 字段类型长度

- varchar
    - 长度限制：不管中文还是英文，长度是多少就是多少字数，不能超过这个数
- bit 就是二进制的位
    - 长度 ： 长度多少就是多少个 0|1 ，不能超过
- 当这个字段是 number 类型的时候，长度这个属性不再像 文本类型（varchar) ，需要配合 autofill zero 这个来使用，当指定了长度为 4 ，但这个 number 是 22 ，这时如果同时指定了 autofill zero ，就会填入数字 `0022`

## mysql 中的日期时间相关

[参考博客](https://www.cnblogs.com/yhtboke/p/5629152.html)
