## 数字化研发-火箭PDM系统后端服务框架简介

* 采用前后端分离的模式，微服务版本前端采用vue编写
* 后端采用Spring Boot、Spring Cloud & ...
* 注册中心、配置中心选型Nacos，权限认证使用Redis。

### 框架包结构

* pom 文件目录结构

```
    base (公共依赖包)
        auth（权限组件）
        common（公共包）
        redis（缓存组件）
        remote（内部服务调用组件）
        ...
    api (模型服务层公共依赖层)
        entity （需要持久化的模型对象层） 
        model （业务模型对象层，dto、req、res...） 
        service (domain模型对象层，bo领域模型对象，dao层...)
    app （业务微服务模块）
        myworkspace（个人工作台服务）
        library（基础库管理服务）
        ebom（EBOM管理服务）
        pbom（PBOM管理服务）
    
    
         

```

