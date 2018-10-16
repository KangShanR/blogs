---
layout: "post"
title: "mysql"
date: "2018-10-16 15:30"
---

# mysql 语句

```
# 场站桩 枪 查询
SELECT
	s.id AS site_id,s.branch_company_id, s.`status`,s.site_name,
	ss.id AS station_id, st.electricity_type as station_elec_type,
	h.id as headId, h.elec_type as head_ElecType, h.`status` AS head_status
FROM ev_site as s
LEFT JOIN ev_station AS ss ON ss.site_id = s.id
LEFT JOIN ev_station_type as st on ss.station_type_id = st.id
LEFT JOIN ev_charger_head AS h ON h.station_id = ss.id
WHERE
	s.id = 24

# 修改场站坐标
UPDATE
ev_site as s
set s.position = (PointFromText('POINT(104.098632 30.743442)'))
WHERE
s.id = 23


# 更改场站的分公司 id
UPDATE
ev_site AS s
SET s.branch_company_id = 11
WHERE
s.branch_company_id = 1


#查询分公司下所有场站
SELECT

b.id AS company_id, s.id AS site_id, s.site_name, b.region_id
FROM
ev_branch_company AS b
LEFT JOIN ev_site AS s ON s.branch_company_id = b.id
WHERE
b.id = 1



# 计价规则查询
SELECT
 *
from ev_price_rule as p
WHERE
	p.site_id = 2
	and p.rule_status = "NORMAL"




SELECT
	*
FROM
	ev_price_rule AS p
WHERE
	p.`up_time` =
		(
			SELECT
				DISTINCT(up_time)
			FROM
				ev_price_rule AS r
			WHERE
				r.site_id = 2
				AND r.rule_status='NORMAL'
				AND r.fee_type = "ELEC"
				AND NOW() >= r.up_time
			ORDER BY
				r.up_time DESC
			LIMIT 1
		)
	AND p.fee_type = "ELEC"
	AND p.site_id = 2
	AND p.rule_status='NORMAL'




# 添加
INSERT INTO
ev_price_rule
	(site_id, fee_type, time_type,start_time, end_time, price, up_time, rule_status)
VALUES
	(24,"ELEC","LOW","00:00:00","23:59:59", 1.33, "2018-10-10 11:11:11", "NORMAL")




# 分公司
SELECT
*
FROM ev_branch_company AS b

```
