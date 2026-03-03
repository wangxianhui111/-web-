#!/bin/bash
# 自动更新部署脚本

set -e

echo "========================================"
echo "  开始自动更新部署"
echo "========================================"

# 检查是否为root用户
if [ "$EUID" -ne 0 ] && ! sudo -v 2>/dev/null; then
    echo "请使用 sudo 运行此脚本"
    exit 1
fi

SUDO=""
if [ "$EUID" -ne 0 ]; then
    SUDO="sudo"
fi

PROJECT_DIR="/home/ubuntu/scaffold"
cd $PROJECT_DIR

# 1. 停止应用
echo "[1/5] 停止应用..."
$SUDO docker-compose stop app

# 2. 拉取最新代码
echo "[2/5] 拉取最新代码..."
$SUDO git pull origin main

# 3. 重新构建
echo "[3/5] 重新构建Docker镜像..."
$SUDO docker-compose build --no-cache app

# 4. 启动应用
echo "[4/5] 启动应用..."
$SUDO docker-compose up -d app

# 5. 查看状态
echo "[5/5] 查看应用状态..."
$SUDO docker-compose ps

echo ""
echo "========================================"
echo "  更新完成!"
echo "========================================"
