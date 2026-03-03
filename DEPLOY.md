# Docker部署指南

## 快速启动

### Windows
```bash
docker-build.bat
```

### Linux/Mac
```bash
docker-compose up -d --build
```

## Docker镜像说明

### 包含服务
| 容器 | 端口 | 说明 |
|------|------|------|
| scaffold-nginx | 80 | Nginx反向代理 |
| scaffold-app | 8080 | Spring Boot应用 |
| scaffold-mysql | 3306 | MySQL数据库 |
| scaffold-redis | 6379 | Redis缓存 |

## 手动构建

### 构建Spring Boot镜像
```bash
# 构建镜像
docker build -t scaffold-app:latest .

# 运行容器
docker run -d -p 8080:8080 --name scaffold-app \
  -e SPRING_DATASOURCE_URL=jdbc:mysql://localhost:3306/scaffold \
  -e SPRING_DATASOURCE_USERNAME=root \
  -e SPRING_DATASOURCE_PASSWORD=root123 \
  scaffold-app:latest
```

### 推送到镜像仓库
```bash
# 标记镜像
docker tag scaffold-app:latest your-registry/scaffold-app:latest

# 推送镜像
docker push your-registry/scaffold-app:latest
```

## Docker Compose命令

```bash
# 启动所有服务
docker-compose up -d

# 查看日志
docker-compose logs -f

# 停止服务
docker-compose stop

# 销毁服务
docker-compose down

# 重新构建
docker-compose up -d --build
```

## 环境变量

| 变量名 | 默认值 | 说明 |
|--------|--------|------|
| SPRING_DATASOURCE_URL | jdbc:mysql://mysql:3306/scaffold | 数据库连接 |
| SPRING_DATASOURCE_USERNAME | root | 数据库用户名 |
| SPRING_DATASOURCE_PASSWORD | root123 | 数据库密码 |
| SPRING_DATA_REDIS_HOST | redis | Redis主机 |
| SPRING_DATA_REDIS_PORT | 6379 | Redis端口 |
