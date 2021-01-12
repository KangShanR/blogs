---
tag: [ssh, port forwarding]
date: 2020-12-09 12:12:00
description: java transient
---

# transient

用于标明在序列化时此变量不被序列化，反序列化时会将其赋予默认值。

- 与 final 连用，final 修辞字段值会直接被序列化，因此 transient 修辞 final 字段无效
- 与 static 连用，static 字段非对象所拥有，所以 transient 修辞的 static 字段无效。
