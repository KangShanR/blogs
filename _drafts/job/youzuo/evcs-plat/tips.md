---
layout: "post"
title: "tips"
date: "2019-09-19 15:57"
---

- mybatis 替换表达式
    - \n.+\/.+\n.+\n.*\n.+\n.+\n.+\n.+\/$
- 服务器日志目录
    - /smapp/servers/snxia-api-app/logs/

- ev_billing_record request
    - couponId
        - == NULL
            - be paid without coupon
            - unpaid need optimal coupon
        - != NULL
            - == -1 unpaid donot need coupon
            - != -1 unpaid need the coupon data
- sofa host
    - 10.28.18.105:2181,10.28.18.108:2181,10.28.18.82:2181

1. 神能侠监控平台对接（1月7日-1月20日）10天
   1. cat 监控增加钉钉机器人通知
   2. misc 服务对接 cat 钉钉消息通知功能
   3. misc 服务对接 cat 邮件通知功能
   4. misc 服务对接 cat 短信通知
2. 神能侠虚拟货币体系 (1月4日-1月28日) 19天
   1. 增加能量过期定时任务
   2. 老数据能量生产
3. 神能侠电商二期 (1月15日-1月29日) 11天

