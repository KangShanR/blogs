---
categories: programming
date: "2021-1-22 10:42"
tag: Pattern, String
---

# Pattern

## split

- split(CharSequence cs, int limit) 使用正则表达式分割字串,limit 用以指定数量，当为非正数时不限制数量，但为　0   时会丢弃最后面的空串 `""`;
- split(cs) = split(cs, 0)
