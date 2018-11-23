---
layout: "post"
title: "mysql_select"
date: "2018-10-11 21:21"
---

# mysql 查询函数使用

## 先 ORDER BY 后再 LIMIT 1 查找出最近或最大的数据


> 排序查找最近的记录 :: 查找最近 10 个 up_Time 的计价规则

```
SELECT *
	FROM ev_price_rule as r

	WHERE NOW() >= r.up_Time
		and r.site_id =9
		#and r.rule_status != 'DELETE'
		and r.fee_type = 'SERVICE'
	ORDER BY r.up_time DESC
        LIMIT 10
```

> 可以找出最之一次 up_time 的


```
SELECT *
	FROM
		ev_price_rule
	where
		ev_price_rule.`up_time` =
			(SELECT *
				FROM
					(
						SELECT
							DISTINCT(up_time) uTime
						FROM
							ev_price_rule
						WHERE
							ev_price_rule.`site_id` = 9
						ORDER BY
							up_time DESC
					) AS temp
					WHERE NOW() >= temp.uTime
		LIMIT 1)
	AND fee_type = 'SERVICE'

SELECT *
FROM ev_site AS s
WHERE s.site_name LIKE '2%'
```



## DATE_FORMAT(date, pattern) 与 DATE_SUB(date, interval unit) DATE_ADD(date, interval unit) 的使用

> 场站 7 日内成功次数

```
 SELECT
	  DATE_FORMAT(create_time,'%Y-%m-%d') as date,
	  COUNT(1) as num
	FROM
	  ev_billing_record
	WHERE create_time
		BETWEEN DATE_FORMAT(DATE_SUB(NOW(),INTERVAL 7 DAY),'%Y-%m-%d') AND DATE_FORMAT(DATE_ADD(NOW(),INTERVAL 1 DAY),'%Y-%m-%d')
	  AND site_id = 33
		AND STATUS = 'FINISH'
	GROUP BY date
```
