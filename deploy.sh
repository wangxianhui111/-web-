#!/bin/bash
# 云服务器一键部署脚本 - 使用 Docker Compose
set -e

echo "========================================"
echo "  企业级快速开发平台 - Docker Compose部署"
echo "========================================"

# ==================== 1. 检查环境 ====================
echo "[1/5] 检查环境..."

# 检查 Docker
if ! command -v docker &> /dev/null; then
    echo "  错误: 未安装 Docker"
    exit 1
fi
echo "  Docker: $(docker --version | awk '{print $3}')"

# 检查 Docker 权限（不需要 sudo）
if ! docker ps &> /dev/null; then
    echo "  错误: 当前用户无 Docker 权限"
    echo "  请执行: sudo usermod -aG docker \$USER && newgrp docker"
    exit 1
fi

# 检查 Docker Compose
if ! docker compose version &> /dev/null; then
    echo "  错误: 未安装 Docker Compose"
    exit 1
fi
echo "  Compose: $(docker compose version --short)"

# ==================== 2. 检查项目目录 ====================
echo "[2/5] 检查项目目录..."
PROJECT_DIR="/home/ubuntu/scaffold"
cd $PROJECT_DIR

if [ ! -f "docker-compose.yml" ] && [ ! -f "docker-compose.yaml" ]; then
    echo "  错误: 未找到 docker-compose.yml"
    exit 1
fi

echo "  项目目录: $PROJECT_DIR ✅"

# ==================== 3. 检查项目文件 ====================
echo "[3/5] 检查项目文件..."

JAR_FILE=$(ls ${PROJECT_DIR}/*.jar 2>/dev/null | head -1)
[ -n "${JAR_FILE}" ] && echo "  JAR包: $(basename ${JAR_FILE}) ✅"
[ -f "Dockerfile" ] && echo "  Dockerfile: ✅"
[ -f "nginx.conf" ] && echo "  Nginx配置: ✅"

# ==================== 4. 构建并启动 ====================
echo "[4/5] 构建并启动服务..."

# 停止旧服务
echo "  停止旧服务..."
docker compose down 2>/dev/null || true

# 构建并启动
echo "  构建镜像..."
docker compose build --no-cache

echo "  启动所有服务..."
docker compose up -d

# 智能等待
echo -n "  等待服务启动"
MAX_WAIT=60
WAITED=0
while [ $WAITED -lt $MAX_WAIT ]; do
    RUNNING=$(docker compose ps 2>/dev/null | grep -c "Up" || echo "0")
    UNHEALTHY=$(docker compose ps 2>/dev/null | grep -cE "Restarting|Exit" || echo "0")

    if [ "$RUNNING" -gt 0 ] && [ "$UNHEALTHY" -eq 0 ]; then
        echo ""
        echo "  ✅ 所有服务已启动 (${WAITED}秒)"
        break
    fi

    echo -n "."
    sleep 3
    WAITED=$((WAITED + 3))
done

if [ $WAITED -ge $MAX_WAIT ]; then
    echo ""
    echo "  ⚠️  部分服务可能还在启动中"
fi

# ==================== 5. 检查状态 ====================
echo "[5/5] 服务状态..."
echo ""
docker compose ps

SERVER_IP=$(curl -s --max-time 5 ifconfig.me 2>/dev/null || echo "你的服务器IP")

echo ""
echo "========================================"
echo "  ✅ 部署完成!"
echo "========================================"
echo ""
echo "访问地址:"
echo "  - 前端页面: http://${SERVER_IP}"
echo "  - API文档:  http://${SERVER_IP}/doc.html"
echo ""
echo "常用命令:"
echo "  docker compose ps                查看服务状态"
echo "  docker compose logs -f app       查看应用日志"
echo "  docker compose down              停止所有服务"
echo "  docker compose restart           重启所有服务"
echo "  docker compose up -d --build     重新构建并启动"
echo ""
