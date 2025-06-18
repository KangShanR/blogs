### 关于vim编辑器选择文本的操作
#### 实现 vim 使用主机剪切板
1. 需要安装 vim-gtk3
2. 在 .vimrc 中配置剪切板 `set clipboard=unnamedplus`

#### 设置行号
set number         " 显示绝对行号
set relativenumber " 当前行是实际行号，其他行是相对行号，方便跳转

#### 文本选择
- 在命令模式中， `{num}yy`，复制多行。`{num}dd`剪切
- `v` 连续选择
- `V` 整行选择
- `ctrl v` 块选择

