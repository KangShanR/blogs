# 工作纪要 #
> 点成线成面，将每天的问题集中在一个文档中，处理好未处理好都做好标记。按一个月或一周记录一个文档。

## 记录点 ##

1. 10/13/2017 4:44:59 PM 
    2. 真伪随机码生成
    2. redis cache
    3. Hbase
    4. N/A:not applicable，填写表格中表示`此本栏目(对我)不适用`。
    5. ETL:extract原始数据抽取 tranform数据转化 load加载层，后面还有一个持久层
6. 10/16/2017 10:28:17 AM
    1. 测试git与gitlab内网的网络连接时并不是与github与oschina上所用的用户一致，而是使用`ssh -T gitlab@git.yz.local`;github与oschina的测试地址命令都是`ssh -T git@github.com`或`ssh -T git@gitee.com`;
    2. 全局配置文件config的格式与信息如下：
            Host git.yz.local
                HostName git.yz.local
                User git
                IdentityFile C:/Users/69420/.ssh/yz_gitlib_id_rsa
                IdentitiesOnly yes
            Host gitee.com
                HostName git.oschina.net
                User git
                IdentityFile C:/Users/69420/.ssh/oschina_id_rsa
                IdentitiesOnly yes
        1. 从文本信息：
            1. Host 主机
            2. Hostname: 主机名
            2. User： 用户
            3. IdentityFile: id难文件（私钥）
            4. IdentityesOnly: 是否必须验证
5. 10/17/2017 9:24:52 PM
    1. 重新部署项目时，当某个接口没有上线时，整个项目中会出现系统消息乱弹的情况：比如今天的重新部署中，一个接口没有上线，导致之前不存在的bug出现了，多个页面在点击不同地方弹出“与服务器通信失败”“系统错误”的消息；
2. 10/18/2017 11:11:43 AM
    1. 想将xml提示文件（dtd）放入eclipse的workplace，但没有找到之前的预设的路径。只将配置文件中dtd文件的路径改正确。猜想是不是刚安装eclipse没有用到xml文件，便在workspace中还没有生成相关的路径？
    2. 这两天在公司push到github与oschina上的仓库都没有在profileContribution上看到结果，但在相应的repository中却可以看到这些contributions。这是为什么呢？
	    1. **answer:**刚才在oschina码云上看到答案：`贡献度的统计数据包括代码提交、创建 Issue、合并请求，其中代码提交的次数需本地配置的 git 邮箱与码云账号邮箱一致才会被统计。`这就了然了，在公司的git配置里设置的是企业邮箱，明天去改过来应该就可以实现被统计了。![](./imgs/git_comtribute_statistics.png)
    3. 画流程图没有用Windows的`visio`，用的开源的`yEd Graph Editor`软件；
    4. md文档编写使用Atom，开源并与github关联紧密，比起MarkdownPad2更轻量且对软件开发者更为友好；