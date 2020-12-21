 基于干净架构的DDD脚手架 
---
在要创建应用的文件夹下执行以下命令（会根据appName创建文件夹）
```shell script
mvn archetype:generate -DarchetypeCatalog=local -DarchetypeGroupId=com.qnr -DarchetypeArtifactId=ddd-archetype -DarchetypeVersion=0.0.1-SNAPSHOT -DappName=app-example
```
appName是必需字段     
artifactId默认为appName    
version默认为0.0.1-SNAPSHOT    
package默认为groupId + artifactId.replaceAll("-",".")      
author默认值为system
```
|--- adapter                     -- 适配器层 应用与外部应用交互适配
|      |--- controller           -- 控制器层，API中的接口的实现
|      |       |--- assembler    -- 装配器，DTO和领域模型的转换
|      |       |--- impl         -- 协议层中接口的实现
|      |--- repository           -- 仓储层
|      |       |--- assembler    -- 装配器，PO和领域模型的转换
|      |       |--- impl         -- 领域层中仓储接口的实现
|      |--- rpc                  -- RPC层,Domain层中port中依赖的外部的接口实现，调用远程RPC接口
|      |--- task                 -- 任务，主要是调度任务的适配器
|--- api                         -- 应用协议层 应用对外暴露的api接口
|--- boot                        -- 启动层 应用框架、驱动等
|      |--- aop                  -- 切面
|      |--- config               -- 配置
|      |--- Application          -- 启动类
|--- app                         -- 应用层
|      |--- cases                -- 应用服务
|--- domain                      -- 领域层
|      |--- model                -- 领域对象
|      |       |--- aggregate    -- 聚合
|      |       |--- entities     -- 实休
|      |       |--- vo           -- 值对象
|      |--- service              -- 域服务
|      |--- factory              -- 工厂，针对一些复杂的Object可以通过工厂来构建
|      |--- port                 -- 端口，即接口
|      |--- event                -- 领域事件
|      |--- exception            -- 异常封装
|      |--- ability              -- 领域能力
|      |--- extension            -- 扩展点
|      |       |--- impl        -- 扩展点实现
|--- query                       -- 查询层，封装读服务
|      |--- model                -- 查询模型
|      |--- service              -- 查询服务
|--- types                       -- 定义Domain Primitive
```
![image](/clean.png)    
