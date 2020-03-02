---
layout: "post"
title: "git"
date: "2018-10-19 11:06"
---

>Youqian:
>
> 同学们注意哈， Git参数的设置( git config --global merge.ff no )， 有个坑。
>
> 如果设置了这两个参数， 需要同时再设置这下面2个：
>
> git config --global merge.commit no
>
> git config --global pull.ff yes
>
> 如果不设置 merge.commit 为 no ， 那么每次 pull 代码的话会自动给你commit， 会导致多一次无用的提交记录
>
> 还需要同时设置 pull.ff  为yes

## initialization

生成密钥：`$ ssh-keygen -t rsa -C 'email_name'`

## git 常用命令

[reference](http://www.cnblogs.com/vman/articles/Git_cmds.html)

[官方文档](https://git-scm.com/docs/)

### Git 远程仓库管理

- `git remote -v` # 查看远程服务器地址和仓库名称
- `git remote show origin` # 查看远程服务器仓库状态
- `git remote add origin git@github:robbin/robbin_site.git` **添加远程仓库地址**，可以用来一个本地库与多个远程库连接（github & gitee）
- `git remote set-url origin git@github.com:robbin/robbin_site.git` # 设置远程仓库地址(用于修改远程仓库地址)
- `git remote rm <repository>` # 删除远程仓库
- `git remote show <origin>` 查看远程分支与本地分支的跟踪状态
- `git remote set-head origin master` # 设置远程仓库的HEAD指向master分支也可以命令设置跟踪远程库和本地库
- `git remote add <origin> <git@github.com:robbin/robbin_site.git>`  **设置远程仓库地址与本地仓库链接**
- `git branch <--set-upstream-to> <master> <origin>/<master>` **将本地 master 分支与远程 master 分支关联**
- `git branch <--unset-upstream-to> <master> <origin>/<master>` **将本地 master 分支与远程 master 分支解绑**
- `git branch <--track> <master> <origin>/<master>` **将本地 master 分支与远程 master 分支关联** 新创建分支时使用 `--track` 与远程分支连接
- `git branch -r -d <origin>/<branch_name>` 删除远程分支的跟踪信息
- `git branch -r` 查看远程分支
- `git push -u origin develop` # 首次将本地develop分支提交到远程develop分支，并且 track
- `git push` # push 分支
- `git push origin master` # 将本地主分支推到远程主分支
- `git push -u origin master` # 将本地主分支推到远程(如无远程主分支则创建，用于初始化远程仓库)
- `git push origin <local_branch>` # 创建远程分支， origin是远程仓库名
- `git push origin <local_branch>:<remote_branch>` # 创建远程分支
- `git push origin --delete <remote_branch_name>` 彻底删除远程分支，git 版本 1.7 之后添加的新功能
- `git push origin --delete tag <tag_name>` 彻底删除远程 tag ，版本 1.7 之后添加的新功能
- `git pull` # 抓取远程仓库所有分支更新并合并到本地
- `git pull origin <remote_branch> [--allow-unrelated-histories]` # 抓取远程仓库所有分支更新并合并到本地，后一参数用以指定允许 pull 不相关历史
- `git pull --no-ff` # 抓取远程仓库所有分支更新并合并到本地，不要快速合并 no fast forward
- `git fetch origin` # 抓取远程仓库更新
- `git merge origin/master` # 将远程主分支合并到本地当前分支
- `git log remotes/<origin/branch_name>` 查看远程分支提交历史，不使用 remotes 也可以
- `mkdir robbin_site.git && cd robbin_site.git && git --bare init` # 在服务器创建纯仓库
- `git clone --bare robbin_site robbin_site.git` # 用带版本的项目创建纯版本仓库
- `scp -r my_project.git git@ git.csdn.net:~` # 将纯仓库上传到服务器上

### Git 本地分支管理

查看、切换、创建和删除分支

- `git br -r` # 查看远程分支
- `git br <new_branch>` # 创建新的分支
- `git br -v` # 查看各个分支最后提交信息
- `git br --merged` # 查看已经被合并到当前分支的分支
- `git br --no-merged` # 查看尚未被合并到当前分支的分支
- `git br -d <branch>` # 删除某个本地分支
- `git br -D <branch>` # 强制删除某个本地分支 (未被合并的分支被删除的时候需要强制)
- `git co <branch>` # 切换到某个分支
- `git co -b <new_branch>` # 创建新的分支，并且切换过去
- `git co -b <new_branch> <branch>` # 基于branch创建新的new_branch
- `git co $id` # 把某次历史提交记录checkout出来，但无分支信息，切换到其他分支会自动删除
- `git co $id -b <new_branch>` # 把某次历史提交记录 checkout 出来，创建成一个分支
- `git checkout --track origin/branch` # 跟踪某个远程分支创建相应的本地分支（使用此命令跟踪远程分支前，需要确定是否已抓取远程分支的更新 `git remote show origin`，如果没有抓取，使用 `git fetch origin` 命令抓取）
- `git checkout -b <local_branch> origin/<remote_branch>` # 基于远程分支创建本地分支，功能同上

### 分支合并 merge 和 rebase

- `git merge <branch>` # 将branch分支合并到当前分支
- `git merge origin/master --no-ff` # 不要Fast-Foward合并，这样可以生成merge提交
- `git rebase master <branch>` # 将master rebase到branch，相当于： `git co <branch> && git rebase master && git co master && git merge <branch>`

### Git补丁管理(方便在多台机器上开发同步时用)

- `git diff > ../sync.patch` # 生成补丁
- `git apply ../sync.patch` # 打补丁
- `git apply --check ../sync.patch` #测试补丁能否成功

#### stash

- `git stash` # 暂存
- `git stash list` # 列所有stash
- `git stash apply` # 恢复暂存的内容
- `git stash drop` # 删除暂存区
- `git stash pop` # pop stash 栈

### git tag

- 添加 tag : `git tag -a [tagName] -m ['note']`
  - version 指版本号。 note 指文本说明。 `-a` 指 `add` 添加标签。
- 提交 tag 到远程仓库： `git push [remote nate] -tags`
  - 此命令可以把本地的 tag 全部提交到远程仓库。
- 删除本地 tag: `git tag -d [tagName]`
  - `-d` 表示 `delete`
- 删除远程 tag : `git push [remoteName 如 origin]:refs/tags/[tagName]`
  - 与 `git push origin:origin/branchName` 命令一致的原理，将空分支 push 到远程对应分支而将其置空。
- 查看 tag : `git tag`/`git tag -l`

## 案例

### 错误 E325

> 使用 git bash 时遇到的错误。[参考](https://blog.csdn.net/wangzunkuan/article/details/80484119)

现象

使用 git bash 时，不管在任何分支上执行 `git commit --amend` 命令（可能是使用 vi 相关文本编辑命令都会出现）出现了错误 `E325: 注意[dev 0cd818fb3] Merge remote-tracking branch 'origin/dev' into dev`。同时， git bash 卡死不能再输入输出任何命令。

原因

在 git bash 中执行某个命令时，未待执行完成就将 bash 窗口关闭。 在 .git 文件夹中将产生一个 `.COMMIT_EDITMSG.swp` 文件，此文件应是用于记录 commit 编辑信息的。在每次打开 commit 文本时就会因此而产生卡死。

解决方案

直接删除 `.COMMIT_EDITMSG.swp` 文件即可。

### 将本地工作空间关联两个远程库

> 需求来源于 github 远程仓库连接太慢，而国内的 gitee 连接速度相对更快，有必要将自己的代码放入两个仓库备份使用。

步骤：

1. 基本操作，先生成两套密钥（注意 generate 时命名区分），一套放 github 一套放 gitee，并测试（SSH -T git@github.com）连接成功；
2. 远程分别创建仓库；
3. 先将一个 gitee(随便哪个都可，反之后续则反) 远程库 clone 到本地；
4. 在本地库增加远程机：`git remote add <origin2> <git@github.com:robbin/robbin_site.git>`，注意增加第二个远程主机时命名不要与前面自动生成的 origin 重复。仓库 url 在见面上 copy 即可。
5. fetch 新远程主机数据：`git fetch <origin2>`;
6. 将本地分支 master（其他亦可）与 github 上分支关联：`git branch <--set-upstream-to> <master> <origin2>/<master>` 。至此，已经本地库也与两个远程库关联成功。前面使用过程中此步骤出现 `fatal : branch orgin2/master not exist`，换用被废弃的指令 `--set-upstream` 成功，不知道为什么？
7. 将新远程库代码 pull 到本地 merge：`git pull <origin2> <master>`；、
8. 再次 push 时可加上远程主机名与分支名以区分：`git push origin master`
