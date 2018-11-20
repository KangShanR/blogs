md---
layout: "post"
title: "@lombok"
date: "2018-09-29 11:34"
---

# lambok 的使用

> 使用此包对于创建一个类来说很方便，使用其标签就可以实现各种方法的实现。包括了 getter setter constructor 的各种不同形式地实现。

- [Lombok使用示例详情](https://blog.csdn.net/vbirdbest/article/details/79495398)

## skills

### 各个标签的用途

**@Data**

> 原谅释义：
```
    - Generates getters for all fields, a useful toString method, and hashCode and equals implementations that check
     - all non-transient fields. Will also generate setters for all non-final fields, as well as a constructor.
     - Equivalent to {@code @Getter @Setter @RequiredArgsConstructor @ToString @EqualsAndHashCode}.
 ```
- 此标签让类中的 field 都自动生成相应的 getter 与 setter ，同时，还会重新覆盖生成 Object 相关方法：equals hashCode toString
