---
tag: [idea, shortcuts]
date: 2020-11-28 18:41:00
---

# idea

> Jetbrains IEAD cheat sheet
> 使用了 github 账户登录了 Intellij IDEA

## shortcuts ＆ cheat sheet

- F2/shift+F2 直接跳到当前文件下一个／上一个错误
- ctrl+alt+v 抽取代码成变量
- alt+shift+7 定位代码引用，包括了类／方法／参数／语句／字段
- ctrl+space 代码快速补全，按两次可以实例静态方法引用。alt+enter 导入选中的方法。
- ctrl+shift+space smart type completion 自动获取可供补全选择的类或其他。**按两次有奇效。**
    - 在自动补全选中后可以使用 enter 或 tab 来选中，不同之处：**｀tab｀** 可以直接覆盖后面的原有内容。
    - 当指定了类型数据　```String s =(```，直接使用此键可生成表达式？
    - 在　new　后面使用　smart　type　comletion
- ctrl+k commit 新加的代码，unstaged 也会在供选项中
- ctrl+o 快速覆盖其类方法 override；ctrl+l 快速实现基类／接口方法 implements
- 查看类所实现的接口的方法：光标定位在接口上后按 ctrl+shift+f7，也可定位在 throws 语句上再按此键定位到相应抛出异常的语句。
- 多文本片段选择：hold shift+alt and drag your mouse across the text
- ctrl+q 查看 documentation
- **ctrl+p** parameter info
- **ctrl+b** declaration
- 当 code completion 选中时可以通过 **` ` `,` `.` `;`** 四个符号按键接受补全。
- 快速打开一个类／方法／字段 ctrl+shift+alt+n
- ctrl+/ 注释单行代码
- ctrl+shift+/ 注释代码块
- ctrl+shift+alt+left/right 移动方法参数顺序，同样影响方法被调用处的顺序，之后再按 alt+enter 选择 apply signature change。
- ctrl+f 查找文本时再按 *alt+down* 可以唤出最近的搜索历史
- *ctrl+y* 删除整行
- **ctrl+d** 复制所选内容，若没有选择将复制　caret　所在行
- hold shift+alt 点选出多个 caret
- ctrl+e view list of recently opened files
- shift+f1 在外部浏览器打开文档
- ctrl+shift+n 可以通过输入*不同层次目录字符*定位到深层次的文件，字符之间使用 `\` 分隔。
- 验证正则表达式 将 caret 放在表达式上，按 alt+enter 选择 check regexp
- **ctrl+shift+v** 从剪切板中选择最近多个剪切的文本粘贴。
- ctrl+shift+enter 自动补全所有的代码块，包括　try　return　if　while
- **ctrl+f12** 打开文件结构，再使用　enter　或　f４　追溯到相应成员上去。
- **根据驼峰法则输入大写字符**来让idea自动补全代码。
- **ctrl＋shift+f7** 在当前文件中高亮　caret　所在变量使用处，f３／shift＋f３　跳向下／上一个
- **f２／shift＋f２**　跳往下／上一个高亮的语法错误
- **alt+shift+up/down** 在错误信息与result　间跳跃
- ctrl+h 查看当前类层级 hierarche
