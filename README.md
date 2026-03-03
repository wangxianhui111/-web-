# 企业级 Java 快速开发基座项目 (Scaffold)

> 企业级 Java 快速开发基础模板

## 项目简介

这是一个企业级 Java 快速开发基座项目，基于 Spring Boot 3、MyBatis-Plus、Spring Security 6 + JWT 构建。旨在为公司后续所有微服务或单体应用提供统一的基础模板。

## 技术栈

- **Java**: JDK 21+
- **Spring Boot**: 3.2.5
- **Spring Security**: 6.x + JWT
- **MyBatis-Plus**: 3.5.6
- **MySQL**: 8.0+
- **Redis**: 6.0+
- **Flyway**: 数据库版本管理
- **Knife4j**: API 文档

## 已实现功能

### 核心功能
- ✅ 用户登录/登出 (JWT认证)
- ✅ 用户管理 (CRUD)
- ✅ 角色管理 (CRUD)
- ✅ 菜单管理 (CRUD)
- ✅ 部门管理 (CRUD)
- ✅ 岗位管理 (CRUD)
- ✅ 统一响应结构
- ✅ 全局异常处理
- ✅ Spring Security 6 安全配置
- ✅ 自定义权限注解 `@RequirePermission`

### 前端
- ✅ 登录页面
- ✅ 用户管理页面
- ✅ 角色/菜单/部门/岗位管理页面

### 基础设施
- ✅ Flyway 数据库版本管理
- ✅ Redis 缓存集成
- ✅ Knife4j API 文档
- ✅ Docker + Docker Compose 部署
- ✅ Nginx 反向代理

## 待实现功能

### 系统运维模块

| 模块 | 说明 | 优先级 |
|------|------|--------|
| 操作日志 | 记录用户操作行为 | 高 |
| 登录日志 | 记录登录时间、IP、设备 | 高 |
| 在线用户 | 查看当前在线用户 | 中 |
| 系统监控 | JVM、数据库、Redis监控 | 中 |

### 通用功能模块

| 模块 | 说明 | 优先级 |
|------|------|--------|
| 文件上传/下载 | 通用文件服务（本地/OSS） | 高 |
| 字典管理 | 静态数据配置 | 中 |
| 参数配置 | 系统参数配置 | 中 |
| 定时任务 | 动态定时任务管理 | 中 |

## 快速开始

### 环境要求

- JDK 21+
- Maven 3.8+
- MySQL 8.0+
- Redis 6.0+

### 配置步骤

1. 克隆项目
2. 配置数据库连接 (`application-dev.yml`)
3. 配置 Redis 连接
4. 运行 `mvn spring-boot:run`
5. 访问 http://localhost:8080

### 默认账号

- 用户名: admin
- 密码: admin123

## 项目结构

```
scaffold/
├── src/main/java/com/company/scaffold/
│   ├── common/           # 公共组件
│   │   ├── annotation/   # 自定义注解
│   │   ├── constant/     # 常量定义
│   │   ├── core/         # 核心(结果封装、异常处理)
│   │   └── handler/      # 处理器
│   ├── config/           # 配置类
│   ├── generator/        # 代码生成器
│   ├── module/           # 业务模块
│   │   └── system/       # 系统模块
│   ├── security/         # 安全组件
│   └── ScaffoldApplication.java
├── src/main/resources/
│   ├── db/migration/     # Flyway 迁移脚本
│   ├── generator/        # 代码生成器模板
│   ├── static/           # 静态资源
│   └── application.yml   # 应用配置
└── pom.xml
```

## API 文档

- Knife4j: http://localhost:8080/doc.html
- Swagger: http://localhost:8080/swagger-ui.html

## 开发规范

- 遵循阿里巴巴 Java 开发手册
- 使用 DDD 分层架构
- Entity 使用 Lombok，DTO/VO 使用 Java Record
- 使用 JSR-303 进行参数校验
- 使用 MyBatis-Plus 进行数据库操作

## License

MIT
