---
layout: "post"
title: "tips"
date: "2019-09-19 15:57"
---

# Tips

- mybatis 替换表达式
  - \n.+\/.+\n.+\n.*\n.+\n.+\n.+\n.+\/$
- 服务器日志目录
  - /smapp/servers/snxia-api-app/logs/2019-09-29



- ev_billing_record request
  - couponId
    - == NULL
      - be paid without coupon
      - unpaid need optimal coupon
    - != NULL
      - == -1 unpaid donot need coupon
      - != -1 unpaid need the coupon data
