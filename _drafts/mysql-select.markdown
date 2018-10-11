---
layout: "post"
title: "mysql_select"
date: "2018-10-11 21:21"
---



# 排序查找最近的记录


查找最近 10 个 up_Time 的计价规则
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

可以找出最之一次 up_time 的
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
