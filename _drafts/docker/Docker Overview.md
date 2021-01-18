---
tag: [mysql, Docker]
categories: programming
description: The Overview of Docker
date: "2021-1-16 23:13:00"
---

# Overview of Docker

[reference](https://docs.docker.com/engine/)

Docker 引擎是一个构建并包含应用的开源容器技术。Docker 引擎作为一个 cs 应用主要有三种形式：

1. 长生命周期的守护进程(Docker daemon) dockerd
2. 与 dockerd 会话并指令的 api 接口
3. 命令行接口（CLI）客户端 docker

CLI 使用 Docker APIs 通过脚本或 CLI 命令控制并与 Docker daemon 交互。许多其他的 Docker 应用都使用 CLI、API 。守护进程 daemon 创建并管理 Docker 对象（object），比如：Images,containers,networks,volumes。

## Docker Architecture

[reference](https://docs.docker.com/get-started/overview/#docker-architecture)

![Docker use a client-server architecture](https://docs.docker.com/engine/images/architecture.svg)

### The Docker Daemon

dockerd 监听 Docker API 请求，并管理诸如 Images,containers,networks, volumes Docker Objects。daemon 也会与其他 daemon 通信以管理 Docker 服务。

### The Docker Client

Docker client `docker` 是 Docker 用户与 Docker 交互的主要方式。当输入 `docker` 命令时，client 就发送此命令给 daemon `dockerd`，dockerd　接收命令并处理。docker 命令使用 docker API。一个 Docker client 可以与多个 daemon 交互。

### Docker registries

Docker 注册器是用来存储Docker 镜像(Images)的。Docker Hub 是公有注册器,Docker 默认被被配置为在 Docker Hub 中查找镜像。也可以运行自己的私有注册器。

当你使用 `docker pull` `docker run` 命令时，将从你配置的注册器（registry 叫登记中心应该更合理）拉取需要的镜像。`docker push` 命令推送镜像到配置的注册中心中。

### Docker Objects

当使用 Docker 时，实际上在创建并使用 Docker Objects。这些 Object 包括: images,containers,networks, plugins, volumes 等。

#### IMAGES

一个 Image 实际上是一个只读的创建 Docker 容器指令模版。一个 image 通常是基于另外一个 image 建立但额外添加了其他定制化。比如：可能创建一个基于 Ubuntu 的 image，但安装了 Apache web server 和自己的应用，与其他一些运行应用需要的配置细节。

你可以创建自己的 image，也可以直接使用其他人创建并已发布到注册中心的 image。创建自己的 image ，先使用简单语法创建一个 *Dockerfile* 定义创建、运行 image 需要的步骤。Dockerfile 中的每个指令都会在 image 中创建一个层（layer）。当修改 Dockerfile 并重建 image 时，只有这些被修改的 layer 需要被重建。这也 Docker 与其他虚拟化相比相对更轻量、快捷的原因。

#### CONTAINERS

一个 Container 是一个 image 运行起来的实例。可以通过 Docker API 或 CLI create, start, stop, delete, move 一个容器。可以连接一个窗口到一个或多个网络，存储数据到容器，甚至基于当前状态创建一个新的 image。

默认情况下，container 与其宿主机、其他窗口是相对完好地隔离开的。可以控制容器的网络、存储、其他间接的子系统与其他窗口、宿主机的隔离。

一个 container 通过其 image 定义，在你创建并启动它时，其配置项都已经配置好。容器的状态数据都不持久化存储，当一个 container 被移除，其修改的状态都会消失。

#### Example `docker run` command

在 Ubuntu 命令行 bash 环境下运行命令：`docker run -i -t ubuntu /bin/bash`

在默认注册配置下，这行命令的执行包括以下：

1. 如果本地没有 ubuntu 镜像，Docker 会从配置注册处拉取一个，就像运行 `docker pull ubuntu` 命令。
2. Docker 创建一个新的 container，与手动执行命令 `docker container create` 一样。
3. Docker 创建一个读写文件系统到容器中作为其最终层。这样容器运行起来后就可以在其本地文件系统中创建修改文件与目录。
4. 上述命令行中并没有指定网络项，所以 Docker 会创建一个网络接口用于容器与默认网络连接。这其中包括给容器指定一个 IP 地址。默认情况下，容器可以使用宿主机网络连接与外部网络通信。
5. Docker 开启容器并执行 `/bin/bash`。`-i` interactively 选项指定了容器交互式运行，`-t` terminal 选项指定容器在终端中运行，所以可以用键盘输入而输出就直接在终端中。
6. 当输入 `exit` 结束 `/bin/bash` 命令时，容器将停止但并不被移除，可以重启容器或移除容器。

#### Services

Service 可以跨 Docker daemon 操作 container ，所有管理 managers 工作进程 workers 在一起就类似 *swarm* 。*swarm* 每个成员都是一个 Docker daemon，并且所有的 daemon 都通过 Docker API 通信。Service 允许你定义需要的状态，比如复制品（replica）的在任何指定的时间都可得的数量。默认情况下，service 在所有 worker node 间是 load-balanced。而对于 consumer ,Docker service 是单个应用。Docker 引擎在 1.12 及之后版本支持 swarm 模式的。

## Underlying Technology

Docker 是使用 go 语言，并组合了 linux kernel 某些特征。

### Namespace

Docker 使用 `namespaces` 技术以提供隔离的工作空间 *container* 。当运行 container ，Docker 会为 container 创建一组 namespace 。

namespace 提供隔离层，container 的不同隔离层在各自的命名空间运行，访问是受命名空间限制的。

Docker 引擎在 linux 使用以下的命名空间：

- `pid` namespace:进程隔离（process ID）
- `net` namespace:管理网络接口（NET:Networking）
- `ipc` namespace:管理 IPC 资源访问访问（IPC:InterProcess Communication）
- `mnt` namespace:管理文件系统挂载点（MNT:Mount）
- `uts` namespace:Isolating kernel and version identifiers.(UTS:Unix Timesharing System)

### Control Groups

在 Linux 系统上的 Docker 引擎依赖 *control group* 技术（`cgroups`）。cgroup 限制应用访问资源。Control Groups 让 Docker 引擎分享可得的硬件资源给窗口，并有可选的限制约束。比如：可以指定容器限制可用内存。

### Union file systems

Union File system or UnionFS 是创建层操作的文件系统，让其异常地轻量与快速。Docker engine 使用 UnionFS 为容器提供构建 blocks.Docker engine 可以使用多个 UnionFS 变体，包括：AUFs, btrfs,vfs, DeviceMapper。

### Container Format

Docker Engine 将 namespace,control groups,UnionFS包装在一起叫：container format (容器格式化)。默认的 container format 是 `libcontainer`。未来 Docker 会通过整合 BSD Jails , Solaris Zones 技术以支付其他 container format。
